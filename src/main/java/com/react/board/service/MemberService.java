package com.react.board.service;

import com.react.board.domain.Member;

public interface MemberService {
    Member register(Member member);
    Member login(String email, String password);
}