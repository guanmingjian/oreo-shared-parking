package cn.oreo.server.admin.controller;

import cn.oreo.common.model.entity.po.User;
import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.common.util.common.QueryRequest;
import cn.oreo.common.util.constant.OreoConstant;
import cn.oreo.common.util.constant.StringConstant;
import cn.oreo.server.admin.service.UserService;
import io.swagger.annotations.ApiParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 *
 * @author GuanMingJian
 * @since 2020-11-01
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    /**
     * 查询用户列表
     * 根据小区分类查询
     * 可查询全部
     */
    @GetMapping
    //@PreAuthorize("hasAuthority('real-user:view')")
    public OreoResponse userList(
            QueryRequest queryRequest,
            User user,
            @ApiParam("小区分类查询，传小区id") String communityId
    ){

        return userService.userList(queryRequest,user,communityId);
    }

    @PutMapping
    //@PreAuthorize("hasAuthority('real-user:update')")
    public void updateUser(@Valid User user){

        userService.updateUser(user);
    }

    @DeleteMapping("/{userIds}")
    //@PreAuthorize("hasAuthority('real-user:delete')")
    public void deleteUsers(@NotBlank(message = OreoConstant.MESSAGE_REQUEST_REQUIRED) @PathVariable String userIds){

        String[] ids = userIds.split(StringConstant.COMMA);

        userService.deleteUsers(ids);
    }

}

