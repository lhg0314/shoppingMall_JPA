package com.example.demo.repository.query;

import java.time.LocalDateTime;
import java.util.List;

import com.example.demo.domain.Address;
import com.example.demo.domain.OrderStatus;

import lombok.Data;

@Data
public class OrdrerFlatDto {
	
	private Long orderId;
	private String name;
	private LocalDateTime orderDate;
	private OrderStatus orderStatus;
	private Address address;
	
	private String itemName;
	private int orderPrice;
	private int count;

	public OrdrerFlatDto(Long orderId, String name, LocalDateTime orderDate, OrderStatus orderStatus, Address address,
			String itemName, int orderPrice, int count) {
		this.orderId = orderId;
		this.name = name;
		this.orderDate = orderDate;
		this.orderStatus = orderStatus;
		this.address = address;
		this.itemName = itemName;
		this.orderPrice = orderPrice;
		this.count = count;
	}
}
