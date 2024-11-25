package study.data_jpa.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import study.data_jpa.entity.Member;
import study.data_jpa.repository.MemberRepository;

@RestController
@RequiredArgsConstructor
public class MemberController {

    private final MemberRepository memberRepository;

    @GetMapping("/members/{id}")
    public String findMember(@PathVariable("id") Long id) {
        Member member = memberRepository.findById(id).get();
        return member.getUsername();
    }
    @GetMapping("/members2/{id}")
    public String findMember(@PathVariable("id") Member member) { // 도메인 클래스 컨버터
        return member.getUsername();
    }
    
    @PostConstruct
    public void init() {
        memberRepository.save(new Member("userA"));
    }
}
