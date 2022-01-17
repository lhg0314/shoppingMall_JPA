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
	public void 상품주문() throws Exception {
		Member member = new Member();
		member.setName("member1");
		member.setAddress(new Address("서울","강가","123-123"));
		em.persist(member);
		
		Book book =new Book();
		book.setName("JPA");
		book.setPrice(10000);
		book.setStockQuantity(10);
		em.persist(book);
		
		
		int orderCount =2;
		Long orderId = orderService.order(member.getId(), book.getId(), orderCount);
		
		Order getOrder = orderRepsitory.findOne(orderId);
		
		assertEquals("상품주문시 상태는 ORDER", OrderStatus.ORDER,getOrder.getStatus());
		assertEquals("주문한 상품 종류 수가 정확해야 한다.", 1,getOrder.getOrderItems().size());
		assertEquals("주문 가격은 가격*수량이다.", 10000 * orderCount, getOrder.getTotalPrice());
		assertEquals("주문 수량 만큼 재고가 줄어야한다.", 8, book.getStockQuantity());
	}
	
	
	@Test(expected = NotEnoughStockException.class)
	public void 상품재고초과() throws Exception{
		Member member = new Member();
		member.setName("member1");
		member.setAddress(new Address("서울","강가","123-123"));
		em.persist(member);
		
		Item book =new Book();
		book.setName("JPA");
		book.setPrice(10000);
		book.setStockQuantity(10);
		em.persist(book);
		
		int orderCount = 11;
		
		orderService.order(member.getId(), book.getId(), orderCount);
		
		fail("재고수량 부족 예외가 발생해야 한다.");
	}
	
	@Test
	public void 주문취소() throws Exception{
		Member member = new Member();
		member.setName("member1");
		member.setAddress(new Address("서울","강가","123-123"));
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
		
		assertEquals("주문 취소시 상태는 CANCEL이다.", OrderStatus.CANCEL,getOrder.getStatus());
		assertEquals("주문이 취소된 상품은 그만큼 재고가 증가해야 한다.", 10, book.getStockQuantity());
		
		
	}

}
