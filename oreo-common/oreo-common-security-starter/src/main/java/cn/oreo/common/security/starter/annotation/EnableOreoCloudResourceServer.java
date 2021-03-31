package cn.oreo.common.security.starter.annotation;


import cn.oreo.common.security.starter.configure.OreoCloudResourceServerConfigure;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author GuanMingJian
 * @since 2020/10/4
 */
@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Import(OreoCloudResourceServerConfigure.class)
public @interface EnableOreoCloudResourceServer {
}
