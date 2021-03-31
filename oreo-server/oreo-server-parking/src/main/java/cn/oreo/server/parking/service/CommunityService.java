package cn.oreo.server.parking.service;

import cn.oreo.common.model.entity.dto.server.parking.CommunityQuery;
import cn.oreo.common.util.common.OreoResponse;

/**
 * 小区服务
 *
 * @author XuMin
 */
public interface CommunityService {

    /**
     * 根据经纬度查询2km范围内的小区
     *
     * @param communityQuery 查询条件：省、市、区、经纬度
     */
    OreoResponse communityList(CommunityQuery communityQuery);
}
