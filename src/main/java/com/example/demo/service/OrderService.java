package com.example.demo.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Delivery;
import com.example.demo.domain.Member;
import com.example.demo.domain.Order;
import com.example.demo.domain.OrderItem;
import com.example.demo.domain.Item.Item;
import com.example.demo.repository.ItemRepository;
import com.example.demo.repository.MemberRepository;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.OrderSearch;

import lombok.RequiredArgsConstructor;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class OrderService {
	
	private final OrderRepository orderRepository; 
	private final MemberRepository memberRepository;
	private final ItemRepository itemRepository;
	
	//�ֹ� 
	@Transactional
	public Long order(Long memberId, Long itemId, int count) {
		Member member = memberRepository.findOne(memberId);
		Item item = itemRepository.findOne(itemId);
		
		//������� ����
		Delivery delivery = new Delivery();
		delivery.setAddress(member.getAddress());
		
		//�ֹ� ��ǰ ����
		
		OrderItem orderItem = OrderItem.createOrderItem(item, item.getPrice(), count);
		
		//�ֹ� ����
		Order order = Order.createOrder(member, delivery, orderItem);
		
		//�ֹ� ����
		orderRepository.save(order);
		
		return order.getId();
	}
	//���
	@Transactional
	public void cancelOrder(Long orderId) {
		//�ֹ� ������ ��ȸ
		Order order = orderRepository.findOne(orderId);
		//�ֹ� ���
		order.cancel();
	}
	
	//�˻�
	//public list<Order> findOrders(OrderSearch ordersearch){}
	public List<Order> findOrders(OrderSearch orderSearch) {
        return orderRepository.findAllByString(orderSearch);
    }
}
