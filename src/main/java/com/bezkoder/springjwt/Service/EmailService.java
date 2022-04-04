package com.bezkoder.springjwt.Service;

import com.bezkoder.springjwt.payload.request.ForgotPasswordRequest;

public interface EmailService   {

    void sendForgotPassword(ForgotPasswordRequest email);
}
