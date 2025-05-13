//package com.react.board.controller;
//
//import com.react.board.domain.Member;
//import com.react.board.service.MemberService;
//import jakarta.servlet.http.HttpSession;
//import lombok.RequiredArgsConstructor;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.HashMap;
//import java.util.Map;
//
//@RestController
//@RequiredArgsConstructor
//@RequestMapping("/api/member")
//public class MemberController {
//
//    private final MemberService memberService;
//
//    @PostMapping("/register")
//    public ResponseEntity<Map<String, String>> register(@RequestBody Member member) {
//        Map<String, String> response = new HashMap<>();
//        try {
//            memberService.register(member);
//            response.put("result", "success");
//            response.put("message", "회원가입이 완료되었습니다.");
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            response.put("result", "fail");
//            response.put("message", e.getMessage());
//            return ResponseEntity.badRequest().body(response);
//        }
//    }
//
//    @PostMapping("/login")
//    public ResponseEntity<Map<String, String>> login(
//            @RequestBody Map<String, String> loginData,
//            HttpSession session) {
//        Map<String, String> response = new HashMap<>();
//        try {
//            Member member = memberService.login(
//                    loginData.get("email"),
//                    loginData.get("password")
//            );
//            session.setAttribute("loginMember", member);
//            response.put("result", "success");
//            return ResponseEntity.ok(response);
//        } catch (Exception e) {
//            response.put("result", "fail");
//            response.put("message", e.getMessage());
//            return ResponseEntity.badRequest().body(response);
//        }
//    }
//
//    @PostMapping("/logout")
//    public ResponseEntity<Map<String, String>> logout(HttpSession session) {
//        session.invalidate();
//        Map<String, String> response = new HashMap<>();
//        response.put("result", "success");
//        return ResponseEntity.ok(response);
//    }
//
//    @GetMapping("/check-session")
//    public ResponseEntity<Map<String, Object>> checkSession(HttpSession session) {
//        Map<String, Object> response = new HashMap<>();
//        Member member = (Member) session.getAttribute("loginMember");
//
//        if (member != null) {
//            response.put("isLoggedIn", true);
//            response.put("member", member);
//        } else {
//            response.put("isLoggedIn", false);
//        }
//
//        return ResponseEntity.ok(response);
//    }
//}