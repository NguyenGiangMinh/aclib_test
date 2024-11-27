package com.aclib.aclib_deploy.ThirdPartyService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class EmailAsyncService {

    private final EmailService emailService;

    public EmailAsyncService(EmailService emailService) {
        this.emailService = emailService;
    }

    @Async
    public void sendEmailAsync(String email, String username, String otp, String registrationId, LocalDateTime expiredTime) {
        emailService.sendOTPNotifications(email, username, otp, registrationId, expiredTime);
    }
}