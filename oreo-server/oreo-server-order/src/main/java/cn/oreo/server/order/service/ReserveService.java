package cn.oreo.server.order.service;

import cn.oreo.common.model.entity.dto.server.order.ReserveDto;
import cn.oreo.common.model.entity.po.Reserve;
import cn.oreo.common.util.common.OreoResponse;
import com.baomidou.mybatisplus.extension.service.IService;

import java.io.UnsupportedEncodingException;

/**
 * <p>
 * 预约表 服务类
 * </p>
 *
 * @author GuanMingJian
 * @since 2021-01-22
 */
public interface ReserveService extends IService<Reserve> {

    /**
     * 预约
     * @param reserveDto
     * @return
     */
    OreoResponse reserveParking(ReserveDto reserveDto) throws UnsupportedEncodingException;

    /**
     * 取消预约
     * @param reserveDto
     * @return
     */
    OreoResponse reserveUpdateParking(ReserveDto reserveDto);

    /**
     * 查看预约信息
     * @param userId
     * @return
     */
    OreoResponse reserveList(Long userId);

    /**
     * 查看预约详情信息
     * @param reserveId
     * @return
     */
    OreoResponse reserveDetail(Long reserveId);

    /**
     * 通过小区id获取小区详细信息
     * @param communityId
     * @return
     */
    OreoResponse communityById(String communityId);
}
