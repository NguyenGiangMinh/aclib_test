//package ThirdPartyService;
//
//import com.aclib.aclib_deploy.ThirdPartyService.EmailService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.Mockito;
//
//import static org.mockito.Mockito.when;
//
//public class EmailAsyncService {
//    private EmailService emailService;
//
//    private EmailAsyncService emailAsyncService;
//
//    @BeforeEach
//    public void setUp() {
//        emailService = Mockito.mock(EmailService.class);
//        emailAsyncService = new EmailAsyncService(emailService);
//    }
//
//    @Test
//    public void testSendEmailAsyncOverdue() {
//        when(emailService.sendOverdueNotification("toEmail", "userName", "bookTitle")).thenReturn();
//    }
//}
