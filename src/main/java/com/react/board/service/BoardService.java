package com.react.board.service;

import com.react.board.domain.Board;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public interface BoardService {
    Page<Board> listS(Pageable pageable); // 🔥 페이지네이션 추가
    Board insertS(Board board);
    void deleteS(long seq);
    Board updateS(Board board);
    Optional<Board> contentS(long seq);
    Page<Board> searchS(Pageable pageable, String searchKeyword, String catgo); // 검색 기능
    List<Map<String, String>> getAutoCompleteDataS(String writer); // 자동완성 데이터 제공
}