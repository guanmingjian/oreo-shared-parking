package cn.oreo.server.order.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.oreo.common.model.entity.dto.server.order.OrderDto;
import cn.oreo.common.model.entity.po.*;
import cn.oreo.common.util.common.CommonCode;
import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.common.util.common.TimeUtils;
import cn.oreo.common.util.constant.OreoConstant;
import cn.oreo.server.order.mapper.*;
import cn.oreo.server.order.service.OrderService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;
import javax.annotation.Resource;
import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * @author GuanMingJian
 * @since 2020/11/10
 */
@Service
@Slf4j
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderMapper orderMapper;

    @Resource
    private ReserveMapper reserveMapper;

    @Resource
    private CommunityPhotoMapper communityPhotoMapper;

    @Autowired
    private TimeFeeMapper timeFeeMapper;

    @Autowired
    private UnitFeeMapper unitFeeMapper;


    private final static Integer PRICE = 2;
    private final static Integer TIME = 8;
    private final static  Integer TIMEFEE = 12;


    @Override
    @Transactional  // 事务回滚
    public OreoResponse commitOrder(OrderDto orderDto) {

        // 1. 查询是否有预约，查到即是有预约，传用户id等
        QueryWrapper<Reserve> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("user_id",orderDto.getUserId());
        queryWrapper.eq("reserve_status","1");
        Reserve reserve = null;
        try {
            reserve = reserveMapper.selectOne(queryWrapper);
            // 预约状态（1、预约成功 2、预约过期 3、手动取消  4、下单成功）
            reserve.setReserveStatus("4");
            reserveMapper.updateById(reserve);
        } catch (Exception e) {
            e.printStackTrace();
            CommonCode.returnException(e);
        }

        // 2. 预约时已经检查了库存，那么在提交订单时，不需要再次检查库存
        if(BeanUtil.isNotEmpty(reserve,"")){
            // 3. 生成订单流水号
            String orderSn = RandomUtil.randomNumbers(15);

            Order order = new Order();
            // 属性赋值
            BeanUtil.copyProperties(orderDto, order);

            // 不确定下订单过程中，前端OrderDto是否会带来以下信息，后面再做删减
            order.setUserId(orderDto.getUserId());
            order.setCommunityId(reserve.getCommunityId());
            order.setCarId(orderDto.getCarId());
            order.setParkingId(reserve.getParkingId());
            order.setEnterTime(reserve.getEnterTime());
            order.setLeaveTime(reserve.getLeaveTime());
            order.setRates(reserve.getRates());// 收费方式

            // 冗余车辆车牌号
            // 冗余小区名字
            // 冗余停车位编号

            // 添加订单流水号
            order.setOrderSn(orderSn);

            // 4. 设置未支付标识符，设置订单状态
            // 订单状态(1、进行时   2、完成)
            order.setOrderStatus("1");
            // 支付状态（1、未支付   2、已支付    3、已退款）
            order.setPayStatus("1");

            // 下单时间
            order.setAddTime(DateUtil.date());

            order.setDeleted("0");

            // 5. 插入订单
            try {
                orderMapper.insert(order);
            } catch (Exception e) {
                e.printStackTrace();
                return CommonCode.returnException(e);
            }
            return OreoResponse.successMessage(OreoConstant.MESSAGE_OPERATE_SUCCESS);
        }else {
            return OreoResponse.failure(OreoConstant.MESSAGE_ORDER_NOT_RESERVE);
        }
    }

    @Override
    public OreoResponse completeOrder(Long orderId) {

        Order order = null;
        try {
            order = orderMapper.selectById(orderId);
        } catch (Exception e) {
            e.printStackTrace();
            CommonCode.returnException(e);
        }
        // 订单状态(1、进行时  2、完成)
        order.setOrderStatus("2");
        order.setCompleteTime(DateUtil.date());

        BigDecimal price = null;

        // 订单完成之后，计算金额
        if(order.getRates() == 2) {
            // 2 是包时套餐
            QueryWrapper<TimeFee> timeFeeQueryWrapper = new QueryWrapper<>();
            timeFeeQueryWrapper.eq("community_id", order.getCommunityId());
            TimeFee timeFee = timeFeeMapper.selectOne(timeFeeQueryWrapper);

            int time = order.getLeaveTime() - order.getEnterTime();
            if(timeFee!=null){
                // 如果时间 <= 套餐时间，则按套餐的费用计算
                if(time <= timeFee.getTime()){
                    price = timeFee.getTimeFee();
                } else{
                    // 否则  价格 = 套餐费用 + 超时费用
                    int extraTime = timeFee.getTime() - time; // 多余的时间
                    if(timeFee.getExtraUnitFee()!=null){
                        //如果已经设计了，则按设计的计算
                        price = timeFee.getTimeFee().add(timeFee.getExtraUnitFee().multiply(new BigDecimal(extraTime)));//套餐费用 + 多余时间*价格
                    } else {
                        // 如果没有设计，则根据小区计时收费标准计算
                        QueryWrapper<UnitFee> unitFeeQueryWrapper = new QueryWrapper<>();
                        unitFeeQueryWrapper.eq("community_id", order.getCommunityId());
                        UnitFee unitFee = unitFeeMapper.selectOne(unitFeeQueryWrapper);
                        if(unitFee!=null){
                            price = timeFee.getTimeFee().add(unitFee.getUnitFee().multiply(new BigDecimal(extraTime)));
                        } else {
                            // 否则默认2块/半小时
                            price = new BigDecimal(PRICE*time);
                        }
                    }
                }
            }else{
                // 管理员没有设计，按默认计算
               // 4 小时
                if(time <= TIME){
                    price = new BigDecimal(TIMEFEE); // 4 小时 12 远
                } else {
                    int extraTime = timeFee.getTime() - time; // 多余的时间
                    price = new BigDecimal(TIMEFEE + extraTime*PRICE);
                }
            }

        } else {
            // 计时收费
            QueryWrapper<UnitFee> unitFeeQueryWrapper = new QueryWrapper<>();
            unitFeeQueryWrapper.eq("community_id", order.getCommunityId());
            UnitFee unitFee = unitFeeMapper.selectOne(unitFeeQueryWrapper);
            int time = order.getLeaveTime() - order.getEnterTime();
            //如果查出来不为空，则按照管理员设计的价格计算
            if (unitFee != null) {
                // 时间*价格
                price = unitFee.getUnitFee().multiply(new BigDecimal(time));
            } else {
                // 否则默认2块/半小时
                price = new BigDecimal(PRICE*time);
            }
        }
        order.setOrderPrice(price);
        // 目前没有优惠，实际支付价格等于订单价格
        order.setActualPrice(price);

        orderMapper.updateById(order);

        Map<String,Long> data = new HashMap<>();
        data.put("orderId",orderId);

        // 返回成功信息
        return OreoResponse.successData(data);
    }

    @Override
    public OreoResponse orderSearch(Order order) {

        if(!StringUtils.isEmpty(order.getCommunityName())&&order.getUserId()!=null){
            QueryWrapper<Order> orderQueryWrapper = new QueryWrapper<>();
            orderQueryWrapper.like("community_name",order.getCommunityName());
            orderQueryWrapper.eq("user_id",order.getUserId());
            return getCommunityPhotoList(orderQueryWrapper);
        }
        return OreoResponse.failure(OreoConstant.MESSAGE_ORDER_SEARCH_FAILURE_PARAM);
    }

    private OreoResponse getCommunityPhotoList(QueryWrapper<Order> orderQueryWrapper) {
        List<Order> orderList = orderMapper.selectList(orderQueryWrapper);

        orderList.stream().forEach(orderElement -> {
            if(orderElement.getCommunityId()!=null){
                QueryWrapper<CommunityPhoto> communityPhotoQueryWrapper = new QueryWrapper<>();
                communityPhotoQueryWrapper.eq("community_id",orderElement.getCommunityId());
                List<CommunityPhoto> communityPhotoList = communityPhotoMapper.selectList(communityPhotoQueryWrapper);
                orderElement.setCommunityPhotoList(communityPhotoList);
            }
        });
        return OreoResponse.successData(orderList);
    }

    @Override
    public OreoResponse cancelOrder(Long orderId) {
        Order order = orderMapper.selectById(orderId);
        // 订单状态(1、进行时   2、完成  3、取消)
        order.setOrderStatus("3");
        order.setCancelTime(DateUtil.date());
        orderMapper.updateById(order);
        return OreoResponse.success();
    }

    @Override
    public OreoResponse orderList(Long id, Integer orderStatus) {
        QueryWrapper<Order> queryWrapper = new QueryWrapper<>();

        if(orderStatus != null) {
            queryWrapper.eq("order_status", orderStatus);
        }
        queryWrapper.eq("user_id", id);

        return getCommunityPhotoList(queryWrapper);
    }

    @Override
    public OreoResponse orderDetail(Long orderId) {

        Order order = orderMapper.selectById(orderId);

        QueryWrapper<CommunityPhoto> communityPhotoQueryWrapper = new QueryWrapper<>();
        communityPhotoQueryWrapper.eq("community_id",order.getCommunityId());

        List<CommunityPhoto> communityPhotos = communityPhotoMapper.selectList(communityPhotoQueryWrapper);
        order.setCommunityPhotoList(communityPhotos);

        return OreoResponse.successData(order);
    }

}



