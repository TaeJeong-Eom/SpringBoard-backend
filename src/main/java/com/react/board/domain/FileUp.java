package com.react.board.domain;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Data
@Table(name = "fileup")
public class FileUp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="file_id")
    private Long id;
    private String orgnm;
    private String savednm;
    private String savedpath;

    @Builder
    public FileUp(Long id, String orgnm, String savednm, String savedpath) {
        this.id = id;
        this.orgnm = orgnm;
        this.savednm = savednm;
        this.savedpath = savedpath;
    }
}