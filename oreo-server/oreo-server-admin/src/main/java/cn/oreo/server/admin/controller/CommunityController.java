package cn.oreo.server.admin.controller;

import cn.hutool.core.bean.BeanUtil;
import cn.oreo.common.model.entity.dto.server.admin.CommunityAddDto;
import cn.oreo.common.model.entity.dto.server.admin.CommunityDto;
import cn.oreo.common.model.entity.dto.server.admin.CommunityOption;
import cn.oreo.common.model.entity.po.Community;
import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.common.util.common.QueryRequest;
import cn.oreo.common.util.constant.StringConstant;
import cn.oreo.server.admin.service.CommunityService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 *
 * @author GuanMingJian
 * @since 2020-10-30
 */
@RestController
@RequestMapping("/community")
public class CommunityController {

    @Resource
    private CommunityService communityService;

    /**
     * 小区选择下拉框
     * @return
     */
    @GetMapping("/options")
    public OreoResponse communityOptions(CommunityOption communityOption){

        return communityService.communityOptions(communityOption);
    }

    @GetMapping
    //@PreAuthorize("hasAuthority('community:view')")
    public OreoResponse communityList(
            QueryRequest queryRequest,
            Community community
    ){
        return communityService.communityList(queryRequest, community);
    }

    @GetMapping("/check/{value}")
    //@PreAuthorize("hasAuthority('community:check')")
    public Object communityCheck(@PathVariable String value){

        return communityService.communityCheck(value);
    }

    @PutMapping
    //@PreAuthorize("hasAuthority('community:update')")
    public void updateCommunity(@Valid CommunityDto communityDto){

        Community community = new Community();
        BeanUtil.copyProperties(communityDto,community);

        communityService.updateCommunity(community);
    }

    @DeleteMapping("/{communityIds}")
    //@PreAuthorize("hasAuthority('community:delete')")
    public void deleteCommunitys(@NotNull @PathVariable String communityIds){

        String[] ids = communityIds.split(StringConstant.COMMA);

        communityService.deleteCommunitys(ids);
    }

    @PostMapping
    //@PreAuthorize("hasAuthority('community:add')")
    public void addCommunity(@RequestBody CommunityAddDto communityAddDto){

        communityService.addCommunity(communityAddDto);
    }

}

