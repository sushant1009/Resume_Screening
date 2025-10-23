package com.example.Resume_Screening_Backend.Service;

import com.example.Resume_Screening_Backend.Entity.Applications;
import com.example.Resume_Screening_Backend.Entity.Job;
import com.example.Resume_Screening_Backend.Entity.User;
import com.example.Resume_Screening_Backend.Repository.ApplicationsRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;


@Service
//@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ApplicationsService {

    private final ApplicationsRepository applicationRepository;
    private final UserService userService;
    private final JobService jobService;

    public Applications applyForJob(Applications application) {
        User user = userService.getByUsername(application.getUser().getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        Job job =jobService.getJobById(application.getJob().getJobId())
                .orElseThrow(() -> new RuntimeException("Job not found"));

        return applicationRepository.save(application);
    }
}

