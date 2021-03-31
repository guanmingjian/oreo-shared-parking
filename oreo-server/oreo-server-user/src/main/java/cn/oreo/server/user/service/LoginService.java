package cn.oreo.server.user.service;

import cn.oreo.common.model.entity.dto.server.user.login.LoginDto;
import cn.oreo.common.util.common.OreoResponse;

/**
 * @author GuanMingJian
 * @since 2020/10/23
 */
public interface LoginService {

    /**
     * 前台用户登录
     * @param loginDto
     * @param loginMethod
     * @return
     */
    OreoResponse userLogin(LoginDto loginDto, String loginMethod);

}
