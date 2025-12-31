package com.example.Resume_Screening_Backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Job {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true)
    private Long jobId;

    @Column(nullable = false)
    private String role;

    @Column(columnDefinition = "TEXT",nullable = false)
    private String jobDescription;

    private Long salary;

    @Column(nullable = false)
    private Integer noOpenings;

    @Column(nullable = false)
    private String minQualification;

    @Column(nullable = false)
    private Integer minExperience;

    @Column(nullable = false)
    private List<String> skills;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private Date deadline;

    @ManyToOne
    @JoinColumn(name = "username", nullable = false)
    private Recruiter recruiter;

    @PrePersist
    public void setDefaultSalary() {
        if (salary == null) {
            salary = Long.parseLong(String.valueOf('0')); // default salary
        }
    }

}
