package cn.oreo.server.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import cn.oreo.common.model.entity.dto.server.order.AssignParkingResult;
import cn.oreo.common.model.entity.dto.server.order.CancelReserveDto;
import cn.oreo.common.model.entity.dto.server.order.ReserveDto;
import cn.oreo.common.model.entity.dto.server.parking.GiveBackParkingDto;
import cn.oreo.common.model.entity.po.*;
import cn.oreo.common.util.common.CommonCode;
import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.common.util.constant.OreoConstant;
import cn.oreo.common.util.exception.PentiumException;
import cn.oreo.server.order.mapper.*;
import cn.oreo.server.order.service.ParkingServiceRemote;
import cn.oreo.server.order.service.ReserveService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.AmqpException;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 预约表 服务实现类
 * </p>
 *
 * @author GuanMingJian
 * @since 2021-01-22
 */
@Service
@Slf4j
public class ReserveServiceImpl extends ServiceImpl<ReserveMapper, Reserve> implements ReserveService {

    @Resource
    private ParkingMapper parkingMapper;

    @Resource
    private ParkingTimeMapper parkingTimeMapper;

    @Resource
    private ReserveMapper reserveMapper;

    @Resource
    private CommunityMapper communityMapper;

    @Autowired
    private EarnestPriceMapper earnestPriceMapper;

    @Resource
    private CommunityPhotoMapper communityPhotoMapper;

    @Resource
    private ParkingServiceRemote parkingServiceRemote;

    @Resource
    private RabbitTemplate rabbitTemplate;

    /**
     * 使用redis 1库
     */
    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    @Transactional
    @Override
    public OreoResponse reserveParking(ReserveDto reserveDto) {

        // 判断结束时间是否大于开始时间
        if(reserveDto.getReserveStartTime()>reserveDto.getReserveEndTime()){
            return OreoResponse.failure(OreoConstant.RESERVE_TIME_EXCEPTION);
        }

        // 远程调用oreo-server-parking模块，分配最佳停车位
        AssignParkingResult assignParkingResult = parkingServiceRemote.assignBestParking(reserveDto);
        System.out.println("assignParkingResult:"+assignParkingResult);

        // 插入预约信息
        if(assignParkingResult.getIsSurplus()){

            // 从社区表中获取担保金金额
            QueryWrapper<CommunityEarnestPrice> communityEarnestPriceQueryWrapper = new QueryWrapper<>();
            communityEarnestPriceQueryWrapper.eq("community_id",reserveDto.getCommunityId());
            CommunityEarnestPrice communityEarnestPrice = earnestPriceMapper.selectOne(communityEarnestPriceQueryWrapper);

            Reserve reserve = new Reserve();
            if(communityEarnestPrice!=null){
                reserve.setEarnestPrice(communityEarnestPrice.getEarnestPrice());
            } else {
                // 如果管理员没有设置担保金金额，则采取默认的15快
                reserve.setEarnestPrice(new BigDecimal(15));
            }
            reserve.setUserId(reserveDto.getUserId());
            reserve.setCarId(reserveDto.getCarId());
            reserve.setCommunityId(reserveDto.getCommunityId());
            reserve.setParkingId(assignParkingResult.getParkingId());
            // 插入停车位编号
            Parking parking = parkingMapper.selectById(assignParkingResult.getParkingId());
            if(StrUtil.isNotBlank(parking.getParkingSn())){
                reserve.setParkingSn(parking.getParkingSn());
            }

            // 插入收费方式
            reserve.setRates(reserveDto.getRates());

            reserve.setEnterTime(reserveDto.getReserveStartTime());
            reserve.setLeaveTime(reserveDto.getReserveEndTime());

            // 生成预约流水号
            reserve.setReserveSn(RandomUtil.randomNumbers(15));
            reserve.setReserveDate(DateUtil.parse(DateUtil.today()));
            reserve.setReserveTime(DateUtil.date());
            // 预约状态（1、预约成功 2、预约过期 3、手动取消 4、下单成功）
            reserve.setReserveStatus("1");
            // 支付状态（1、未支付 2、已支付 3、已退款）
            reserve.setPayStatus("1");

            // 冗余车辆车牌号
            reserve.setCarNumber(reserveDto.getCarNumber());
            // 冗余小区名字
            reserve.setCommunityName(reserveDto.getCommunityName());
            // 冗余停车位编号
            reserve.setCommunityAddress(reserveDto.getCommunityAddress());

            // 插入预约,自定义生成雪花算法id
            long id = IdWorker.getId(reserve);
            reserve.setId(id);
            try {
                reserveMapper.insert(reserve);
            } catch (Exception e) {
                e.printStackTrace();
                return CommonCode.returnException(e);
            }

            // 小区可用停车位数量减1
            Community community = communityMapper.selectById(reserveDto.getCommunityId());
            Integer spaceAvailableNumber = community.getSpaceAvailableNumber();
            // 判断可用停车位是否小于等于0
            if(spaceAvailableNumber<=0){
                throw new PentiumException(300,OreoConstant.MESSAGE_COMMUNITY_SPACE_TOTAL_NUMBER_SHORTAGE);
            }
            // 操作减1
            community.setSpaceAvailableNumber(spaceAvailableNumber-1);
            // 更新记录
            communityMapper.updateById(community);

            // 切割空闲的时间
            ParkingTime parkingTime;
            try {
                parkingTime = parkingTimeMapper.selectById(assignParkingResult.getParkingTimeId());
                // 将剩余的空闲时间重新写入数据库中
                newParkingTimeInsert(parkingTime, parkingTime.getParkingStartTime(), reserveDto.getReserveStartTime());
                newParkingTimeInsert(parkingTime, reserveDto.getReserveEndTime(), parkingTime.getParkingEndTime());
                // 将原来的空闲时间从表里面删除
                parkingTimeMapper.deleteById(parkingTime.getId());
            } catch (Exception e) {
                e.printStackTrace();
                return CommonCode.returnException(e);
            }

            // 转换成json,cancelType:2 自动取消
            JSONObject jsonObject = JSONUtil.parseObj(new CancelReserveDto(2,id));

            try {
                // 向rabbitmq消息队列中插入取消预约定时任务
                rabbitTemplate.convertAndSend(OreoConstant.DELAYED_EXCHANGE_NAME, OreoConstant.DELAYED_ROUTING_KEY, jsonObject.toString(), a ->{
                    // 设置定时时间
                    a.getMessageProperties().setDelay(OreoConstant.RESERVE_CANCEL_TIME);
                    return a;
                });
            } catch (AmqpException e) {
                e.printStackTrace();
                return CommonCode.returnException(e);
            }

            // 把用户id插入redis所在小区中，成为该小区的用户
            String key = "users_community:"+reserveDto.getCommunityId();
            redisTemplate.opsForSet().add(key,reserveDto.getUserId());

            Map<String,Long> data = new HashMap<>();
            data.put("orderId",id);

            // 返回成功信息
            return OreoResponse.successData(data);
        }
        // 返回失败信息
        return OreoResponse.failure(OreoConstant.RESERVE_FAILURE);
    }

    private void newParkingTimeInsert(ParkingTime parkingTime, int newStartTime, int newEndTime) {
        if (newStartTime != newEndTime) {
            ParkingTime newParkingTime = new ParkingTime();
            newParkingTime.setParkingId(parkingTime.getParkingId());
            newParkingTime.setParkingStartDate(parkingTime.getParkingStartDate());
            newParkingTime.setParkingStartTime(newStartTime);
            newParkingTime.setParkingEndTime(newEndTime);
            newParkingTime.setCommunityId(parkingTime.getCommunityId());
            long newId = IdWorker.getId(newParkingTime);
            newParkingTime.setId(newId);
            parkingTimeMapper.insert(newParkingTime);
        }
    }

    @Override
    public OreoResponse reserveUpdateParking(ReserveDto reserveDto) {

        Long reserveId = reserveDto.getReserveId();
        Reserve reserve;
        try {
            reserve = reserveMapper.selectById(reserveId);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonCode.returnException(e);
        }

        if(BeanUtil.isNotEmpty(reserve,"")){
            // 更新信息
            reserve.setUserId(reserveDto.getUserId());
            reserve.setCommunityId(reserveDto.getCommunityId());
            reserve.setCarId(reserveDto.getCarId());
            reserve.setEnterTime(reserveDto.getReserveStartTime());
            reserve.setLeaveTime(reserveDto.getReserveEndTime());
        }else {
            return OreoResponse.failure(OreoConstant.MESSAGE_QUERY_RESULT_EMPTY);
        }

        try {
            // 更新记录
            reserveMapper.updateById(reserve);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonCode.returnException(e);
        }
        // 返回成功信息
        return OreoResponse.successMessage(OreoConstant.RESERVE_UPDATE_SUCCESS);
    }

    @Override
    public OreoResponse reserveList(Long userId) {

        QueryWrapper<Reserve> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",userId);

        List<Reserve> reserves;
        try {
            reserves = reserveMapper.selectList(queryWrapper);

            // 查询小区图片信息
            reserves.forEach(reserve -> {
                QueryWrapper<CommunityPhoto> communityPhotoQueryWrapper = new QueryWrapper<>();
                communityPhotoQueryWrapper.eq("community_id",reserve.getCommunityId());
                List<CommunityPhoto> communityPhotoList = communityPhotoMapper.selectList(communityPhotoQueryWrapper);
                reserve.setCommunityPhotoList(communityPhotoList);
            });

        } catch (Exception e) {
            e.printStackTrace();
            return CommonCode.returnException(e);
        }
        return OreoResponse.successData(reserves);
    }

    @Override
    public OreoResponse reserveDetail(Long reserveId) {

        Reserve reserve = reserveMapper.selectById(reserveId);

        QueryWrapper<CommunityPhoto> communityPhotoQueryWrapper = new QueryWrapper<>();
        communityPhotoQueryWrapper.eq("community_id",reserve.getCommunityId());

        List<CommunityPhoto> communityPhotos = communityPhotoMapper.selectList(communityPhotoQueryWrapper);

        reserve.setCommunityPhotoList(communityPhotos);

        return OreoResponse.successData(reserve);
    }

    // RabbitMQ监听取消预约事件
    @RabbitListener(queues = OreoConstant.DELAYED_QUEUE_NAME)
    public void cancelReserveTTL(Message msg, Channel channel) throws IOException {
        cancelReserveBody(msg,channel);
    }

    // 取消预约
    @Transactional
    public void cancelReserveBody(Message msg, Channel channel) throws IOException {

        String message = new String(msg.getBody());

        CancelReserveDto cancelReserveDto = JSONUtil.toBean(message, CancelReserveDto.class);

        if(BeanUtil.isNotEmpty(cancelReserveDto)){

            Reserve reserve = null;
            try {
                reserve = reserveMapper.selectById(cancelReserveDto.getReserveId());
            } catch (Exception e) {
                e.printStackTrace();
                log.info(OreoConstant.MESSAGE_SYSTEM_INTERNAL_FAILED,e.getMessage());
            }

            // 判断是否已经被手动取消
            if(!"1".equals(reserve.getReserveStatus())){
                log.info(cancelReserveDto.getReserveId()+":"+OreoConstant.RESERVE_STATUS_INVALID);
            }else {
                // 预约状态（1、预约成功 2、预约过期 3、手动取消 4、下单成功）
                if(cancelReserveDto.getCancelType()==2){
                    reserve.setReserveStatus("2");
                }else if(cancelReserveDto.getCancelType()==3){
                    reserve.setReserveStatus("3");
                }

                reserve.setReserveCancelTime(DateUtil.date());

                // 更新reserve记录
                reserveMapper.updateById(reserve);

                // 归还该停车位的可用时间段
                GiveBackParkingDto giveBackParkingDto = new GiveBackParkingDto();
                giveBackParkingDto.setCommunityId(reserve.getCommunityId());
                giveBackParkingDto.setParkingId(reserve.getParkingId());
                giveBackParkingDto.setParkingStartTime(reserve.getEnterTime());
                giveBackParkingDto.setParkingEndTime(reserve.getLeaveTime());
                parkingServiceRemote.giveBackParking(giveBackParkingDto);

                // 小区可用停车位数量加1
                Community community = communityMapper.selectById(reserve.getCommunityId());
                community.setSpaceAvailableNumber(community.getSpaceAvailableNumber()-1);
                // 更新记录
                communityMapper.updateById(community);

            }

            channel.basicAck(msg.getMessageProperties().getDeliveryTag(), false);

        }else {
            log.info(OreoConstant.MESSAGE_SYSTEM_INTERNAL_FAILED);
        }
    }

    @Override
    public OreoResponse communityById(String communityId) {
        Community community = communityMapper.selectById(communityId);

        return OreoResponse.successData(community);
    }
}
