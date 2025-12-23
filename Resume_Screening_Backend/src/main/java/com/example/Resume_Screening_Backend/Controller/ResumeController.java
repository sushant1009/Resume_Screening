package com.example.Resume_Screening_Backend.Controller;

import com.example.Resume_Screening_Backend.Entity.User;
import com.example.Resume_Screening_Backend.MongoDB.Document.ResumeDocument;
import com.example.Resume_Screening_Backend.MongoDB.Service.FileStorageService;
import com.example.Resume_Screening_Backend.MongoDB.Service.ResumeService;
import com.example.Resume_Screening_Backend.Repository.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.util.*;

@RestController
@RequestMapping("/api/resume")
@RequiredArgsConstructor
public class ResumeController {

    private final ResumeService resumeService;
    private final FileStorageService fileStorageService;
    private final UserRepository userRepository;

    @PostMapping("/upload")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> uploadResume(@RequestParam("resume") MultipartFile file,
                                          @RequestParam("role") String role, Authentication authentication) {
        try {
         
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("Resume file is missing or empty.");
            }
            if (role == null || role.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Job Description / Role is required.");
            }
            String username = authentication.getName();
            User user = userRepository.findByUsername(username).orElse(null);
            if(user == null)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("User Not Found: ");
           
            String filepath = fileStorageService.saveFile(file);

            ResumeDocument doc = new ResumeDocument();
            doc.setUserName(username.trim());
            doc.setRole(role.trim());
            doc.setOriginalFileName(file.getOriginalFilename());
            doc.setFilePath(filepath);
            File resumefile = new File(filepath);
            byte[] filebytes = Files.readAllBytes(resumefile.toPath());
            ByteArrayResource resource = new ByteArrayResource(filebytes) {
                @Override
                public String getFilename() {
                    return resumefile.getName();
                }
            };
            int score = 0;
            List<String> skills = new ArrayList<>();
            int experience = 0;
            ResponseEntity<String> response = resumeService.analyzeResume(resource, role);
            try {
                ObjectMapper mapper = new ObjectMapper();
                JsonNode rootNode = mapper.readTree(response.getBody());
                System.out.println("Done");
                score = rootNode.get("score").asInt();
                skills = Arrays.asList(String.valueOf(rootNode.get("skills")));
                experience = rootNode.get("experience").asInt();

            } catch (Exception e) {
                System.out.println("Python api unavailable");
                throw new RuntimeException("Failed to parse score from Python API response: " + response.getBody(), e);
            }
            doc.setScore(score);
            doc.setExperience(experience);
            doc.setSkills(skills);
            doc.setRemarks("Resume uploaded and screened. Score: " + score);

            ResumeDocument saved = resumeService.saveResume(doc);

            return ResponseEntity.ok(saved);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while uploading resume: " + e.getMessage());
        }
    }

    @PreAuthorize("hasRole('USER') or hasRole('RECRUITER')")
    @GetMapping("/{id}")
    public ResponseEntity<?> getResume(@PathVariable String id) {
        Optional<ResumeDocument> doc = resumeService.getResumeById(id);
        if (doc.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resume Not Found");
        }
        return ResponseEntity.ok(doc);
    }


    @GetMapping("/me")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<List<ResumeDocument>> getMyResumes(
            Authentication authentication) {

        String username = authentication.getName(); // extracted from JWT
        return ResponseEntity.ok(
                resumeService.getResumesByUserName(username)
        );
    }

    @GetMapping("/all")
    public ResponseEntity<List<ResumeDocument>> getAllResumes()
    {
        return ResponseEntity.ok(resumeService.getAllResumes());
    }

    @GetMapping("/preview/{id}")
    @PreAuthorize("hasRole('RECRUITER') or hasRole('USER')")
    public ResponseEntity<byte[]> previewResume(@PathVariable String id) throws IOException {

        byte[] pdfBytes = resumeService.getResumeBytes(id);

        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_PDF)
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=resume.pdf")
                .body(pdfBytes);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteResume(
            @PathVariable String id,
            Authentication authentication) throws AccessDeniedException {

        resumeService.deleteResume(id, authentication.getName());
        return ResponseEntity.noContent().build(); // 204
    }


}
