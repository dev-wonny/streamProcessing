package com.market.payment.config;

import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentAppQueueConfig {
	// Spring AMQP에서는 메시지를 객체로 변환하기 위해 메시지 컨버터를 사용
	// Cannot convert from error 발생
	@Bean
	public Jackson2JsonMessageConverter jsonMessageConverter() {
		return new Jackson2JsonMessageConverter();
	}
}