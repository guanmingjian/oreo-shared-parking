package cn.oreo.server.user.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.oreo.common.model.entity.po.Parking;
import cn.oreo.common.model.entity.po.User;
import cn.oreo.common.model.entity.po.UserVerify;
import cn.oreo.common.util.common.MD5Utils;
import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.common.util.constant.OreoConstant;
import cn.oreo.server.user.mapper.ParkingMapper;
import cn.oreo.server.user.mapper.UserMapper;
import cn.oreo.server.user.mapper.UserVerifyMapper;
import cn.oreo.server.user.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author GuanMingJian
 * @since 2020-10-27
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private ParkingMapper parkingMapper;

    @Resource
    private UserVerifyMapper userVerifyMapper;

    @Override
    public OreoResponse userList(Long id) {

        User selectUser = userMapper.selectById(id);
        return new OreoResponse().code("200").data(selectUser);

    }

    @Override
    public void updateUser(User user) {
        user.setUpdateTime(DateUtil.date());
        if(StringUtils.isNotBlank(user.getPassword())){
            user.setPassword(MD5Utils.encode(user.getPassword()));
        }
        userMapper.updateById(user);
    }

    @Override
    public OreoResponse addUserVerify(UserVerify userVerify) {

        // 判断该小区的停车位是否被认领过
        QueryWrapper queryWrapper = new QueryWrapper();
        queryWrapper.eq("community_id",userVerify.getCommunityId());
        queryWrapper.eq("parking_sn",userVerify.getParkingSn());
        Parking parking = parkingMapper.selectOne(queryWrapper);
        if(BeanUtil.isNotEmpty(parking)){
            return OreoResponse.failure(OreoConstant.MESSAGE_VERIFY_EXIST_PARKING);
        }

        // 审核状态（1、审核中 2、审核通过 3、审核不通过）
        userVerify.setVerifyStatus("1");
        userVerify.setApplyTime(DateUtil.date());
        userVerifyMapper.insert(userVerify);
        return OreoResponse.success();
    }

    @Override
    public OreoResponse updateUserVerify(UserVerify userVerify) {
        userVerify.setUpdateTime(DateUtil.date());
        userVerifyMapper.updateById(userVerify);
        return OreoResponse.success();
    }

}