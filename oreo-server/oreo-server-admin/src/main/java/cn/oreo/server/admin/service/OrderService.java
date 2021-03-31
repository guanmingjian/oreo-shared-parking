package cn.oreo.server.admin.service;

import cn.oreo.common.model.entity.po.Order;
import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.common.util.common.QueryRequest;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author GuanMingJian
 * @since 2021-01-29
 */
public interface OrderService extends IService<Order> {

    /**
     * 查询订单列表
     * @param queryRequest
     * @param order
     * @return
     */
    OreoResponse userList(QueryRequest queryRequest, Order order);

    /**
     * 修改订单信息
     * @param order
     */
    void updateOrder(Order order);

    /**
     * 取消订单
     * @param orderId
     */
    void cancelOrder(Long orderId);
}
