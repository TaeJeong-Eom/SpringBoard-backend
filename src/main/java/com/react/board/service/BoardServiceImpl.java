package com.react.board.service;

import com.react.board.domain.Board;
import com.react.board.repository.BoardRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BoardServiceImpl implements BoardService {
    private final BoardRepository boardRepository;

    @Override
    public Page<Board> listS(Pageable pageable) {
        return boardRepository.findAll(pageable);
    }

    @Override
    public Board insertS(Board board) {
        board.setSeq(null);
        return boardRepository.save(board);
    }

    @Override
    public Optional<Board> contentS(long seq) {
        return boardRepository.findById(seq);
    }

    @Override
    public void deleteS(long seq) {
        boardRepository.deleteById(seq);
    }

    @Transactional
    @Override
    public Board updateS(Board board) {
        Board existingBoard = boardRepository.findById(board.getSeq())
                .orElseThrow(() -> new IllegalStateException("해당 게시글이 존재하지 않습니다!"));

        existingBoard.setWriter(board.getWriter());
        existingBoard.setEmail(board.getEmail());
        existingBoard.setSubject(board.getSubject());
        existingBoard.setContent(board.getContent());
        existingBoard.setUdate(new Date());

        return boardRepository.save(existingBoard);
    }

    @Override
    public Page<Board> searchS(Pageable pageable, String searchKeyword, String catgo) {
        if ("writer".equals(catgo)) {
            return boardRepository.findByWriterContaining(searchKeyword, pageable);
        } else if ("content".equals(catgo)) {
            return boardRepository.findByContentContaining(searchKeyword, pageable);
        } else {
            return boardRepository.findBySubjectContaining(searchKeyword, pageable);
        }
    }

    @Override
    public List<Map<String, String>> getAutoCompleteDataS(String writer) {
        List<Board> boards = boardRepository.findTop5ByWriterStartingWith(writer);
        return boards.stream()
                .map(board -> Map.of("writer", board.getWriter()))
                .collect(Collectors.toList());
    }
}