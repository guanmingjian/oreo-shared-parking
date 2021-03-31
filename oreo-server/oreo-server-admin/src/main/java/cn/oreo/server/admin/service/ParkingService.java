package cn.oreo.server.admin.service;

import cn.oreo.common.model.entity.po.Parking;
import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.common.util.common.QueryRequest;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author GuanMingJian
 * @since 2021-01-29
 */
public interface ParkingService extends IService<Parking> {

    /**
     * 查看停车位列表
     * @param queryRequest
     * @param parking
     * @return
     */
    OreoResponse parkingList(QueryRequest queryRequest, Parking parking);

    /**
     * 修改停车位信息
     * @param parking
     */
    void updateParking(Parking parking);

    /**
     * 新增停车位
     * @param parking
     */
    void addParking(Parking parking);

    /**
     * 删除停车位
     * @param ids
     */
    void deleteParkings(String[] ids);
}
