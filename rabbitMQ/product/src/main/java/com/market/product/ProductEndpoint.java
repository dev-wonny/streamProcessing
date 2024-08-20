package com.market.product;

import com.market.product.message.DeliveryMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Value;


@Slf4j
@Component
public class ProductEndpoint {

	@Value("${spring.application.name}")
	private String appName;

	private final ProductService productService;

	public ProductEndpoint(ProductService productService) {
		this.productService = productService;
	}

	@RabbitListener(queues = "${message.queue.product}")
	public void receiveMessage(DeliveryMessage deliveryMessage) {
		log.info("receive deliveryMessage:{}, appName : {}", deliveryMessage.toString(), appName);
		productService.reduceProductAmount(deliveryMessage);

	}

	@RabbitListener(queues="${message.queue.err.product}")
	public void receiveErrorMessage(DeliveryMessage deliveryMessage) {
		log.info("ERROR RECEIVE !!!");
		productService.rollbackProduct(deliveryMessage);
	}
}