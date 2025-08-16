package com.example.Resume_Screening_Backend.Repository;

import com.example.Resume_Screening_Backend.Entity.Recruiter;
import com.example.Resume_Screening_Backend.Entity.User;
import jdk.jfr.Registered;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RecruiterRepository extends JpaRepository<Recruiter,String> {
    Optional<Recruiter> findByUsername(String username);
}
