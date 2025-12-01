package com.aclib.aclib_deploy.Controller;

import com.aclib.aclib_deploy.DTO.UserDTO;
import com.aclib.aclib_deploy.Entity.User;
import com.aclib.aclib_deploy.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private HttpSession session;

    @InjectMocks
    private UserController userController;

    private User testUser;
    private UserDTO testUserDTO;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        testUser.setPhone("1234567890");
        testUser.setRole(User.UserRole.ROLE_USER);
        // Set ID using reflection since there's no setter
        try {
            java.lang.reflect.Field idField = User.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(testUser, 1L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        testUserDTO = new UserDTO();
        testUserDTO.setUsername("testuser");
        testUserDTO.setEmail("test@example.com");
        testUserDTO.setPhone("1234567890");
        testUserDTO.setUserId(1L);
        testUserDTO.setRole(User.UserRole.ROLE_USER);
    }

    @Test
    void testGetProfile_Success() {
        // Arrange
        when(session.getAttribute("authUsername")).thenReturn("testuser");
        when(userService.findUser("testuser")).thenReturn(testUser);
        when(userService.getProfile(testUser.getId())).thenReturn(testUserDTO);

        // Act
        ResponseEntity<UserDTO> response = userController.getProfile(session);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("testuser", response.getBody().getUsername());
        assertEquals("test@example.com", response.getBody().getEmail());
        assertEquals("1234567890", response.getBody().getPhone());
        assertEquals(User.UserRole.ROLE_USER, response.getBody().getRole());
        verify(userService).findUser("testuser");
        verify(userService).getProfile(testUser.getId());
    }

    @Test
    void testGetProfile_UserNotAuthenticated() {
        // Arrange
        when(session.getAttribute("authUsername")).thenReturn("testuser");
        when(userService.findUser("testuser")).thenReturn(null);

        // Act
        ResponseEntity<UserDTO> response = userController.getProfile(session);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        assertNull(response.getBody());
    }

    @Test
    void testGetProfile_UserNotFound() {
        // Arrange
        when(session.getAttribute("authUsername")).thenReturn("nonexistent");
        when(userService.findUser("nonexistent"))
            .thenThrow(new RuntimeException("User not found"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            userController.getProfile(session);
        });
    }

    @Test
    void testGetProfile_AdminUser() {
        // Arrange
        testUser.setRole(User.UserRole.ROLE_ADMIN);
        testUserDTO.setRole(User.UserRole.ROLE_ADMIN);

        when(session.getAttribute("authUsername")).thenReturn("testuser");
        when(userService.findUser("testuser")).thenReturn(testUser);
        when(userService.getProfile(testUser.getId())).thenReturn(testUserDTO);

        // Act
        ResponseEntity<UserDTO> response = userController.getProfile(session);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(User.UserRole.ROLE_ADMIN, response.getBody().getRole());
    }

    @Test
    void testGetProfile_VerifyAllFields() {
        // Arrange
        testUserDTO.setUserId(123L);
        when(session.getAttribute("authUsername")).thenReturn("testuser");
        when(userService.findUser("testuser")).thenReturn(testUser);
        when(userService.getProfile(testUser.getId())).thenReturn(testUserDTO);

        // Act
        ResponseEntity<UserDTO> response = userController.getProfile(session);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        UserDTO result = response.getBody();
        assertNotNull(result);
        assertEquals(123L, result.getUserId());
        assertEquals("testuser", result.getUsername());
        assertEquals("test@example.com", result.getEmail());
        assertEquals("1234567890", result.getPhone());
    }

    @Test
    void testGetProfile_MultipleCallsSameUser() {
        // Arrange
        when(session.getAttribute("authUsername")).thenReturn("testuser");
        when(userService.findUser("testuser")).thenReturn(testUser);
        when(userService.getProfile(testUser.getId())).thenReturn(testUserDTO);

        // Act
        ResponseEntity<UserDTO> response1 = userController.getProfile(session);
        ResponseEntity<UserDTO> response2 = userController.getProfile(session);

        // Assert
        assertEquals(HttpStatus.OK, response1.getStatusCode());
        assertEquals(HttpStatus.OK, response2.getStatusCode());
        assertEquals(response1.getBody().getUsername(), response2.getBody().getUsername());
        verify(userService, times(2)).findUser("testuser");
        verify(userService, times(2)).getProfile(testUser.getId());
    }

    @Test
    void testGetProfile_ServiceThrowsException() {
        // Arrange
        when(session.getAttribute("authUsername")).thenReturn("testuser");
        when(userService.findUser("testuser")).thenReturn(testUser);
        when(userService.getProfile(testUser.getId()))
            .thenThrow(new RuntimeException("Database connection error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
            userController.getProfile(session));
    }

    @Test
    void testGetProfile_InvalidUserId() {
        // Arrange
        try {
            java.lang.reflect.Field idField = User.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(testUser, -1L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        when(session.getAttribute("authUsername")).thenReturn("testuser");
        when(userService.findUser("testuser")).thenReturn(testUser);
        when(userService.getProfile(-1L))
            .thenThrow(new IllegalArgumentException("Invalid user ID"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
            userController.getProfile(session));
    }

    @Test
    void testGetProfile_NullSession() {
        // Arrange - null session attribute will cause NullPointerException
        when(session.getAttribute("authUsername")).thenReturn(null);

        // Act & Assert
        assertThrows(NullPointerException.class, () ->
            userController.getProfile(session));
    }

    @Test
    void testGetProfile_EmptyUsername() {
        // Arrange
        when(session.getAttribute("authUsername")).thenReturn("");
        when(userService.findUser(""))
            .thenThrow(new IllegalArgumentException("Username cannot be empty"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
            userController.getProfile(session));
    }

    @Test
    void testGetProfile_MultipleUsers_DifferentRoles() {
        // Arrange - Test with both USER and ADMIN roles
        User adminUser = new User();
        adminUser.setUsername("adminuser");
        adminUser.setRole(User.UserRole.ROLE_ADMIN);
        try {
            java.lang.reflect.Field idField = User.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(adminUser, 2L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        UserDTO adminDTO = new UserDTO();
        adminDTO.setUsername("adminuser");
        adminDTO.setUserId(2L);
        adminDTO.setRole(User.UserRole.ROLE_ADMIN);

        when(session.getAttribute("authUsername")).thenReturn("adminuser");
        when(userService.findUser("adminuser")).thenReturn(adminUser);
        when(userService.getProfile(2L)).thenReturn(adminDTO);

        // Act
        ResponseEntity<UserDTO> response = userController.getProfile(session);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(User.UserRole.ROLE_ADMIN, response.getBody().getRole());
    }

    @Test
    void testGetProfile_UserServiceReturnsNull() {
        // Arrange
        when(session.getAttribute("authUsername")).thenReturn("testuser");
        when(userService.findUser("testuser")).thenReturn(testUser);
        when(userService.getProfile(testUser.getId())).thenReturn(null);

        // Act
        ResponseEntity<UserDTO> response = userController.getProfile(session);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
    }
}
