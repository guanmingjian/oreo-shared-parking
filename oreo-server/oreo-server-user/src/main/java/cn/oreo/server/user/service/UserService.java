package cn.oreo.server.user.service;

import cn.oreo.common.model.entity.dto.server.user.SearchCommunityDto;
import cn.oreo.common.model.entity.po.User;
import cn.oreo.common.model.entity.po.UserVerify;
import cn.oreo.common.util.common.OreoResponse;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 *
 * @author GuanMingJian
 * @since 2020-10-27
 */
public interface UserService extends IService<User> {

    /**
     * 查询用户信息
     * @param id
     * @return
     */
    OreoResponse userList(Long id);

    /**
     * 修改用户信息
     * @param user
     */
    void updateUser(User user);

    /**
     * 认证为业主
     * @param userVerify
     * @return
     */
    OreoResponse addUserVerify(UserVerify userVerify);

    /**
     * 修改认证信息
     * @param userVerify
     * @return
     */
    OreoResponse updateUserVerify(UserVerify userVerify);
}
