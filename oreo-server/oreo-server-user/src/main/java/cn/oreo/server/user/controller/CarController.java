package cn.oreo.server.user.controller;

import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.server.user.service.CarService;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * 车辆信息控制器
 *
 * @author XuMin
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/car")
public class CarController {

    @Resource
    private CarService carService;

    /**
     * 根据用户id查询车辆信息
     * @param userId 用户id
     */
    @GetMapping("/query/{userId}")
    public OreoResponse carList(@NotNull @PathVariable("userId") @ApiParam("userId不可为空") Long userId) {
        return carService.carList(userId);
    }

    /**
     * 解绑用户的车牌号码
     * @param userId     用户id
     * @param carNumber  车牌号码
     */
    @DeleteMapping("/delete/{userId}/{carNumber}")
    public OreoResponse deleteCar(@NotNull @PathVariable("userId") @ApiParam("userId不可为空") Long userId,
                                  @NotNull @PathVariable("carNumber") @ApiParam("carNumber不为空") String carNumber) {
        return carService.deleteCar(userId, carNumber);
    }
}
