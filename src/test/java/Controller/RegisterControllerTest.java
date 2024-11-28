package Controller;

import com.aclib.aclib_deploy.Controller.RegisterController;
import com.aclib.aclib_deploy.DTO.RegistrationResponse;
import com.aclib.aclib_deploy.Entity.User;
import com.aclib.aclib_deploy.Service.UserService;
import com.aclib.aclib_deploy.ThirdPartyService.JsonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class RegisterControllerTest {
    private RegisterController registerController;

    private UserService userService;

    private JsonService jsonService;

    @BeforeEach
    public void setUp() {
        userService = mock(UserService.class);
        jsonService = mock(JsonService.class);
        registerController = new RegisterController(userService, jsonService);
    }

    @Test
    public void testRegister_Success() {
        RegisterController.RegisterRequest registerRequest = new RegisterController.RegisterRequest("username", "password", "email", "phone", "user", null);
        User newUser = new User();
        newUser.setRegistrationId("a123");

        when(userService.registerNewUser(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setRegistrationId("a123");
            return user;
        });

        ResponseEntity<RegistrationResponse> response = registerController.register(registerRequest);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals("a123", response.getBody().getRegistrationId());
        assertEquals("please check the OTP sent to your email and verify!", response.getBody().getNotifications());
    }

    @Test
    public void testVerifingOTP_Success() {
        RegisterController.VerifyOtpRequest verifyOtpRequest = new RegisterController.VerifyOtpRequest("a123", "123456");
        when(userService.verifyOtp("123456", "a123")).thenReturn(true);

        ResponseEntity<String> response = registerController.verifingOTP(verifyOtpRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Registration complete! Your account is now active.Please login again and enjoy the service", response.getBody());
    }

    @Test
    public void testVerifingOTP_Fail() {
        RegisterController.VerifyOtpRequest verifyOtpRequest = new RegisterController.VerifyOtpRequest("a123", "123456");
        when(userService.verifyOtp("123456", "a123")).thenReturn(false);

        ResponseEntity<String> response = registerController.verifingOTP(verifyOtpRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid or expired OTP."
                + "\nYou have to wait five minutes to get a new OTP code", response.getBody());
    }

    @Test
    public void testResentingOTP_Success() {
        RegisterController.ResentingRequest resendOtpRequest = new RegisterController.ResentingRequest("a123");
        when(userService.canRequestNewOtp("a123")).thenReturn(true);

        ResponseEntity<String> response = registerController.resentingOTP(resendOtpRequest);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("We sent OTP code to your email. Please check", response.getBody());
    }

    @Test
    public void testResentingOTP_Fail() {
        RegisterController.ResentingRequest resendOtpRequest = new RegisterController.ResentingRequest("a123");
        when(userService.canRequestNewOtp("a123")).thenReturn(false);

        ResponseEntity<String> response = registerController.resentingOTP(resendOtpRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("We will send the OTP code after 5 minutes."
                + "\nPlease wait.", response.getBody());
    }
}
