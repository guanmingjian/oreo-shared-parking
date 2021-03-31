package cn.oreo.common.security.starter.interceptor;

import cn.oreo.common.core.entity.OreoResponse;
import cn.oreo.common.core.entity.constant.OreoConstant;
import cn.oreo.common.core.utils.OreoUtil;
import cn.oreo.common.security.starter.properties.OreoCloudSecurityProperties;
import org.apache.commons.lang3.StringUtils;
import org.springframework.lang.NonNull;
import org.springframework.util.Base64Utils;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author GuanMingJian
 * @since 2020/10/4
 */
public class OreoServerProtectInterceptor implements HandlerInterceptor {

    private OreoCloudSecurityProperties properties;

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws IOException {
        if (!properties.getOnlyFetchByGateway()) {
            return true;
        }
        String token = request.getHeader(OreoConstant.GATEWAY_TOKEN_HEADER);
        String gatewayToken = new String(Base64Utils.encode(OreoConstant.GATEWAY_TOKEN_VALUE.getBytes()));
        if (StringUtils.equals(gatewayToken, token)) {
            return true;
        } else {
            OreoResponse OreoResponse = new OreoResponse();
            OreoUtil.makeJsonResponse(response, HttpServletResponse.SC_FORBIDDEN, OreoResponse.message("请通过网关获取资源"));
            return false;
        }
    }

    public void setProperties(OreoCloudSecurityProperties properties) {
        this.properties = properties;
    }
}
