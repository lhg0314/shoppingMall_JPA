package com.example.demo.service;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import javax.persistence.EntityManager;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Address;
import com.example.demo.domain.Member;
import com.example.demo.domain.Order;
import com.example.demo.domain.OrderStatus;
import com.example.demo.domain.Item.Book;
import com.example.demo.domain.Item.Item;
import com.example.demo.exception.NotEnoughStockException;
import com.example.demo.repository.OrderRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class OrderServiceTest {
	
	@Autowired
	EntityManager em;
	@Autowired OrderService orderService;
	@Autowired OrderRepository orderRepsitory;
	
	@Test
	public void ��ǰ�ֹ�() throws Exception {
		Member member = new Member();
		member.setName("member1");
		member.setAddress(new Address("����","����","123-123"));
		em.persist(member);
		
		Book book =new Book();
		book.setName("JPA");
		book.setPrice(10000);
		book.setStockQuantity(10);
		em.persist(book);
		
		
		int orderCount =2;
		Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
		
		Order getOrder = orderRepsitory.findOne(orderId);
		
		assertEquals("��ǰ�ֹ��� ���´� ORDER", OrderStatus.ORDER,getOrder.getStatus());
		assertEquals("�ֹ��� ��ǰ ���� ���� ��Ȯ�ؾ� �Ѵ�.", 1,getOrder.getOrderItems().size());
		assertEquals("�ֹ� ������ ����*�����̴�.", 10000 * orderCount, getOrder.getTotalPrice());
		assertEquals("�ֹ� ���� ��ŭ ��� �پ���Ѵ�.", 8, book.getStockQuantity());
	}
	
	
	@Test(expected = NotEnoughStockException.class)
	public void ��ǰ����ʰ�() throws Exception{
		Member member = new Member();
		member.setName("member1");
		member.setAddress(new Address("����","����","123-123"));
		em.persist(member);
		
		Item book =new Book();
		book.setName("JPA");
		book.setPrice(10000);
		book.setStockQuantity(10);
		em.persist(book);
		
		int orderCount = 11;
		
		orderService.order(member.getId(), book.getId(), orderCount);
		
		fail("������ ���� ���ܰ� �߻��ؾ� �Ѵ�.");
	}
	
	@Test
	public void �ֹ����() throws Exception{
		Member member = new Member();
		member.setName("member1");
		member.setAddress(new Address("����","����","123-123"));
		em.persist(member);
		
		Book book =new Book();
		book.setName("JPA");
		book.setPrice(10000);
		book.setStockQuantity(10);
		em.persist(book);
		
		int orderCount = 2;
		Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
		
		orderService.cancelOrder(orderId);
		
		Order getOrder = orderRepsitory.findOne(orderId);
		
		assertEquals("�ֹ� ��ҽ� ���´� CANCEL�̴�.", OrderStatus.CANCEL,getOrder.getStatus());
		assertEquals("�ֹ��� ��ҵ� ��ǰ�� �׸�ŭ ��� �����ؾ� �Ѵ�.", 10, book.getStockQuantity());
		
		
	}

}
