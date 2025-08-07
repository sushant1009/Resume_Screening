package com.example.Resume_Screening_Backend.Controller;

import com.example.Resume_Screening_Backend.MongoDB.Document.ResumeDocument;
import com.example.Resume_Screening_Backend.MongoDB.Service.FileStorageService;
import com.example.Resume_Screening_Backend.MongoDB.Service.ResumeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@RestController
@RequestMapping("/api/resume")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;
    private final FileStorageService fileStorageService;

    // ✅ Upload Resume Endpoint
    @PostMapping("/upload")
    public ResponseEntity<?> uploadResume(@RequestParam("resume") MultipartFile file,
                                          @RequestParam("userId") String username) {
        try {
            String filepath = fileStorageService.saveFile(file);

            ResumeDocument doc = new ResumeDocument();
            doc.setUserName(username);
            doc.setOriginalFileName(file.getOriginalFilename());
            doc.setFilePath(filepath);

            doc.setExtractedText("Screening Pending");
            doc.setMatchedSkills(Collections.emptyList()); // ✅ typo fixed here
            doc.setScore(0.0);
            doc.setRemarks("Resume uploaded, not yet screened.");

            ResumeDocument saved = resumeService.saveResume(doc);
            return ResponseEntity.ok(saved);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while uploading resume: " + e.getMessage());
        }
    }

    // ✅ Get resume by ID
    @GetMapping("/{id}")
    public ResponseEntity<?> getResume(@PathVariable String id) {
        ResumeDocument doc = resumeService.getResumeById(id);
        if (doc == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resume Not Found");
        }
        return ResponseEntity.ok(doc);
    }

    // ✅ Get all resumes by user ID
    @GetMapping("/user/{userName}")
    public ResponseEntity<List<ResumeDocument>> getResumesByUserId(@PathVariable String userName) {
        return ResponseEntity.ok(resumeService.getResumesByUserId(userName));
    }
}
