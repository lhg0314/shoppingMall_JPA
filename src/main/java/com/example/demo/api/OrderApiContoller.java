package com.example.demo.api;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.api.OrderSimpleApiController.SimpleOrderDto;
import com.example.demo.domain.Address;
import com.example.demo.domain.Order;
import com.example.demo.domain.OrderItem;
import com.example.demo.domain.OrderStatus;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.OrderSearch;
import com.example.demo.repository.query.OrderQueryDto;
import com.example.demo.repository.query.OrderQueryRepository;
import com.example.demo.repository.query.OrdrerFlatDto;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class OrderApiContoller {
	
	private final OrderRepository orderRepository;
	private final OrderQueryRepository orderQueryRepository;
	
	@GetMapping("/api/v1/orders")	
	public List<Order> orderV1(){
		List<Order> all = orderRepository.findAllByString(new OrderSearch());
		
		for(Order order : all) {
			order.getMember().getName();
			order.getDelivery().getAddress();
			List<OrderItem> orderItems = order.getOrderItems();
			orderItems.stream()
			.forEach(o -> o.getItem().getName());
		}
		
		return all;
	}
	
	@GetMapping("/api/v2/orders")	
	public List<OrderDto> orderV2(){
		List<Order> orders = orderRepository.findAllByString(new OrderSearch());
		List<OrderDto> collect = orders.stream().map(o -> new OrderDto(o)).collect(Collectors.toList());
		
		return collect;
	}
	
	@GetMapping("/api/v3/orders")	
	public List<OrderDto> orderV3(){
		//���� : ������ �ѹ��� ����
		//���� : ����¡ �Ұ��� -> setFirstResult, setMaxResult ���Ұ�
		
		List<Order> orders = orderRepository.findAllWithItem();
		List<OrderDto> collect = orders.stream().map(o -> new OrderDto(o)).collect(Collectors.toList());
		
		return collect;
	}
	
	@GetMapping("/api/v3.1/orders")	
	public List<OrderDto> orderV3_page(@RequestParam(value = "offset",defaultValue ="0")int offset,@RequestParam(value = "limit",defaultValue = "100")int limit){
		//default_batch_fetch_size :�÷��� ��ȸ �� in�� ����ؼ� �ѹ��� ������ -> �����ε� �ذ� 
		// 1000�� ���Ϸ� �Ұ�
		//����¡ ���� , �ߺ����� ������
		
		List<Order> orders = orderRepository.findAllWithMemberDilivery(offset,limit);
		List<OrderDto> collect = orders.stream().map(o -> new OrderDto(o)).collect(Collectors.toList());
		
		return collect;
	}
	
	@GetMapping("/api/v4/orders")
	public List<OrderQueryDto> ordersV4(){
		// N+1���ǻ�
		return orderQueryRepository.findOrderQueryDtos();
	}
	
	
	@GetMapping("/api/v5/orders")
	public List<OrderQueryDto> ordersV5(){
		return orderQueryRepository.findAllByDto_oprtimization();
	}
	
	@GetMapping("/api/v6/orders")
	public List<OrdrerFlatDto> ordersV6(){
		//�ѹ� ����
		//���� �ߺ� ����
		//aplication�۾� ŭ
		//����¡ �Ұ���
		return orderQueryRepository.findAllByDto_flat();
	}
	
	@Data
	static class OrderDto{
		private Long orderId;
		private String name;
		private LocalDateTime orderDate;
		private OrderStatus orderStatus;
		private Address address;
//		private List<OrderItem> orderItems;
		private List<OrderItemDto> orderItems;
		
		
		public OrderDto(Order order) {
			orderId = order.getId();
			name = order.getMember().getName(); 
			orderDate = order.getOrderDate();
			orderStatus = order.getStatus();
			address = order.getDelivery().getAddress();
			orderItems = order.getOrderItems().stream()
			.map(o -> new OrderItemDto(o)).collect(Collectors.toList()); // �̷��� ��������
//			orderItems = order.getOrderItems();
		}
	}
	
	@Data
	static class OrderItemDto {
		
		private String itemName;
		private int orderPrice;
		private int count;
		
		public OrderItemDto(OrderItem orderItem) {
			itemName = orderItem.getItem().getName();
			orderPrice = orderItem.getOrderPrice();
			count = orderItem.getCount();
		}
	}

}
