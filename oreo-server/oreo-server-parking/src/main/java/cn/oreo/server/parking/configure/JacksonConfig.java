package cn.oreo.server.parking.configure;

import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.math.BigInteger;

/**
 * 配置全局过滤器
 * 把Long类型序列化为String类型
 * @author GuanMingJian
 * @since 2020/11/17
 */
@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer jackson2ObjectMapperBuilderCustomizer() {

        Jackson2ObjectMapperBuilderCustomizer cunstomizer = jacksonObjectMapperBuilder -> {
            jacksonObjectMapperBuilder.serializerByType(BigInteger.class, ToStringSerializer.instance);
            jacksonObjectMapperBuilder.serializerByType(Long.class, ToStringSerializer.instance);
        };
        return cunstomizer;
    }
}
