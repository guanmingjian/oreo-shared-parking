package cn.oreo.server.admin.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.oreo.common.model.entity.po.CommunityPhoto;
import cn.oreo.common.model.entity.po.Order;
import cn.oreo.common.model.entity.po.Reserve;
import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.common.util.common.OreoUtil;
import cn.oreo.common.util.common.QueryRequest;
import cn.oreo.server.admin.mapper.CommunityPhotoMapper;
import cn.oreo.server.admin.mapper.OrderMapper;
import cn.oreo.server.admin.service.OrderService;
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
public class OrderServiceImpl extends ServiceImpl<OrderMapper, Order> implements OrderService {

    @Resource
    private OrderMapper orderMapper;

    @Resource
    private CommunityPhotoMapper communityPhotoMapper;

    @Override
    public OreoResponse userList(QueryRequest queryRequest, Order order) {

        // 分页配置
        Page<Order> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());

        // 查询配置
        QueryWrapper queryWrapper = new QueryWrapper();

        // 分页、模糊查询、排序、是否转下划线
        OreoUtil.handleWrapperPageSort(queryRequest,queryWrapper,order,queryRequest.getIsToUnderlineCase());

        // 分页查询
        Page selectPage = orderMapper.selectPage(page,queryWrapper);

        List<Order> records = selectPage.getRecords();

        records.forEach(orderOne -> {
            Long communityId = orderOne.getCommunityId();
            QueryWrapper<CommunityPhoto> communityPhotoQueryWrapper = new QueryWrapper<>();
            communityPhotoQueryWrapper.eq("community_id",communityId);
            List<CommunityPhoto> communityPhotoList = communityPhotoMapper.selectList(communityPhotoQueryWrapper);
            orderOne.setCommunityPhotoList(communityPhotoList);
        });

        // 封装返回对象
        Map<String, Object> dataTable = OreoUtil.getDataTable(selectPage);

        return OreoResponse.successData(dataTable);
    }

    @Override
    public void updateOrder(Order order) {
        order.setUpdateTime(DateUtil.date());
        orderMapper.updateById(order);
    }

    @Override
    public void cancelOrder(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        // 订单状态(1、进行时   2、完成  3、取消)
        order.setOrderStatus("3");
        order.setCancelTime(DateUtil.date());
        orderMapper.updateById(order);
    }
}