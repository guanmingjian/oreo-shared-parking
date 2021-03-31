package cn.oreo.server.admin.service;

import cn.oreo.common.model.entity.dto.server.admin.CommunityAddDto;
import cn.oreo.common.model.entity.dto.server.admin.CommunityOption;
import cn.oreo.common.model.entity.po.Community;
import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.common.util.common.QueryRequest;
import com.baomidou.mybatisplus.extension.service.IService;

import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author GuanMingJian
 * @since 2020-10-30
 */
public interface CommunityService extends IService<Community> {

    /**
     * 查看小区列表
     * @param queryRequest
     * @param community
     * @return
     */
    OreoResponse communityList(QueryRequest queryRequest, Community community);

    /**
     * 修改小区
     * @param community
     */
    void updateCommunity(Community community);

    /**
     * 删除小区
     * @param ids
     */
    void deleteCommunitys(String[] ids);

    /**
     * 新增小区
     * @param communityAddDto
     */
    void addCommunity(CommunityAddDto communityAddDto);

    /**
     * 获得小区下拉框
     * @return
     */
    OreoResponse communityOptions(CommunityOption communityOption);

    /**
     * 检查小区名是否存在
     * @param value
     * @return
     */
    Object communityCheck(String value);
}
