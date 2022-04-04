package com.bezkoder.springjwt.payload.response;


import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TicketResponse {
    private String fullName;
    private String status;
    private Integer[] pic;
    private int jobId;
    private int levelId;
    private String priority;
    private Integer[] department;
    private String start;
    private String deadline;
    private String cvUrl;
    private String description;


}
