package cn.oreo.server.order.service;

import cn.oreo.common.model.entity.dto.server.order.OrderDto;
import cn.oreo.common.model.entity.dto.server.order.ReserveDto;
import cn.oreo.common.model.entity.po.Order;
import cn.oreo.common.model.entity.po.Reserve;
import cn.oreo.common.util.common.OreoResponse;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.time.LocalTime;
import java.util.Map;

/**
 * @author GuanMingJian
 * @since 2020/11/10
 */
public interface OrderService {

    /**
     * 提交订单
     * @param orderDto
     * @return
     */
    OreoResponse commitOrder(OrderDto orderDto);

    /**
     * 取消订单
     * @return
     */
    OreoResponse cancelOrder(Long orderId);

    /**
     * 订单列表
     * @return
     */
    OreoResponse orderList(Long id,Integer orderStatus);

    /**
     * 查看某个订单的信息
     * @param orderId
     * @return
     */
    OreoResponse orderDetail(Long orderId);

    /**
     * 订单完成
     * @param orderId
     * @return
     */
    OreoResponse completeOrder(Long orderId);

    /**
     * 搜索订单
     * @param order
     * @return
     */
    OreoResponse orderSearch(Order order);



}
