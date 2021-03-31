package cn.oreo.server.parking.service;

import cn.oreo.common.model.entity.dto.server.order.AssignParkingResult;
import cn.oreo.common.model.entity.dto.server.order.ReserveDto;
import cn.oreo.common.model.entity.dto.server.parking.GiveBackParkingDto;

import java.text.ParseException;

/**
 * 车位相关操作
 *
 * @author XuMin
 */
public interface ParkingService {

    /**
     * 预约时分配最佳车位
     *
     * @param  reserveDto  预约条件
     * @return 车位信息
     */
    AssignParkingResult assignBestParking(ReserveDto reserveDto);

    /**
     * 取消预约时归还车位
     *
     * @param  giveBackParkingDto  归还车位Dto(parkingId, parkingStartTime, parkingEndTime)
     */
    void giveBackParking(GiveBackParkingDto giveBackParkingDto) throws ParseException;
}
