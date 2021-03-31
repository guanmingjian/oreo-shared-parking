package cn.oreo.server.admin.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.date.DateUtil;
import cn.oreo.common.model.entity.po.Parking;
import cn.oreo.common.model.entity.po.User;
import cn.oreo.common.model.entity.po.UserCommunity;
import cn.oreo.common.model.entity.po.UserVerify;
import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.common.util.common.OreoUtil;
import cn.oreo.common.util.common.QueryRequest;
import cn.oreo.common.util.constant.OreoConstant;
import cn.oreo.server.admin.mapper.ParkingMapper;
import cn.oreo.server.admin.mapper.UserCommunityMapper;
import cn.oreo.server.admin.mapper.UserMapper;
import cn.oreo.server.admin.mapper.UserVerifyMapper;
import cn.oreo.server.admin.service.UserVerifyService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.IdWorker;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
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
public class UserVerifyServiceImpl extends ServiceImpl<UserVerifyMapper, UserVerify> implements UserVerifyService {

    @Resource
    private UserVerifyMapper userVerifyMapper;

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserCommunityMapper userCommunityMapper;

    @Resource
    private ParkingMapper parkingMapper;

    @Override
    public OreoResponse userVerifyList(QueryRequest queryRequest, UserVerify userVerify) {
        // 分页配置
        Page<UserVerify> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());

        // 查询配置
        QueryWrapper queryWrapper = new QueryWrapper();

        // 分页、模糊查询、排序、是否转下划线
        OreoUtil.handleWrapperPageSort(queryRequest,queryWrapper,userVerify,queryRequest.getIsToUnderlineCase());

        // 分页查询
        Page selectPage = userVerifyMapper.selectPage(page,queryWrapper);
        // 查询用户信息
        List<UserVerify> records = selectPage.getRecords();
        records.forEach(uv -> {
            Long userId = uv.getUserId();
            User user = userMapper.selectById(userId);
            uv.setUser(user);
        });

        // 封装返回对象
        Map<String, Object> dataTable = OreoUtil.getDataTable(selectPage);

        return OreoResponse.successData(dataTable);
    }

    @Transactional
    @Override
    public OreoResponse userVerifyHandle(Long userVerifyId, Boolean isPass) {

        UserVerify userVerify = userVerifyMapper.selectById(userVerifyId);
        if(BeanUtil.isEmpty(userVerify)){
            return OreoResponse.failure(OreoConstant.MESSAGE_SYSTEM_INTERNAL_FAILED);
        }

        //判断审核状态（1、审核中 2、审核通过 3、审核不通过）
        if("1".equals(userVerify.getVerifyStatus())){

            if(isPass){

                verifyPass(userVerify);

            }else {
                // 修改审核信息
                // 审核状态（1、审核中 2、审核通过 3、审核不通过）
                userVerify.setVerifyStatus("3");
            }

        }else if("2".equals(userVerify.getVerifyStatus())){

            if(isPass){

                // 返回已经审核通过信息
                return OreoResponse.failure(OreoConstant.MESSAGE_SYSTEM_INTERNAL_FAILED);

            }else {
                // 审核通过->审核不通过

                // 修改审核信息
                // 审核状态（1、审核中 2、审核通过 3、审核不通过）
                userVerify.setVerifyStatus("3");

                // 修改用户类型
                User user = userMapper.selectById(userVerify.getUserId());
                // 用户类型 1：普通用户 2：业主
                user.setUserType("1");
                userMapper.updateById(user);

                // 删除用户-小区关联表
                userCommunityMapper.deleteById(userVerify.getUserCommunityId());

                // 删除停车位表
                parkingMapper.deleteById(userVerify.getParkingId());

            }

        }else if("3".equals(userVerify.getVerifyStatus())){

            if(isPass){

                // 审核不通过->审核通过
                verifyPass(userVerify);

            }else {

                // 返回已经审核不通过信息
                return OreoResponse.failure(OreoConstant.MESSAGE_SYSTEM_INTERNAL_FAILED);
            }

        }else {
            return OreoResponse.failure(OreoConstant.MESSAGE_SYSTEM_INTERNAL_FAILED);
        }


        userVerify.setVerifyTime(DateUtil.date());
        userVerifyMapper.updateById(userVerify);

        return OreoResponse.success();
    }

    public void  verifyPass(UserVerify userVerify){
        // 修改审核信息
        // 审核状态（1、审核中 2、审核通过 3、审核不通过）
        userVerify.setVerifyStatus("2");

        // 修改用户类型
        User user = userMapper.selectById(userVerify.getUserId());
        // 用户类型 1：普通用户 2：业主
        user.setUserType("2");
        userMapper.updateById(user);

        // 增加用户-小区关联表
        UserCommunity userCommunity = new UserCommunity();
        // 自定义生成雪花算法id
        long userCommunityId = IdWorker.getId(userCommunity);
        userCommunity.setId(userCommunityId);
        userCommunity.setCommunityId(userVerify.getCommunityId());
        userCommunity.setUserId(userVerify.getUserId());
        userCommunity.setAddTime(DateUtil.date());
        userCommunityMapper.insert(userCommunity);

        // 插入停车位
        Parking parking = new Parking();
        long parkingId = IdWorker.getId(userCommunity);
        parking.setId(parkingId);
        parking.setParkingSn(userVerify.getParkingSn());
        parking.setCommunityId(userVerify.getCommunityId());
        parking.setCommunityName(userVerify.getCommunityName());
        parking.setUserId(userVerify.getUserId());
        parking.setUserName(userVerify.getOwnerName());
        parking.setAddTime(DateUtil.date());
        parking.setDeleted("0");
        // 车位状态（0：可用 1：禁用）
        // 业主添加完显示时间后才可启动
        parking.setParkingStatus("1");
        parkingMapper.insert(parking);

        // 设置主键
        userVerify.setUserCommunityId(userCommunityId);
        userVerify.setParkingId(parkingId);
    }
}
