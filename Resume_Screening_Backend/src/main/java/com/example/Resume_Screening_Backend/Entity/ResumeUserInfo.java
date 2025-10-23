package com.example.Resume_Screening_Backend.Entity;

import jakarta.persistence.Column;

import java.util.List;

public class ResumeUserInfo {

    private String role;
    private double score;
    private Integer experience;
    private List<String> skills;
    private String remarks;
    private String fName ;
    private String lName;
    private String email;
}
