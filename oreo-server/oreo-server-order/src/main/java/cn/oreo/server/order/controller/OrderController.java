package cn.oreo.server.order.controller;


import cn.oreo.common.model.entity.dto.server.order.OrderDto;
import cn.oreo.common.model.entity.po.Order;
import cn.oreo.common.util.common.OreoResponse;


import cn.oreo.server.order.service.OrderService;

import io.swagger.annotations.ApiOperation;

import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;


/**
 * @author GuanMingJian
 * @since 2020/11/10
 */
@RestController
public class OrderController {

    @Resource
    private OrderService orderService;

    /**
     * 订单提交
     * @param orderDto
     * @return
     */
    @PostMapping("commit")
    @ApiOperation("订单提交")
    public OreoResponse commitOrder(@Valid OrderDto orderDto){

        return orderService.commitOrder(orderDto);
    }

    /**
     * 订单完成
     * @param orderId
     * @return
     */
    @PostMapping("complete")
    @ApiOperation("订单完成")
    public OreoResponse completeOrder(@NotNull @RequestParam Long orderId){

        return orderService.completeOrder(orderId);
    }

    /**
     * 取消订单(意外处理)
     * @param orderId
     * @return
     */
    @DeleteMapping("/cancel")
    @ApiOperation("订单取消")
    public OreoResponse cancelOrder(@NotNull @RequestParam Long orderId){
        return orderService.cancelOrder(orderId);
    }

    /**
     * 查看订单列表
     * @return
     */
    @GetMapping("/list")
    @ApiOperation("查看订单列表")
    public OreoResponse orderList(Long userId, Integer orderStatus){
        return orderService.orderList(userId,orderStatus);
    }

    /**
     * 查看某个订单
     * @return
     */
    @GetMapping("/detail")
    @ApiOperation("查看订单详情")
    public OreoResponse orderDetail(Long orderId){
        return orderService.orderDetail(orderId);
    }

    /**
     * 搜索订单
     * @return
     */
    @GetMapping("/search")
    @ApiOperation("搜索订单")
    public OreoResponse orderSearch(Order order){
        return orderService.orderSearch(order);
    }



}
