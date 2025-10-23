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
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
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
    public ResponseEntity<?> uploadResume(@RequestParam("resume") MultipartFile file,
                                          @RequestParam("userId") String username,
                                          @RequestParam("role") String role) {
        try {
            // ✅ Validate inputs
            if (file == null || file.isEmpty()) {
                return ResponseEntity.badRequest().body("Resume file is missing or empty.");
            }
            if (username == null || username.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Username is required.");
            }
            if (role == null || role.trim().isEmpty()) {
                return ResponseEntity.badRequest().body("Job Description / Role is required.");
            }
            User user = userRepository.findByUsername(username).orElse(null);
            if(user == null)
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                        .body("User Not Found: ");
            // ✅ Save the file first
            String filepath = fileStorageService.saveFile(file);

            // ✅ Analyze resume (pass file bytes + role to Python)


            // ✅ Create and save resume document
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





    @GetMapping("/{id}")
    public ResponseEntity<?> getResume(@PathVariable String id) {
        Optional<ResumeDocument> doc = resumeService.getResumeById(id);
        if (doc.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resume Not Found");
        }
        return ResponseEntity.ok(doc);
    }


    @GetMapping("/user/{userName}")
    public ResponseEntity<List<ResumeDocument>> getResumesByUserId(@PathVariable String userName) {
        return ResponseEntity.ok(resumeService.getResumesByUserName(userName));
    }
    @GetMapping("/all")
    public ResponseEntity<List<ResumeDocument>> getAllResumes()
    {
        return ResponseEntity.ok(resumeService.getAllResumes());
    }
}
