package cn.oreo.server.order.service;

import cn.oreo.common.model.entity.dto.server.order.AssignParkingResult;
import cn.oreo.common.model.entity.dto.server.order.ReserveDto;
import cn.oreo.common.model.entity.dto.server.parking.GiveBackParkingDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.text.ParseException;

/**
 * @author GuanMingJian
 * @since 2021/1/20
 */
@FeignClient(value = "oreo-server-parking")
public interface ParkingServiceRemote {

    /**
     * 预约时分配最佳车位
     * @param reserveDto
     * @return
     */
    @GetMapping("/assign")
    AssignParkingResult assignBestParking(@RequestBody ReserveDto reserveDto);

    /**
     * 归还预约时间
     * @param giveBackParkingDto
     */
    @PostMapping("/giveback")
    void giveBackParking(@RequestBody GiveBackParkingDto giveBackParkingDto);
}
