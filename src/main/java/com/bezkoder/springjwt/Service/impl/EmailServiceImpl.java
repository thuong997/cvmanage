package com.bezkoder.springjwt.Service.impl;

import com.bezkoder.springjwt.Service.EmailService;
import com.bezkoder.springjwt.Utils.ErrorCode;
import com.bezkoder.springjwt.models.UUIDEntity;
import com.bezkoder.springjwt.models.UserEntity;
import com.bezkoder.springjwt.payload.request.ForgotPasswordRequest;
import com.bezkoder.springjwt.repository.UUIDRepository;
import com.bezkoder.springjwt.repository.UserRepository;
import com.bezkoder.springjwt.security.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import javax.transaction.Transactional;
import java.util.Optional;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private HttpServletRequest request;

    @Autowired
    private UUIDRepository uuidRepository;

    @Override
    @Transactional
    public void sendForgotPassword(ForgotPasswordRequest email) {

        Optional<UserEntity> userOpt = userRepository.findByUsernameAndIsDeletedFalse(email.getEmail());//check email có tồn tại không
        if(userOpt.isPresent()) {
            if (userOpt.get().getUsername().equals(email.getEmail())) {
                UUID uuid = UUID.randomUUID();
                UUIDEntity uuidEntity = new UUIDEntity();
                uuidEntity.setUserEntity(userOpt.get());
                uuidEntity.setUuidStr(String.valueOf(uuid));
                uuidRepository.save(uuidEntity);
                String forgotPasswordUrl = "http://10.99.81.109:8080/HO102/resetPassword/" + uuid;

                String subject = "Thiết lập lại mật khẩu";

                String content = "Click vào link dưới đây để thiết lập lại mật khẩu (nếu không phải bạn xin vui lòng bỏ qua).\n"
                        + forgotPasswordUrl ;

                sendEmail(email.getEmail(), subject, content);
            } else {
                throw new CustomException(HttpStatus.BAD_REQUEST)
                        .addError(ErrorCode.ERR_EMAIL_NOT_EXISTS)
                        .withMessage(ErrorCode.ERR_EMAIL_NOT_EXISTS.getMessage());
            }
        }else {
            throw new CustomException(HttpStatus.BAD_REQUEST)
                    .addError(ErrorCode.ERR_USER_NOT_FOUND)
                    .withMessage(ErrorCode.ERR_USER_NOT_FOUND.getMessage());
        }
    }
    private void sendEmail(final String recipientEmail, final String subject, final String content) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(recipientEmail);
        message.setSubject(subject);
        message.setText(content);


        mailSender.send(message);
    }
}
//if(userOpt.isPresent()){
//            if(Constant.USER_NORMAL.equals(userOpt.get().getRole())){
//                throw new CustomException(HttpStatus.BAD_REQUEST)
//                        .addError(ErrorCode.ERR_NOT_ROLE)
//                        .withMessage(ErrorCode.ERR_NOT_ROLE.getMessage());
//            }
//        } else {
//            throw new CustomException(HttpStatus.BAD_REQUEST)
//                    .addError(ErrorCode.ERR_USER_NOT_FOUND)
//                    .withMessage(ErrorCode.ERR_USER_NOT_FOUND.getMessage());
//        }