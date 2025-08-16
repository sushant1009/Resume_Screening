package com.example.Resume_Screening_Backend.Repository;


import com.example.Resume_Screening_Backend.Entity.Job;
import com.example.Resume_Screening_Backend.Entity.Recruiter;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobRepository extends JpaRepository<Job, String> {
    Optional<Job> findByRecruiter(Recruiter recruiter);
    Optional<Job> findByRole(String role);
}
