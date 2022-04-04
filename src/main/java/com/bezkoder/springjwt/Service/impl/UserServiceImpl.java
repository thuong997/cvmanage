package com.bezkoder.springjwt.Service.impl;


import com.bezkoder.springjwt.models.UUIDEntity;
import com.bezkoder.springjwt.payload.request.*;
import com.bezkoder.springjwt.Service.UserService;
import com.bezkoder.springjwt.Utils.ErrorCode;
import com.bezkoder.springjwt.models.DepEntity;
import com.bezkoder.springjwt.models.UserEntity;
import com.bezkoder.springjwt.payload.response.LoginInfoRespone;
import com.bezkoder.springjwt.repository.DepartmentRepository;
import com.bezkoder.springjwt.repository.UUIDRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import com.bezkoder.springjwt.security.CustomException;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    DepartmentRepository depRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UUIDRepository uuidRepository;


    @Override
    public List<UserEntity> getAllUserList(GetListUserRequest userIdLogin) {

            Optional<UserEntity> userEntityOpt = userRepository.findByUserIdAndIsDeletedFalse(userIdLogin.getUserIdLogin());
            if (!userEntityOpt.isPresent()) {
                throw new CustomException(HttpStatus.BAD_REQUEST)
                        .addError(ErrorCode.ERR_USER_NOT_FOUND)
                        .withMessage(ErrorCode.ERR_USER_NOT_FOUND.getMessage());
            }
            UserEntity userEntity = userEntityOpt.get();
            if(userEntity.getRole().equals("0")) {
                return userRepository.findAllByIsDeletedFalse();
            }else{
                throw new CustomException(HttpStatus.BAD_REQUEST)
                        .addError(ErrorCode.ERR_USER_NOT_ADMIN)
                        .withMessage(ErrorCode.ERR_USER_NOT_ADMIN.getMessage());
            }
    }

    @Override
    public UserEntity getUserByID(int userId) {
            return userRepository.findByUserIdAndIsDeletedFalse(userId).get();
    }

    @Override
    @Transactional
    public LoginInfoRespone getUserByUsername(LoginRequest loginRequest) {
//        String emailPattern = "^([a-zA-Z0-9.!#$%&'*+\\/=?^_`{|}~-]){1,64}@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,61}[a-zA-Z0-9])?)*$";
//        String passwordPattern = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,10}$";
            Optional<UserEntity> user = userRepository.findByUsernameAndIsDeletedFalse(loginRequest.getEmail());
            if (!user.isPresent()) {
                throw new CustomException(HttpStatus.BAD_REQUEST)
                        .addError(ErrorCode.ERR_USER_NOT_FOUND)
                        .withMessage(ErrorCode.ERR_USER_NOT_FOUND.getMessage());
            }

            if (!BCrypt.checkpw(loginRequest.getPassword(), user.get().getPassword())) {
                throw new CustomException(HttpStatus.BAD_REQUEST)
                        .addError(ErrorCode.ERR_PASSWORD_IS_INCORRECT)
                        .withMessage(ErrorCode.ERR_PASSWORD_IS_INCORRECT.getMessage());
            }

            return new LoginInfoRespone(
                    user.get().getUserId(),
                    user.get().getUsername(),
                    user.get().getRole(),
                    user.get().getDepEntity().getDepId());

    }


    @Override
    @Transactional
    public void updateUser(int userId, UserFormForUpdateRequest form) {
            Optional<UserEntity> userEntityOpt = userRepository.findByUserIdAndIsDeletedFalse(userId);
            if (!userEntityOpt.isPresent()) {
                throw new CustomException(HttpStatus.BAD_REQUEST)
                        .addError(ErrorCode.ERR_USER_NOT_FOUND)
                        .withMessage(ErrorCode.ERR_USER_NOT_FOUND.getMessage());
            }
            UserEntity userEntity = userEntityOpt.get();

        Optional<UserEntity> userLoginEntityOpt = userRepository.findByUserIdAndIsDeletedFalse(form.getUserIdLogin());
        if (!userLoginEntityOpt.isPresent()) {
            throw new CustomException(HttpStatus.BAD_REQUEST)
                    .addError(ErrorCode.ERR_USER_NOT_FOUND)
                    .withMessage(ErrorCode.ERR_USER_NOT_FOUND.getMessage());
        } else if (!userLoginEntityOpt.get().getRole().equals("0")) {
            throw new CustomException(HttpStatus.BAD_REQUEST)
                    .addError(ErrorCode.ERR_NOT_ROLE)
                    .withMessage(ErrorCode.ERR_NOT_ROLE.getMessage());
        }

        Optional<UserEntity> userOpt = userRepository.findByUsernameAndIsDeletedFalse(form.getEmail());
        if (userOpt.isPresent()) {
            if (userOpt.get().getUserId() != userId) {
                throw new CustomException(HttpStatus.BAD_REQUEST)
                        .addError(ErrorCode.ERR_USER_NAME_DUPLICATE)
                        .withMessage(ErrorCode.ERR_USER_NAME_DUPLICATE.getMessage());
            }
        }


            userEntity.setUsername(form.getEmail().trim());
            if ( !StringUtils.isBlank(form.getPassword()) && StringUtils.isNoneEmpty(form.getPassword().trim())) {
                String regex = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)[a-zA-Z\\d]{6,10}$";
                if (form.getPassword().trim().matches(regex)) {
                    userEntity.setPassword(passwordEncoder.encode(form.getPassword()).trim());
                } else {
                    throw new CustomException(HttpStatus.BAD_REQUEST)
                            .addError(ErrorCode.ERR_WRONG_PASSWORD_FORMAT)
                            .withMessage(ErrorCode.ERR_WRONG_PASSWORD_FORMAT.getMessage());
                }
            }
            userEntity.setFullName(form.getFullName().trim());
            DepEntity depEntity = depRepository.findById(form.getDepId()).get();
            userEntity.setDepEntity(depEntity);
            userEntity.setRole(form.getRole());
            userRepository.save(userEntity);
    }

    @Override
    public void deleteUser(DeleteUserRequest deleteUserRequest) {
            Optional<UserEntity> userEntityOpt = userRepository.findByUserIdAndIsDeletedFalse(deleteUserRequest.getUserIdLogin());
            if (!userEntityOpt.isPresent()) {
                throw new CustomException(HttpStatus.BAD_REQUEST)
                        .addError(ErrorCode.ERR_USER_NOT_FOUND)
                        .withMessage(ErrorCode.ERR_USER_NOT_FOUND.getMessage());
            }
            Optional<UserEntity> userOpt = userRepository.findByUserIdAndIsDeletedFalse(deleteUserRequest.getUserId());

            // 0: Admin;    1: User
            if (userEntityOpt.get().getRole().equals("0") && userOpt.isPresent()) {
                UserEntity user = userOpt.get();
                user.setDeleted(true);
                userRepository.save(user);
            } else {
                throw new CustomException(HttpStatus.BAD_REQUEST)
                        .addError(ErrorCode.ERR_USER_NOT_FOUND)
                        .withMessage(ErrorCode.ERR_USER_NOT_FOUND.getMessage());
            }
    }

    @Override
    @Transactional
    public void resetPassword(String uuid, ResetPassRequest resetPassRequest) {
            LocalDateTime dateTimeNow = LocalDateTime.now();
            LocalTime timeNow = dateTimeNow.toLocalTime();
            UUIDEntity uuidEntity = uuidRepository.findUserByUuidStr(uuid);
            LocalDateTime dateTimeOld = uuidEntity.getCreatedAt();
            LocalTime timeOld = dateTimeOld.toLocalTime();
            if (timeNow.until(timeOld, ChronoUnit.HOURS) > 1) {
                throw new CustomException(HttpStatus.BAD_REQUEST)
                        .addError(ErrorCode.ERR_THE_LINK_TIME_OUT)
                        .withMessage(ErrorCode.ERR_THE_LINK_TIME_OUT.getMessage());
            } else{
                if(resetPassRequest.getNewPass().equals(resetPassRequest.getConfirmPass())) {
                    UserEntity user = uuidEntity.getUserEntity();
                    user.setPassword(passwordEncoder.encode(resetPassRequest.getNewPass()));
                    userRepository.save(user);
                    uuidEntity.setDeleted(true);
                }else {
                    throw new CustomException(HttpStatus.BAD_REQUEST)
                            .addError(ErrorCode.ERR_PASSWORD_IS_INCORRECT)
                            .withMessage(ErrorCode.ERR_PASSWORD_IS_INCORRECT.getMessage());
                }
            }

    }

    @Override
    public Optional<UserEntity> findUserByUsername(String email) {
        try {
            return userRepository.findByUsernameAndIsDeletedFalse(email);
        } catch (Exception e) {
            throw new CustomException(HttpStatus.BAD_REQUEST)
                    .addError(ErrorCode.ERR_SYSTEM_ERROR)
                    .withMessage(ErrorCode.ERR_SYSTEM_ERROR.getMessage());
        }

    }

    @Override
    @Transactional
    public void changePass(int userId, ChangePassRequest changePassRequest) {
            Optional<UserEntity> userOpt = userRepository.findByUserIdAndIsDeletedFalse(userId);
            if (userOpt.isPresent()) {
                UserEntity userEntity = userOpt.get();
                if (BCrypt.checkpw(changePassRequest.getOldPass(), userEntity.getPassword()) &&
                        changePassRequest.getNewPass().equals(changePassRequest.getConfirmPass())) {
                    userEntity.setPassword(passwordEncoder.encode(changePassRequest.getNewPass()).trim());
                    userRepository.save(userEntity);
                } else {
                    if (!BCrypt.checkpw(changePassRequest.getOldPass(), userEntity.getPassword())) {
                        throw new CustomException(HttpStatus.BAD_REQUEST)
                                .addError(ErrorCode.ERR_OLD_PASSWORD_IS_NOT_CORRECT)
                                .withMessage(ErrorCode.ERR_OLD_PASSWORD_IS_NOT_CORRECT.getMessage());
                    } else if (!changePassRequest.getNewPass().equals(changePassRequest.getConfirmPass())) {
                        throw new CustomException(HttpStatus.BAD_REQUEST)
                                .addError(ErrorCode.ERR_NEW_PASSWORD_DOES_NOT_MATCH)
                                .withMessage(ErrorCode.ERR_NEW_PASSWORD_DOES_NOT_MATCH.getMessage());
                    }
                }
            } else {
                throw new CustomException(HttpStatus.BAD_REQUEST)
                        .addError(ErrorCode.ERR_PASSWORD_NOT_FOUND)
                        .withMessage(ErrorCode.ERR_PASSWORD_NOT_FOUND.getMessage());
            }

    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
            UserEntity user = userRepository.findByUsername(username);
            if (user == null) {
                throw new UsernameNotFoundException(username);
            }

        return new User(
                user.getUsername(),
                user.getPassword(),
                AuthorityUtils.createAuthorityList(user.getRole()));
    }


}
