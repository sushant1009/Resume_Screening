package com.example.Resume_Screening_Backend.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class User implements AppUser {

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
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    private String password;

    private LocalDateTime createdAt = LocalDateTime.now();

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Applications> applications;


    @Override
    public String getRole() {
        return "USER";
    }
}
