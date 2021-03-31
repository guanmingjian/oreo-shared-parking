package cn.oreo.server.admin.service;

import cn.oreo.common.model.entity.po.Reserve;
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
public interface ReserveService extends IService<Reserve> {

    /**
     * 查看预约列表
     * @param queryRequest
     * @param reserve
     * @return
     */
    OreoResponse reserveList(QueryRequest queryRequest, Reserve reserve);

    /**
     * 修改预约信息
     * @param reserve
     */
    void updateReserve(Reserve reserve);
}
