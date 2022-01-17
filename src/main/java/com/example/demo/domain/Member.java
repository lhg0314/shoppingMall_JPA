package com.example.demo.domain;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotEmpty;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Member {
	
	@Id @GeneratedValue
	@Column(name = "member_id")
	private Long Id;
	
	@NotEmpty
	private String name;
	
	@Embedded
	private Address address;
	
	@JsonIgnore // json정보에서 빠진다.
	@OneToMany(mappedBy = "member")
	private List<Order> orders = new ArrayList<>();

}
