package com.example.Resume_Screening_Backend.MongoDB.Repository;

import com.example.Resume_Screening_Backend.MongoDB.Document.ResumeDocument;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResumeRepository extends MongoRepository<ResumeDocument, String> {



    List<ResumeDocument> findByUserName(String userName);
}
