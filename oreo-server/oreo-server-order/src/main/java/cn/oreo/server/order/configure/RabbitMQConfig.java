package cn.oreo.server.order.configure;

import cn.oreo.common.util.common.OreoResponse;
import cn.oreo.common.util.constant.OreoConstant;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class RabbitMQConfig {

	@Bean
	public Queue immediateQueue() {
		return new Queue(OreoConstant.DELAYED_QUEUE_NAME);
	}

	@Bean
	public CustomExchange customExchange() {
		Map<String, Object> args = new HashMap<>();
		args.put("x-delayed-type", "direct");
		return new CustomExchange(OreoConstant.DELAYED_EXCHANGE_NAME, "x-delayed-message", true, false, args);
	}

	@Bean
	public Binding bindingNotify(@Qualifier("immediateQueue") Queue queue,
								 @Qualifier("customExchange") CustomExchange customExchange) {
		return BindingBuilder.bind(queue).to(customExchange).with(OreoConstant.DELAYED_ROUTING_KEY).noargs();
	}
}
