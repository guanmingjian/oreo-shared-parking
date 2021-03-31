package cn.oreo.server.user.controller;


import cn.hutool.core.bean.BeanUtil;
import cn.oreo.common.model.entity.dto.server.admin.UserVerifyDto;
import cn.oreo.common.model.entity.dto.server.user.SearchCommunityDto;
import cn.oreo.common.model.entity.po.User;
import cn.oreo.common.model.entity.po.UserVerify;
import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.server.user.service.UserService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 *
 * @author GuanMingJian
 * @since 2020-10-27
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 根据用户id查询用户信息
     */
    @GetMapping("/{id}")
    public OreoResponse userList(@NotNull @PathVariable("id") @ApiParam("id不可为空")Long id){
        return userService.userList(id);
    }

    /**
     * 修改用户信息
     */
    @PutMapping
    public void updateUser(@Valid User user){
        userService.updateUser(user);
    }

    @PostMapping("/verify/add")
    @ApiOperation("认证为业主")
    public OreoResponse addUserVerify(UserVerifyDto userVerifyDto){

        UserVerify userVerify = new UserVerify();

        BeanUtil.copyProperties(userVerifyDto,userVerify);

        return userService.addUserVerify(userVerify);
    }

    @PostMapping("/verify/update")
    @ApiOperation("修改认证信息")
    public OreoResponse updateUserVerify(UserVerify userVerify){
        return userService.updateUserVerify(userVerify);
    }
}

