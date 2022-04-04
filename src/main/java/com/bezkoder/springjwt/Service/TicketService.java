package com.bezkoder.springjwt.Service;
import com.bezkoder.springjwt.DTO.TicketDTO;
import com.bezkoder.springjwt.models.TicketEntity;
import com.bezkoder.springjwt.payload.request.GetListTicketRequest;
import com.bezkoder.springjwt.payload.request.TicketCreateRequest;
import com.bezkoder.springjwt.payload.request.TicketDetailRequest;
import com.bezkoder.springjwt.payload.request.TicketEditRequest;
import com.bezkoder.springjwt.payload.response.TicketResponse;
import com.bezkoder.springjwt.payload.response.TicketTopPageResponse;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public interface TicketService {

    List<TicketTopPageResponse> getAllTicket();
    List<TicketTopPageResponse>getAllTicket(GetListTicketRequest userId);


    TicketResponse ticketDetail(TicketDetailRequest detail);

//    void createdTicket(String fullName,
//                       String status,
//                       String priority,
//                       String start,
//                       String deadline,
//                       String description,
//                       int job, int level,
//                       Integer[] pic,
//                       Integer[] department,
//                       MultipartFile file);

    void createdTicket(TicketCreateRequest ticketRequest);

    void editTicket(int ticketId, TicketEditRequest editRequest);

}
