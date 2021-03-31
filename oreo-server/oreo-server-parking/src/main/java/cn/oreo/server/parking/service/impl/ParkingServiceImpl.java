package cn.oreo.server.parking.service.impl;

import cn.oreo.common.model.entity.dto.server.order.AssignParkingResult;
import cn.oreo.common.model.entity.dto.server.order.ReserveDto;
import cn.oreo.common.model.entity.dto.server.parking.GiveBackParkingDto;
import cn.oreo.common.model.entity.po.ParkingTime;
import cn.oreo.server.parking.mapper.ParkingTimeMapper;
import cn.oreo.server.parking.service.ParkingService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ParkingServiceImpl implements ParkingService {

    @Resource
    private ParkingTimeMapper parkingTimeMapper;

    @Override
    public AssignParkingResult assignBestParking(ReserveDto reserveDto) {
        QueryWrapper<ParkingTime> queryWrapper = new QueryWrapper<>();
        List<ParkingTime> parkingTimes = null;
        AssignParkingResult assignParkingResult = new AssignParkingResult();
        try {
            queryWrapper.eq("community_id", reserveDto.getCommunityId());
            queryWrapper.eq("parking_start_date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
            parkingTimes = parkingTimeMapper.selectList(queryWrapper);
            // 若无空闲车位，return
            if (parkingTimes.isEmpty()) {
                assignParkingResult.setIsSurplus(false);
                return assignParkingResult;
            }
            // 升序排序，先按parkingStartTime升序排序，若相等，在按parkingEndTime升序排序
            parkingTimes.sort((o1, o2) -> {
                if (!o1.getParkingStartTime().equals(o2.getParkingStartTime())) {
                    return o2.getParkingStartTime() - o1.getParkingStartTime();
                }
                return o2.getParkingEndTime() - o1.getParkingEndTime();
            });
            for (ParkingTime p : parkingTimes) {
                // 找到最佳时间的车位
                if (p.getParkingStartTime() <= reserveDto.getReserveStartTime() && p.getParkingEndTime() >= reserveDto.getReserveEndTime()) {
                    assignParkingResult.setIsSurplus(true);               // 是否有车位
                    assignParkingResult.setParkingId(p.getParkingId());   // 车位id
                    assignParkingResult.setParkingTimeId(p.getId());      // parkingTime id
                    return assignParkingResult;
                }
                // 没有适合的停车位，不必再循环，return
                if (p.getParkingStartTime() > reserveDto.getReserveStartTime()) {
                    assignParkingResult.setIsSurplus(false);
                    return assignParkingResult;
                }
            }
            // 没有适合的停车位
            assignParkingResult.setIsSurplus(false);
            return assignParkingResult;
        } catch (Exception e) {
            e.printStackTrace();
            assignParkingResult.setIsSurplus(false);
            return assignParkingResult;
        }
    }

    @Override
    public void giveBackParking(GiveBackParkingDto giveBackParkingDto) throws ParseException {
        QueryWrapper<ParkingTime> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parking_id", giveBackParkingDto.getParkingId());
        queryWrapper.eq("community_id", giveBackParkingDto.getCommunityId());
        queryWrapper.eq("parking_start_date", new SimpleDateFormat("yyyy-MM-dd").format(new Date()));
        List<ParkingTime> parkingTimes = parkingTimeMapper.selectList(queryWrapper);

        Vector<ParkingTime> temp = new Vector<>();
        // 将所需合并的时间段加入到vector中
        for (ParkingTime parkingTime : parkingTimes) {
            if (parkingTime.getParkingEndTime().equals(giveBackParkingDto.getParkingStartTime()) || parkingTime.getParkingStartTime()
                    .equals(giveBackParkingDto.getParkingEndTime())) {
                temp.add(parkingTime);
            }
        }
        if (temp.size() == 0) {
            // 若无需合并的时间段，则将该取消预约的车位直接归还数据库
            ParkingTime giveBackPt = new ParkingTime();
            giveBackPt.setParkingId(giveBackParkingDto.getParkingId());
            giveBackPt.setCommunityId(giveBackParkingDto.getCommunityId());
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            giveBackPt.setParkingStartDate(simpleDateFormat.parse(simpleDateFormat.format(new Date())));
            giveBackPt.setParkingStartTime(giveBackParkingDto.getParkingStartTime());
            giveBackPt.setParkingEndTime(giveBackParkingDto.getParkingEndTime());
            // 插入一个新的时间段，自定义生成雪花算法id
            long id = IdWorker.getId(giveBackPt);
            giveBackPt.setId(id);
            parkingTimeMapper.insert(giveBackPt);
        } else if (temp.size() == 1) {
            // 若合并的时间段为1个
            ParkingTime giveBackPt = temp.get(0);
            // 更新一个时间段
            if (giveBackPt.getParkingStartTime().equals(giveBackParkingDto.getParkingEndTime())) {
                giveBackPt.setParkingStartTime(giveBackParkingDto.getParkingStartTime());
            } else {
                giveBackPt.setParkingEndTime(giveBackParkingDto.getParkingEndTime());
            }
            parkingTimeMapper.updateById(giveBackPt);
        } else {
            // 若合并的时间段为2个
            // 先排序，升序
            temp.sort((o1, o2) -> o2.getParkingStartTime() - o1.getParkingEndTime());
            // 更新一个时间段
            ParkingTime giveBackPt = temp.get(0);
            giveBackPt.setParkingEndTime(temp.get(1).getParkingEndTime());
            parkingTimeMapper.updateById(giveBackPt);
            // 删除一个时间段
            QueryWrapper<ParkingTime> wrapper = new QueryWrapper<>();
            wrapper.eq("id", temp.get(1).getId());
            parkingTimeMapper.delete(wrapper);
        }
    }
}
