package com.example.Resume_Screening_Backend.Entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Recruiter {
    @Id
    @Column(nullable = false,unique = true)
    private String username;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String fName ;
    @Column(nullable = false)
    private String lName;

    @Column(nullable = false)
    private String password;

    private LocalDateTime createdAt = LocalDateTime.now();
}
