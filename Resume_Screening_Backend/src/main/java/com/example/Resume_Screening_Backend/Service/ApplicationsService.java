package com.example.Resume_Screening_Backend.Service;

import com.example.Resume_Screening_Backend.Entity.ApplicationStatus;
import com.example.Resume_Screening_Backend.Entity.Applications;
import com.example.Resume_Screening_Backend.Entity.Job;
import com.example.Resume_Screening_Backend.Entity.User;
import com.example.Resume_Screening_Backend.MongoDB.Document.ResumeDocument;
import com.example.Resume_Screening_Backend.MongoDB.Repository.ResumeRepository;
import com.example.Resume_Screening_Backend.Repository.ApplicationsRepository;
import com.example.Resume_Screening_Backend.dto.ApplicationResponce;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
//@NoArgsConstructor(force = true)
@AllArgsConstructor
public class ApplicationsService {

    private final ApplicationsRepository applicationRepository;
    private final UserService userService;
    private final JobService jobService;
    private final ResumeRepository resumeRepository;

    public Applications applyForJob(Applications application) {

        User user = userService.getByUsername(application.getUser().getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        System.out.println(application.getJob());
        Job job = jobService.getJobById(application.getJob().getJobId())
                .orElseThrow(() -> new RuntimeException("Job not found"));

        if (applicationRepository.existsByUserUsernameAndJobJobId(
                user.getUsername(), job.getJobId())) {
            throw new IllegalStateException("Already applied for this job");
        }

        application.setUser(user);
        application.setJob(job);
        application.setStatus(ApplicationStatus.PENDING);

        return applicationRepository.save(application);
    }


//    public Optional<List<Applications>> getApplicationByJobId(Long id) {
//        return Optional.ofNullable(applicationRepository.findByJob_JobId(id));
//    }


    public Applications updateApplicationStatus(Long id, ApplicationStatus status) {
        Applications app = applicationRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Application not found"));

        app.setStatus(status);
        return applicationRepository.save(app);
    }

    public Optional<List<ApplicationResponce>> getApplicationByJobId(Long id) {

        List<Applications> applications =
                applicationRepository.findByJob_JobId(id);

        if (applications.isEmpty()) {
            return Optional.empty();
        }

        List<ApplicationResponce> response = applications.stream().map(app -> {

            ResumeDocument resume = null;
            if (app.getResumeId() != null) {
                resume = resumeRepository
                        .findById(app.getResumeId())
                        .orElse(null);
            }

            return new ApplicationResponce(
                    app.getId(),
                    app.getStatus(),
                    app.getAppliedAt(),
                    app.getJob(),
                    app.getUser(),
                    resume
            );
        }).toList();
//        response.forEach(res->System.out.println(res.toString()));
        return Optional.of(response);
    }

    public Optional<List<Applications>> getApplicationsByUsername(String username)
    {
        return Optional.ofNullable(applicationRepository.findByUserUsername(username));
    }


}

