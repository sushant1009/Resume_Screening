package com.example.Resume_Screening_Backend.Repository;

import com.example.Resume_Screening_Backend.Entity.Applications;
import org.hibernate.dialect.LobMergeStrategy;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ApplicationsRepository extends JpaRepository<Applications, Long> {
    List<Applications> findByUserUsername(String username);
    List<Applications> findByJob_JobId(Long jobId);
}
