package com.bezkoder.springjwt.controllers;

import com.bezkoder.springjwt.Service.EmailService;
import com.bezkoder.springjwt.payload.request.*;
import com.bezkoder.springjwt.payload.response.UserResponse;
import com.bezkoder.springjwt.Service.UserService;
import com.bezkoder.springjwt.models.UserEntity;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.List;

@RestController

public class UserController {

    @Autowired
    UserService userService;

    @Autowired
    EmailService emailService;

    @GetMapping("/HO106/manage")
    public List<UserResponse> getAllUser(@RequestBody GetListUserRequest userIdlogin){
        List<UserEntity> userEntities = userService.getAllUserList(userIdlogin);

        // convert to DTO
        List<UserResponse> dtos = new ArrayList<>();
        for (UserEntity entity : userEntities){
            UserResponse dto = new UserResponse(entity.getUsername(), entity.getFullName(), entity.getDepEntity().getDepName(),
                    entity.getRole(), entity.getCreatedAt(), entity.getCreatedBy());
            dtos.add(dto);
        }
        return dtos;
    }

    @GetMapping("/HO106/manage/{userId}")
    public UserResponse getUserById(@PathVariable(name = "userId") int userId){
        UserEntity entity = userService.getUserByID(userId);

        //convert to dto
        UserResponse dto = new UserResponse(entity.getUsername(), entity.getFullName(), entity.getDepEntity().getDepName(),
                entity.getRole(), entity.getCreatedAt(), entity.getCreatedBy());
        return dto;
    }

    @PutMapping("/HO107/editUser/{userId}")
    public ResponseEntity<?> updateUserById( @PathVariable(name = "userId") int userId,@Valid @RequestBody UserFormForUpdateRequest form){
        userService.updateUser(userId, form);
        return new ResponseEntity<>("Update Successfully", HttpStatus.OK);
    }

    @PutMapping("/HO109/deleteUser")
    public ResponseEntity<?> deleteUserById(@RequestBody DeleteUserRequest deleteUserRequest){
        userService.deleteUser(deleteUserRequest);
        return new ResponseEntity<>("Delete Successfully", HttpStatus.OK);
    }

    @PostMapping("/HO100/forgot")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPasswordRequest email){
//        userService.sendForgotPasswordViaEmail(email);
        emailService.sendForgotPassword(email);
        return new ResponseEntity<>("We have sent 1 email. Please check email to reset password!",HttpStatus.OK);
    }

    @PutMapping("/HO102/resetPassword/{uuidStr}")
    public ResponseEntity<?> resetPassword(@PathVariable(name = "uuidStr") String uuidStr,@Valid @RequestBody ResetPassRequest resetPassRequest){
        userService.resetPassword(uuidStr,resetPassRequest);
        return new ResponseEntity<>("Reset Password Successfully",HttpStatus.OK);
    }

    @PutMapping("/HO110/changePass/{userId}")
    public ResponseEntity<?> changePassword(@PathVariable(name = "userId") int userId,@Valid @RequestBody ChangePassRequest changePassRequest){
        userService.changePass(userId,changePassRequest);
        return new ResponseEntity<>("Change Password Successfully!",HttpStatus.OK);

    }


}
