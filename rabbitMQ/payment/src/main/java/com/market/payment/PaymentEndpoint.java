package com.market.payment;

import com.market.payment.message.DeliveryMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;


@Slf4j
@Component
public class PaymentEndpoint {

	@Value("${spring.application.name}")
	private String appName;

	private final PaymentService paymentService;

	public PaymentEndpoint(PaymentService paymentService) {
		this.paymentService = paymentService;
	}

	@RabbitListener(queues = "${message.queue.payment}")
	public void receiveMessage(DeliveryMessage deliveryMessage) {
		log.info("PAYMENT RECEIVE : {}", deliveryMessage.toString());
		paymentService.createPayment(deliveryMessage);

	}
}