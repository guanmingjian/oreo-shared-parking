package cn.oreo.auth.controller;

import cn.oreo.auth.entity.OauthClientDetails;
import cn.oreo.auth.service.OauthClientDetailsService;
import cn.oreo.common.core.entity.OreoResponse;
import cn.oreo.common.core.entity.QueryRequest;
import cn.oreo.common.core.exception.OreoException;
import cn.oreo.common.core.utils.OreoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * @author GuanMingJian
 * @since 2020/10/4
 */
@Slf4j
@Validated
@RestController
@RequiredArgsConstructor
@RequestMapping("client")
public class OauthClientDetailsController {

    private final OauthClientDetailsService oauthClientDetailsService;

    /**
     * 校验客户端
     * @param clientId
     * @return
     */
    @GetMapping("check/{clientId}")
    public boolean checkUserName(@NotBlank(message = "{required}") @PathVariable String clientId) {
        OauthClientDetails client = this.oauthClientDetailsService.findById(clientId);
        return client == null;
    }

    /**
     * 客户端解密
     * @param clientId
     * @return
     */
    @GetMapping("secret/{clientId}")
    @PreAuthorize("hasAuthority('client:decrypt')")
    public OreoResponse getOriginClientSecret(@NotBlank(message = "{required}") @PathVariable String clientId) {
        OauthClientDetails client = this.oauthClientDetailsService.findById(clientId);
        String origin = client != null ? client.getOriginSecret() : StringUtils.EMPTY;
        return new OreoResponse().data(origin);
    }

    /**
     * 查看客户端信息
     * @param request
     * @param oAuthClientDetails
     * @return
     */
    @GetMapping
    @PreAuthorize("hasAuthority('client:view')")
    public OreoResponse oauthCliendetailsList(QueryRequest request, OauthClientDetails oAuthClientDetails) {
        Map<String, Object> dataTable = OreoUtil.getDataTable(this.oauthClientDetailsService.findOauthClientDetails(request, oAuthClientDetails));
        return new OreoResponse().data(dataTable);
    }

    /**
     * 增加客户端
     * @param oAuthClientDetails
     * @throws OreoException
     */
    @PostMapping
    @PreAuthorize("hasAuthority('client:add')")
    public void addOauthCliendetails(@Valid OauthClientDetails oAuthClientDetails) throws OreoException {
        try {
            this.oauthClientDetailsService.createOauthClientDetails(oAuthClientDetails);
        } catch (Exception e) {
            String message = "新增客户端失败";
            log.error(message, e);
            throw new OreoException(message);
        }
    }

    /**
     * 删除客户端
     * @param clientIds
     * @throws OreoException
     */
    @DeleteMapping
    @PreAuthorize("hasAuthority('client:delete')")
    public void deleteOauthCliendetails(@NotBlank(message = "{required}") String clientIds) throws OreoException {
        try {
            this.oauthClientDetailsService.deleteOauthClientDetails(clientIds);
        } catch (Exception e) {
            String message = "删除客户端失败";
            log.error(message, e);
            throw new OreoException(message);
        }
    }

    /**
     * 修改客户端
     * @param oAuthClientDetails
     * @throws OreoException
     */
    @PutMapping
    @PreAuthorize("hasAuthority('client:update')")
    public void updateOauthCliendetails(@Valid OauthClientDetails oAuthClientDetails) throws OreoException {
        try {
            this.oauthClientDetailsService.updateOauthClientDetails(oAuthClientDetails);
        } catch (Exception e) {
            String message = "修改客户端失败";
            log.error(message, e);
            throw new OreoException(message);
        }
    }
}
