package com.study.filescontroltool.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.XmlMapper;
import com.study.filescontroltool.model.FileMetadata;
import com.study.filescontroltool.repository.FileRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDate;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
public class FileServiceImpl implements FileService {
    private final FileRepository fileRepository;
    private final XmlMapper xmlMapper = new XmlMapper();
    private final ObjectMapper jsonMapper = new ObjectMapper();
    private final Path rootLocation = Paths.get("upload");

    private static final Pattern FILE_NAME_PATTERN =
            Pattern.compile("^(?<customer>[^_]+)_(?<type>[^_]+)_(?<date>\\d{4}-\\d{2}-\\d{2})\\.xml$");

    public FileServiceImpl(FileRepository fileRepository) {
        this.fileRepository = fileRepository;
    }

    private FileMetadata validateAndParse(String fileName) {
        Matcher matcher = FILE_NAME_PATTERN.matcher(fileName);

        if (!matcher.matches()) {
            log.error("Failed to process file: invalid name format '{}'", fileName);
            throw new IllegalArgumentException("Invalid file name format: " + fileName);
        }

        return FileMetadata.builder()
                .fileName(fileName)
                .customer(matcher.group("customer"))
                .documentType(matcher.group("type"))
                .documentDate(LocalDate.parse(matcher.group("date")))
                .build();
    }

    @Override
    @Transactional
    public FileMetadata uploadFile(MultipartFile file) {
        String fileName = file.getOriginalFilename();
        log.info("Starting upload for file: {}", fileName);

        FileMetadata metadata = validateAndParse(fileName);

        if (fileRepository.existsByFileName(fileName)) {
            log.warn("File already exists: {}", fileName);
            throw new RuntimeException("File with name " + fileName + " already exists!");
        }

        try {
            JsonNode xmlTree = xmlMapper.readTree(file.getInputStream());
            String jsonContent = jsonMapper.writeValueAsString(xmlTree);
            metadata.setJsonContent(jsonContent);

            if (!Files.exists(rootLocation)) {
                Files.createDirectories(rootLocation);
            }

            Path destinationFile = rootLocation.resolve(fileName);
            Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

            metadata.setFilePath(destinationFile.toString());

            log.info("File {} successfully processed and saved", fileName);
            return fileRepository.save(metadata);

        } catch (IOException e) {
            log.error("Failed to store or parse file {}", fileName, e);
            throw new RuntimeException("Could not store file.");
        }
    }

    @Override
    public FileMetadata replaceFile(MultipartFile file) {
        return null;
    }

    @Override
    public void deleteFile(String fileName) {

    }

    @Override
    public FileMetadata getFileByName(String fileName) {
        return null;
    }

    @Override
    public List<FileMetadata> getFilesByDate(LocalDate date) {
        return List.of();
    }

    @Override
    public List<FileMetadata> getFilesByCustomer(String customer) {
        return List.of();
    }

    @Override
    public List<FileMetadata> getFilesByType(String type) {
        return List.of();
    }
}
