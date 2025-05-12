package com.react.board.service;

import com.react.board.domain.FileUp;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface FileService {
    long saveFile(MultipartFile mf) throws IOException; // file upload
    List<FileUp> getFileUpAll(); // file listing
    FileUp getFileUp(long file_id); // download
}
