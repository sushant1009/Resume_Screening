package com.example.Resume_Screening_Backend.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public class Recruiter implements  AppUser {
    @Id
    @Column(nullable = false,unique = true)
    private String username;


    @Column(nullable = false,unique = true)
    private String email;

    @Column(nullable = false)
    private String fName ;

    @Column(nullable = false)
    private String lName;

    @Column(nullable = false)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    @Column(nullable = false)
    private String companyName;

    private LocalDateTime createdAt = LocalDateTime.now();

    @Override
    public String getRole() {
        return "RECRUITER";
    }
}
