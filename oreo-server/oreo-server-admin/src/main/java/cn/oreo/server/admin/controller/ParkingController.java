package cn.oreo.server.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.oreo.common.model.entity.dto.server.admin.ParkingDto;
import cn.oreo.common.model.entity.po.Parking;
import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.common.util.common.QueryRequest;
import cn.oreo.common.util.constant.StringConstant;
import cn.oreo.server.admin.service.ParkingService;
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
@RequestMapping("/parking")
public class ParkingController {

    @Resource
    private ParkingService parkingService;

    @GetMapping
    @ApiOperation(value = "查看停车位列表",notes = "")
    //@PreAuthorize("hasAuthority('parking:view')")
    public OreoResponse parkingList(
            QueryRequest queryRequest,
            Parking parking
    ){

        return parkingService.parkingList(queryRequest,parking);
    }

    @PutMapping
    @ApiOperation(value = "修改停车位信息",notes = "")
    //@PreAuthorize("hasAuthority('parking:update')")
    public OreoResponse updateParking(Parking parking){

        parkingService.updateParking(parking);

        return OreoResponse.success();
    }

    @PostMapping
    @ApiOperation(value = "新增停车位",notes = "")
    //@PreAuthorize("hasAuthority('parking:add')")
    public OreoResponse addParking(@Valid ParkingDto parkingDto){

        Parking parking = new Parking();
        BeanUtil.copyProperties(parkingDto,parking);

        parkingService.addParking(parking);

        return OreoResponse.success();
    }

    @DeleteMapping("/{parkingIds}")
    //@PreAuthorize("hasAuthority('parking:delete')")
    public OreoResponse deleteParkings(@NotNull @PathVariable String parkingIds){

        String[] ids = parkingIds.split(StringConstant.COMMA);

        parkingService.deleteParkings(ids);

        return OreoResponse.success();
    }
}
