package com.example.Resume_Screening_Backend.MongoDB.Service;

import com.example.Resume_Screening_Backend.MongoDB.Document.ResumeDocument;
import com.example.Resume_Screening_Backend.MongoDB.Repository.ResumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.file.AccessDeniedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;

    @Value("${python.api.url}")
    private String pythonApiUrl;

    public ResumeService(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    public ResumeDocument saveResume(ResumeDocument resume){
        return resumeRepository.save(resume);
    }

    public Optional<ResumeDocument> getResumeById(String resumeId){
        return resumeRepository.findById(resumeId);
    }

    public ResponseEntity<String> analyzeResume(ByteArrayResource resource, String jobDescription) throws IOException {
        RestTemplate restTemplate = new RestTemplate();
        
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", resource); 
        body.add("role", jobDescription); 

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<String> response = restTemplate.postForEntity(pythonApiUrl, requestEntity, String.class);

        return  response;

    }


    public List<ResumeDocument> getAllResumes() {
        List<ResumeDocument> resumes = resumeRepository.findAll();
        List<Map<String, Object>> response = new ArrayList<>();
        return resumeRepository.findAll();
    }
    public List<ResumeDocument> getResumesByUserName(String username)
    {

        List<ResumeDocument> resumes = resumeRepository.findByUserName(username);
        System.out.println("Username"+resumes.size());
            resumes.forEach(System.out::println);
        return resumes;
    }

    public byte[] getResumeBytes(String id) throws IOException {
        ResumeDocument resume = resumeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        Path path = Paths.get(resume.getFilePath());
        return Files.readAllBytes(path);
    }

    public void deleteResume(String resumeId, String username) throws AccessDeniedException {

        ResumeDocument resume = resumeRepository.findById(resumeId)
                .orElseThrow(() -> new RuntimeException("Resume not found"));

        // 🔐 Allow only owner or recruiter
        if (!resume.getUserName().equals(username) &&
                !SecurityContextHolder.getContext()
                        .getAuthentication()
                        .getAuthorities()
                        .stream()
                        .anyMatch(a -> a.getAuthority().equals("ROLE_RECRUITER"))) {

            throw new AccessDeniedException("Not allowed to delete this resume");
        }

        // 🗑 Delete file from storage
        Path path = Paths.get(resume.getFilePath());
        try {
            Files.deleteIfExists(path);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete resume file");
        }

        // 🗑 Delete DB record
        resumeRepository.delete(resume);
    }

}
