package com.example.demo.domain.Item;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;

import com.example.demo.domain.Category;
import com.example.demo.exception.NotEnoughStockException;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Inheritance(strategy = InheritanceType.SINGLE_TABLE) //�� ���̺��� �� ��������
@DiscriminatorColumn(name = "dtype")
public abstract class Item {
	
	@Id
	@GeneratedValue
	@Column(name="item_id")
	private Long id;
	
	private String name;
	private int price;
	private int stockQuantity;
	
	@ManyToMany(mappedBy = "items")
	@JsonIgnore
	private List<Category> catecories = new ArrayList<>();
	
	// ����Ͻ� ����
	
	public void addStock(int quantity) {
		this.stockQuantity += quantity;
	}
	
	public void removeStock(int quantity) {
		int restStock = this.stockQuantity -quantity;
		if(restStock < 0) {
			throw new NotEnoughStockException("need more stick");
		}
		
		this.stockQuantity = restStock;
		
	}
	

}