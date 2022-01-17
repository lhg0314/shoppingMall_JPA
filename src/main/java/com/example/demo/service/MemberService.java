package com.example.demo.service;

import java.util.List;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.example.demo.domain.Member;
import com.example.demo.repository.MemberRepository;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor //final 생성자를 만들어준다.
public class MemberService {
	
	
	private final MemberRepository memberRepository;
	
	
	//회원가입
	
	public Long join(Member member) {
		validateDuplidateMember(member);//중복 검증
		memberRepository.save(member);
		return member.getId();
	}

	private void validateDuplidateMember(Member member) {
		List<Member> findMember = memberRepository.findByName(member.getName());
		if(!findMember.isEmpty()) {
			throw new IllegalStateException("이미 존재하는 화원입니다.");
		}
		
	}
	
	//전체조회
	public List<Member> findMembers(){
		return memberRepository.findAll();
	}
	
	public Member findOne(Long memberId) {
		return memberRepository.findOne(memberId);
	}

	@Transactional
	public void update(Long id, String name) {
		// TODO Auto-generated method stub
		Member member = memberRepository.findOne(id);
		member.setName(name);
		
	}

}
