package ThirdPartyService;

import com.aclib.aclib_deploy.ThirdPartyService.EmailService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.lang.reflect.Field;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class EmailServiceTest {
    private void setMailSender(EmailService emailService, JavaMailSender mailSender) {
        try {
            Field field = EmailService.class.getDeclaredField("mailSender");
            field.setAccessible(true);
            field.set(emailService, mailSender);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }


    }

    @Test
    public void testSendOverdueNotification() {
        JavaMailSender mailSender = Mockito.mock(JavaMailSender.class);
        EmailService emailService = new EmailService();
        setMailSender(emailService, mailSender);

        String toEmail = "email@gmail.com";
        String userName = "user";
        String bookTitle = "book";

        emailService.sendOverdueNotification(toEmail, userName, bookTitle);

        ArgumentCaptor<SimpleMailMessage> argument = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(argument.capture());

        SimpleMailMessage message = argument.getValue();

        assertEquals(toEmail, message.getTo()[0]);
        assertEquals("Overdue Book Notification", message.getSubject());
        assertEquals("Hello " + userName + ",\n\nThe book '" + bookTitle + "' is overdue. "
                + "Please return it or renew your loan as soon as possible.", message.getText());
        assertEquals("mlsproject2110@gmail.com", message.getFrom());
    }

    @Test
    public void testSendAutoReturn() {
        JavaMailSender mailSender = Mockito.mock(JavaMailSender.class);
        EmailService emailService = new EmailService();
        setMailSender(emailService, mailSender);

        String toEmail = "test@gmail.com";
        String userName = "user";
        String bookTitle = "book";

        emailService.sendAutoReturn(toEmail, userName, bookTitle);

        ArgumentCaptor<SimpleMailMessage> argument = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(argument.capture());

        SimpleMailMessage message = argument.getValue();
        assertEquals(toEmail, message.getTo()[0]);
        assertEquals("Auto return Book Notification", message.getSubject());
        assertEquals("Hello " + userName + ", your book '" + bookTitle + "' has been automatically returned "
                + "because it was overdue for the second time.", message.getText());
        assertEquals("mlsproject2110@gmail.com", message.getFrom());
    }

    @Test
    public void testNotifyAdminOfLostBook() {
        JavaMailSender mailSender = Mockito.mock(JavaMailSender.class);
        EmailService emailService = new EmailService();
        setMailSender(emailService, mailSender);

        String toEmail = "test@gmail.com";
        String userName = "user";
        String bookTitle = "book";

        emailService.notifyAdminOfLostBook(toEmail, userName, bookTitle);
        ArgumentCaptor<SimpleMailMessage> argument = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(argument.capture());

        SimpleMailMessage message = argument.getValue();
        assertEquals(toEmail, message.getTo()[0]);
        assertEquals("Notification: Book Marked as Lost", message.getSubject());
        assertEquals("The book '" + bookTitle + "', borrowed by user " + userName
                + ", has been marked as lost and is no longer in stock.", message.getText());
        assertEquals("mlsproject2110@gmail.com", message.getFrom());
    }

    @Test
    public void testMakingLoanSuccessfully() {
        JavaMailSender mailSender = Mockito.mock(JavaMailSender.class);
        EmailService emailService = new EmailService();
        setMailSender(emailService, mailSender);

        String toEmail = "test@gmail.com";
        String userName = "user";
        String bookTitle = "book";
        LocalDateTime dueDate = LocalDateTime.now().plusDays(7);

        emailService.makingLoanSuccessfully(toEmail, userName, bookTitle, dueDate);
        ArgumentCaptor<SimpleMailMessage> argument = ArgumentCaptor.forClass(SimpleMailMessage.class);
        verify(mailSender, times(1)).send(argument.capture());

        SimpleMailMessage message = argument.getValue();
        assertEquals(toEmail, message.getTo()[0]);
        assertEquals("Notification: You successfully make an loan", message.getSubject());
        assertEquals("The book '" + bookTitle + "' was successfully borrowed by user " + userName
                + "\n" + "Your loans will be over due date at " + dueDate
                + "\n" + "Thanks for using our service."
                + "\n" + "Have a nice day", message.getText());
        assertEquals("mlsproject2110@gmail.com", message.getFrom());
    }

    @Test
    public void testSendOTPNotifications() {
        JavaMailSender mailSender = Mockito.mock(JavaMailSender.class);
        EmailService emailService = new EmailService();
        setMailSender(emailService, mailSender);

        String toEmail = "test@gmail.com";
        String userName = "user";
        String OTP = "1234";
        String registrationId = "123456";
        LocalDateTime expiredTime = LocalDateTime.parse("2023-01-01T12:00:00");

        ArgumentCaptor<SimpleMailMessage> argument = ArgumentCaptor.forClass(SimpleMailMessage.class);
        emailService.sendOTPNotifications(toEmail, userName, OTP, registrationId, expiredTime);
        verify(mailSender, times(1)).send(argument.capture());

        SimpleMailMessage message = argument.getValue();
        assertEquals(toEmail, message.getTo()[0]);
        assertEquals("Notification: Here is your OTP code and your registrationID", message.getSubject());
        assertEquals("Your otp code of " + userName + " is: " + OTP
                + "\n" + "Your registration Id is: " + registrationId
                + "\n" + "Please type as soon as you given your code"
                + "\n" + "Your code will active in " + expiredTime, message.getText());
        assertEquals("mlsproject2110@gmail.com", message.getFrom());

    }

    @Test
    public void testSendRoleUpdateNotifications() {
        JavaMailSender mailSender = Mockito.mock(JavaMailSender.class);
        EmailService emailService = new EmailService();
        setMailSender(emailService, mailSender);

        String toEmail = "test@gmail.com";
        String userName = "user";
        String role = "ROLE_ADMIN";

        ArgumentCaptor<SimpleMailMessage> argument = ArgumentCaptor.forClass(SimpleMailMessage.class);
        emailService.sendRoleUpdateNotifications(toEmail, userName, role);
        verify(mailSender, times(1)).send(argument.capture());

        SimpleMailMessage message = argument.getValue();
        assertEquals(toEmail, message.getTo()[0]);
        assertEquals("Notification: Role Updated", message.getSubject());
        assertEquals("Your role updated for " + userName + " is: " + role
                + "\n" + "\tYou are an administrator of my team at the moment"
                + "\n" + "\tHave a great day", message.getText());
        assertEquals("mlsproject2110@gmail.com", message.getFrom());
    }
}
