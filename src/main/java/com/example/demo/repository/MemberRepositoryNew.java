package com.example.demo.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.demo.domain.Member;

public interface MemberRepositoryNew extends JpaRepository<Member, Long>{
	//spring Data Jpa
	//select m from Member m where m.name = ?
	List<Member> findByName(String name);
}
