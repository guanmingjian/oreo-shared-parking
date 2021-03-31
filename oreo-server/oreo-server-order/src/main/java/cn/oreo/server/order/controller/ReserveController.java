package cn.oreo.server.order.controller;


import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.oreo.common.model.entity.dto.server.order.CancelReserveDto;
import cn.oreo.common.model.entity.dto.server.order.ReserveDto;
import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.common.util.constant.OreoConstant;
import cn.oreo.server.order.service.ReserveService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.UnsupportedEncodingException;

/**
 * <p>
 * 预约表 前端控制器
 * </p>
 *
 * @author GuanMingJian
 * @since 2021-01-22
 */
@Slf4j
@RestController
@RequestMapping("/reserve")
public class ReserveController {

    @Resource
    private ReserveService reserveService;

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 查看预约信息
     */
    @GetMapping
    @ApiOperation("查看预约信息列表")
    public OreoResponse reserveList(@NotNull @ApiParam("用户id") @RequestParam Long UserId){
        return reserveService.reserveList(UserId);
    }

    /**
     * 查看预约详情
     */
    @GetMapping("/detail")
    @ApiOperation("查看预约详情信息")
    public OreoResponse reserveDetail(@NotNull @ApiParam("预约id") @RequestParam Long reserveId){
        return reserveService.reserveDetail(reserveId);
    }

    /**
     * 预约停车位
     */
    @PostMapping("commit")
    @ApiOperation("预约停车位")
    public OreoResponse reserveParking(@Valid @ApiParam("所有属性都是必要") ReserveDto reserveDto) throws UnsupportedEncodingException {
        return reserveService.reserveParking(reserveDto);
    }

    /**
     * 更改预约信息
     */
    @PutMapping("update")
    @ApiOperation("更改预约信息")
    public OreoResponse reserveUpdateParking(@Valid ReserveDto reserveDto){
        return reserveService.reserveUpdateParking(reserveDto);
    }

    /**
     * 取消预约
     */
    @PostMapping("cancel")
    @ApiOperation("取消预约")
    public OreoResponse reserveCancelParking(Long reserveId) {

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

    /**
     * 获取小区详细信息
     */
    @PostMapping("community/{id}")
    @ApiOperation("获取小区详细信息")
    public OreoResponse communityById(@PathVariable String communityId) {

        return reserveService.communityById(communityId);
    }
}

