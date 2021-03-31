package cn.oreo.server.user.service.impl;

import cn.hutool.core.util.StrUtil;
import cn.oreo.common.model.entity.dto.server.user.SearchCommunityDto;
import cn.oreo.common.model.entity.po.Community;
import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.common.util.constant.OreoConstant;
import cn.oreo.common.util.exception.PentiumException;
import cn.oreo.server.user.mapper.CommunityMapper;
import cn.oreo.server.user.service.CommunityService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author GuanMingJian
 * @since 2020-10-27
 */
@Service
public class CommunityServiceImpl implements CommunityService {

    @Resource
    private CommunityMapper communityMapper;

    @Override
    public OreoResponse communitySearch(SearchCommunityDto searchCommunityDto) {

        QueryWrapper<Community> communityQueryWrapper = new QueryWrapper<>();

        if(StrUtil.isNotBlank(searchCommunityDto.getProvince())){
            communityQueryWrapper.eq("province",searchCommunityDto.getProvince());
            if(StrUtil.isNotBlank(searchCommunityDto.getArea())){
                communityQueryWrapper.eq("area",searchCommunityDto.getArea());
                if(StrUtil.isNotBlank(searchCommunityDto.getCity())){
                    communityQueryWrapper.eq("city",searchCommunityDto.getCity());
                }
            }
        }

        // 小区名称模糊搜索
        if(StrUtil.isNotBlank(searchCommunityDto.getCommunityName())){
            communityQueryWrapper.like("name",searchCommunityDto.getCommunityName());
        }

        if(searchCommunityDto.getIsNeedDetail()==null){
            throw new PentiumException(300,OreoConstant.MESSAGE_PARAMETER_EXCEPTION);
        }

        if(searchCommunityDto.getIsNeedDetail()){
            List<Community> communityList = communityMapper.selectList(communityQueryWrapper);
            return OreoResponse.successData(communityList);
        }else {
            communityQueryWrapper.select("id","name");

            List<Map<String, Object>> maps = communityMapper.selectMaps(communityQueryWrapper);
            return OreoResponse.successData(maps);
        }

    }
}