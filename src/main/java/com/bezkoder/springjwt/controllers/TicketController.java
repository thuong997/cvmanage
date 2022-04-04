package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.Service.TicketService;
import com.bezkoder.springjwt.payload.request.GetListTicketRequest;
import com.bezkoder.springjwt.payload.request.TicketCreateRequest;
import com.bezkoder.springjwt.payload.request.TicketDetailRequest;
import com.bezkoder.springjwt.payload.request.TicketEditRequest;
import com.bezkoder.springjwt.payload.response.TicketResponse;
import com.bezkoder.springjwt.repository.TicketRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TicketController {

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    TicketService ticketService;

    @Autowired
    TicketRepository ticketRepository;

    @GetMapping("/HO108/getListTicket")
    public ResponseEntity<?> getListTicket(@RequestBody GetListTicketRequest userId){
      return new ResponseEntity<>(ticketService.getAllTicket(userId), HttpStatus.OK);
    }


    @PostMapping("/HO103/ticketDetail")
    public ResponseEntity<TicketResponse> ticketDetail(@RequestBody TicketDetailRequest detail){

        return new ResponseEntity<>(ticketService.ticketDetail(detail), HttpStatus.OK);
    }

    @PostMapping( "/HO104/createTicket")
    public ResponseEntity<?> createdTicket(@RequestBody TicketCreateRequest ticketRequest) {
        ticketService.createdTicket(ticketRequest);
        return new ResponseEntity<String>("Create Successfully", HttpStatus.OK);
    }

    @PutMapping( "/HO105/editTicket/{ticketId}")
    public ResponseEntity<?> editTicket(@PathVariable(name = "ticketId") int ticketId,@RequestBody TicketEditRequest editRequest) {
        ticketService.editTicket(ticketId,editRequest);
        return new ResponseEntity<String>("Update Successfully", HttpStatus.OK);
    }


}
