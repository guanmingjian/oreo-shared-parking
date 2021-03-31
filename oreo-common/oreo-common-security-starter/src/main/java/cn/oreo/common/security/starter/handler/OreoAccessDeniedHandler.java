package cn.oreo.common.security.starter.handler;

import cn.oreo.common.core.entity.OreoResponse;
import cn.oreo.common.core.utils.OreoUtil;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author GuanMingJian
 * @since 2020/10/4
 */
public class OreoAccessDeniedHandler implements AccessDeniedHandler {

    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        OreoResponse febsResponse = new OreoResponse();
        OreoUtil.makeJsonResponse(response, HttpServletResponse.SC_FORBIDDEN, febsResponse.message("没有权限访问该资源"));
    }
}
