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
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.List;
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

    public List<ResumeDocument> getResumesByUserId(String userName) {
        return resumeRepository.findByUserName(userName);
    }
    public Optional<ResumeDocument> getResumeById(String resumeId){
        return resumeRepository.findById(resumeId);
    }


    public ResponseEntity<String> analyzeResume(ByteArrayResource resource, String jobDescription) throws IOException {
        RestTemplate restTemplate = new RestTemplate();

        // Wrap MultipartFile into a ByteArrayResource so RestTemplate can send it as multipart


        // Prepare request body with matching keys from Python API
        MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
        body.add("file", resource); // matches request.files["file"]
        body.add("role", jobDescription); // matches request.form["role"]

        // Prepare headers
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);

        // Create HTTP entity
        HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(body, headers);

        // Send POST request
        ResponseEntity<String> response = restTemplate.postForEntity(pythonApiUrl, requestEntity, String.class);

        return  response;

    }



}
