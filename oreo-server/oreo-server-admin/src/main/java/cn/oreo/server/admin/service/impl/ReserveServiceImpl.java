package cn.oreo.server.admin.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.oreo.common.model.entity.po.CommunityPhoto;
import cn.oreo.common.model.entity.po.Reserve;
import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.common.util.common.OreoUtil;
import cn.oreo.common.util.common.QueryRequest;
import cn.oreo.server.admin.mapper.CommunityPhotoMapper;
import cn.oreo.server.admin.mapper.ReserveMapper;
import cn.oreo.server.admin.service.ReserveService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author GuanMingJian
 * @since 2021-01-29
 */
@Service
public class ReserveServiceImpl extends ServiceImpl<ReserveMapper, Reserve> implements ReserveService {

    @Resource
    private ReserveMapper reserveMapper;

    @Resource
    private CommunityPhotoMapper communityPhotoMapper;

    @Override
    public OreoResponse reserveList(QueryRequest queryRequest, Reserve reserve) {
        // 分页配置
        Page<Reserve> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());

        // 查询配置
        QueryWrapper queryWrapper = new QueryWrapper();

        // 分页、模糊查询、排序、是否转下划线
        OreoUtil.handleWrapperPageSort(queryRequest,queryWrapper,reserve,queryRequest.getIsToUnderlineCase());

        // 分页查询
        Page selectPage = reserveMapper.selectPage(page,queryWrapper);

        List<Reserve> records = selectPage.getRecords();
        
        records.forEach(reserveOne -> {
            Long communityId = reserveOne.getCommunityId();
            QueryWrapper<CommunityPhoto> communityPhotoQueryWrapper = new QueryWrapper<>();
            communityPhotoQueryWrapper.eq("community_id",communityId);
            List<CommunityPhoto> communityPhotoList = communityPhotoMapper.selectList(communityPhotoQueryWrapper);
            reserveOne.setCommunityPhotoList(communityPhotoList);
        });

        // 封装返回对象
        Map<String, Object> dataTable = OreoUtil.getDataTable(selectPage);

        return OreoResponse.successData(dataTable);
    }

    @Override
    public void updateReserve(Reserve reserve) {
        reserve.setUpdateTime(DateUtil.date());
        reserveMapper.updateById(reserve);
    }
}