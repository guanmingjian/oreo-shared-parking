package cn.oreo.auth.runner;

import cn.oreo.common.core.utils.OreoUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

/**
 * @author GuanMingJian
 * @since 2020/10/4
 */
@Component
@RequiredArgsConstructor
public class StartedUpRunner implements ApplicationRunner {

    private final ConfigurableApplicationContext context;
    private final Environment environment;

    @Override
    public void run(ApplicationArguments args) {
        if (context.isActive()) {
            OreoUtil.printSystemUpBanner(environment);
        }
    }
}
