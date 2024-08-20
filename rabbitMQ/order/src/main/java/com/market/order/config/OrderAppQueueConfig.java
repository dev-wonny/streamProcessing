package com.market.order.config;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OrderAppQueueConfig {

	@Bean
	public Jackson2JsonMessageConverter producerJackson2MessageConverter() {
		return new Jackson2JsonMessageConverter();
	}

	// 정상 과정
	@Value("${message.exchange}")
	private String exchange;

	@Value("${message.queue.product}")
	private String queueProductName;

	@Value("${message.queue.payment}")
	private String queuePaymentName;

	// 에러 과정
	@Value("${message.err.exchange}")
	private String exchangeErr;

	@Value("${message.queue.err.order}")
	private String queueErrOrderName;

	@Value("${message.queue.err.product}")
	private String queueErrProductName;

	// Exchange 생성
	@Bean
	public TopicExchange exchange() {
		return new TopicExchange(exchange);
	}

	@Bean
	public TopicExchange exchangeErr() {
		return new TopicExchange(exchangeErr);
	}

	// Queue 생성
	@Bean
	public Queue queueProduct() {
		return new Queue(queueProductName);
	}

	@Bean
	public Queue queuePayment() {
		return new Queue(queuePaymentName);
	}

	@Bean
	public Queue queueErrOrder() {
		return new Queue(queueErrOrderName);
	}

	@Bean
	public Queue queueErrProduct() {
		return new Queue(queueErrProductName);
	}

	// Binding 생성

	/**
	 * Exchange -> QueueProduct
	 * @return
	 */
	@Bean
	public Binding bindingProduct() {
		return BindingBuilder
				.bind(queueProduct())
				.to(exchange())
				.with(queueProductName);
	}

	/**
	 *  Exchange -> Queue Payment
	 * @return
	 */
	@Bean
	public Binding bindingPayment() {
		return BindingBuilder
				.bind(queuePayment())
				.to(exchange())
				.with(queuePaymentName);
	}

	/**
	 * Payment > Product > Order
	 * Error Exchange -> Error Queue Order
	 * @return
	 */
	@Bean
	public Binding bindingErrOrder() {
		return BindingBuilder
				.bind(queueErrOrder())
				.to(exchangeErr())
				.with(queueErrOrderName);
	}

	/**
	 * Payment > Product > Order
	 * Error Exchange -> Error Queue Product
	 * @return
	 */
	@Bean
	public Binding bindingErrProduct() {
		return BindingBuilder
				.bind(queueErrProduct())
				.to(exchangeErr())
				.with(queueErrProductName);
	}
}