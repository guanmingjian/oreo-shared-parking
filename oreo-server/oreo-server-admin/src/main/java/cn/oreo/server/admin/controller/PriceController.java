package cn.oreo.server.admin.controller;

import cn.oreo.common.model.entity.dto.server.admin.TimeFeeDto;
import cn.oreo.common.model.entity.dto.server.admin.UnitFeeDto;
import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.common.util.common.QueryRequest;
import cn.oreo.server.admin.service.PriceService;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

@Slf4j
@RestController("/price")
public class PriceController {

    @Autowired
    private PriceService priceService;

    /**
     * 新增收费标准
     * @return
     */
    @PostMapping("/timeFee")
    @ApiOperation("新增包时套餐收费标准")
    public OreoResponse setTimeFee(TimeFeeDto timeFeeDto){
        return priceService.setTimeFee(timeFeeDto);
    }

    @PutMapping("/timeFee")
    @ApiOperation("编辑包时套餐收费标准")
    public OreoResponse editTimeFee(TimeFeeDto timeFeeDto){
        return priceService.editTimeFee(timeFeeDto);
    }

    @PostMapping("/unitFee")
    @ApiOperation("新增计时收费标准")
    public OreoResponse setUnitFee(UnitFeeDto unitFeeDto){
        return priceService.setUnitFee(unitFeeDto);
    }

    @PutMapping("/unitFee")
    @ApiOperation("编辑计时收费标准")
    public OreoResponse editUnitFee(UnitFeeDto unitFeeDto){
        return priceService.editUnitFee(unitFeeDto);
    }


    /**
     * 修改社区担保金金额
     * @param earnestPrice
     * @param communityId
     * @return
     */
    @PutMapping("/earnestPrice")
    @ApiOperation("编辑担保金")
    public OreoResponse editEarnestPrice(@NotNull BigDecimal earnestPrice,
                                         @NotNull Long communityId){
        System.out.println("编辑担保金");
        return priceService.editEarnestPrice(earnestPrice,communityId);
    }

    /**
     * 新增社区担保金
     * @param earnestPrice
     * @param communityId
     * @return
     */
    @PostMapping("/earnestPrice")
    @ApiOperation("设置担保金")
    public OreoResponse earnestPrice(@NotNull BigDecimal earnestPrice,
                                     @NotNull Long communityId){
        System.out.println("设置担保金");
        return priceService.setEarnestPrice(earnestPrice,communityId);
    }

    @GetMapping("/query")
    @ApiOperation("查看所有收费标准和定金(无排序功能)")
    public OreoResponse queryFee(QueryRequest queryRequest, Long communityId){
        System.out.println("communityId:"+communityId);
        return priceService.queryFee(queryRequest,communityId);
    }

}
