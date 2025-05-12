package com.react.board.repository;

import com.react.board.domain.FileUp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileUp, Long> { }
