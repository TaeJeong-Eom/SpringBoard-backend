package com.react.board.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BoardFile {
    private String filename;
    private String orgFilename;
    private String filepath;
    private long filesize;
}
