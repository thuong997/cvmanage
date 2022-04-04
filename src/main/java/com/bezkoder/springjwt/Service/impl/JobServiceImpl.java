package com.bezkoder.springjwt.Service.impl;

import com.bezkoder.springjwt.Service.JobService;
import com.bezkoder.springjwt.models.JobEntity;
import com.bezkoder.springjwt.repository.JobRespository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class JobServiceImpl implements JobService {
    @Autowired
    JobRespository jobRespository;

    @Override
    public JobEntity getJobById(int id) {
        return jobRespository.findByJobId(id);
    }

    @Override
    public JobEntity findByJobName(String name) {
        return jobRespository.findByJobName(name);
    }
}
