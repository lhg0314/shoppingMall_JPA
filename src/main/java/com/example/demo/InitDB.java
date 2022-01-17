package com.example.demo;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Address;
import com.example.demo.domain.Delivery;
import com.example.demo.domain.Member;
import com.example.demo.domain.Order;
import com.example.demo.domain.OrderItem;
import com.example.demo.domain.Item.Book;
import com.example.demo.domain.Item.Item;

import lombok.RequiredArgsConstructor;



/**
 * userA
 * JPA1 Book
 * JPA2 Book
 * 
 * userB
 * Spring1 book
 * spring2 book
 *
 */
@Component
@RequiredArgsConstructor
public class InitDB {
	
	private final InitService initService;
	
	@PostConstruct //스프링빈이 뜨면 호출
	public void init() {
		initService.dbInit();
		initService.dbInit2();
	}
	
	@Component
	@Transactional
	@RequiredArgsConstructor
	static class InitService{
		
		
		
		private final EntityManager em;
		
		public void dbInit() {
			Member member = createMember("user1","서울","1","1111");
			em.persist(member);
			
			Book book = createBook("JPA1 BOOK", 10000, 100);
			em.persist(book);
			
			Book book2 = createBook("JPA2 Book",20000,100);
			em.persist(book2);
			
			OrderItem orderItem1 = OrderItem.createOrderItem(book, 10000, 1);
			OrderItem orderItem2 = OrderItem.createOrderItem(book2, 20000, 2);
			
			Delivery delivery = createDelivery(member);
			Order order = Order.createOrder(member, delivery, orderItem1,orderItem2);
			em.persist(order);
			
			
		}
		
		public void dbInit2() {
			Member member = createMember("user2","부산","2","2222");
			em.persist(member);
			
			Book book = createBook("spring1 Book", 20000, 200);
			em.persist(book);
			
			Book book2 = createBook("spring2 Book", 40000, 300);
			em.persist(book2);
			
			OrderItem orderItem1 = OrderItem.createOrderItem(book, 20000, 3);
			OrderItem orderItem2 = OrderItem.createOrderItem(book2, 40000, 4);
			
			Delivery delivery = createDelivery(member);
			Order order = Order.createOrder(member, delivery, orderItem1,orderItem2);
			em.persist(order);
			
		}
		
		public Member createMember(String name, String city, String street ,String zipcode) {
			Member member = new Member();
			member.setName(name);
			member.setAddress(new Address(city,street,zipcode));
			
			return member;
		}
		
		public Book createBook(String name, int price, int quantity) {
			Book book = new Book();
			book.setName(name);
			book.setPrice(price);
			book.setStockQuantity(quantity);
			return book;
		}
		
		
		public Delivery createDelivery(Member member) {
			Delivery delivery = new Delivery();
			delivery.setAddress(member.getAddress());
			return delivery;
		}
	}
	

}


