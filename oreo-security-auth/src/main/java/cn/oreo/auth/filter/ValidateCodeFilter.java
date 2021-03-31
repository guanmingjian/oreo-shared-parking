package cn.oreo.auth.filter;

import cn.oreo.auth.service.ValidateCodeService;
import cn.oreo.common.core.entity.OreoResponse;
import cn.oreo.common.core.entity.constant.EndpointConstant;
import cn.oreo.common.core.entity.constant.GrantTypeConstant;
import cn.oreo.common.core.entity.constant.ParamsConstant;
import cn.oreo.common.core.exception.ValidateCodeException;
import cn.oreo.common.core.utils.OreoUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.annotation.Nonnull;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 验证码过滤器
 *
 * @author GuanMingJian
 * @since 2020/10/4
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class ValidateCodeFilter extends OncePerRequestFilter {

    private final ValidateCodeService validateCodeService;

    @Override
    protected void doFilterInternal(@Nonnull HttpServletRequest httpServletRequest, @Nonnull HttpServletResponse httpServletResponse,
                                    @Nonnull FilterChain filterChain) throws ServletException, IOException {
        String header = httpServletRequest.getHeader(HttpHeaders.AUTHORIZATION);

        RequestMatcher matcher = new AntPathRequestMatcher(EndpointConstant.OAUTH_TOKEN, HttpMethod.POST.toString());
        if (matcher.matches(httpServletRequest)
                && StringUtils.equalsIgnoreCase(httpServletRequest.getParameter(ParamsConstant.GRANT_TYPE), GrantTypeConstant.PASSWORD)) {
            try {
                validateCode(httpServletRequest);
                filterChain.doFilter(httpServletRequest, httpServletResponse);
            } catch (Exception e) {
                OreoResponse OreoResponse = new OreoResponse();
                OreoUtil.makeFailureResponse(httpServletResponse, OreoResponse.message(e.getMessage()));
                log.error(e.getMessage(), e);
            }
        } else {
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        }
    }

    private void validateCode(HttpServletRequest httpServletRequest) throws ValidateCodeException {
        String code = httpServletRequest.getParameter(ParamsConstant.VALIDATE_CODE_CODE);
        String key = httpServletRequest.getParameter(ParamsConstant.VALIDATE_CODE_KEY);
        validateCodeService.check(key, code);
    }
}
