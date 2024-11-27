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
    public void sendEmailAsyncOverdue(String toEmail, String userName, String bookTitle) {
        emailService.sendOverdueNotification(toEmail, userName, bookTitle);
    }

    @Async
    public void sendEmailAsyncAutoReturn(String toEmail, String userName, String bookTitle) {
        emailService.sendAutoReturn(toEmail, userName, bookTitle);
    }

    @Async
    public void setEmailAsyncNotifyAdminOfLostBook(String toEmail, String userName, String bookTitle) {
        emailService.notifyAdminOfLostBook(toEmail, userName, bookTitle);
    }

    @Async
    public void sendEmailAsyncMakingLoanSuccessfully(String toEmail, String userName, String bookTitle, LocalDateTime dueDate) {
        emailService.makingLoanSuccessfully(toEmail, userName, bookTitle, dueDate);
    }

    @Async
    public void sendEmailAsyncSendOTPSuccessfully(String toEmail, String userName,
                                                  String OTP, String registrationId, LocalDateTime expiredTime) {
        emailService.sendOTPNotifications(toEmail, userName, OTP, registrationId, expiredTime);
    }

    @Async
    public void sendEmailAsyncSendRoleUpdateNotifications(String toEmail, String userName, String role) {
        emailService.sendRoleUpdateNotifications(toEmail, userName, role);
    }

    @Async
    public void sendEmailAsyncReturnSuccessfully(String toEmail, String userName, String bookTitle) {
        emailService.returnSuccessfully(toEmail, userName, bookTitle);
    }
}