package cn.oreo.server.user.service.impl;

import cn.hutool.core.date.DateUtil;
import cn.hutool.core.util.RandomUtil;
import cn.oreo.common.model.entity.dto.server.user.login.LoginDto;
import cn.oreo.common.model.entity.po.User;
import cn.oreo.common.util.common.MD5Utils;
import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.common.util.constant.OreoConstant;
import cn.oreo.server.user.mapper.UserMapper;
import cn.oreo.server.user.service.LoginService;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
 * @author GuanMingJian
 * @since 2020/10/23
 */
@Service
public class LoginServiceImpl implements LoginService {

    @Resource
    private UserMapper userMapper;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public OreoResponse userLogin(LoginDto loginDto, String loginMethod) {

        // 查询数据库是否存在此手机号码
        QueryWrapper<User> userQueryWrapper = new QueryWrapper<>();
        userQueryWrapper.eq("phone_number",loginDto.getPhoneNumber());
        User user = userMapper.selectOne(userQueryWrapper);

        // 密码登录
        if("password".equals(loginMethod)){

            // 手机号存在
            if(user != null){

                // 验证密码
                if(!StringUtils.isBlank(user.getPassword())){    // 密码不为空，已录入密码

                    // 密码加密
                    String encodePassword = MD5Utils.encode(loginDto.getPassword());

                    // 比对密码
                    if(encodePassword.equals(user.getPassword())){

                        // 添加最后登录时间
                        user.setLastLoginTime(DateUtil.date());
                        userMapper.updateById(user);

                        // 清除密码等敏感信息
                        user.setPassword(null);

                        // 密码正确，返回成功
                        return new OreoResponse().code("200").message("登录成功").data(user);
                    }else {
                        // 密码错误，返回错误信息
                        return new OreoResponse().code("400").message(OreoConstant.MESSAGE_LOGIN_PASSWORD_FAILED);
                    }

                }else{
                    // 密码为空，之前用验证码登录
                    return new OreoResponse().code("400").message(OreoConstant.MESSAGE_LOGIN_UN_PASSWORD);
                }

            }else{
                // 密码登录时，手机号不存在，不自动注册
                // 返回 400，提醒手机号码错误
                return new OreoResponse().code("400").message(OreoConstant.MESSAGE_LOGIN_PHONE_FAILED);
            }

        }else if("captcha".equals(loginMethod)){  // 验证码登录

            // 根据手机号去redis中查询验证码
            ValueOperations<String, String> operations = redisTemplate.opsForValue();
            String captcha = operations.get(OreoConstant.ATTR_REDIS_PHONE_CAPTCHA_PREFIX+loginDto.getPhoneNumber());

            // 查不到，报400，返回重新获取信息
            if(StringUtils.isBlank(captcha)){
                return new OreoResponse().code("400").message(OreoConstant.MESSAGE_CAPTCHA_UN_EXISTED);
            }else {
                // 查到了，比对验证码是否一致
                if(captcha.equals(loginDto.getCaptcha())){
                    // 一致,判断手机号是否存在
                    // 存在手机号
                    if(user != null){
                        // 清除密码等敏感信息
                        user.setPassword(null);
                        // 登录成功，返回200
                        return new OreoResponse().code("200").message("登录成功").data(user);
                    }else {
                        // 不存在，未新用户，自动注册
                        // 把手机号存入数据库，使用默认头像，自动生成昵称，无密码
                        User newUser = new User();
                        String userName = RandomUtil.randomString(15);
                        newUser.setPhoneNumber(loginDto.getPhoneNumber());
                        newUser.setRegisterTime(DateUtil.date());
                        newUser.setAvatarUrl("https://oreox.oss-cn-guangzhou.aliyuncs.com/42491778-cd6a-4388-819e-cccbc1c178f4.jpg");
                        newUser.setUserName(userName);
                        // 0：未完善身份信息，1：完善身份信息
                        newUser.setInfoCode("0");
                        userMapper.insert(newUser);

                        // 雪花算法生成的ID无法获取，所以根据用户名去从数据库查多一次
                        QueryWrapper<User> newUserQueryWrapper = new QueryWrapper<>();
                        newUserQueryWrapper.eq("user_name",userName);
                        User returnUser = userMapper.selectOne(newUserQueryWrapper);

                        // 返回201，让前端提醒用户已自动创建,然后跳转页面
                        return new OreoResponse().code("201").message(OreoConstant.MESSAGE_LOGIN_UN_ACCOUNT).data(returnUser);
                    }
                }else {
                    // 不一致
                    // 验证码错误，返回400，报验证码不正确信息
                    return new OreoResponse().code("400").message(OreoConstant.MESSAGE_LOGIN_CAPTCHA_FAILED);
                }
            }
        }

        return new OreoResponse().code("500").message(OreoConstant.MESSAGE_SYSTEM_INTERNAL_FAILED);
    }
}
