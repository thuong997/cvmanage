package com.bezkoder.springjwt.repository;

import com.bezkoder.springjwt.models.JobEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface JobRespository extends JpaRepository<JobEntity, Integer> {
    JobEntity findByJobId(int id);
    JobEntity findByJobName(String jobName);
}
