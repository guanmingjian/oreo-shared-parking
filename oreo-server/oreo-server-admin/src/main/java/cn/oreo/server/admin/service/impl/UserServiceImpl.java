package cn.oreo.server.admin.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.StrUtil;
import cn.oreo.common.model.entity.po.UserCommunity;
import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.common.util.common.OreoUtil;
import cn.oreo.common.util.common.QueryRequest;
import cn.oreo.common.model.entity.po.User;
import cn.oreo.common.util.constant.OreoConstant;
import cn.oreo.server.admin.mapper.UserCommunityMapper;
import cn.oreo.server.admin.mapper.UserMapper;
import cn.oreo.server.admin.service.UserService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;

/**
 * <p>
 *  服务实现类
 * </p>
 *
 * @author GuanMingJian
 * @since 2020-11-01
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;

    @Resource
    private UserCommunityMapper userCommunityMapper;

    @Autowired
    private RedisTemplate<String, Object> redisTemplate;

    /**
     * 注意：此模块用的是Redis中的1库
     * @param queryRequest
     * @param user
     * @param communityId 小区id
     * @return
     */
    @Override
    public OreoResponse userList(QueryRequest queryRequest, User user, String communityId) {

        // 分页配置
        Page<User> page = new Page<>(queryRequest.getPageNum(), queryRequest.getPageSize());

        // 查询配置
        QueryWrapper queryWrapper = new QueryWrapper();

        System.out.println("communityId:"+communityId);

        // 判断是否小区分类查询
        if(StrUtil.isNotBlank(communityId)){
            // 根据小区id从 redis 库中查询用户id表
            String key = "users_community:"+communityId;
            Set<Object> members = redisTemplate.opsForSet().members(key);
            System.out.println("#################");
            members.forEach(o -> System.out.println(o));

            if(members.isEmpty()){

                return new OreoResponse().code("400").message("没有与该小区相关的用户");
            }else {

                queryWrapper.in("id",members);
                // 清除用户类型
                user.setUserType("");
            }
        }

        // 分页、模糊查询、排序、是否转下划线
        OreoUtil.handleWrapperPageSort(queryRequest,queryWrapper,user,queryRequest.getIsToUnderlineCase());

        // 分页查询
        Page selectPage = userMapper.selectPage(page,queryWrapper);

        // 封装返回对象
        Map<String, Object> dataTable = OreoUtil.getDataTable(selectPage);

        return new OreoResponse().code("200").data(dataTable);
    }

    @Override
    public void updateUser(User user) {

        user.setUpdateTime(DateUtil.date());
        userMapper.updateById(user);
    }

    @Override
    public void deleteUsers(String[] userIds) {

        List<String> userIdsList = Arrays.asList(userIds);

        userMapper.deleteBatchIds(userIdsList);
    }
}