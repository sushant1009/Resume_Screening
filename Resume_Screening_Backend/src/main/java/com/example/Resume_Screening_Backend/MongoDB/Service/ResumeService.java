package com.example.Resume_Screening_Backend.MongoDB.Service;


import com.example.Resume_Screening_Backend.MongoDB.Document.ResumeDocument;
import com.example.Resume_Screening_Backend.MongoDB.Repository.ResumeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ResumeService {

    @Autowired
    private ResumeRepository resumeRepository;

    public ResumeService(ResumeRepository resumeRepository) {
        this.resumeRepository = resumeRepository;
    }

    public ResumeDocument saveResume(ResumeDocument resume){
        return resumeRepository.save(resume);
    }
    public ResumeDocument getResumeById(String id){
        return resumeRepository.findById(id).orElse(null);
    }

    public List<ResumeDocument> getResumesByUserId(String userName) {
        return resumeRepository.findByUserName(userName);
    }

}
