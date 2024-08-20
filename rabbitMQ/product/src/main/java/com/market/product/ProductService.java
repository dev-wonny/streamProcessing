package com.market.product;

import com.market.product.message.DeliveryMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Slf4j
@Service
public class ProductService {
	private final RabbitTemplate rabbitTemplate;

	@Value("${message.queue.payment}")
	private String paymentQueue;

	@Value("${message.queue.err.order}")
	private String orderErrorQueue;

	public ProductService(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	/**
	 * 수량을 줄이는
	 * @param deliveryMessage
	 */
	public void reduceProductAmount(DeliveryMessage deliveryMessage) {

		final String productId = deliveryMessage.getProductId();
		final Integer productQuantity = deliveryMessage.getProductQuantity();

		// 에러 발생 -> 롤백
		if (!productId.equals("product1") || productQuantity > 1) {
			this.rollbackProduct(deliveryMessage);
			return;
		}

		// 정상 결제 큐로 보냄
		rabbitTemplate.convertAndSend(paymentQueue, deliveryMessage);
	}

	/**
	 * 롤백 함수 Order -> product 에러 발생 -> Order로 다시 보냄
	 * @param deliveryMessage
	 */
	public void rollbackProduct(DeliveryMessage deliveryMessage) {
		log.info("PRODUCT ROLLBACK!!!");
		if (!StringUtils.hasText(deliveryMessage.getErrorType())) {
			deliveryMessage.setErrorType("PRODUCT ERROR");
		}
		rabbitTemplate.convertAndSend(orderErrorQueue, deliveryMessage);
	}
}
