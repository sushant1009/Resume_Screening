package com.example.Resume_Screening_Backend.Controller;

import com.example.Resume_Screening_Backend.Entity.Job;
import com.example.Resume_Screening_Backend.Entity.User;
import com.example.Resume_Screening_Backend.Service.JobService;
import com.example.Resume_Screening_Backend.Service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/jobs")
public class JobController {

    private JobService jobService;
    private UserService userService;

    @GetMapping("/{jobId}")
    public ResponseEntity<?> getJobById(@PathVariable String jobId)
    {
        return jobService.getJobById(jobId)
                .map(job -> ResponseEntity.ok(job))
                .orElse(ResponseEntity.noContent().build());
    }
    @PostMapping("/{role}")
    public ResponseEntity<?> saveJob(@RequestBody Job job)
    {
         return ResponseEntity.ok(jobService.saveJob(job));
    }
}
