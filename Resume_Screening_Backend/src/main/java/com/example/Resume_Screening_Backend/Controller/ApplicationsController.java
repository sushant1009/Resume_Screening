package com.example.Resume_Screening_Backend.Controller;

import com.example.Resume_Screening_Backend.Entity.Applications;
import com.example.Resume_Screening_Backend.Service.ApplicationsService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/applications")
@AllArgsConstructor
public class ApplicationsController {

    private ApplicationsService applicationsService;

    @PostMapping("/")
    public ResponseEntity<Applications> apply(@RequestBody Applications application) {


        return ResponseEntity.ok(applicationsService.applyForJob(application));
    }
}
