package com.example.Resume_Screening_Backend.Controller;

import com.example.Resume_Screening_Backend.Entity.Applications;
import com.example.Resume_Screening_Backend.Service.ApplicationsService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/applications")
public class ApplicationsController {

    private ApplicationsService applicationsService;

    @PostMapping("/{username}/{jobId}")
    public ResponseEntity<Applications> apply(
            @PathVariable String username,
            @PathVariable String jobId) {

        Applications application = applicationsService.applyForJob(username, jobId);
        return ResponseEntity.ok(application);
    }
}
