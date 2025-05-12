package com.react.board.config;

import com.react.board.domain.Member;
import com.react.board.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AdminInitializer implements CommandLineRunner {

    private final MemberRepository memberRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) {
        // 관리자 계정이 없으면 생성
        if (!memberRepository.existsByEmail("admin@example.com")) {
            Member admin = new Member();
            admin.setName("관리자");
            admin.setEmail("admin@example.com");
            admin.setPwd(passwordEncoder.encode("admin1234")); // 초기 비밀번호
            admin.setPhone("01012345678"); // 필요한 경우 설정

            memberRepository.save(admin);
            System.out.println("관리자 계정이 생성되었습니다.");
        }
    }
}