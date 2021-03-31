package cn.oreo.server.admin.service;

import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.common.util.common.QueryRequest;
import cn.oreo.common.model.entity.po.User;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 *  服务类
 * </p>
 *
 * @author GuanMingJian
 * @since 2020-11-01
 */
public interface UserService extends IService<User> {

    /**
     * 查询用户信息
     * @param queryRequest
     * @param user
     * @param communityId 小区id
     * @return
     */
    OreoResponse userList(QueryRequest queryRequest, User user, String communityId);

    /**
     * 修改用户信息
     * @param user
     */
    void updateUser(User user);

    /**
     * 逻辑删除用户
     * @param userIds
     */
    void deleteUsers(String[] userIds);
}
