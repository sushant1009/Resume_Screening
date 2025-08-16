package com.example.Resume_Screening_Backend.Service;

import com.example.Resume_Screening_Backend.Entity.Applications;
import com.example.Resume_Screening_Backend.Entity.Job;
import com.example.Resume_Screening_Backend.Entity.User;
import com.example.Resume_Screening_Backend.Repository.ApplicationsRepository;
import com.example.Resume_Screening_Backend.Repository.JobRepository;
import com.example.Resume_Screening_Backend.Repository.UserRepository;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@NoArgsConstructor(force = true)
public class ApplicationsService {

    private final ApplicationsRepository applicationRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public Applications applyForJob(String username, String jobId) {
        User user = userRepository.findById(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Job job = jobRepository.findById(jobId)
                .orElseThrow(() -> new RuntimeException("Job not found"));

        Applications application = new Applications();
        application.setUser(user);
        application.setJob(job);

        return applicationRepository.save(application);
    }
}

