package cn.oreo.server.parking.service.impl;

import cn.oreo.common.model.entity.dto.server.parking.CommunityQuery;
import cn.oreo.common.model.entity.po.Community;
import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.common.util.constant.OreoConstant;
import cn.oreo.server.parking.mapper.CommunityMapper;
import cn.oreo.server.parking.service.CommunityService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class CommunityServiceImpl implements CommunityService {

    @Resource
    private CommunityMapper communityMapper;

    @Override
    public OreoResponse communityList(CommunityQuery communityQuery) {
        QueryWrapper<Community> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("province", communityQuery.getProvince());   // 省
        queryWrapper.eq("city", communityQuery.getCity());           // 市
        queryWrapper.eq("area", communityQuery.getArea());           // 区
        queryWrapper.gt("space_available_number", 0);            // 停车位可用数量大于0
        List<Community> communities = communityMapper.selectList(queryWrapper);

        if (communities == null) {
            return new OreoResponse().code("300").message(OreoConstant.MESSAGE_PARKING_IS_NULL);
        }

        // 保留2km之内的小区
        communities.removeIf(community -> getDistance(community.getLongitude(), community.getLatitude(),
                communityQuery.getLongitude(), communityQuery.getLatitude()) > 2);

        return new OreoResponse().code("200").data(communities);
    }

    /**
     * 计算两个经纬度之间的距离
     *
     * @param lonA 第一个经度
     * @param latA 第一个纬度
     * @param lonB 第二个经度
     * @param latB 第二个纬度
     * @return 两个经纬度之间的距离
     */
    private double getDistance(String lonA, String latA, String lonB, String latB) {
        double EARTH_RADIUS = 6378.137;   //地球半径,单位千米
        double radLatA = rad(Double.parseDouble(latA));
        double radLatB = rad(Double.parseDouble(latB));
        double a = radLatA - radLatB;
        double b = rad(Double.parseDouble(lonA)) - rad(Double.parseDouble(lonB));
        double distance = 2 * Math.asin(Math.sqrt(Math.pow(Math.sin(a / 2), 2) +
                                                    Math.cos(radLatA) * Math.cos(radLatB) * Math.pow(Math.sin(b / 2), 2)));
        distance = distance * EARTH_RADIUS;
        distance = Math.round(distance * 10000) / 10000.0;
        return distance;
    }

    private double rad(double d) {
        return d * Math.PI / 180.0;
    }
}
