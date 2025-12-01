package com.aclib.aclib_deploy.Controller;

import com.aclib.aclib_deploy.DTO.UserDTO;
import com.aclib.aclib_deploy.Entity.User;
import com.aclib.aclib_deploy.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoginControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private HttpServletRequest request;

    @Mock
    private HttpSession session;

    @Mock
    private Authentication authentication;

    private LoginController loginController;

    private User testUser;
    private UserDTO testUserDTO;

    @BeforeEach
    void setUp() {
        loginController = new LoginController(authenticationManager);
        // Use reflection to set private field
        try {
            java.lang.reflect.Field field = LoginController.class.getDeclaredField("userService");
            field.setAccessible(true);
            field.set(loginController, userService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setPassword("password123");
        testUser.setEmail("test@example.com");
        testUser.setPhone("1234567890");
        testUser.setRole(User.UserRole.ROLE_USER);
        testUser.setActive(true);

        testUserDTO = new UserDTO();
        testUserDTO.setUsername("testuser");
        testUserDTO.setEmail("test@example.com");
        testUserDTO.setRole(User.UserRole.ROLE_USER);
    }

    @Test
    void testLoginSuccess() {
        // Arrange
        LoginController.LoginRequest loginRequest = new LoginController.LoginRequest("testuser", "password123");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(authentication);
        when(authentication.getName()).thenReturn("testuser");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("authUsername")).thenReturn("testuser");
        when(userService.findUser("testuser")).thenReturn(testUser);
        when(userService.mapToUserDTO(testUser)).thenReturn(testUserDTO);

        // Act
        ResponseEntity<UserDTO> response = loginController.login(loginRequest, request, session);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("testuser", response.getBody().getUsername());
        verify(session).setAttribute("authUsername", "testuser");
        verify(userService).findUser("testuser");
        verify(userService).mapToUserDTO(testUser);
    }

    @Test
    void testLoginFailure_InvalidCredentials() {
        // Arrange
        LoginController.LoginRequest loginRequest = new LoginController.LoginRequest("testuser", "wrongpassword");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Invalid credentials") {});

        // Act
        ResponseEntity<UserDTO> response = loginController.login(loginRequest, request, session);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNotNull(response.getBody());
        // UserDTO constructor with String stores the message internally, so we can't directly check username
        // Just verify that response body is not null and status is UNAUTHORIZED
    }

    @Test
    void testLoginFailure_UserNotFound() {
        // Arrange
        LoginController.LoginRequest loginRequest = new LoginController.LoginRequest("nonexistent", "password123");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("User not found") {});

        // Act
        ResponseEntity<UserDTO> response = loginController.login(loginRequest, request, session);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testGetAuthenticatedUser_Success() {
        // Arrange
        when(session.getAttribute("authUsername")).thenReturn("testuser");
        when(userService.findUser("testuser")).thenReturn(testUser);
        when(userService.mapToUserDTO(testUser)).thenReturn(testUserDTO);

        // Act
        ResponseEntity<?> response = loginController.getAuthenticatedUser(session);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void testGetAuthenticatedUser_NotAuthenticated() {
        // Arrange
        when(session.getAttribute("authUsername")).thenReturn(null);

        // Act
        ResponseEntity<?> response = loginController.getAuthenticatedUser(session);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testLogin_Failure_EmptyUsername() {
        // Arrange
        LoginController.LoginRequest loginRequest = new LoginController.LoginRequest("", "password123");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Username cannot be empty") {});

        // Act
        ResponseEntity<UserDTO> response = loginController.login(loginRequest, request, session);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testLogin_Failure_EmptyPassword() {
        // Arrange
        LoginController.LoginRequest loginRequest = new LoginController.LoginRequest("testuser", "");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Password cannot be empty") {});

        // Act
        ResponseEntity<UserDTO> response = loginController.login(loginRequest, request, session);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testLogin_Failure_NullCredentials() {
        // Arrange
        LoginController.LoginRequest loginRequest = new LoginController.LoginRequest(null, null);

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Credentials cannot be null") {});

        // Act
        ResponseEntity<UserDTO> response = loginController.login(loginRequest, request, session);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testLogin_Failure_AccountDisabled() {
        // Arrange
        LoginController.LoginRequest loginRequest = new LoginController.LoginRequest("testuser", "password123");

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new AuthenticationException("Account is disabled") {});

        // Act
        ResponseEntity<UserDTO> response = loginController.login(loginRequest, request, session);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testGetAuthenticatedUser_Failure_UserServiceThrowsException() {
        // Arrange
        when(session.getAttribute("authUsername")).thenReturn("testuser");
        when(userService.findUser("testuser"))
            .thenThrow(new RuntimeException("Database connection error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
            loginController.getAuthenticatedUser(session));
    }
}
