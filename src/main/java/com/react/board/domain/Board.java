package com.react.board.domain;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity // ✅ JPA 엔티티 선언
@Table(name = "board") // ✅ 실제 DB 테이블명 지정
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Board {

    @Id // ✅ 기본키(PK)
    @GeneratedValue(strategy = GenerationType.IDENTITY) // ✅ 자동 증가 (MySQL, MariaDB용)
    private Long seq;

    @Column(nullable = false) // ✅ NOT NULL 적용
    private String writer;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String subject;

    @Column(columnDefinition = "TEXT") // ✅ 긴 글 저장
    private String content;

    @Temporal(TemporalType.TIMESTAMP) // ✅ 날짜 타입 지정
    @Column(updatable = false) // ✅ 최초 등록일 수정 불가
    private Date rdate = new Date();

    @Temporal(TemporalType.TIMESTAMP) // ✅ 수정 날짜
    private Date udate;

    //  파일 업로드
    @Column
    private String filepath;

    @Column
    private String filename;

    @Column
    private String orgfilename;

    @Column
    private Long filesize;

}