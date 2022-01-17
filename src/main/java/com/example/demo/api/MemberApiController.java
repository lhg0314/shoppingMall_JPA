package com.example.demo.api;

import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.domain.Member;
import com.example.demo.service.MemberService;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class MemberApiController {
	
	private final MemberService memberService;
	
	@GetMapping("/api/v1/members")
	public List<Member> membersV1(){ //entity의 정보가 외부에 노출됨
		return memberService.findMembers();
	}
	
	@GetMapping("/api/v2/members")
	public Result membersV2(){ //entity의 정보가 외부에 노출됨
		List<Member> members = memberService.findMembers();
		List<MemberDto> collect = members.stream().map(m -> new MemberDto(m.getName())).collect(Collectors.toList());
		
		return new Result(collect.size(),collect);
	}
	
	
	@PostMapping("/api/v1/members")
	public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) { //엔티티를 그대로 parameter로 쓰는것은 좋지않다.
		Long id = memberService.join(member);
		return new CreateMemberResponse(id);
	}
	
	@PostMapping("/api/v2/members") // 엔티티에서 변경이 일어나도 api는 영향을 받지 않는다. (덜받는다)
	public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
		Member member = new Member();
		member.setName(request.getName()); //별도의 dto사용
		
		Long id = memberService.join(member);
		return new CreateMemberResponse(id);
	}
	
	@PutMapping("/api/v2/members/{id}")
	public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id, 
				@RequestBody @Valid UpdateMemnerRequest request) {
		memberService.update(id,request.getName());
		Member findMember = memberService.findOne(id);
		return new UpdateMemberResponse(findMember.getId(),findMember.getName());
	}
	
	@Data
	@AllArgsConstructor
	static class Result<T>{
		private int count;
		private T data;
	}
	
	@Data
	@AllArgsConstructor
	static class MemberDto{
		private String name;
	}
	
	@Data
	static class UpdateMemnerRequest{
		private String name;
	}
	@Data
	@AllArgsConstructor
	static class UpdateMemberResponse{
		private Long id;
		private String name;
	}
	
	
	
	@Data
	static class CreateMemberRequest{ //api스펙을 알수 있다.
		private String name; //name만 필요하구나~
	}
	
	@Data
	@AllArgsConstructor
	static class CreateMemberResponse{
		private Long id;
	}

}
