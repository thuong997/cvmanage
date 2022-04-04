package com.bezkoder.springjwt.DTO;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
public class JobDTO implements Serializable {
    private int jobId;

    private String jobName;
}
