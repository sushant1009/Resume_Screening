package com.example.Resume_Screening_Backend.Controller;

import com.example.Resume_Screening_Backend.Entity.Job;
import com.example.Resume_Screening_Backend.Entity.Recruiter;
import com.example.Resume_Screening_Backend.Entity.User;
import com.example.Resume_Screening_Backend.Service.JobService;
import com.example.Resume_Screening_Backend.Service.UserService;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
@AllArgsConstructor
public class JobController {

    private final JobService jobService;
    private final UserService userService;

    @GetMapping("/{jobId}")
    public ResponseEntity<?> getJobById(@PathVariable Long jobId)
    {
        return jobService.getJobById(jobId)
                .map(job -> ResponseEntity.ok(job))
                .orElse(ResponseEntity.noContent().build());
    }
    @PostMapping("/create")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> saveJob(@RequestBody Job job, Authentication authentication)
    {
        String id = authentication.getName();
        Recruiter recruiter = new Recruiter() ;
        recruiter.setUsername(id);
        job.setRecruiter(recruiter);
        System.out.println(job.toString());
         return ResponseEntity.ok(jobService.saveJob(job));
    }

    @GetMapping("/")
    public ResponseEntity<?> getAllJobs(){
        return ResponseEntity.ok(jobService.getAllJobs());
    }
}
