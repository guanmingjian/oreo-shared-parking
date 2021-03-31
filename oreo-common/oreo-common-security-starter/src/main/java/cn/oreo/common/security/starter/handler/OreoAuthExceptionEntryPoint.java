package cn.oreo.common.security.starter.handler;

import cn.oreo.common.core.entity.OreoResponse;
import cn.oreo.common.core.utils.OreoUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author GuanMingJian
 * @since 2020/10/4
 */
@Slf4j
public class OreoAuthExceptionEntryPoint implements AuthenticationEntryPoint {

    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
                         AuthenticationException authException) throws IOException {
        String requestUri = request.getRequestURI();
        int status = HttpServletResponse.SC_UNAUTHORIZED;
        String message = "访问令牌不合法";
        log.error("客户端访问{}请求失败: {}", requestUri, message, authException);
        OreoUtil.makeJsonResponse(response, status, new OreoResponse().message(message));
    }
}
