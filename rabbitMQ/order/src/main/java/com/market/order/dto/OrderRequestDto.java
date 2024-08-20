package com.market.order.dto;

import com.market.order.entity.Order;
import com.market.order.message.DeliveryMessage;
import lombok.Data;

import java.util.UUID;

@Data
public class OrderRequestDto {
	// 주문 요청 객체
	private String userId;
	private String productId;
	private Integer productQuantity;
	private Integer payAmount;

	// 빌더 패턴
	public Order toOrder() {
		return Order.builder()
				.orderId(UUID.randomUUID())// id 생성
				.userId(userId)
				.orderStatus("RECEIPT")//영수증
				.build();

	}

	// 주문 메시지
	public DeliveryMessage toDeliveryMessage(UUID orderId) {// id 생성한 것 받아 옴
		return DeliveryMessage.builder()
				.orderId(orderId)
				.userId(userId)
				.productId(productId)
				.productQuantity(productQuantity)
				.payAmount(payAmount)
				.build();
	}
}
