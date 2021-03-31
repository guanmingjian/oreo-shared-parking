package cn.oreo.server.system.service.impl;

import cn.oreo.common.core.entity.DeptTree;
import cn.oreo.common.core.entity.QueryRequest;
import cn.oreo.common.core.entity.Tree;
import cn.oreo.common.core.entity.constant.OreoConstant;
import cn.oreo.common.core.entity.constant.PageConstant;
import cn.oreo.common.core.entity.system.Dept;
import cn.oreo.common.core.utils.SortUtil;
import cn.oreo.common.core.utils.TreeUtil;
import cn.oreo.server.system.mapper.DeptMapper;
import cn.oreo.server.system.service.IDeptService;
import cn.oreo.server.system.service.IUserDataPermissionService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

/**
 * @author GuanMingJian
 * @since 2020/10/5
 */
@Slf4j
@Service("deptService")
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class DeptServiceImpl extends ServiceImpl<DeptMapper, Dept> implements IDeptService {

    private final IUserDataPermissionService userDataPermissionService;

    @Override
    public Map<String, Object> findDepts(QueryRequest request, Dept dept) {
        Map<String, Object> result = new HashMap<>(2);
        try {
            List<Dept> depts = findDepts(dept, request);
            depts.stream().forEach(dept1 -> System.out.println("dept:"+dept1));

            List<DeptTree> trees = new ArrayList<>();
            buildTrees(trees, depts);

            List<? extends Tree<?>> deptTree = TreeUtil.build(trees);
            deptTree.stream().forEach(deptTree1 -> System.out.println("deptTree1:"+deptTree1));

            result.put(PageConstant.ROWS, deptTree);
            result.put(PageConstant.TOTAL, depts.size());
        } catch (Exception e) {
            log.error("获取部门列表失败", e);
            result.put(PageConstant.ROWS, null);
            result.put(PageConstant.TOTAL, 0);
        }
        return result;
    }

    @Override
    public List<Dept> findDepts(Dept dept, QueryRequest request) {
        QueryWrapper<Dept> queryWrapper = new QueryWrapper<>();

        if (StringUtils.isNotBlank(dept.getDeptName())) {
            queryWrapper.lambda().like(Dept::getDeptName, dept.getDeptName());
        }
        if (StringUtils.isNotBlank(dept.getCreateTimeFrom()) && StringUtils.isNotBlank(dept.getCreateTimeTo())) {
            queryWrapper.lambda()
                    .ge(Dept::getCreateTime, dept.getCreateTimeFrom())
                    .le(Dept::getCreateTime, dept.getCreateTimeTo());
        }
        SortUtil.handleWrapperSort(request, queryWrapper, "orderNum", OreoConstant.ORDER_ASC, true);
        return this.baseMapper.selectList(queryWrapper);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void createDept(Dept dept) {
        if (dept.getParentId() == null) {
            dept.setParentId(Dept.TOP_DEPT_ID);
        }
        dept.setCreateTime(new Date());
        this.save(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateDept(Dept dept) {
        if (dept.getParentId() == null) {
            dept.setParentId(Dept.TOP_DEPT_ID);
        }
        dept.setModifyTime(new Date());
        this.baseMapper.updateById(dept);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteDepts(String[] deptIds) {
        this.delete(Arrays.asList(deptIds));
    }

    private void buildTrees(List<DeptTree> trees, List<Dept> depts) {
        depts.forEach(dept -> {
            DeptTree tree = new DeptTree();
            tree.setId(dept.getDeptId().toString());
            tree.setParentId(dept.getParentId().toString());
            tree.setLabel(dept.getDeptName());
            tree.setOrderNum(dept.getOrderNum());
            trees.add(tree);
        });
    }

    private void delete(List<String> deptIds) {
        removeByIds(deptIds);
        userDataPermissionService.deleteByDeptIds(deptIds);

        LambdaQueryWrapper<Dept> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.in(Dept::getParentId, deptIds);
        List<Dept> depts = baseMapper.selectList(queryWrapper);
        if (CollectionUtils.isNotEmpty(depts)) {
            List<String> deptIdList = new ArrayList<>();
            depts.forEach(d -> deptIdList.add(String.valueOf(d.getDeptId())));
            this.delete(deptIdList);
        }
    }

}
