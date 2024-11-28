package ThirdPartyService;

import com.aclib.aclib_deploy.ThirdPartyService.EmailAsyncService;
import com.aclib.aclib_deploy.ThirdPartyService.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

public class EmailAsyncServiceTest {

    private EmailService emailService;

    private EmailAsyncService emailAsyncService;

    @BeforeEach
    public void setUp() {
        emailService = Mockito.mock(EmailService.class);
        emailAsyncService = new EmailAsyncService(emailService);
    }

    @Test
    public void testSendEmailAsyncOverdue() {
        doNothing().when(emailService).sendOverdueNotification(anyString(), anyString(), anyString());

        emailAsyncService.sendEmailAsyncOverdue("toEmail", "userName", "bookTitle");

        verify(emailService, times(1)).sendOverdueNotification("toEmail", "userName", "bookTitle");
    }

    @Test
    public void testSendEmailAsyncAutoReturn() {
        doNothing().when(emailService).sendAutoReturn(anyString(), anyString(), anyString());

        emailAsyncService.sendEmailAsyncAutoReturn("toEmail", "userName", "bookTitle");

        verify(emailService, times(1)).sendAutoReturn("toEmail", "userName", "bookTitle");
    }

    @Test
    public void testSetEmailAsyncNotifyAdminOfLostBook() {
        doNothing().when(emailService).notifyAdminOfLostBook(anyString(), anyString(), anyString());

        emailAsyncService.setEmailAsyncNotifyAdminOfLostBook("toEmail", "userName", "bookTitle");

        verify(emailService, times(1)).notifyAdminOfLostBook("toEmail", "userName", "bookTitle");
    }

    @Test
    public void testSendEmailAsyncMakingLoanSuccessfully() {
        doNothing().when(emailService).makingLoanSuccessfully(anyString(), anyString(), anyString(), any());

        emailAsyncService.sendEmailAsyncMakingLoanSuccessfully("toEmail", "userName", "bookTitle", null);

        verify(emailService, times(1)).makingLoanSuccessfully("toEmail", "userName", "bookTitle", null);
    }

    @Test
    public void testSendEmailAsyncSendOTPSuccessfully() {
        doNothing().when(emailService).sendOTPNotifications(anyString(), anyString(), anyString(), anyString(), any());

        emailAsyncService.sendEmailAsyncSendOTPSuccessfully("toEmail", "userName", "OTP", "registrationId", null);

        verify(emailService, times(1)).sendOTPNotifications("toEmail", "userName", "OTP", "registrationId", null);
    }


    @Test
    public void testSendEmailAsyncSendRoleUpdateNotifications() {
        doNothing().when(emailService).sendRoleUpdateNotifications(anyString(), anyString(), anyString());

        emailAsyncService.sendEmailAsyncSendRoleUpdateNotifications("toEmail", "userName", "role");

        verify(emailService, times(1)).sendRoleUpdateNotifications("toEmail", "userName", "role");
    }

    @Test
    public void testSendEmailAsyncReturnSuccessfully() {
        doNothing().when(emailService).returnSuccessfully(anyString(), anyString(), anyString());

        emailAsyncService.sendEmailAsyncReturnSuccessfully("toEmail", "userName", "bookTitle");

        verify(emailService, times(1)).returnSuccessfully("toEmail", "userName", "bookTitle");
    }

}
