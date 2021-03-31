package cn.oreo.server.user.service;

import cn.oreo.common.model.entity.dto.server.user.SearchCommunityDto;
import cn.oreo.common.util.common.OreoResponse;

/**
 *
 * @author GuanMingJian
 * @since 2020-10-27
 */
public interface CommunityService{

    /**
     * 根据省市区模糊搜索小区
     * @param searchCommunityDto
     * @return
     */
    OreoResponse communitySearch(SearchCommunityDto searchCommunityDto);

}
