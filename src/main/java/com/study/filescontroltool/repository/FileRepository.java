package com.study.filescontroltool.repository;

import com.study.filescontroltool.model.FileMetadata;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface FileRepository extends JpaRepository<FileMetadata, UUID> {
    boolean existsByFileName(String fileName);

    Optional<FileMetadata> findByFileName(String fileName);

    List<FileMetadata> findAllByDocumentDate(LocalDate date);

    List<FileMetadata> findAllByCustomer(String customer);

    List<FileMetadata> findAllByDocumentType(String type);

    void deleteByFileName(String fileName);
}
