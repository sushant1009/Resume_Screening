package com.example.Resume_Screening_Backend.MongoDB.Document;



import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "resumes")
public class ResumeDocument {

    @Id
    private String id;

    private String  userName;
    private String role;
    private String originalFileName;
    private String filePath;
    private double score;
    private Integer experience;
    private List<String> skills;
    private String remarks;
    @JsonIgnore
    private LocalDateTime uploadedAt = LocalDateTime.now();

}
