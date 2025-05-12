package com.react.board.service;

import com.react.board.domain.FileUp;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.UUID;
import com.react.board.repository.FileRepository;

@RequiredArgsConstructor
@Service
public class FileServiceImpl implements FileService {
    @org.springframework.beans.factory.annotation.Value("${file.dir}")
    private String fileDir;
    private final FileRepository fileRepository;

    // upload
    @Override
    public long saveFile(MultipartFile mf) throws IOException {
        File dirStore = new File(fileDir);
        if (!dirStore.exists()) dirStore.mkdirs();
        if (mf.isEmpty()) {
            return -1L;
        }
        String origName = mf.getOriginalFilename(); // original file name 추출
        if (origName == null || origName.trim().isEmpty()) {
            throw new IllegalArgumentException("파일 이름이 비어 있습니다.");
        }

        // 확장자 추출 (확장자가 없는 경우 기본 확장자 ".unknown" 사용)
        int dotIndex = origName.lastIndexOf(".");
        String extension = (dotIndex != -1) ? origName.substring(dotIndex) : ".unknown";

        String uuid = UUID.randomUUID().toString(); // 겹치지 않는 해시코드 만들기
        String savedName = uuid + extension;
        String savedPath = fileDir + savedName; // 파일이 저장될 경로(위치)

        FileUp fileUp = FileUp.builder()
                .orgnm(origName)
                .savednm(savedName)
                .savedpath(savedPath)
                .build();
        mf.transferTo(new File(savedPath)); // 서버에 UUID를 파일명으로 저장

        FileUp savedFile = fileRepository.save(fileUp);

        return savedFile.getId();
    }


    // listing
    @Override
    public List<FileUp> getFileUpAll() {
        List<FileUp> fileUps = fileRepository.findAll();
        return fileUps;
    }

    // download
    @Override
    public FileUp getFileUp(long file_id) {
        FileUp fileUp = fileRepository.findById(file_id).orElse(null);
        return fileUp;
    }
}