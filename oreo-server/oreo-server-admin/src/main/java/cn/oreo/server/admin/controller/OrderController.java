package cn.oreo.server.admin.controller;

import cn.oreo.common.model.entity.po.Order;
import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.common.util.common.QueryRequest;
import cn.oreo.server.admin.service.OrderService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * @author GuanMingJian
 * @since 2021/1/29
 */
@Slf4j
@RestController
@RequestMapping("/order")
public class OrderController {

    @Resource
    private OrderService orderService;

    @GetMapping
    @ApiOperation(value = "查看订单列表",notes = "如需查看不同的小区订单，请传communityId")
    //@PreAuthorize("hasAuthority('order:view')")
    public OreoResponse orderList(
            QueryRequest queryRequest,
            Order order
    ){
        return orderService.userList(queryRequest,order);
    }

    @PutMapping
    @ApiOperation(value = "修改订单信息",notes = "敏感信息不可修改")
    //@PreAuthorize("hasAuthority('order:update')")
    public void updateOrder(@Valid Order order){

        orderService.updateOrder(order);
    }

    @PostMapping("cancel")
    @ApiOperation(value = "取消订单",notes = "意外情况取消或不满x分钟")
    //@PreAuthorize("hasAuthority('order:cancel')")
    public void cancelOrder(@NotNull @RequestParam Long orderId){

        orderService.cancelOrder(orderId);
    }
}
