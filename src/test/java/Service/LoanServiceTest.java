package Service;

import com.aclib.aclib_deploy.DTO.LoanDTO;
import com.aclib.aclib_deploy.Entity.Book;
import com.aclib.aclib_deploy.Entity.Loans;
import com.aclib.aclib_deploy.Entity.User;
import com.aclib.aclib_deploy.Exception.BookNotFoundException;
import com.aclib.aclib_deploy.Repository.BookRepository;
import com.aclib.aclib_deploy.Repository.LoanRepository;
import com.aclib.aclib_deploy.Repository.UserRepository;
import com.aclib.aclib_deploy.Service.LoanService;
import com.aclib.aclib_deploy.ThirdPartyService.EmailAsyncService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LoanServiceTest {

    private LoanService loanService;

    private LoanRepository loanRepository;

    private UserRepository userRepository;

    private BookRepository bookRepository;

    private EmailAsyncService emailAsyncService;


    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        userRepository = mock(UserRepository.class);
        bookRepository = mock(BookRepository.class);
        loanRepository = mock(LoanRepository.class);
        loanService = new LoanService();
        emailAsyncService = mock(EmailAsyncService.class);

        Field loanRepositoryField = LoanService.class.getDeclaredField("loanRepository");
        loanRepositoryField.setAccessible(true);
        loanRepositoryField.set(loanService, loanRepository);

        Field userRepositoryField = LoanService.class.getDeclaredField("userRepository");
        userRepositoryField.setAccessible(true);
        userRepositoryField.set(loanService, userRepository);

        Field bookRepositoryField = LoanService.class.getDeclaredField("bookRepository");
        bookRepositoryField.setAccessible(true);
        bookRepositoryField.set(loanService, bookRepository);


        Field emailAsyncServiceField = LoanService.class.getDeclaredField("emailAsyncService");
        emailAsyncServiceField.setAccessible(true);
        emailAsyncServiceField.set(loanService, emailAsyncService);

    }

    @Test
    public void testSearchByLoansId() throws NoSuchFieldException, IllegalAccessException {
        Long id = 1L;
        Long userId = 1L;
        Long bookId = 1L;
        Loans mockLoans = new Loans();
        User mockUser = new User();
        Book mockBook = new Book();

        // Set the user ID
        Field userIdField = User.class.getDeclaredField("id");
        userIdField.setAccessible(true);
        userIdField.set(mockUser, userId);

        // Set the book ID
        Field bookIdField = Book.class.getDeclaredField("id");
        bookIdField.setAccessible(true);
        bookIdField.set(mockBook, bookId);

        // Set the user and book in the loan
        Field userField = Loans.class.getDeclaredField("user");
        userField.setAccessible(true);
        userField.set(mockLoans, mockUser);

        Field bookField = Loans.class.getDeclaredField("book");
        bookField.setAccessible(true);
        bookField.set(mockLoans, mockBook);

        // Set the loan ID
        Field loansId = Loans.class.getDeclaredField("loansId");
        loansId.setAccessible(true);
        loansId.set(mockLoans, id);

        when(loanRepository.findById(id)).thenReturn(java.util.Optional.of(mockLoans));
        LoanDTO result = loanService.searchByLoansId(id);

        assertNotNull(result);
        assertEquals(id, result.getLoansId());
        assertEquals(userId, result.getUserId());
        assertEquals(bookId, result.getBookId());
    }

    @Test
    public void testGetUsernameWithLoans() throws NoSuchFieldException, IllegalAccessException {
        Long loanId = 1L;
        Long userId = 1L;
        String username = "username";

        Loans mockLoans = new Loans();
        User mockUser = new User();

        Field userIdField = User.class.getDeclaredField("id");
        userIdField.setAccessible(true);
        userIdField.set(mockUser, userId);

        Field usernameField = User.class.getDeclaredField("username");
        usernameField.setAccessible(true);
        usernameField.set(mockUser, username);

        Field loanIdField = Loans.class.getDeclaredField("loansId");
        loanIdField.setAccessible(true);
        loanIdField.set(mockLoans, loanId);

        mockLoans.setUser(mockUser);

        when(loanRepository.findById(loanId)).thenReturn(java.util.Optional.of(mockLoans));
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(mockUser));

        String result = loanService.getUsernameWithLoans(loanId);

        assertNotNull(result);
        assertEquals(username, result);
    }

    @Test
    public void testBorrowBook() throws NoSuchFieldException, IllegalAccessException {
        String bookId = "1";
        Long userId = 1L;
        User mockUser = new User();
        Book mockBook = new Book();
        mockBook.setCopy(1);

        // Set the user ID
        Field userIdField = User.class.getDeclaredField("id");
        userIdField.setAccessible(true);
        userIdField.set(mockUser, userId);

        // Set the book ID
        Field bookIdField = Book.class.getDeclaredField("idSelfLink");
        bookIdField.setAccessible(true);
        bookIdField.set(mockBook, bookId);

        when(bookRepository.findByIdSelfLink(bookId)).thenReturn(mockBook);
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(mockUser));
        when(loanRepository.findByIdSelfLinkAndUserId(bookId, userId)).thenReturn(null);

        LoanDTO result = loanService.borrowBook(bookId, userId);

        assertNotNull(result);
        assertEquals(bookId, result.getIdSelfLink());
        assertEquals(userId, result.getUserId());
        assertEquals(Loans.LoanStatus.ACTIVE.name(), result.getLoanStatus());
        assertEquals(0, mockBook.getCopy()); // Ensure the book copy count is decremented
    }

    @Test
    public void testBorrowBook_BookNotFound() {
        String bookId = "1";
        Long userId = 1L;

        when(bookRepository.findByIdSelfLink(bookId)).thenReturn(null);

        assertThrows(BookNotFoundException.class, () -> loanService.borrowBook(bookId, userId));
    }


    @Test
    public void testReturnBook() throws Exception {
        // Mock dữ liệu
        long bookId = 1L;
        long userId = 1L;

        Book mockBook = new Book();
        mockBook.setId(1L);
        mockBook.setCopy(1);
        mockBook.setTitle("Test Book");

        Loans mockLoan = new Loans();
        mockLoan.setBook(mockBook);

        User mockUser = new User();
        Field userIdField = User.class.getDeclaredField("id");
        userIdField.setAccessible(true);
        userIdField.set(mockUser, userId);
        mockUser.setEmail("test@example.com");
        mockUser.setUsername("testuser");
        mockLoan.setUser(mockUser);

        // Cấu hình behavior của mock
        when(loanRepository.findByIdSelfLinkAndUserId(String.valueOf(bookId), userId))
                .thenReturn(mockLoan);
        when(userRepository.findById(userId))
                .thenReturn(Optional.of(mockUser));
        when(loanRepository.save(any(Loans.class))).thenReturn(mockLoan);
        when(bookRepository.save(any(Book.class))).thenReturn(mockBook);
        doNothing().when(emailAsyncService).sendEmailAsyncReturnSuccessfully(anyString(), anyString(), anyString());

        // Thực hiện test
        LoanDTO result = loanService.returnBook(String.valueOf(bookId), userId);

        // Kiểm tra kết quả
        assertNotNull(result);
        assertEquals("RETURNED", result.getLoanStatus());
        assertEquals(2, mockLoan.getBook().getCopy()); // Bản copy tăng lên 1
    }


    @Test
    public void testCheckOverDueDateLoans() {
        User mockUser = new User();
        mockUser.setEmail("test@example.com");
        mockUser.setUsername("testuser");

        Loans mockLoans = new Loans();
        mockLoans.setLoanStatus(Loans.LoanStatus.ACTIVE);
        mockLoans.setDueDate(LocalDateTime.now().minusDays(1));
        mockLoans.setRenewalCount(0);
        mockLoans.setReturnDate(null);
        mockLoans.setUser(mockUser);
        mockLoans.setNotificationSentDate(LocalDateTime.now().minusMinutes(3)); // Set notification sent date

        List<Loans> loanList = new ArrayList<>();
        loanList.add(mockLoans);

        when(loanRepository.findAllByDueDateAndReturnDateIsNull(any(LocalDateTime.class))).thenReturn(loanList);

        loanService.checkOverDueDateLoans();

        for (Loans loan : loanList) {
            verify(loanRepository, times(1)).save(loan);
            assertEquals(Loans.LoanStatus.LOST, loan.getLoanStatus());
            assertNotNull(loan.getReturnDate());
        }
    }

    @Test
    public void testBorrowAgain() throws NoSuchFieldException, IllegalAccessException {
        Long loanId = 1L;
        User mockUser = new User();
        Field userIdField = User.class.getDeclaredField("id");
        userIdField.setAccessible(true);
        userIdField.set(mockUser, 1L);

        Book mockBook = new Book();
        Field bookIdField = Book.class.getDeclaredField("id");
        bookIdField.setAccessible(true);
        bookIdField.set(mockBook, 1L);

        Loans mockLoans = new Loans();
        mockLoans.setRenewalCount(0);
        mockLoans.setUser(mockUser);
        mockLoans.setBook(mockBook);

        Field loanIdField = Loans.class.getDeclaredField("loansId");
        loanIdField.setAccessible(true);
        loanIdField.set(mockLoans, loanId);

        when(loanRepository.findById(loanId)).thenReturn(Optional.of(mockLoans));

        loanService.borrowAgain(loanId);

        assertEquals(1, mockLoans.getRenewalCount());
        assertNotNull(mockLoans.getDueDate());
    }
}
