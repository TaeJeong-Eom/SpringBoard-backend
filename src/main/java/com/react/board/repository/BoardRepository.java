package com.react.board.repository;

import com.react.board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface BoardRepository extends JpaRepository<Board, Long> {
    // ✅ 제목 검색
    Page<Board> findBySubjectContaining(String searchKeyword, Pageable pageable);

    // ✅ 내용 검색
    Page<Board> findByContentContaining(String searchKeyword, Pageable pageable);

    // ✅ 작성자 검색
    Page<Board> findByWriterContaining(String searchKeyword, Pageable pageable);

    // ✅ 자동완성 (작성자 이름으로 시작하는 데이터 찾기)
    List<Board> findTop5ByWriterStartingWith(String writer);
}
