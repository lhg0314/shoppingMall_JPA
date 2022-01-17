package com.example.demo;

import javax.transaction.Transactional;

import org.assertj.core.api.Assertions;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;

import com.example.demo.domain.Member;
import com.example.demo.repository.MemberRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback(false)
public class MemberRepositoryTest {
	
	@Autowired
	MemberRepository memberRepository;
	
	@Test
	@Transactional
	public void testMember() throws Exception{

		
		
	}

}
