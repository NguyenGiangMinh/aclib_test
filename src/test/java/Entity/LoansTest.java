package Entity;

import com.aclib.aclib_deploy.Entity.Book;
import com.aclib.aclib_deploy.Entity.Loans;
import com.aclib.aclib_deploy.Entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoansTest {
    private Loans loan;
    private Book book;
    private User user;

    @BeforeEach
    public void setUp() {
        loan = new Loans();
        book = new Book();
        user = new User();
    }

    /**
     * LoansId mac dinh la 0 truoc khi set
     */
    @Test
    public void testgetLoansId() {
        assertEquals(0, loan.getLoansId());
    }

    @Test
    public void testSetAndGetUser() {
        loan.setUser(user);
        assertEquals(user, loan.getUser());
    }


    @Test
    public void testSetAndGetBook() {
        loan.setBook(book);
        assertEquals(book, loan.getBook());
    }

    @Test
    public void testSetAndGetIdSelfLink() {
        loan.setIdSelfLink("12340abc");
        assertEquals("12340abc", loan.getIdSelfLink());
    }

    @Test
    public void testSetAndGetIdSelfLink1() {
        loan.setIdSelfLink("abcdef");
        assertEquals("abcdef", loan.getIdSelfLink());
    }

    @Test
    public void testSetAndGetBookTitle() {
        loan.setBookTitle("Harry Potter");
        assertEquals("Harry Potter", loan.getBookTitle());
    }

    @Test
    public void testSetAndGetBookTitle1() {
        loan.setBookTitle("The Platform");
        assertEquals("The Platform", loan.getBookTitle());
    }

    @Test
    public void testSetAndGetBorrowDate() {
        loan.setBorrowDate(java.time.LocalDate.now());
        assertEquals(java.time.LocalDate.now(), loan.getBorrowDate());
    }

    @Test
    public void testSetAndGetBorrowDate1() {
        loan.setBorrowDate(java.time.LocalDate.of(2021, 12, 31));
        assertEquals(java.time.LocalDate.of(2021, 12, 31), loan.getBorrowDate());
    }

    @Test
    public void testSetAndGetDueDate() {
        loan.setDueDate(java.time.LocalDate.now());
        assertEquals(java.time.LocalDate.now(), loan.getDueDate());
    }

    @Test
    public void testSetAndGetDueDate1() {
        loan.setDueDate(java.time.LocalDate.of(2021, 12, 31));
        assertEquals(java.time.LocalDate.of(2021, 12, 31), loan.getDueDate());
    }

    @Test
    public void testSetAndGetReturnDate() {
        loan.setReturnDate(java.time.LocalDate.now());
        assertEquals(java.time.LocalDate.now(), loan.getReturnDate());
    }

    @Test
    public void testSetAndGetReturnDate1() {
        loan.setReturnDate(java.time.LocalDate.of(2021, 12, 31));
        assertEquals(java.time.LocalDate.of(2021, 12, 31), loan.getReturnDate());
    }

    @Test
    public void testSetAndGetRenewalCount() {
        loan.setRenewalCount(1);
        assertEquals(1, loan.getRenewalCount());
    }

    @Test
    public void testSetAndGetRenewalCount1() {
        loan.setRenewalCount(0);
        assertEquals(0, loan.getRenewalCount());
    }

    @Test
    public void testSetAndGetNotificationSentDate() {
        loan.setNotificationSentDate(java.time.LocalDate.now());
        assertEquals(java.time.LocalDate.now(), loan.getNotificationSentDate());
    }

    @Test
    public void testSetAndGetNotificationSentDate1() {
        loan.setNotificationSentDate(java.time.LocalDate.of(2021, 12, 31));
        assertEquals(java.time.LocalDate.of(2021, 12, 31), loan.getNotificationSentDate());
    }

    @Test
    public void testSetAndGetLoanStatus() {
        loan.setLoanStatus(Loans.LoanStatus.ACTIVE);
        assertEquals(Loans.LoanStatus.ACTIVE, loan.getLoanStatus());
    }

    @Test
    public void testSetAndGetLoanStatus1() {
        loan.setLoanStatus(Loans.LoanStatus.RETURNED);
        assertEquals(Loans.LoanStatus.RETURNED, loan.getLoanStatus());
    }

    @Test
    public void testSetAndGetLoanStatus2() {
        loan.setLoanStatus(Loans.LoanStatus.OVERDUE);
        assertEquals(Loans.LoanStatus.OVERDUE, loan.getLoanStatus());
    }

    @Test
    public void testSetAndGetLoanStatus3() {
        loan.setLoanStatus(Loans.LoanStatus.LOST);
        assertEquals(Loans.LoanStatus.LOST, loan.getLoanStatus());
    }
}
