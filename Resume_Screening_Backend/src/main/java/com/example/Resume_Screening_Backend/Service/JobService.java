package com.example.Resume_Screening_Backend.Service;

import com.example.Resume_Screening_Backend.Entity.Job;
import com.example.Resume_Screening_Backend.Repository.JobRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class JobService{

    @Autowired
    private JobRepository jobRepository;

    public List<Job> getJobs(){
        return jobRepository.findAll();
    }
    public Optional<Job> getJobById(String jobId)
    {
        return jobRepository.findById(jobId);
    }
    public Optional<Job> getJobByRole(String role)
    {
        return jobRepository.findByRole(role);
    }
    public Job saveJob(Job job){
        return jobRepository.save(job);
    }
}
