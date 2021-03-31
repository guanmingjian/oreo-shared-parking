package cn.oreo.server.system.service.impl;

import cn.oreo.common.core.entity.CurrentUser;
import cn.oreo.common.core.entity.QueryRequest;
import cn.oreo.common.core.entity.constant.OreoConstant;
import cn.oreo.common.core.entity.constant.StringConstant;
import cn.oreo.common.core.entity.system.SystemUser;
import cn.oreo.common.core.entity.system.UserDataPermission;
import cn.oreo.common.core.entity.system.UserRole;
import cn.oreo.common.core.exception.OreoException;
import cn.oreo.common.core.utils.OreoUtil;
import cn.oreo.common.core.utils.SortUtil;
import cn.oreo.server.system.mapper.UserMapper;
import cn.oreo.server.system.service.IUserDataPermissionService;
import cn.oreo.server.system.service.IUserRoleService;
import cn.oreo.server.system.service.IUserService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * @author GuanMingJian
 * @since 2020/10/5
 */
@Service
@RequiredArgsConstructor
@Transactional(propagation = Propagation.SUPPORTS, readOnly = true)
public class UserServiceImpl extends ServiceImpl<UserMapper, SystemUser> implements IUserService {

    private final IUserRoleService userRoleService;
    private final IUserDataPermissionService userDataPermissionService;
    private final PasswordEncoder passwordEncoder;

    @Override
    public SystemUser findByName(String username) {
        LambdaQueryWrapper<SystemUser> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(SystemUser::getUsername, username);
        return this.baseMapper.selectOne(queryWrapper);
    }

    @Override
    public IPage<SystemUser> findUserDetailList(SystemUser user, QueryRequest request) {

        // mybatis plus的分页设置，学习网址：https://mybatis.plus/guide/page.html
        // Page<Object>中Object是对象类型，然后传入PageNum和PageSize
        Page<SystemUser> page = new Page<>(request.getPageNum(), request.getPageSize());

        // 处理排序（分页情况下） for mybatis-plus
        // 第三个参数：需要按哪个字段排序
        // 第四个参数：升序还是降序
        // 第四个参数：是否开启驼峰转下划线
        SortUtil.handlePageSort(request, page, "userId", OreoConstant.ORDER_ASC, false);

        // 调用mapper层的分页查询
        // this.baseMapper实际上是UserMapper，UserMapper继承了BaseMapper<SystemUser>
        // 下面有很多常用接口，非常方便，学习网站：https://mybatis.plus/guide/crud-interface.html#mapper-crud-接口
        IPage<SystemUser> userDetailPage = this.baseMapper.findUserDetailPage(page, user);

        return userDetailPage;
    }

    @Override
    public SystemUser findUserDetail(String username) {
        SystemUser param = new SystemUser();
        param.setUsername(username);
        List<SystemUser> users = this.baseMapper.findUserDetail(param);
        return CollectionUtils.isNotEmpty(users) ? users.get(0) : null;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateLoginTime(String username) {
        SystemUser user = new SystemUser();
        user.setLastLoginTime(new Date());

        this.baseMapper.update(user, new LambdaQueryWrapper<SystemUser>().eq(SystemUser::getUsername, username));
    }

    @Override
    // 这里就是异常处理的位置了，很多优秀的程序，都有兜底的方法
    // 他这个兜底的方法，就是抛出异常了。。。
    // 其实我们可以自定义兜底的方法，可以处理异常，也可以抛出比较友好的提示
    @Transactional(rollbackFor = Exception.class)
    public void createUser(SystemUser user) {
        // 补充一些用户信息
        // 这里也可以看到，我们应该尽量不要把变量写死在所在类中，到时候很难找，写在常量类中
        // 创建用户
        user.setCreateTime(new Date());
        user.setAvatar(SystemUser.DEFAULT_AVATAR);
        user.setPassword(passwordEncoder.encode(SystemUser.DEFAULT_PASSWORD));

        // 保存用户
        // 这里不是本地方法，是因为上面继承了extends ServiceImpl<UserMapper, SystemUser>
        // 这里属于mybatis plus的 Service CRUD接口
        // 官方学习文档：https://mybatis.plus/guide/crud-interface.html#service-crud-接口
        save(user);

        // 保存用户角色
        // 使用，号把字符串切割格式化成字符串数组
        String[] roles = StringUtils.splitByWholeSeparatorPreserveAllTokens(user.getRoleId(), StringConstant.COMMA);
        // 请点击查看这个类
        setUserRoles(user, roles);

        // 其实很多业务，增删改查的时候并不是单一的，还有很多附加业务需要处理的

        // 前端我不要这个功能了，所以我也注释掉了
        // 保存用户数据权限关联关系
//        String[] deptIds = StringUtils.splitByWholeSeparatorPreserveAllTokens(user.getDeptIds(), StringConstant.COMMA);
//        setUserDataPermissions(user, deptIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateUser(SystemUser user) {
        // 更新用户
        user.setPassword(null);
        user.setUsername(null);
        user.setCreateTime(null);
        user.setModifyTime(new Date());
        updateById(user);

        // 保存用户-角色关联表
        String[] userIds = {String.valueOf(user.getUserId())};
        userRoleService.deleteUserRolesByUserId(userIds);
        String[] roles = StringUtils.splitByWholeSeparatorPreserveAllTokens(user.getRoleId(), StringConstant.COMMA);
        setUserRoles(user, roles);

        // 保存用户-数据权限关联表
//        userDataPermissionService.deleteByUserIds(userIds);
//        String[] deptIds = StringUtils.splitByWholeSeparatorPreserveAllTokens(user.getDeptIds(), StringConstant.COMMA);
//        setUserDataPermissions(user, deptIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void deleteUsers(String[] userIds) {
        List<String> list = Arrays.asList(userIds);
        // 类似于，前面有讲解
        // 这里不是本地方法，是因为上面继承了extends ServiceImpl<UserMapper, SystemUser>
        // 这里属于mybatis plus的 Service CRUD接口
        // 官方学习文档：https://mybatis.plus/guide/crud-interface.html#service-crud-接口
        removeByIds(list);
        // 删除用户角色
        this.userRoleService.deleteUserRolesByUserId(userIds);
        //this.userDataPermissionService.deleteByUserIds(userIds);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateProfile(SystemUser user) throws OreoException {
        user.setPassword(null);
        user.setUsername(null);
        user.setStatus(null);
        if (isCurrentUser(user.getUserId())) {
            updateById(user);
        } else {
            throw new OreoException("您无权修改别人的账号信息！");
        }
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updateAvatar(String avatar) {
        SystemUser user = new SystemUser();
        user.setAvatar(avatar);
        String currentUsername = OreoUtil.getCurrentUsername();
        this.baseMapper.update(user, new LambdaQueryWrapper<SystemUser>().eq(SystemUser::getUsername, currentUsername));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void updatePassword(String password) {
        SystemUser user = new SystemUser();
        user.setPassword(passwordEncoder.encode(password));
        String currentUsername = OreoUtil.getCurrentUsername();
        this.baseMapper.update(user, new LambdaQueryWrapper<SystemUser>().eq(SystemUser::getUsername, currentUsername));
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public void resetPassword(String[] usernames) {
        SystemUser params = new SystemUser();
        params.setPassword(passwordEncoder.encode(SystemUser.DEFAULT_PASSWORD));

        List<String> list = Arrays.asList(usernames);
        this.baseMapper.update(params, new LambdaQueryWrapper<SystemUser>().in(SystemUser::getUsername, list));

    }

    private void setUserRoles(SystemUser user, String[] roles) {
        List<UserRole> userRoles = new ArrayList<>();
        // 这里使用了stream流技术，把角色字符串数组中的ID和用户ID保存在集合中
        Arrays.stream(roles).forEach(roleId -> {
            UserRole userRole = new UserRole();
            userRole.setUserId(user.getUserId());
            userRole.setRoleId(Long.valueOf(roleId));
            userRoles.add(userRole);
        });
        // 批量新增
        // 因为新增和修改都用了这个函数，这个saveBatch有可能是 不存在就新增，存在就修改
        userRoleService.saveBatch(userRoles);
    }

    private void setUserDataPermissions(SystemUser user, String[] deptIds) {
        List<UserDataPermission> userDataPermissions = new ArrayList<>();
        Arrays.stream(deptIds).forEach(deptId -> {
            UserDataPermission permission = new UserDataPermission();
            permission.setDeptId(Long.valueOf(deptId));
            permission.setUserId(user.getUserId());
            userDataPermissions.add(permission);
        });
        userDataPermissionService.saveBatch(userDataPermissions);
    }

    private boolean isCurrentUser(Long id) {
        CurrentUser currentUser = OreoUtil.getCurrentUser();
        return currentUser != null && id.equals(currentUser.getUserId());
    }
}
