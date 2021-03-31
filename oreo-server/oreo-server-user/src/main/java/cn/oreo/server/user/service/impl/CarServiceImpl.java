package cn.oreo.server.user.service.impl;

import cn.oreo.common.model.entity.po.Car;
import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.common.util.constant.OreoConstant;
import cn.oreo.server.user.mapper.CarMapper;
import cn.oreo.server.user.service.CarService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author XuMin
 */
@Service
public class CarServiceImpl extends ServiceImpl<CarMapper, Car> implements CarService {

    @Resource
    private CarMapper carMapper;

    @Override
    public OreoResponse carList(Long userId) {
        // 根据用户id查询车辆信息
        QueryWrapper<Car> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        List<Car> cars = null;
        // 异常捕获
        try {
            cars = carMapper.selectList(wrapper);
            if (cars == null) {
                return new OreoResponse().code("300").message(OreoConstant.MESSAGE_CAR_NULL);
            }
            return new OreoResponse().code("200").data(cars);
        } catch (Exception e) {
            e.printStackTrace();
            return new OreoResponse().code("300").message(e.getMessage());
        }
    }

    @Override
    public OreoResponse deleteCar(Long userId, String carNumber) {
        QueryWrapper<Car> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        wrapper.eq("car_number", carNumber);
        try {
            int delete = carMapper.delete(wrapper);
            if (delete == 1) {
                return new OreoResponse().code("200").message(OreoConstant.MESSAGE_CAR_DELETE_SUCCESS);
            }
            return new OreoResponse().code("300").message(OreoConstant.MESSAGE_CAR_DELETE_FAILURE);
        } catch (Exception e) {
            e.printStackTrace();
            return new OreoResponse().code("300").message(e.getMessage());
        }
    }
}
