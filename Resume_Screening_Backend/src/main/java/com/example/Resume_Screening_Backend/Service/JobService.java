package com.example.Resume_Screening_Backend.Service;

import com.example.Resume_Screening_Backend.Entity.Job;
import com.example.Resume_Screening_Backend.Repository.JobRepository;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@AllArgsConstructor

@Service
public class JobService{


    private final JobRepository jobRepository;
    private final RecruiterService recruiterService;

    public List<Job> getJobs(){
        return jobRepository.findAll();
    }
    public Optional<Job> getJobById(Long jobId)
    {
        return jobRepository.findById(jobId);
    }
    public Optional<Job> getJobByRole(String role)
    {
        return jobRepository.findByRole(role);
    }
    public ResponseEntity<?> saveJob(Job job){
        System.out.println(String.valueOf(job.getRecruiter().getUsername()));
        String recruiterId = String.valueOf(job.getRecruiter().getUsername());
        if(recruiterService.isRecruiterExists(recruiterId))
            return ResponseEntity.ok(jobRepository.save(job));
        System.out.println("Recruiter not found");
        return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Access Denied");
    }

    public List<Job> getAllJobs() {

        return jobRepository.findAll().stream().toList();
    }
}
