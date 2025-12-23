package com.example.Resume_Screening_Backend.dto;

import com.example.Resume_Screening_Backend.Entity.ApplicationStatus;
import com.example.Resume_Screening_Backend.Entity.Job;
import com.example.Resume_Screening_Backend.Entity.User;
import com.example.Resume_Screening_Backend.MongoDB.Document.ResumeDocument;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ApplicationResponce {

    private Long id;
    private ApplicationStatus status;
    private LocalDateTime appliedAt;

    private Job job;
    private User user;

    private ResumeDocument resume; // 🔥 FULL resume object


}
