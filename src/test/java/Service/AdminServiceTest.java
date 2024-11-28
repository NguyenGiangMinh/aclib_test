package Service;

import com.aclib.aclib_deploy.DTO.UserDTO;
import com.aclib.aclib_deploy.Entity.Book;
import com.aclib.aclib_deploy.Entity.Loans;
import com.aclib.aclib_deploy.Entity.User;
import com.aclib.aclib_deploy.Repository.BookRepository;
import com.aclib.aclib_deploy.Repository.LoanRepository;
import com.aclib.aclib_deploy.Repository.UserRepository;
import com.aclib.aclib_deploy.Service.AdminService;
import com.aclib.aclib_deploy.ThirdPartyService.EmailAsyncService;
import com.aclib.aclib_deploy.ThirdPartyService.GoogleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AdminServiceTest {
    private AdminService adminService;

    private BookRepository bookRepository;

    private GoogleService googleService;

    private UserRepository userRepository;

    private LoanRepository loanRepository;

    private EmailAsyncService emailAsyncService;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        adminService = new AdminService();
        bookRepository = mock(BookRepository.class);
        googleService = mock(GoogleService.class);
        userRepository = mock(UserRepository.class);
        loanRepository = mock(LoanRepository.class);
        emailAsyncService = mock(EmailAsyncService.class);

        Field bookRepositoryField = AdminService.class.getDeclaredField("bookRepository");
        bookRepositoryField.setAccessible(true);
        bookRepositoryField.set(adminService, bookRepository);

        Field googleServiceField = AdminService.class.getDeclaredField("googleService");
        googleServiceField.setAccessible(true);
        googleServiceField.set(adminService, googleService);

        Field userRepositoryField = AdminService.class.getDeclaredField("userRepository");
        userRepositoryField.setAccessible(true);
        userRepositoryField.set(adminService, userRepository);

        Field loanRepositoryField = AdminService.class.getDeclaredField("loanRepository");
        loanRepositoryField.setAccessible(true);
        loanRepositoryField.set(adminService, loanRepository);

        Field emailAsyncServiceField = AdminService.class.getDeclaredField("emailAsyncService");
        emailAsyncServiceField.setAccessible(true);
        emailAsyncServiceField.set(adminService, emailAsyncService);
    }

    @Test
    public void testDeleteBookInStock() throws Exception {
        Long bookId = 1L;
        Book book = new Book();
        Field field = Book.class.getDeclaredField("id");
        field.setAccessible(true);
        field.set(book, bookId);

        Loans loan = new Loans();

        when(bookRepository.findById(bookId)).thenReturn(Optional.of(book));
        when(loanRepository.findByBookId(bookId)).thenReturn(loan);

        adminService.deleteBookInStock(bookId);

        verify(loanRepository, times(1)).delete(loan);
        verify(bookRepository, times(1)).delete(book);

        when(bookRepository.findById(bookId)).thenReturn(null);
        assertThrows(Exception.class, () -> adminService.deleteBookInStock(bookId));
    }


    @Test
    public void testUpdateBookCopy() {
        String bookId = "123";
        int newCopy = 5;
        Book book = new Book();
        book.setCopy(1);
        book.setIdSelfLink(bookId);
        when(bookRepository.findByIdSelfLink(bookId)).thenReturn(book);
        adminService.updateBookCopy(bookId, newCopy);

        assertEquals(newCopy, book.getCopy());
    }

    @Test
    public void testAddBookCopyPart2() {
        String category = "category";
        Book book = new Book();
        book.setCopy(1);
        book.setIdSelfLink("123");
        when(bookRepository.findByIdSelfLink("123")).thenReturn(book);
        when(googleService.searchBooksWithCategory(category)).thenReturn(null);
        adminService.addBookCopyPart2(category);

        verify(bookRepository, times(0)).save(book);

        when(googleService.searchBooksWithCategory(category)).thenReturn(null);
        adminService.addBookCopyPart2(category);

        verify(bookRepository, times(0)).save(book);
    }


    @Test
    public void testUpdateAdminRole() throws IllegalAccessException, NoSuchFieldException {
        Long id = 1L;
        User user = new User();
        user.setRole(User.UserRole.ROLE_USER);
        user.setUsername("user");
        user.setEmail("email@.com");

        Field field = User.class.getDeclaredField("id");
        field.setAccessible(true);
        field.set(user, id);

        when(userRepository.findById(id)).thenReturn(Optional.of(user));

        UserDTO userDTO = adminService.updateAdminRole(id);

        assertEquals(User.UserRole.ROLE_ADMIN, userDTO.getRole());
        verify(emailAsyncService, times(1)).sendEmailAsyncSendRoleUpdateNotifications(user.getEmail(), user.getUsername(), "Admin");
    }

}
