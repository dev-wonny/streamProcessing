package com.market.order.entity;

import lombok.Builder;
import lombok.Data;
import lombok.ToString;

import java.util.UUID;

// entity
@Builder
@Data
@ToString
public class Order {
	// 주문
	private UUID orderId;

	// 고객
	private String userId;

	// 주문 상태
	private String orderStatus;

	// 에러 사유
	private String errorType;

	// 에러 객체
	public void changeOrderToCanceled(String receiveErrorType) {
		orderStatus = "CANCELED";
		errorType = receiveErrorType;
	}
}
