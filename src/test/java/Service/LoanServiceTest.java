package Service;

import com.aclib.aclib_deploy.Entity.Book;
import com.aclib.aclib_deploy.Entity.Loans;
import com.aclib.aclib_deploy.Entity.User;
import com.aclib.aclib_deploy.Repository.BookRepository;
import com.aclib.aclib_deploy.Repository.LoanRepository;
import com.aclib.aclib_deploy.Repository.UserRepository;
import com.aclib.aclib_deploy.Service.LoanService;
import com.aclib.aclib_deploy.ThirdPartyService.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.lang.reflect.Field;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

public class LoanServiceTest {

    private LoanService loanService;

    private LoanRepository loanRepository;

    private UserRepository userRepository;

    private BookRepository bookRepository;

    private EmailService emailService;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        userRepository = mock(UserRepository.class);
        bookRepository = mock(BookRepository.class);
        loanRepository = mock(LoanRepository.class);
        emailService = mock(EmailService.class);
        loanService = new LoanService();

        Field loanRepositoryField = LoanService.class.getDeclaredField("loanRepository");
        loanRepositoryField.setAccessible(true);
        loanRepositoryField.set(loanService, loanRepository);

        Field userRepositoryField = LoanService.class.getDeclaredField("userRepository");
        userRepositoryField.setAccessible(true);
        userRepositoryField.set(loanService, userRepository);

        Field bookRepositoryField = LoanService.class.getDeclaredField("bookRepository");
        bookRepositoryField.setAccessible(true);
        bookRepositoryField.set(loanService, bookRepository);

        Field emailServiceField = LoanService.class.getDeclaredField("emailService");
        emailServiceField.setAccessible(true);
        emailServiceField.set(loanService, emailService);
    }

    @Test
    public void testSearchByLoansId() throws NoSuchFieldException, IllegalAccessException {
        Long id = 1L;
        Loans mockLoans = new Loans();
        Field loansId = Loans.class.getDeclaredField("loansId");
        loansId.setAccessible(true);
        loansId.set(mockLoans, id);
        when(loanRepository.findById(id)).thenReturn(java.util.Optional.of(mockLoans));
        Loans result = loanService.searchByLoansId(id).get();

        assertNotNull(result);
        assertEquals(id, result.getLoansId());
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

        Field userIdField = User.class.getDeclaredField("id");
        userIdField.setAccessible(true);
        userIdField.set(mockUser, userId);

        when(bookRepository.findByIdSelfLink(bookId)).thenReturn(mockBook);
        when(userRepository.findById(userId)).thenReturn(java.util.Optional.of(mockUser));
        Loans result = loanService.borrowBook(bookId, userId);

        assertNotNull(result);
        assertEquals(bookId, result.getIdSelfLink());
        assertEquals(userId, result.getUser().getId());
        assertEquals(Loans.LoanStatus.ACTIVE, result.getLoanStatus());
        //giam so luong sach thanh 0 khi muon thanh cong
        assertEquals(0, mockBook.getCopy());
    }

    @Test
    public void testReturnBook() throws NoSuchFieldException, IllegalAccessException {
        String bookId = "1";
        Long userId = 1L;

        Loans mockLoans = new Loans();
        Book mockBook = new Book();
        mockBook.setCopy(0);

        User mockUser = new User();
        Field userIdField = User.class.getDeclaredField("id");
        userIdField.setAccessible(true);
        userIdField.set(mockUser, userId);

        Field userField = Loans.class.getDeclaredField("user");
        userField.setAccessible(true);
        userField.set(mockLoans, mockUser);

        mockLoans.setBook(mockBook);
        mockBook.setStatus(String.valueOf(Loans.LoanStatus.ACTIVE));

        when(loanRepository.findByIdSelfLinkAndUserId(bookId, userId)).thenReturn(mockLoans);
        when(bookRepository.findByIdSelfLink(bookId)).thenReturn(mockBook);

        Loans result = loanService.returnBook(bookId, userId);

        assertNotNull(result);
        assertEquals(1, mockBook.getCopy());
        assertEquals(Loans.LoanStatus.RETURNED, result.getLoanStatus());
        assertNotNull(result.getReturnDate());
    }

    @Test
    public void testCheckOverDueDateLoans() {
        Loans mockLoans = new Loans();
        mockLoans.setNotificationSentDate(null);
        mockLoans.setLoanStatus(Loans.LoanStatus.ACTIVE);
        mockLoans.setUser(new User());
        mockLoans.setBook(new Book());
        mockLoans.setDueDate(LocalDate.now().minusDays(1));

        List<Loans> loanList = new ArrayList<>();
        loanList.add(mockLoans);

        when(loanRepository.findAllByDueDateAndReturnDateIsNull(LocalDate.now())).thenReturn(loanList);
        loanService.checkOverDueDateLoans();

        assertEquals(Loans.LoanStatus.OVERDUE, mockLoans.getLoanStatus());
    }

    @Test
    public void testBorrowAgain() throws NoSuchFieldException, IllegalAccessException {
        Long loanId = 1L;
        Loans mockLoans = new Loans();
        mockLoans.setRenewalCount(0);

        Field loanIdField = Loans.class.getDeclaredField("loansId");
        loanIdField.setAccessible(true);
        loanIdField.set(mockLoans, loanId);

        when(loanRepository.findById(loanId)).thenReturn(java.util.Optional.of(mockLoans));

        loanService.borrowAgain(loanId);

        assertEquals(1, mockLoans.getRenewalCount());
        assertNotNull(mockLoans.getDueDate());
    }
}
