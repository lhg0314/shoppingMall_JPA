package com.example.demo.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.Address;
import com.example.demo.domain.Order;
import com.example.demo.domain.OrderStatus;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.OrderSearch;
import com.example.demo.repository.SimpleOrderQueryDto;

import lombok.Data;
import lombok.RequiredArgsConstructor;



/**
 * ToOne(ManyToOne, OneToMany)
 * Order -> Member (ManyToOne)
 * Order -> Delivery (OneToMany)
 *
 *
 *1. 엔티티 -> Dto
 *2. 패치조인
 *3. Dto로 직접조회
 *4. native query 사용
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {
	
	private final OrderRepository orderRepository;
	
	@GetMapping("/api/v1/simple-orders")
	public List<Order> ordersV1(){
		//엔티티는 그대로 사용하면 안됨
		List<Order> all = orderRepository.findAllByString(new OrderSearch());
		return all;
	}
	
	@GetMapping("/api/v2/simple-orders")
	public List<SimpleOrderDto> ordersV2(){
		//DTO조회 방법 -> N+1의 문제가 생김
		List<Order> orders = orderRepository.findAllByString(new OrderSearch());
		List<SimpleOrderDto> result = orders.stream()
		.map(o -> new SimpleOrderDto(o))
		.collect(Collectors.toList());
		
		return result;
	}
	
	@GetMapping("/api/v3/simple-orders")
	public List<SimpleOrderDto> ordersV3(){
		
		List<Order> orders = orderRepository.findAllWithMemberDilivery();
		List<SimpleOrderDto> result = orders.stream()
				.map(o -> new SimpleOrderDto(o))
				.collect(Collectors.toList());
		return result;
	}
	
	@GetMapping("/api/v4/simple-orders")
	public List<SimpleOrderQueryDto> ordersV4(){
		return orderRepository.findOrderDtos();
	}
	
	
	
	
	@Data
	static class SimpleOrderDto{
		private Long orderId;
		private String name;
		private LocalDateTime orderDate;
		private OrderStatus orderStatus;
		private Address address;
		
		public SimpleOrderDto(Order order) {
			orderId = order.getId();
			name = order.getMember().getName(); //lazy초기화
			orderDate = order.getOrderDate();
			orderStatus = order.getStatus();
			address = order.getDelivery().getAddress(); //lazy초기화
		}
		
	}

}
