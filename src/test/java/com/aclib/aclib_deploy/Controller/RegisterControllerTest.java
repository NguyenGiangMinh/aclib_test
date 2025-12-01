package com.aclib.aclib_deploy.Controller;

import com.aclib.aclib_deploy.DTO.RegistrationResponse;
import com.aclib.aclib_deploy.Entity.User;
import com.aclib.aclib_deploy.Service.UserService;
import com.aclib.aclib_deploy.ThirdPartyService.JsonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RegisterControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JsonService jsonService;

    @InjectMocks
    private RegisterController registerController;

    private User testUser;
    private String registrationId;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("newuser");
        testUser.setPassword("password123");
        testUser.setEmail("newuser@example.com");
        testUser.setPhone("1234567890");
        testUser.setRole(User.UserRole.ROLE_USER);

        registrationId = UUID.randomUUID().toString();
        testUser.setRegistrationId(registrationId);
    }

    @Test
    void testRegister_Success_AsUser() {
        // Arrange
        RegisterController.RegisterRequest registerRequest =
            new RegisterController.RegisterRequest("newuser", "password123",
                "newuser@example.com", "1234567890", null, null);

        when(userService.registerNewUser(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setRegistrationId(registrationId);
            return user;
        });

        // Act
        ResponseEntity<RegistrationResponse> response = registerController.register(registerRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getRegistrationId());
        assertTrue(response.getBody().getNotifications().contains("OTP"));
        verify(userService).registerNewUser(any(User.class));
        verify(jsonService).saveRegistrationIdToJson(anyString(), anyString());
    }

    @Test
    void testRegister_Success_AsAdmin_WithValidCode() {
        // Arrange
        RegisterController.RegisterRequest registerRequest =
            new RegisterController.RegisterRequest("adminuser", "password123",
                "admin@example.com", "1234567890", "admin", "STAFF_SECRET_CODE");

        when(userService.registerNewUser(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setRegistrationId(registrationId);
            return user;
        });

        // Act
        ResponseEntity<RegistrationResponse> response = registerController.register(registerRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(userService).registerNewUser(any(User.class));
    }

    @Test
    void testRegister_Failure_AdminWithInvalidCode() {
        // Arrange
        RegisterController.RegisterRequest registerRequest =
            new RegisterController.RegisterRequest("adminuser", "password123",
                "admin@example.com", "1234567890", "admin", "WRONG_CODE");

        // Act
        ResponseEntity<RegistrationResponse> response = registerController.register(registerRequest);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getNotifications().contains("Invalid admin code"));
        verify(userService, never()).registerNewUser(any(User.class));
    }

    @Test
    void testRegister_Failure_UsernameAlreadyTaken() {
        // Arrange
        RegisterController.RegisterRequest registerRequest =
            new RegisterController.RegisterRequest("existinguser", "password123",
                "new@example.com", "1234567890", null, null);

        when(userService.registerNewUser(any(User.class)))
            .thenThrow(new RuntimeException("Username already taken"));

        // Act
        ResponseEntity<RegistrationResponse> response = registerController.register(registerRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getNotifications().contains("Registration failed"));
    }

    @Test
    void testRegister_Failure_EmailAlreadyUsed() {
        // Arrange
        RegisterController.RegisterRequest registerRequest =
            new RegisterController.RegisterRequest("newuser", "password123",
                "existing@example.com", "1234567890", null, null);

        when(userService.registerNewUser(any(User.class)))
            .thenThrow(new RuntimeException("Email already used"));

        // Act
        ResponseEntity<RegistrationResponse> response = registerController.register(registerRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getNotifications().contains("Email already used"));
    }

    @Test
    void testRegister_Failure_InvalidRole() {
        // Arrange
        RegisterController.RegisterRequest registerRequest =
            new RegisterController.RegisterRequest("newuser", "password123",
                "new@example.com", "1234567890", "INVALID_ROLE", null);

        // Act
        ResponseEntity<RegistrationResponse> response = registerController.register(registerRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().getNotifications().contains("Invalid role type"));
    }

    @Test
    void testVerifyOtp_Success() {
        // Arrange
        RegisterController.VerifyOtpRequest verifyRequest =
                new RegisterController.VerifyOtpRequest(registrationId, "12345");

        // Fix: OTP code is first parameter, registrationId is second
        when(userService.verifyOtp("12345", registrationId)).thenReturn(true);

        // Act
        ResponseEntity<String> response = registerController.verifingOTP(verifyRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Registration complete"));
    }

    @Test
    void testVerifyOtp_Failure_InvalidOtp() {
        // Arrange
        RegisterController.VerifyOtpRequest verifyRequest =
                new RegisterController.VerifyOtpRequest(registrationId, "99999");

        // Fix: OTP code is first parameter, registrationId is second
        when(userService.verifyOtp("99999", registrationId)).thenReturn(false);

        // Act
        ResponseEntity<String> response = registerController.verifingOTP(verifyRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Invalid or expired OTP"));
    }

    @Test
    void testResentOtp_Success() {
        // Arrange
        RegisterController.ResentingRequest resentRequest =
            new RegisterController.ResentingRequest(registrationId);

        when(userService.canRequestNewOtp(registrationId)).thenReturn(true);
        when(userService.findByRegistrationId(registrationId)).thenReturn(testUser);

        // Act
        ResponseEntity<String> response = registerController.resentingOTP(resentRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("We sent OTP code"));
        verify(userService).sendOTP(testUser);
    }

    @Test
    void testResentOtp_Failure_TooSoon() {
        // Arrange
        RegisterController.ResentingRequest resentRequest =
            new RegisterController.ResentingRequest(registrationId);

        when(userService.canRequestNewOtp(registrationId)).thenReturn(false);

        // Act
        ResponseEntity<String> response = registerController.resentingOTP(resentRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNotNull(response.getBody());
        assertTrue(response.getBody().contains("Please wait"));
        verify(userService, never()).sendOTP(any());
    }

    @Test
    void testRegister_Failure_NullUsername() {
        // Arrange
        RegisterController.RegisterRequest registerRequest =
            new RegisterController.RegisterRequest(null, "password123",
                "new@example.com", "1234567890", null, null);

        when(userService.registerNewUser(any(User.class)))
            .thenThrow(new RuntimeException("Username cannot be null"));

        // Act
        ResponseEntity<RegistrationResponse> response = registerController.register(registerRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getNotifications().contains("Registration failed"));
    }

    @Test
    void testRegister_Failure_EmptyPassword() {
        // Arrange
        RegisterController.RegisterRequest registerRequest =
            new RegisterController.RegisterRequest("newuser", "",
                "new@example.com", "1234567890", null, null);

        when(userService.registerNewUser(any(User.class)))
            .thenThrow(new RuntimeException("Password cannot be empty"));

        // Act
        ResponseEntity<RegistrationResponse> response = registerController.register(registerRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
    }

    @Test
    void testRegister_Failure_InvalidEmailFormat() {
        // Arrange
        RegisterController.RegisterRequest registerRequest =
            new RegisterController.RegisterRequest("newuser", "password123",
                "invalid-email", "1234567890", null, null);

        when(userService.registerNewUser(any(User.class)))
            .thenThrow(new RuntimeException("Invalid email format"));

        // Act
        ResponseEntity<RegistrationResponse> response = registerController.register(registerRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().getNotifications().contains("Invalid email format"));
    }

    @Test
    void testVerifyOtp_Failure_ExpiredOtp() {
        // Arrange
        RegisterController.VerifyOtpRequest verifyRequest =
                new RegisterController.VerifyOtpRequest(registrationId, "12345");

        when(userService.verifyOtp("12345", registrationId)).thenReturn(false);

        // Act
        ResponseEntity<String> response = registerController.verifingOTP(verifyRequest);

        // Assert
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody().contains("Invalid or expired OTP"));
    }

    @Test
    void testVerifyOtp_Failure_InvalidRegistrationId() {
        // Arrange
        String invalidRegId = "invalid-id";
        RegisterController.VerifyOtpRequest verifyRequest =
                new RegisterController.VerifyOtpRequest(invalidRegId, "12345");

        when(userService.verifyOtp("12345", invalidRegId))
            .thenThrow(new RuntimeException("User not found"));

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
            registerController.verifingOTP(verifyRequest));
    }

    @Test
    void testResentOtp_Failure_UserNotFound() {
        // Arrange
        String invalidRegId = "invalid-id";
        RegisterController.ResentingRequest resentRequest =
            new RegisterController.ResentingRequest(invalidRegId);

        when(userService.canRequestNewOtp(invalidRegId)).thenReturn(true);
        when(userService.findByRegistrationId(invalidRegId)).thenReturn(null);

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
            registerController.resentingOTP(resentRequest));
    }

    @Test
    void testRegister_Failure_AdminWithNullCode() {
        // Arrange
        RegisterController.RegisterRequest registerRequest =
            new RegisterController.RegisterRequest("adminuser", "password123",
                "admin@example.com", "1234567890", "admin", null);

        // Act
        ResponseEntity<RegistrationResponse> response = registerController.register(registerRequest);

        // Assert
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
        assertTrue(response.getBody().getNotifications().contains("Invalid admin code"));
    }
}
