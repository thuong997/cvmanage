package com.bezkoder.springjwt.Service;

import com.bezkoder.springjwt.models.JobEntity;


public interface JobService {
    JobEntity getJobById(int id);

    JobEntity findByJobName(String name);

}
