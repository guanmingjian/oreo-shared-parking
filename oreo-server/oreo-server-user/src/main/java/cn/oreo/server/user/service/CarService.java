package cn.oreo.server.user.service;

import cn.oreo.common.model.entity.po.Car;
import cn.oreo.common.util.common.OreoResponse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * @author XuMin
 */
public interface CarService extends IService<Car> {

    /**
     * 通过用户id查询车辆信息
     * @param userId 用户id
     */
    OreoResponse carList(Long userId);

    /**
     * 解绑用户的车牌号码
     * @param userId     用户id
     * @param carNumber  车牌号码
     */
    OreoResponse deleteCar(Long userId, String carNumber);
}
