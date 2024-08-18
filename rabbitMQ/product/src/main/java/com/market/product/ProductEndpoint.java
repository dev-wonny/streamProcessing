package com.market.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;


@Slf4j
@Component
public class ProductEndpoint {

	@Value("${spring.application.name}")
	private String appName;

	@RabbitListener(queues = "${message.queue.product}")
	public void receiveMessage(String orderId) {
		log.info("receive orderId:{}, appName : {}", orderId, appName);
	}
}