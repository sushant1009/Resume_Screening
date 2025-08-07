package com.example.Resume_Screening_Backend.MongoDB.Service;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

@Service
public class FileStorageService {
    private final String uploadDir = "D:/resumes/";

    public String saveFile(MultipartFile file) throws IOException {
        Files.createDirectories(Paths.get(uploadDir));

        String originalFileName = file.getOriginalFilename();

        if (originalFileName == null || originalFileName.isBlank()) {
            throw new IllegalArgumentException("Invalid file name");
        }

        if (!(originalFileName.endsWith(".pdf") || originalFileName.endsWith(".docx"))) {
            throw new IllegalArgumentException("Only PDF and DOCX files are allowed");
        }

        String uniqueFileName = UUID.randomUUID() + "_" + originalFileName;
        String filePath = Paths.get(uploadDir, uniqueFileName).toString();

        file.transferTo(new File(filePath));
        return filePath;
    }
}
