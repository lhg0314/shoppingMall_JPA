package com.example.demo.repository;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;

import org.springframework.stereotype.Repository;
import org.springframework.util.StringUtils;

import com.example.demo.domain.Order;
import com.example.demo.domain.OrderStatus;
import com.example.demo.domain.QMember;
import com.example.demo.domain.QOrder;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class OrderRepository {
	
	private final EntityManager em;
	
	public void save(Order order) {
		em.persist(order);
	}
	
	public Order findOne(Long id) {
		return em.find(Order.class,id);
	}
	
	public List<Order> findAll(OrderSearch orderSearch) { //queryDSL사용
		QOrder order = QOrder.order;
		QMember member = QMember.member;
		
		JPAQueryFactory query = new JPAQueryFactory(em);
		return query.select(order)
				.from(order)
				.join(order.member,member)
				.where(statusEq(orderSearch.getOrderStatus()),nameLike(orderSearch.getMemberName()))
				.limit(100)
				.fetch();
	}
	
	private BooleanExpression nameLike(String name) {
		// TODO Auto-generated method stub
		if(!StringUtils.hasText(name)) {return null;}
		return QMember.member.name.like(name);
	}

	private BooleanExpression statusEq(OrderStatus statusCond) {
		if(statusCond == null) {return null;}
		return QOrder.order.status.eq(statusCond);
	}
	
	public List<Order> findAllByString(OrderSearch orderSearch) {

        String jpql = "select o from Order o join o.member m";
        boolean isFirstCondition = true;

    //주문 상태 검색
    if (orderSearch.getOrderStatus() != null) {
        if (isFirstCondition) {
            jpql += " where";
            isFirstCondition = false;
        } else {
            jpql += " and";
        }
        jpql += " o.status = :status";
    }

    //회원 이름 검색
    if (StringUtils.hasText(orderSearch.getMemberName())) {
        if (isFirstCondition) {
            jpql += " where";
            isFirstCondition = false;
        } else {
            jpql += " and";
        }
        jpql += " m.name like :name";
    }

    TypedQuery<Order> query = em.createQuery(jpql, Order.class)
            .setMaxResults(1000);

    if (orderSearch.getOrderStatus() != null) {
        query = query.setParameter("status", orderSearch.getOrderStatus());
    }
    if (StringUtils.hasText(orderSearch.getMemberName())) {
        query = query.setParameter("name", orderSearch.getMemberName());
    }

    return query.getResultList();
}

	public List<Order> findAllWithMemberDilivery() {
		
		return em.createQuery("select o from Order o "+
		"join fetch o.member m "+
		"join fetch o.delivery d",Order.class).getResultList();
		
	
	}
	
	public List<SimpleOrderQueryDto> findOrderDtos(){
		return em.createQuery("select new com.example.demo.repository.SimpleOrderQueryDto(o.id,m.name,o.orderDate, o.status, d.address) from Order o "+
				"join o.member m "+
				"join o.delivery d",SimpleOrderQueryDto.class).getResultList(); // dto조회는 fetch를 사용할수 없다. 재사용성이 없다, 더티체킹 x
	}

	public List<Order> findAllWithItem() {
		return em.createQuery(
				//distinct : 쿼리 + 엔티티 중복 제거
				"select distinct o from Order o" +
				"  join fetch o.member m" +
				" join fetch o.delivery d" +
				" join fetch o.orderItems oi"+
				" join fetch oi.item i", Order.class).getResultList();
	}

	public List<Order> findAllWithMemberDilivery(int offset, int limit) {
		return em.createQuery("select o from Order o "+
				"join fetch o.member m "+
				"join fetch o.delivery d",Order.class)
				.setFirstResult(offset)
				.setMaxResults(limit)
				.getResultList();
	}

}
