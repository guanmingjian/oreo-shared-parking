package cn.oreo.server.admin.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.oreo.common.model.entity.po.*;
import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.common.util.common.OreoUtil;
import cn.oreo.common.util.common.QueryRequest;
import cn.oreo.common.util.constant.OreoConstant;
import cn.oreo.common.util.exception.PentiumException;
import cn.oreo.server.admin.mapper.*;
import cn.oreo.server.admin.service.ParkingService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author GuanMingJian
 * @since 2021-01-29
 */
@Service
public class ParkingServiceImpl extends ServiceImpl<ParkingMapper, Parking> implements ParkingService {

    @Resource
    private ParkingMapper parkingMapper;

    @Resource
    private CommunityMapper communityMapper;

    @Resource
    private ParkingTimeMapper parkingTimeMapper;

    @Resource
    private UserMapper userMapper;

    @Override
    public OreoResponse parkingList(QueryRequest queryRequest, Parking parking) {

        // 分页配置
        Page<Parking> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());

        // 查询配置
        QueryWrapper queryWrapper = new QueryWrapper();

        // 分页、模糊查询、排序、是否转下划线
        OreoUtil.handleWrapperPageSort(queryRequest,queryWrapper,parking,queryRequest.getIsToUnderlineCase());

        // 分页查询
        Page selectPage = parkingMapper.selectPage(page,queryWrapper);

        // 封装返回对象
        Map<String, Object> dataTable = OreoUtil.getDataTable(selectPage);

        return OreoResponse.successData(dataTable);
    }

    @Override
    public void updateParking(Parking parking) {
        parking.setUpdateTime(DateUtil.date());
        parkingMapper.updateById(parking);
    }

    @Transactional
    @Override
    public void addParking(Parking parking) {

        Long userId = parking.getUserId();
        User user = userMapper.selectById(userId);
        // 用户类型 1：普通用户 2：业主
        String userType = user.getUserType();
        if(!"2".equals(userType)){
            throw new PentiumException(300, OreoConstant.MESAAGE_ADD_PARKING_IS_NOT_OWNER);
        }

        parking.setAddTime(DateUtil.date());
        parkingMapper.insert(parking);

        // 小区总停车位数量加1
        Community community = communityMapper.selectById(parking.getCommunityId());
        community.setSpaceTotalNumber(community.getSpaceTotalNumber()+1);
        communityMapper.updateById(community);

    }

    @Transactional
    @Override
    public void deleteParkings(String[] ids) {

        List<String> idsList = Arrays.asList(ids);

        idsList.forEach(s -> {
            Parking parking = parkingMapper.selectById(s);
            // 小区总停车位数量减1
            Community community = communityMapper.selectById(parking.getCommunityId());
            Integer spaceTotalNumber = community.getSpaceTotalNumber();
            // 判断总停车位数量是否小于等于0
            if(spaceTotalNumber<=0){
                throw new PentiumException(300,OreoConstant.MESSAGE_COMMUNITY_SPACE_AVAILABLE_NUMBER_SHORTAGE);
            }
            // 操作减1
            community.setSpaceAvailableNumber(spaceTotalNumber-1);
            // 更新记录
            communityMapper.updateById(community);

            // 删除停车位时间表
            QueryWrapper<ParkingTime> parkingTimeQueryWrapper = new QueryWrapper<>();
            parkingTimeQueryWrapper.eq("parking_id",s);
            parkingTimeMapper.delete(parkingTimeQueryWrapper);

            // 删除停车位
            parkingMapper.deleteById(s);
        });

    }
}