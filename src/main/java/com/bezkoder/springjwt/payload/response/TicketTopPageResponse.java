package com.bezkoder.springjwt.payload.response;

import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@Builder
@AllArgsConstructor
public class TicketTopPageResponse {

    private int id;
    private int jobId;
    private int levelId;
    private String status;
    private String priority;
    private LocalDate start;
    private LocalDate deadline;
    private List<Integer> pic;
    private List<Integer> depId;
    String cvUrl;


}
