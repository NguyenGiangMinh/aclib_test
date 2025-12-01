package com.aclib.aclib_deploy.Controller;

import com.aclib.aclib_deploy.DTO.UserDTO;
import com.aclib.aclib_deploy.Service.AdminService;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AdminControllerTest {

    @Mock
    private AdminService adminService;

    @Mock
    private HttpSession session;

    @InjectMocks
    private AdminController adminController;

    private UserDTO testAdminDTO;

    @BeforeEach
    void setUp() {
        testAdminDTO = new UserDTO();
        testAdminDTO.setUsername("newadmin");
        testAdminDTO.setUserId(1L);
    }

    @Test
    void testDeleteBookFromStock_Success() {
        // Arrange
        long bookId = 1L;
        when(session.getAttribute("authUsername")).thenReturn("admin");
        doNothing().when(adminService).deleteBookInStock(bookId);

        // Act
        ResponseEntity<Void> response = adminController.deleteBookFromStock(bookId, session);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(adminService).deleteBookInStock(bookId);
    }

    @Test
    void testDeleteBookFromStock_BookNotFound() {
        // Arrange
        long bookId = 999L;
        when(session.getAttribute("authUsername")).thenReturn("admin");
        doThrow(new RuntimeException("Book not found")).when(adminService).deleteBookInStock(bookId);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            adminController.deleteBookFromStock(bookId, session);
        });
    }

    @Test
    void testUpdateBookIntoStock_Success() {
        // Arrange
        AdminController.updateBook updateBook = new AdminController.updateBook("book123", 10);
        when(session.getAttribute("authUsername")).thenReturn("admin");
        doNothing().when(adminService).updateBookCopy("book123", 10);

        // Act
        ResponseEntity<String> response = adminController.updateBookIntoStock(updateBook, session);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(adminService).updateBookCopy("book123", 10);
    }

    @Test
    void testUpdateBookIntoStock_WithZeroCopies() {
        // Arrange
        AdminController.updateBook updateBook = new AdminController.updateBook("book123", 0);
        when(session.getAttribute("authUsername")).thenReturn("admin");
        doNothing().when(adminService).updateBookCopy("book123", 0);

        // Act
        ResponseEntity<String> response = adminController.updateBookIntoStock(updateBook, session);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(adminService).updateBookCopy("book123", 0);
    }

    @Test
    void testUpdateBookIntoStock_WithNegativeCopies() {
        // Arrange
        AdminController.updateBook updateBook = new AdminController.updateBook("book123", -5);
        when(session.getAttribute("authUsername")).thenReturn("admin");
        doThrow(new IllegalArgumentException("Copies cannot be negative"))
            .when(adminService).updateBookCopy("book123", -5);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            adminController.updateBookIntoStock(updateBook, session);
        });
    }

    @Test
    void testAddBookCopies_Success() {
        // Arrange
        String category = "Fiction";
        doNothing().when(adminService).addBookCopyPart2(category);

        // Act
        ResponseEntity<Void> response = adminController.addBookCopies(category);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(adminService).addBookCopyPart2(category);
    }

    @Test
    void testAddBookCopies_MultipleCategories() {
        // Arrange
        String[] categories = {"Fiction", "Science", "History"};

        for (String category : categories) {
            doNothing().when(adminService).addBookCopyPart2(category);

            // Act
            ResponseEntity<Void> response = adminController.addBookCopies(category);

            // Assert
            assertEquals(HttpStatus.OK, response.getStatusCode());
        }

        verify(adminService, times(3)).addBookCopyPart2(anyString());
    }

    @Test
    void testAddBookCopies_EmptyCategory() {
        // Arrange
        String category = "";
        doNothing().when(adminService).addBookCopyPart2(category);

        // Act
        ResponseEntity<Void> response = adminController.addBookCopies(category);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(adminService).addBookCopyPart2(category);
    }

    @Test
    void testUpdatingNewAdmin_Success() {
        // Arrange
        AdminController.updateNewAdmin updateNewAdmin = new AdminController.updateNewAdmin(1L);
        when(session.getAttribute("authUsername")).thenReturn("admin");
        when(adminService.updateAdminRole(1L)).thenReturn(testAdminDTO);

        // Act
        ResponseEntity<UserDTO> response = adminController.updatingNewAdmin(updateNewAdmin, session);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("newadmin", response.getBody().getUsername());
        assertEquals(1L, response.getBody().getUserId());
        verify(adminService).updateAdminRole(1L);
    }

    @Test
    void testUpdatingNewAdmin_UserNotFound() {
        // Arrange
        AdminController.updateNewAdmin updateNewAdmin = new AdminController.updateNewAdmin(999L);
        when(session.getAttribute("authUsername")).thenReturn("admin");
        when(adminService.updateAdminRole(999L))
            .thenThrow(new RuntimeException("User not found"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            adminController.updatingNewAdmin(updateNewAdmin, session);
        });
    }

    @Test
    void testUpdatingNewAdmin_AlreadyAdmin() {
        // Arrange
        AdminController.updateNewAdmin updateNewAdmin = new AdminController.updateNewAdmin(2L);
        when(session.getAttribute("authUsername")).thenReturn("admin");
        when(adminService.updateAdminRole(2L))
            .thenThrow(new RuntimeException("User is already an admin"));

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            adminController.updatingNewAdmin(updateNewAdmin, session);
        });
    }

    @Test
    void testDeleteBookFromStock_WithActiveLoan() {
        // Arrange
        long bookId = 1L;
        when(session.getAttribute("authUsername")).thenReturn("admin");
        doThrow(new IllegalStateException("Cannot delete book with active loans"))
            .when(adminService).deleteBookInStock(bookId);

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> {
            adminController.deleteBookFromStock(bookId, session);
        });
    }

    @Test
    void testUpdateBookIntoStock_BookNotFound() {
        // Arrange
        AdminController.updateBook updateBook = new AdminController.updateBook("nonexistent", 10);
        when(session.getAttribute("authUsername")).thenReturn("admin");
        doThrow(new RuntimeException("Book not found"))
            .when(adminService).updateBookCopy("nonexistent", 10);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            adminController.updateBookIntoStock(updateBook, session);
        });
    }

    @Test
    void testUpdateBookIntoStock_InvalidCopyCount() {
        // Arrange
        AdminController.updateBook updateBook = new AdminController.updateBook("book123", -10);
        when(session.getAttribute("authUsername")).thenReturn("admin");
        doThrow(new IllegalArgumentException("Copy count must be non-negative"))
            .when(adminService).updateBookCopy("book123", -10);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            adminController.updateBookIntoStock(updateBook, session);
        });
    }

    @Test
    void testAddBookCopies_InvalidCategory() {
        // Arrange
        String category = "NonExistentCategory";
        doThrow(new RuntimeException("Category not found"))
            .when(adminService).addBookCopyPart2(category);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            adminController.addBookCopies(category);
        });
    }

    @Test
    void testAddBookCopies_GoogleAPIError() {
        // Arrange
        String category = "Fiction";
        doThrow(new RuntimeException("Google Books API error"))
            .when(adminService).addBookCopyPart2(category);

        // Act & Assert
        assertThrows(RuntimeException.class, () -> {
            adminController.addBookCopies(category);
        });
    }

    @Test
    void testUpdatingNewAdmin_InvalidUserId() {
        // Arrange
        AdminController.updateNewAdmin updateNewAdmin = new AdminController.updateNewAdmin(-1L);
        when(session.getAttribute("authUsername")).thenReturn("admin");
        when(adminService.updateAdminRole(-1L))
            .thenThrow(new IllegalArgumentException("Invalid user ID"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            adminController.updatingNewAdmin(updateNewAdmin, session);
        });
    }

    @Test
    void testDeleteBookFromStock_NullBookId() {
        // Arrange
        when(session.getAttribute("authUsername")).thenReturn("admin");
        doThrow(new IllegalArgumentException("Book ID cannot be null"))
            .when(adminService).deleteBookInStock(0L);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> {
            adminController.deleteBookFromStock(0L, session);
        });
    }
}
