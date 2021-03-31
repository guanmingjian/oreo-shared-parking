package cn.oreo.server.parking.controller;


import cn.oreo.common.model.entity.dto.server.parking.CommunityQuery;
import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.server.parking.service.CommunityService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/community")
public class CommunityController {

    @Resource
    private CommunityService communityServiceImpl;

    @PostMapping("/query")
    public OreoResponse communityList(@RequestBody CommunityQuery communityQuery) {
        return communityServiceImpl.communityList(communityQuery);
    }
}

