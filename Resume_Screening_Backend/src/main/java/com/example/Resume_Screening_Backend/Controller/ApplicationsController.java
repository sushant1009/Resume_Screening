package com.example.Resume_Screening_Backend.Controller;

import com.example.Resume_Screening_Backend.Entity.*;
import com.example.Resume_Screening_Backend.Service.ApplicationsService;
import com.example.Resume_Screening_Backend.Service.JobService;
import com.example.Resume_Screening_Backend.Service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/applications")
@AllArgsConstructor
public class ApplicationsController {

    private ApplicationsService applicationsService;
    private UserService userService;
    private final JobService jobService;

    @PostMapping("/apply")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> apply(
            @RequestBody Applications application,
            Authentication authentication) {
        System.out.println("Recieved"+application.getJob().getJobId());
        String username = authentication.getName();   // from JWT
        User user = userService.getByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        application.setUser(user);
//        System.out.println(application.toString());

        try {
            Applications saved = applicationsService.applyForJob(application);
            return ResponseEntity.ok(saved);
        } catch (IllegalStateException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(ex.getMessage());
        }

    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<?> getApplicationByJobId(@PathVariable Long id, Authentication authentication){
        String recruiterid = authentication.getName();
        System.out.println("Got it");
        Job job = jobService.getJobById(id)
                .orElseThrow(() -> new RuntimeException("Job not found"));
        String username = job.getRecruiter().getUsername();
        if(!recruiterid.equals(username))
        {
            return (ResponseEntity<?>) ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

//        String id =
        return applicationsService.getApplicationByJobId(id).map(application -> {
                    return ResponseEntity.ok(application);
                })
                .orElse(ResponseEntity.noContent().build());
    }

    @PutMapping("/{id}/status")
    @PreAuthorize("hasRole('RECRUITER')")
    public ResponseEntity<Applications> updateApplicationStatus(
            @PathVariable Long id,
            @RequestBody Map<String, String> body) {

        ApplicationStatus status =
                ApplicationStatus.valueOf(body.get("status"));

        Applications updatedApp =
                applicationsService.updateApplicationStatus(id, status);

        return ResponseEntity.ok(updatedApp);
    }



}
