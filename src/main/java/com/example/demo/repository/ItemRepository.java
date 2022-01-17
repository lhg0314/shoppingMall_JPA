package com.example.demo.repository;

import java.util.List;

import javax.persistence.EntityManager;

import org.springframework.stereotype.Repository;

import com.example.demo.domain.Item.Item;

import lombok.RequiredArgsConstructor;

@Repository
@RequiredArgsConstructor
public class ItemRepository {
	
	private final EntityManager em;
	
	public void save(Item item) {
		if(item.getId() == null) {
			em.persist(item);
		}else {
			em.merge(item); //update와 비슷한 ? 필드를 모두 바꿔치기하므로 사용하지 않는것이 좋다.
		}
	}
	
	public Item findOne(Long id) {
		return em.find(Item.class, id);
	}
	
	public List<Item> findAll(){
		return em.createQuery("select i from Item i",Item.class).getResultList();
	}

}
