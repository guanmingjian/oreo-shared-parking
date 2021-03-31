package cn.oreo.gateway.enhance.configure;

import cn.oreo.common.core.entity.constant.OreoConstant;
import cn.oreo.gateway.enhance.runner.OreoRouteEnhanceRunner;
import cn.oreo.gateway.enhance.service.BlackListService;
import cn.oreo.gateway.enhance.service.RateLimitRuleService;
import cn.oreo.gateway.enhance.service.RouteEnhanceCacheService;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.ThreadPoolExecutor;

/**
 * @author GuanMingJian
 * @since 2020/10/6
 */
@EnableAsync
@Configuration
@EnableReactiveMongoRepositories(basePackages = "cn.oreo.gateway.enhance.mapper")
@ConditionalOnProperty(name = "oreo.gateway.enhance", havingValue = "true")
public class OreoRouteEnhanceConfigure {

    @Bean(OreoConstant.ASYNC_POOL)
    public ThreadPoolTaskExecutor asyncThreadPoolTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(30);
        executor.setThreadNamePrefix("Oreo-Gateway-Async-Thread");
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }

    @Bean
    public ApplicationRunner oreoRouteEnhanceRunner(RouteEnhanceCacheService cacheService,
                                                    BlackListService blackListService,
                                                    RateLimitRuleService rateLimitRuleService) {
        return new OreoRouteEnhanceRunner(cacheService, blackListService, rateLimitRuleService);
    }
}
