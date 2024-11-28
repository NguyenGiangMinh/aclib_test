package DTO;

import com.aclib.aclib_deploy.DTO.LoanDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class LoanDTOTest {
    private LoanDTO loanDTO;

    @BeforeEach
    void setUp() {
        loanDTO = new LoanDTO(1111L, 22222L, 33333L, "id123", "Title", LocalDateTime.parse("2023-01-01T12:00:00"), "Borrowed", LocalDateTime.parse("2023-01-15T12:00:00"), LocalDateTime.parse("2023-01-08T12:00:00"), 0);
    }

    @Test
    public void testGetLoansId() {
        assertEquals(1111L, loanDTO.getLoansId());
    }

    @Test
    public void testSetLoansId() {
        loanDTO.setLoansId(2222L);
        assertEquals(2222L, loanDTO.getLoansId());
    }


    @Test
    public void testGetUserId() {
        assertEquals(22222L, loanDTO.getUserId());
    }

    @Test
    public void testSetUserId() {
        loanDTO.setUserId(33333L);
        assertEquals(33333L, loanDTO.getUserId());
    }

    @Test
    public void testGetBookId() {
        assertEquals(33333L, loanDTO.getBookId());
    }

    @Test
    public void testSetBookId() {
        loanDTO.setBookId(44444L);
        assertEquals(44444L, loanDTO.getBookId());
    }

    @Test
    public void testGetIdSelfLink() {
        assertEquals("id123", loanDTO.getIdSelfLink());
    }

    @Test
    public void testSetIdSelfLink() {
        loanDTO.setIdSelfLink("id456");
        assertEquals("id456", loanDTO.getIdSelfLink());
    }

    @Test
    public void testGetBookTitle() {
        assertEquals("Title", loanDTO.getBookTitle());
    }

    @Test
    public void testSetBookTitle() {
        loanDTO.setBookTitle("New Title");
        assertEquals("New Title", loanDTO.getBookTitle());
    }

    @Test
    public void testGetBorrowDate() {
        assertEquals(LocalDateTime.parse("2023-01-01T12:00"), loanDTO.getBorrowDate());
    }

    @Test
    public void testSetBorrowDate() {
        loanDTO.setBorrowDate(LocalDateTime.parse("2023-01-02T12:00:00"));
        assertEquals(LocalDateTime.parse("2023-01-02T12:00"), loanDTO.getBorrowDate());
    }

    @Test
    public void testGetLoanStatus() {
        assertEquals("Borrowed", loanDTO.getLoanStatus());
    }

    @Test
    public void testSetLoanStatus() {
        loanDTO.setLoanStatus("Returned");
        assertEquals("Returned", loanDTO.getLoanStatus());
    }

    @Test
    public void testGetDueDate() {
        assertEquals(LocalDateTime.parse("2023-01-08T12:00"), loanDTO.getDueDate());
    }

    @Test
    public void testSetDueDate() {
        loanDTO.setDueDate(LocalDateTime.parse("2023-01-09T12:00:00"));
        assertEquals(LocalDateTime.parse("2023-01-09T12:00"), loanDTO.getDueDate());
    }

    @Test
    public void testGetReturnDate() {
        assertEquals(LocalDateTime.parse("2023-01-15T12:00"), loanDTO.getReturnDate());
    }

    @Test
    public void testSetReturnDate() {
        loanDTO.setReturnDate(LocalDateTime.parse("2023-01-16T12:00:00"));
        assertEquals(LocalDateTime.parse("2023-01-16T12:00"), loanDTO.getReturnDate());
    }

    @Test
    public void testGetRenewalCount() {
        assertEquals(0, loanDTO.getRenewalCount());
    }

    @Test
    public void testSetRenewalCount() {
        loanDTO.setRenewalCount(1);
        assertEquals(1, loanDTO.getRenewalCount());
    }

}
