package com.example.demo.controller;

import java.util.List;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.demo.domain.Address;
import com.example.demo.domain.Member;
import com.example.demo.service.MemberService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Controller
@Slf4j
@RequiredArgsConstructor
public class MemberController {
	
	private final MemberService memberService;
	
	@GetMapping("/members/new")
	public String createForm(Model model) {
		model.addAttribute("memberForm",new MemberForm());
		return "members/createMemberForm";
	}
	
	@PostMapping("/members/new")
	public String create(@Validated MemberForm form, BindingResult result) {
		if(result.hasErrors()) {
			return "members/createMemberForm";
		}
		
		Address address = new Address(form.getCity(),form.getStreet(),form.getZipcode());
		
		Member member = new Member();
		member.setName(form.getName());
		member.setAddress(address);
		memberService.join(member);
		
		return "redirect:/";
	}
	
	@GetMapping("/members")
	public String list(Model model) {
		List<Member> members = memberService.findMembers();
		model.addAttribute("members",members);
		return "members/memberList";
	}

}