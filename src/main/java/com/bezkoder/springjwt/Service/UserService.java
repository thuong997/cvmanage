package com.bezkoder.springjwt.Service;



import com.bezkoder.springjwt.payload.request.*;
import com.bezkoder.springjwt.models.UserEntity;
import com.bezkoder.springjwt.payload.response.LoginInfoRespone;
import org.springframework.security.core.userdetails.UserDetailsService;


import javax.transaction.Transactional;
import java.util.List;
import java.util.Optional;

public interface UserService extends UserDetailsService {
    List<UserEntity> getAllUserList(GetListUserRequest getListUserRequest);

    UserEntity getUserByID(int userId);

    LoginInfoRespone getUserByUsername(LoginRequest loginRequest);

    @Transactional
    void updateUser(int userId, UserFormForUpdateRequest form);

    void deleteUser(DeleteUserRequest deleteUserRequest);

    void resetPassword(String uuid,ResetPassRequest resetPassRequest);

//    void forgotPassword(String email);
//
//    void sendForgotPasswordViaEmail(String email);

    Optional<UserEntity> findUserByUsername(String email);

    @Transactional
    void changePass(int userId, ChangePassRequest changePassRequest);
}
