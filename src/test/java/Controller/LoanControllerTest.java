package Controller;

import com.aclib.aclib_deploy.Controller.LoanController;
import com.aclib.aclib_deploy.DTO.LoanDTO;
import com.aclib.aclib_deploy.Entity.Loans;
import com.aclib.aclib_deploy.Entity.User;
import com.aclib.aclib_deploy.Service.LoanService;
import com.aclib.aclib_deploy.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.aspectj.lang.annotation.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoanControllerTest {
    private LoanController loanController;
    private LoanService loanService;
    private UserService userService;
    private HttpSession session;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        loanController = new LoanController();
        loanService = mock(LoanService.class);
        userService = mock(UserService.class);
        session = mock(HttpSession.class);

        Field loanServiceField = loanController.getClass().getDeclaredField("loanService");
        loanServiceField.setAccessible(true);
        loanServiceField.set(loanController, loanService);

        Field userServiceField = loanController.getClass().getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(loanController, userService);
    }

    @Test
    public void testBorrowBook() throws Exception {
        LoanController.BorrowRequest borrowRequest = new LoanController.BorrowRequest("bookId");
        User user = new User();
        Field idField = User.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(user, 1L);

        Loans loan = new Loans();

        when(session.getAttribute("authUsername")).thenReturn("username");
        when(userService.findUser("username")).thenReturn(user);
        when(loanService.borrowBook("bookId", 1L)).thenReturn(loan);

        ResponseEntity<Loans> response = loanController.borrowBook(borrowRequest, session);

        assertEquals(ResponseEntity.ok(loan), response);

        when(loanService.borrowBook("bookId", 1L)).thenReturn(null);
        ResponseEntity<Loans> response2 = loanController.borrowBook(borrowRequest, session);
        assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).build(), response2);
    }

    @Test
    public void testReturnBook() throws Exception {
        LoanController.ReturnRequest returnRequest = new LoanController.ReturnRequest("bookId");
        User user = new User();
        Field idField = User.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(user, 1L);

        Loans loan = new Loans();

        when(session.getAttribute("authUsername")).thenReturn("username");
        when(userService.findUser("username")).thenReturn(user);
        when(loanService.returnBook("bookId", 1L)).thenReturn(loan);

        ResponseEntity<Loans> response = loanController.returnBook(returnRequest, session);

        assertEquals(ResponseEntity.ok(loan), response);

        when(loanService.returnBook("bookId", 1L)).thenReturn(null);
        ResponseEntity<Loans> response2 = loanController.returnBook(returnRequest, session);
        assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).build(), response2);
    }

    @Test
    public void testRenewLoan() throws Exception {
        LoanController.LoanRenewalRequest renewalRequest = new LoanController.LoanRenewalRequest(1L);
        Loans loan = new Loans();

        when(session.getAttribute("authUsername")).thenReturn("username");
        when(loanService.borrowAgain(1L)).thenReturn(loan);

        ResponseEntity<Loans> response = loanController.renewLoan(renewalRequest, session);

        assertEquals(ResponseEntity.ok(loan), response);

        when(loanService.borrowAgain(1L)).thenReturn(null);

        ResponseEntity<Loans> response2 = loanController.renewLoan(renewalRequest, session);

        assertEquals(ResponseEntity.status(HttpStatus.NOT_FOUND).build(), response2);
    }

    @Test
    public void testGetMyLoans() throws Exception {
        User user = new User();
        Field idField = User.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(user, 1L);
        List<LoanDTO> loans = List.of(
                new LoanDTO(1L, 1L, 1L, "bookTitle", "author", LocalDate.now(), "status", LocalDate.now(), LocalDate.now(), 1)
        );

        when(session.getAttribute("authUsername")).thenReturn("username");
        when(userService.findUser("username")).thenReturn(user);
        when(userService.getLoans(1L)).thenReturn(loans);

        ResponseEntity<List<LoanDTO>> response = loanController.getMyLoans(session);

        assertEquals(ResponseEntity.ok(loans), response);

        when(userService.findUser("username")).thenReturn(null);

        ResponseEntity<List<LoanDTO>> response2 = loanController.getMyLoans(session);

        assertEquals(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null), response2);
    }
}
