package com.study.filescontroltool.service;

import com.study.filescontroltool.model.FileMetadata;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.util.List;

public interface FileService {
    FileMetadata uploadFile(MultipartFile file);

    FileMetadata replaceFile(MultipartFile file);

    void deleteFile(String fileName);

    FileMetadata getFileByName(String fileName);

    List<FileMetadata> getFilesByDate(LocalDate date);

    List<FileMetadata> getFilesByCustomer(String customer);

    List<FileMetadata> getFilesByType(String type);
}
