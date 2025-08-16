package com.example.Resume_Screening_Backend.Service;

import com.example.Resume_Screening_Backend.Entity.Recruiter;
import com.example.Resume_Screening_Backend.Entity.User;
import com.example.Resume_Screening_Backend.Repository.RecruiterRepository;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;

@NoArgsConstructor
@Component
public class RecruiterService {
    private RecruiterRepository recruiterRepository;

    public Optional<Recruiter> getRecruiterByUsername(String username)
    {
        return recruiterRepository.findByUsername(username);
    }

}
