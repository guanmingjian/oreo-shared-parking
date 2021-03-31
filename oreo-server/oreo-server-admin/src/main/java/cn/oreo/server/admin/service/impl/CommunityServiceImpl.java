package cn.oreo.server.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.oreo.common.model.entity.dto.server.admin.CommunityAddDto;
import cn.oreo.common.model.entity.dto.server.admin.CommunityOption;
import cn.oreo.common.model.entity.po.Community;
import cn.oreo.common.model.entity.po.CommunityPhoto;
import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.common.util.common.OreoUtil;
import cn.oreo.common.util.common.QueryRequest;
import cn.oreo.server.admin.mapper.CommunityMapper;
import cn.oreo.server.admin.mapper.CommunityPhotoMapper;
import cn.oreo.server.admin.service.CommunityService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author GuanMingJian
 * @since 2020-10-30
 */
@Service
public class CommunityServiceImpl extends ServiceImpl<CommunityMapper, Community> implements CommunityService {

    @Resource
    private CommunityMapper communityMapper;

    @Resource
    private CommunityPhotoMapper communityPhotoMapper;

    @Override
    public OreoResponse communityList(QueryRequest queryRequest, Community community) {

        // 分页配置
        Page<Community> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());

        // 查询配置
        QueryWrapper queryWrapper = new QueryWrapper();

        // 分页、模糊查询、排序、是否转下划线
        OreoUtil.handleWrapperPageSort(queryRequest,queryWrapper,community,queryRequest.getIsToUnderlineCase());

        // 分页查询
        Page selectPage = communityMapper.selectPage(page,queryWrapper);

        // 封装返回对象
        Map<String, Object> dataTable = OreoUtil.getDataTable(selectPage);

        return new OreoResponse().code("200").data(dataTable);

    }

    @Override
    public void updateCommunity(Community community) {

        community.setUpdateTime(DateUtil.date());

        communityMapper.updateById(community);
    }

    @Override
    public void deleteCommunitys(String[] ids) {

        List<String> idsList = Arrays.asList(ids);

        communityMapper.deleteBatchIds(idsList);
    }

    @Transactional
    @Override
    public void addCommunity(CommunityAddDto communityAddDto) {

        Community community = new Community();
        BeanUtil.copyProperties(communityAddDto,community);

        community.setAddTime(DateUtil.date());

        // 自定义生成雪花算法id
        long id = IdWorker.getId(community);
        community.setId(id);

        communityMapper.insert(community);

        // 存入小区图片
        if(communityAddDto.getCommunityPhotoUrlList()!=null){
            List<String> communityPhotoUrlList = communityAddDto.getCommunityPhotoUrlList();
            if(!communityPhotoUrlList.isEmpty()){
                communityPhotoUrlList.forEach(communityPhotoUrl -> {
                    CommunityPhoto communityPhoto = new CommunityPhoto();
                    communityPhoto.setCommunityId(id);
                    communityPhoto.setPhotoUrl(communityPhotoUrl);
                    communityPhoto.setAddTime(DateUtil.date());
                    communityPhotoMapper.insert(communityPhoto);
                });
            }
        }
    }

    @Override
    public OreoResponse communityOptions(CommunityOption communityOption) {

        QueryWrapper<Community> communityQueryWrapper = new QueryWrapper<>();

        if(StrUtil.isNotBlank(communityOption.getProvince())){
            communityQueryWrapper.eq("province",communityOption.getProvince());
            if(StrUtil.isNotBlank(communityOption.getArea())){
                communityQueryWrapper.eq("area",communityOption.getArea());
                if(StrUtil.isNotBlank(communityOption.getCity())){
                    communityQueryWrapper.eq("city",communityOption.getCity());
                }
            }
        }
        communityQueryWrapper.select("id","name");

        List<Map<String, Object>> maps = communityMapper.selectMaps(communityQueryWrapper);

        return OreoResponse.successData(maps);
    }

    @Override
    public Object communityCheck(String value) {
        // 查询配置
        QueryWrapper queryWrapper = new QueryWrapper();

        queryWrapper.eq("name",value);

        List list = communityMapper.selectList(queryWrapper);

        if(list.size()>0){
            return false;
        }else {
            return true;
        }
    }

}
