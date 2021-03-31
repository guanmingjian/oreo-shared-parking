package cn.oreo.server.admin.controller;

import cn.oreo.common.model.entity.po.UserVerify;
import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.common.util.common.QueryRequest;
import cn.oreo.server.admin.service.UserVerifyService;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.constraints.NotNull;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author GuanMingJian
 * @since 2021-01-29
 */
@Validated
@RestController
@RequestMapping("/user/verify")
public class UserVerifyController {

    @Resource
    private UserVerifyService userVerifyService;

    @GetMapping
    @ApiOperation(value = "查看审核列表",notes = "如需查看不同的审核列表，请传communityId")
    //@PreAuthorize("hasAuthority('user-verify:view')")
    public OreoResponse userVerifyList(
            QueryRequest queryRequest,
            UserVerify userVerify
    ){

        return userVerifyService.userVerifyList(queryRequest,userVerify);
    }

    @PostMapping
    @ApiOperation(value = "审核",notes = "通过：true，不通过：false")
    //@PreAuthorize("hasAuthority('user-verify:handle')")
    public OreoResponse userVerifyHandle(@NotNull @RequestParam Long userVerifyId, @RequestParam @ApiParam("通过：true，不通过：false") @NotNull Boolean isPass){

        return userVerifyService.userVerifyHandle(userVerifyId,isPass);
    }
}

