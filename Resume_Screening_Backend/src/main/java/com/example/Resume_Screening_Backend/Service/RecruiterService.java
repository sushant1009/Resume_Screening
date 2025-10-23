package com.example.Resume_Screening_Backend.Service;

import com.example.Resume_Screening_Backend.Entity.Recruiter;
import com.example.Resume_Screening_Backend.Repository.RecruiterRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.Optional;

@NoArgsConstructor
@AllArgsConstructor
@Service
public class RecruiterService {
    @Autowired
    private RecruiterRepository recruiterRepository;

    public Optional<Recruiter> getRecruiterByUsername(String username)
    {
        return recruiterRepository.findByUsername(username);
    }

    public Recruiter save(Recruiter user) {
        return recruiterRepository.save(user);
    }

    public boolean isRecruiterExists(String recruiterId) {
        return recruiterRepository.existsById(recruiterId);
    }
}
