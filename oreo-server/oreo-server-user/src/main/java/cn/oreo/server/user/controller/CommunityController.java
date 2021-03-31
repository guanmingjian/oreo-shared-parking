package cn.oreo.server.user.controller;

import cn.oreo.common.model.entity.dto.server.user.SearchCommunityDto;
import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.server.user.service.CommunityService;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

/**
 * @author GuanMingJian
 * @since 2021/2/5
 */
@Slf4j
@Validated
@RestController
@RequestMapping("/community")
@RequiredArgsConstructor
public class CommunityController {

    private final CommunityService communityService;

    @PostMapping("/search")
    @ApiOperation(value = "小区搜索",notes = "根据省市区模糊搜索小区,支持省市区分级搜索，支持详细信息返回")
    public OreoResponse communitySearch(SearchCommunityDto searchCommunityDto){

        return communityService.communitySearch(searchCommunityDto);
    }
}
