package com.example.Resume_Screening_Backend.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    private String role;
    private String jobDescription;
    private Integer noOpenings;
    private String companyName;

    @ManyToOne
    @JoinColumn(name = "username", nullable = false)
    private Recruiter recruiter;

    @OneToMany(mappedBy = "job")
    private List<Applications> applications;
}
