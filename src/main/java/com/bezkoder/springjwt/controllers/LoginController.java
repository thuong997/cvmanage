package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.Service.UserService;
import com.bezkoder.springjwt.payload.request.LoginRequest;
import com.bezkoder.springjwt.payload.response.LoginInfoRespone;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;


@RestController
@AllArgsConstructor
public class LoginController {

    private final UserService userService;

    @PostMapping(value = "/HO100/login")
    public ResponseEntity<LoginInfoRespone> login(@Valid @RequestBody LoginRequest loginRequest) {

        LoginInfoRespone user = userService.getUserByUsername(loginRequest);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

}
