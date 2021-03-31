package cn.oreo.server.parking.controller;

import cn.oreo.common.model.entity.dto.server.order.AssignParkingResult;
import cn.oreo.common.model.entity.dto.server.order.ReserveDto;
import cn.oreo.common.model.entity.dto.server.parking.GiveBackParkingDto;
import cn.oreo.server.parking.service.ParkingService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;

@RestController
public class ParkingController {

    @Resource
    private ParkingService parkingServiceImpl;

    @PostMapping("/assign")
    public AssignParkingResult assignBestParking(@RequestBody ReserveDto reserveDto) {
        return parkingServiceImpl.assignBestParking(reserveDto);
    }

    @PostMapping("/giveback")
    public void giveBackParking(@RequestBody GiveBackParkingDto giveBackParkingDto) throws ParseException {
        parkingServiceImpl.giveBackParking(giveBackParkingDto);
    }
}
