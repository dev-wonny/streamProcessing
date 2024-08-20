package com.market.order;

import com.market.order.message.DeliveryMessage;
import com.market.order.entity.Order;
import com.market.order.dto.OrderRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Slf4j
@Service
public class OrderService {

	@Value("${message.queue.product}")
	private String productQueue;

	@Value("${message.queue.payment}")
	private String paymentQueue;

	private final RabbitTemplate rabbitTemplate;

	public OrderService(RabbitTemplate rabbitTemplate) {
		this.rabbitTemplate = rabbitTemplate;
	}

	// DB 연동
	private Map<UUID, Order> orderStore = new HashMap<>();

	// 주문 생성
	public Order createOrder(OrderRequestDto orderRequestDto) {
		final Order order = orderRequestDto.toOrder();
		final DeliveryMessage deliveryMessage = orderRequestDto.toDeliveryMessage(order.getOrderId());

		// DB 저장
		orderStore.put(order.getOrderId(), order);

		log.info("send Message : {}",deliveryMessage.toString());

		// Order -> Product 전달
		rabbitTemplate.convertAndSend(productQueue, deliveryMessage);

		return order;

	}

	// 주문 조회
	public Order getOrder(UUID orderId) {
		// DB 조회
		return orderStore.get(orderId);
	}


	// 주문 취소
	public void rollbackOrder(DeliveryMessage message) {
		// DB 조회
		final Order order = orderStore.get(message.getOrderId());
		order.changeOrderToCanceled(message.getErrorType());
		log.info(order.toString());

	}
}