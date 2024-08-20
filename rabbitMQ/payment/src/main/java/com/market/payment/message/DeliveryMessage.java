package com.market.payment.message;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.UUID;

@Data
@Builder
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class DeliveryMessage {
	// 주문
	private UUID orderId;

	// 결제
	private UUID paymentId;

	// 고객
	private String userId;

	// 상품
	private String productId;
	private Integer productQuantity;

	// 결제 금액
	private Integer payAmount;

	// 에러 사유
	private String errorType;
}
