package com.market.order;

import com.market.order.message.DeliveryMessage;
import com.market.order.entity.Order;
import com.market.order.dto.OrderRequestDto;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@Slf4j
@RestController
public class OrderEndpoint {

	private final OrderService orderService;

	public OrderEndpoint(OrderService orderService) {
		this.orderService = orderService;
	}

	// 생성
	@PostMapping("/order")
	public ResponseEntity<Order> order(@RequestBody OrderRequestDto orderRequestDto) {
		final Order order = orderService.createOrder(orderRequestDto);
		return ResponseEntity.ok(order);
	}

	// 조회
	@GetMapping("/order/{orderId}")
	public ResponseEntity<Order> getOrder(@PathVariable UUID orderId) {
		final Order order = orderService.getOrder(orderId);
		return ResponseEntity.ok(order);
	}

	/**
	 * market.err.order 리슨하게 만들기
	 * @param message
	 */
	@RabbitListener(queues = "${message.queue.err.order}")
	public void errOrder(DeliveryMessage message) {
		log.info("ERROR RECEIVE !!!");
		orderService.rollbackOrder(message);
	}
}