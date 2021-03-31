package cn.oreo.server.admin.controller;

import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.oreo.common.model.entity.dto.server.order.CancelReserveDto;
import cn.oreo.common.model.entity.po.Order;
import cn.oreo.common.model.entity.po.Reserve;
import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.common.util.common.QueryRequest;
import cn.oreo.common.util.constant.OreoConstant;
import cn.oreo.server.admin.service.OrderService;
import cn.oreo.server.admin.service.ReserveService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;

/**
 * @author GuanMingJian
 * @since 2021/1/29
 */
@Slf4j
@RestController
@RequestMapping("/reserve")
public class ReserveController {

    @Resource
    private ReserveService reserveService;

    @Resource
    private RabbitTemplate rabbitTemplate;

    @GetMapping
    @ApiOperation(value = "查看预约列表",notes = "如需查看不同的小区预约，请传communityId")
    //@PreAuthorize("hasAuthority('reserve:view')")
    public OreoResponse reserveList(
            QueryRequest queryRequest,
            Reserve reserve
    ){

        return reserveService.reserveList(queryRequest,reserve);
    }

    @PutMapping
    @ApiOperation(value = "修改预约信息",notes = "敏感信息不可修改")
    //@PreAuthorize("hasAuthority('reserve:update')")
    public void updateReserve(Reserve reserve){

        reserveService.updateReserve(reserve);
    }

    @PostMapping("/cancel")
    @ApiOperation(value = "取消预约",notes = "")
    //@PreAuthorize("hasAuthority('reserve:cancel')")
    public OreoResponse cancelReserve(Long reserveId){

        // 转换成json，cancelType:3 手动取消
        JSONObject jsonObject = JSONUtil.parseObj(new CancelReserveDto(3,reserveId));

        try {
            // 插入取消预约消息队列
            rabbitTemplate.convertAndSend(OreoConstant.DELAYED_EXCHANGE_NAME, OreoConstant.DELAYED_ROUTING_KEY, jsonObject.toString(), a ->{
                a.getMessageProperties().setDelay(0);
                return a;
            });

        } catch (AmqpException e) {
            e.printStackTrace();
            log.info(OreoConstant.MESSAGE_SYSTEM_INTERNAL_FAILED,e.getMessage());
            return OreoResponse.failure(OreoConstant.RESERVE_CANCEL_FAILURE);
        }
        return OreoResponse.successMessage(OreoConstant.RESERVE_CANCEL_SUCCESS);
    }

}
