package com.example.demo.service;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.stereotype.Service;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Member;
import com.example.demo.repository.MemberRepository;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional

public class MemberServiceTest {
	
	
	@Autowired MemberService ms;
	@Autowired MemberRepository mr;
	
	
	@Test
	@Rollback(false)
	public void 회원가입() throws Exception{
		Member member = new Member();
		member.setName("kim");
		
		Long saveId = ms.join(member);
		assertEquals(member, mr.findOne(saveId));

	}
	
	@Test
	public void 중복회원_예외() throws Exception{
		Member member = new Member();
		member.setName("kim");
		
		Member member2 = new Member();
		member2.setName("kim");
		
		ms.join(member);
		ms.join(member2);
		
	}


}
