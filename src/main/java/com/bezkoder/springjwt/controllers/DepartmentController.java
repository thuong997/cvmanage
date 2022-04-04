package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.Service.DepService;
import com.bezkoder.springjwt.Service.impl.DepServiceImpl;
import com.bezkoder.springjwt.payload.response.DepartmentResponse;
import com.bezkoder.springjwt.repository.DepartmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class DepartmentController {
    @Autowired
    DepService depService;

    @GetMapping("department")
    public ResponseEntity<?> getAllDep(){
        List<DepartmentResponse> lst = depService.getAllDepList();
        return new ResponseEntity<>(lst, HttpStatus.OK);
    }

}
