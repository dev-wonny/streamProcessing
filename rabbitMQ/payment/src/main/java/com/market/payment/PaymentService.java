package com.market.payment;

import com.market.payment.entity.Payment;
import com.market.payment.message.DeliveryMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Slf4j
@Service
public class PaymentService {

	private final RabbitTemplate rabbitTemplate;

	public PaymentService(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	@Value("${message.queue.err.product}")
	private String productErrorQueue;

	public void createPayment(DeliveryMessage deliveryMessage) {
		final Payment payment = Payment.builder()
				.paymentId(UUID.randomUUID())
				.userId(deliveryMessage.getUserId())
				.payStatus("SUCCESS").build();

		final Integer payAmount = deliveryMessage.getPayAmount();

		if (payAmount >= 10000) {
			log.error("Payment amount exceeds limit: {}", payAmount);
			payment.setPayStatus("CANCEL");
			deliveryMessage.setErrorType("PAYMENT_LIMIT_EXCEEDED");
			this.rollbackPayment(deliveryMessage);
		}
	}

	public void rollbackPayment(DeliveryMessage deliveryMessage) {
		log.info("PAYMENT ROLLBACK !!!");
		rabbitTemplate.convertAndSend(productErrorQueue, deliveryMessage);
	}
}