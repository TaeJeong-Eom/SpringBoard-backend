package com.react.board.controller;

import com.react.board.domain.Board;
import com.react.board.service.BoardService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/board")
public class BoardController {

    private final BoardService boardService;

    // 게시글 목록 조회
    @GetMapping
    public ResponseEntity<Page<Board>> getBoards(
            @PageableDefault(size = 5, sort = "seq") Pageable pageable,
            @RequestParam(required = false) String searchKeyword,
            @RequestParam(required = false, defaultValue = "subject") String catgo) {

        Page<Board> boardPage;
        if (searchKeyword != null && !searchKeyword.isEmpty()) {
            boardPage = boardService.searchS(pageable, searchKeyword, catgo);
        } else {
            boardPage = boardService.listS(pageable);
        }

        return ResponseEntity.ok(boardPage);
    }

    // 게시글 작성
    @PostMapping("/write")
    public ResponseEntity<Board> writeBoard(@RequestBody Board board) {
        Board savedBoard = boardService.insertS(board);
        return ResponseEntity.ok(savedBoard);
    }

    // 게시글 상세 조회
    @GetMapping("/content")
    public ResponseEntity<Board> getBoard(@RequestParam Long seq) {
        return boardService.contentS(seq)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // 게시글 삭제
    @DeleteMapping("/{seq}")
    public ResponseEntity<Void> deleteBoard(@PathVariable Long seq) {
        boardService.deleteS(seq);
        return ResponseEntity.noContent().build();
    }

    // 게시글 수정
    @PostMapping("/{seq}")
    public ResponseEntity<Board> updateBoard(
            @PathVariable Long seq,
            @RequestBody Board board) {
        board.setSeq(seq);
        Board updatedBoard = boardService.updateS(board);
        return ResponseEntity.ok(updatedBoard);
    }

    // 자동완성 데이터
    @PostMapping("/autocomplete")
    public ResponseEntity<List<Map<String, String>>> getAutocompleteData(
            @RequestParam String writer) {
        List<Map<String, String>> data = boardService.getAutoCompleteDataS(writer);
        return ResponseEntity.ok(data);
    }
}