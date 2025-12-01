package com.aclib.aclib_deploy.Controller;

import com.aclib.aclib_deploy.DTO.LoanDTO;
import com.aclib.aclib_deploy.Entity.User;
import com.aclib.aclib_deploy.Service.LoanService;
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

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LoanControllerTest {

    @Mock
    private LoanService loanService;

    @Mock
    private UserService userService;

    @Mock
    private HttpSession session;

    @InjectMocks
    private LoanController loanController;

    private User testUser;
    private LoanDTO testLoanDTO;
    private List<LoanDTO> testLoanList;

    @BeforeEach
    void setUp() {
        testUser = new User();
        testUser.setUsername("testuser");
        testUser.setEmail("test@example.com");
        // Set ID using reflection since there's no setter
        try {
            java.lang.reflect.Field idField = User.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(testUser, 1L);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        testLoanDTO = new LoanDTO(
            1L,
            1L,
            1L,
            "book123",
            "Test Book",
            LocalDateTime.now(),
            "ACTIVE",
            null,
            LocalDateTime.now().plusDays(14),
            0
        );

        testLoanList = new ArrayList<>();
        testLoanList.add(testLoanDTO);
    }

    @Test
    void testBorrowBook_Success() {
        // Arrange
        LoanController.BorrowRequest borrowRequest = new LoanController.BorrowRequest("book123");
        when(session.getAttribute("authUsername")).thenReturn("testuser");
        when(userService.findUser("testuser")).thenReturn(testUser);
        when(loanService.borrowBook("book123", testUser.getId())).thenReturn(testLoanDTO);

        // Act
        ResponseEntity<LoanDTO> response = loanController.borrowBook(borrowRequest, session);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Book", response.getBody().getBookTitle());
        verify(loanService).borrowBook("book123", testUser.getId());
    }

    @Test
    void testBorrowBook_NotFound() {
        // Arrange
        LoanController.BorrowRequest borrowRequest = new LoanController.BorrowRequest("nonexistent");
        when(session.getAttribute("authUsername")).thenReturn("testuser");
        when(userService.findUser("testuser")).thenReturn(testUser);
        when(loanService.borrowBook("nonexistent", testUser.getId())).thenReturn(null);

        // Act
        ResponseEntity<LoanDTO> response = loanController.borrowBook(borrowRequest, session);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testBorrowBook_BookNotAvailable() {
        // Arrange
        LoanController.BorrowRequest borrowRequest = new LoanController.BorrowRequest("book123");
        when(session.getAttribute("authUsername")).thenReturn("testuser");
        when(userService.findUser("testuser")).thenReturn(testUser);
        when(loanService.borrowBook("book123", testUser.getId()))
            .thenThrow(new RuntimeException("Book not available"));

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
            loanController.borrowBook(borrowRequest, session));
    }

    @Test
    void testBorrowBook_UserAlreadyBorrowed() {
        // Arrange
        LoanController.BorrowRequest borrowRequest = new LoanController.BorrowRequest("book123");
        when(session.getAttribute("authUsername")).thenReturn("testuser");
        when(userService.findUser("testuser")).thenReturn(testUser);
        when(loanService.borrowBook("book123", testUser.getId()))
            .thenThrow(new IllegalStateException("User must return the book before borrowing it again"));

        // Act & Assert
        assertThrows(IllegalStateException.class, () ->
            loanController.borrowBook(borrowRequest, session));
    }

    @Test
    void testReturnBook_Success() {
        // Arrange
        LoanController.ReturnRequest returnRequest = new LoanController.ReturnRequest("book123");
        testLoanDTO.setReturnDate(LocalDateTime.now());
        when(session.getAttribute("authUsername")).thenReturn("testuser");
        when(userService.findUser("testuser")).thenReturn(testUser);
        when(loanService.returnBook("book123", testUser.getId())).thenReturn(testLoanDTO);

        // Act
        ResponseEntity<LoanDTO> response = loanController.returnBook(returnRequest, session);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertNotNull(response.getBody().getReturnDate());
        verify(loanService).returnBook("book123", testUser.getId());
    }

    @Test
    void testReturnBook_NotFound() {
        // Arrange
        LoanController.ReturnRequest returnRequest = new LoanController.ReturnRequest("book123");
        when(session.getAttribute("authUsername")).thenReturn("testuser");
        when(userService.findUser("testuser")).thenReturn(testUser);
        when(loanService.returnBook("book123", testUser.getId())).thenReturn(null);

        // Act
        ResponseEntity<LoanDTO> response = loanController.returnBook(returnRequest, session);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testReturnBook_LoanNotFound() {
        // Arrange
        LoanController.ReturnRequest returnRequest = new LoanController.ReturnRequest("book123");
        when(session.getAttribute("authUsername")).thenReturn("testuser");
        when(userService.findUser("testuser")).thenReturn(testUser);
        when(loanService.returnBook("book123", testUser.getId()))
            .thenThrow(new RuntimeException("Loan not found"));

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
            loanController.returnBook(returnRequest, session));
    }

    @Test
    void testRenewLoan_Success() {
        // Arrange
        LoanController.LoanRenewalRequest renewalRequest = new LoanController.LoanRenewalRequest(1L);
        testLoanDTO.setDueDate(LocalDateTime.now().plusDays(28));
        when(session.getAttribute("authUsername")).thenReturn("testuser");
        when(loanService.borrowAgain(1L)).thenReturn(testLoanDTO);

        // Act
        ResponseEntity<LoanDTO> response = loanController.renewLoan(renewalRequest, session);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(loanService).borrowAgain(1L);
    }

    @Test
    void testRenewLoan_NotFound() {
        // Arrange
        LoanController.LoanRenewalRequest renewalRequest = new LoanController.LoanRenewalRequest(999L);
        when(session.getAttribute("authUsername")).thenReturn("testuser");
        when(loanService.borrowAgain(999L)).thenReturn(null);

        // Act
        ResponseEntity<LoanDTO> response = loanController.renewLoan(renewalRequest, session);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testRenewLoan_MaxRenewalsReached() {
        // Arrange
        LoanController.LoanRenewalRequest renewalRequest = new LoanController.LoanRenewalRequest(1L);
        when(session.getAttribute("authUsername")).thenReturn("testuser");
        when(loanService.borrowAgain(1L))
            .thenThrow(new RuntimeException("Maximum renewals reached"));

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
            loanController.renewLoan(renewalRequest, session));
    }

    @Test
    void testGetMyLoans_Success() {
        // Arrange
        when(session.getAttribute("authUsername")).thenReturn("testuser");
        when(userService.findUser("testuser")).thenReturn(testUser);
        when(userService.getLoans(testUser.getId())).thenReturn(testLoanList);

        // Act
        ResponseEntity<List<LoanDTO>> response = loanController.getMyLoans(session);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Test Book", response.getBody().get(0).getBookTitle());
        verify(userService).getLoans(testUser.getId());
    }

    @Test
    void testGetMyLoans_EmptyList() {
        // Arrange
        when(session.getAttribute("authUsername")).thenReturn("testuser");
        when(userService.findUser("testuser")).thenReturn(testUser);
        when(userService.getLoans(testUser.getId())).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<List<LoanDTO>> response = loanController.getMyLoans(session);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(0, response.getBody().size());
    }

    @Test
    void testGetMyLoans_UserNotAuthenticated() {
        // Arrange
        when(session.getAttribute("authUsername")).thenReturn("testuser");
        when(userService.findUser("testuser")).thenReturn(null);

        // Act
        ResponseEntity<List<LoanDTO>> response = loanController.getMyLoans(session);

        // Assert
        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    @Test
    void testGetMyLoans_MultipleLoans() {
        // Arrange
        LoanDTO loan2 = new LoanDTO(
            2L,
            1L,
            2L,
            "book456",
            "Test Book 2",
            LocalDateTime.now(),
            "ACTIVE",
            null,
            LocalDateTime.now().plusDays(14),
            0
        );
        testLoanList.add(loan2);

        when(session.getAttribute("authUsername")).thenReturn("testuser");
        when(userService.findUser("testuser")).thenReturn(testUser);
        when(userService.getLoans(testUser.getId())).thenReturn(testLoanList);

        // Act
        ResponseEntity<List<LoanDTO>> response = loanController.getMyLoans(session);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testBorrowBook_NullBookId() {
        // Arrange
        LoanController.BorrowRequest borrowRequest = new LoanController.BorrowRequest(null);
        when(session.getAttribute("authUsername")).thenReturn("testuser");
        when(userService.findUser("testuser")).thenReturn(testUser);
        when(loanService.borrowBook(null, testUser.getId()))
            .thenThrow(new IllegalArgumentException("Book ID cannot be null"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
            loanController.borrowBook(borrowRequest, session));
    }

    @Test
    void testBorrowBook_EmptyBookId() {
        // Arrange
        LoanController.BorrowRequest borrowRequest = new LoanController.BorrowRequest("");
        when(session.getAttribute("authUsername")).thenReturn("testuser");
        when(userService.findUser("testuser")).thenReturn(testUser);
        when(loanService.borrowBook("", testUser.getId()))
            .thenThrow(new IllegalArgumentException("Book ID cannot be empty"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
            loanController.borrowBook(borrowRequest, session));
    }

    @Test
    void testReturnBook_AlreadyReturned() {
        // Arrange
        LoanController.ReturnRequest returnRequest = new LoanController.ReturnRequest("book123");
        when(session.getAttribute("authUsername")).thenReturn("testuser");
        when(userService.findUser("testuser")).thenReturn(testUser);
        when(loanService.returnBook("book123", testUser.getId()))
            .thenThrow(new IllegalStateException("Book already returned"));

        // Act & Assert
        assertThrows(IllegalStateException.class, () ->
            loanController.returnBook(returnRequest, session));
    }

    @Test
    void testReturnBook_NullBookId() {
        // Arrange
        LoanController.ReturnRequest returnRequest = new LoanController.ReturnRequest(null);
        when(session.getAttribute("authUsername")).thenReturn("testuser");
        when(userService.findUser("testuser")).thenReturn(testUser);
        when(loanService.returnBook(null, testUser.getId()))
            .thenThrow(new IllegalArgumentException("Book ID cannot be null"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
            loanController.returnBook(returnRequest, session));
    }

    @Test
    void testRenewLoan_LoanOverdue() {
        // Arrange
        LoanController.LoanRenewalRequest renewalRequest = new LoanController.LoanRenewalRequest(1L);
        when(session.getAttribute("authUsername")).thenReturn("testuser");
        when(loanService.borrowAgain(1L))
            .thenThrow(new IllegalStateException("Cannot renew overdue loan"));

        // Act & Assert
        assertThrows(IllegalStateException.class, () ->
            loanController.renewLoan(renewalRequest, session));
    }

    @Test
    void testRenewLoan_NullLoanId() {
        // Arrange
        LoanController.LoanRenewalRequest renewalRequest = new LoanController.LoanRenewalRequest(null);
        when(session.getAttribute("authUsername")).thenReturn("testuser");
        when(loanService.borrowAgain(null))
            .thenThrow(new IllegalArgumentException("Loan ID cannot be null"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
            loanController.renewLoan(renewalRequest, session));
    }

    @Test
    void testGetMyLoans_ServiceThrowsException() {
        // Arrange
        when(session.getAttribute("authUsername")).thenReturn("testuser");
        when(userService.findUser("testuser")).thenReturn(testUser);
        when(userService.getLoans(testUser.getId()))
            .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
            loanController.getMyLoans(session));
    }

    @Test
    void testBorrowBook_UserHasOverdueBooks() {
        // Arrange
        LoanController.BorrowRequest borrowRequest = new LoanController.BorrowRequest("book123");
        when(session.getAttribute("authUsername")).thenReturn("testuser");
        when(userService.findUser("testuser")).thenReturn(testUser);
        when(loanService.borrowBook("book123", testUser.getId()))
            .thenThrow(new IllegalStateException("User has overdue books"));

        // Act & Assert
        assertThrows(IllegalStateException.class, () ->
            loanController.borrowBook(borrowRequest, session));
    }

    @Test
    void testBorrowBook_MaxLoanLimitReached() {
        // Arrange
        LoanController.BorrowRequest borrowRequest = new LoanController.BorrowRequest("book123");
        when(session.getAttribute("authUsername")).thenReturn("testuser");
        when(userService.findUser("testuser")).thenReturn(testUser);
        when(loanService.borrowBook("book123", testUser.getId()))
            .thenThrow(new IllegalStateException("Maximum loan limit reached"));

        // Act & Assert
        assertThrows(IllegalStateException.class, () ->
            loanController.borrowBook(borrowRequest, session));
    }
}
