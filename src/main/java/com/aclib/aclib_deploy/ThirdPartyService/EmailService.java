package com.aclib.aclib_deploy.ThirdPartyService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class EmailService {
    @Autowired
    private JavaMailSender mailSender;

    public void sendOverdueNotification(String toEmail, String userName, String bookTitle) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("Overdue Book Notification");
        message.setText("Hello " + userName + ",\n\nThe book '" + bookTitle + "' is overdue. "
                + "Please return it or renew your loan as soon as possible.");
        message.setFrom("mlsproject2110@gmail.com");
        mailSender.send(message);
    }

    public void sendAutoReturn(String toEmail, String userName, String bookTitle) {
        SimpleMailMessage message = new SimpleMailMessage();

        message.setTo(toEmail);
        message.setSubject("Auto return Book Notification");
        String text = String.format("Hello %s, your book '%s' has been automatically returned "
                + "because it was overdue for the second time.", userName, bookTitle);
        message.setText(text);
        message.setFrom("mlsproject2110@gmail.com");

        mailSender.send(message);
    }

    public void notifyAdminOfLostBook(String toEmail, String userName, String bookTitle) {
        String subject = "Notification: Book Marked as Lost";
        String message = "The book '" + bookTitle + "', borrowed by user " + userName
                + ", has been marked as lost and is no longer in stock.";

        SimpleMailMessage message1 = new SimpleMailMessage();
        message1.setTo(toEmail);
        message1.setSubject(subject);
        message1.setText(message);
        message1.setFrom("mlsproject2110@gmail.com");
        mailSender.send(message1);
    }

    public void makingLoanSuccessfully (String toEmail, String userName, String bookTitle, LocalDateTime dueDate) {
        SimpleMailMessage message = new SimpleMailMessage();

        String subjects1 = "Notification: You successfully make an loan";
        String message1 = "The book '" + bookTitle + "' was successfully borrowed by user " + userName
                + "\n" + "Your loans will be over due date at " + dueDate
                + "\n" + "Thanks for using our service."
                + "\n" + "Have a nice day";

        message.setTo(toEmail);
        message.setSubject(subjects1);
        message.setText(message1);
        message.setFrom("mlsproject2110@gmail.com");
        mailSender.send(message);
    }

    public void sendOTPNotifications(String toEmail, String userName,
                                     String OTP, String registrationId, LocalDateTime expiredTime) {
        SimpleMailMessage message = new SimpleMailMessage();

        String subjects2 = "Notification: Here is your OTP code and your registrationID";
        String message2 = "Your otp code of " + userName + " is: " + OTP
                + "\n" + "Your registration Id is: " + registrationId
                + "\n" + "Please type as soon as you given your code"
                + "\n" + "Your code will active in " + expiredTime;

        message.setTo(toEmail);
        message.setSubject(subjects2);
        message.setText(message2);
        message.setFrom("mlsproject2110@gmail.com");
        mailSender.send(message);
    }

    public void sendRoleUpdateNotifications(String toEmail, String userName, String role) {
        SimpleMailMessage message = new SimpleMailMessage();

        String subjects3 = "Notification: Role Updated";
        String message3 = "Your role updated for " + userName + " is: " + role
                + "\n" + "\tYou are an administrator of my team at the moment"
                + "\n" + "\tHave a great day";

        message.setTo(toEmail);
        message.setSubject(subjects3);
        message.setText(message3);
        message.setFrom("mlsproject2110@gmail.com");
        mailSender.send(message);
    }
}
