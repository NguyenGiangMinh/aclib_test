package Service;

import com.aclib.aclib_deploy.DTO.UserDTO;
import com.aclib.aclib_deploy.Entity.Book;
import com.aclib.aclib_deploy.Entity.User;
import com.aclib.aclib_deploy.Repository.BookRepository;
import com.aclib.aclib_deploy.Repository.UserRepository;
import com.aclib.aclib_deploy.Service.AdminService;
import com.aclib.aclib_deploy.ThirdPartyService.EmailService;
import com.aclib.aclib_deploy.ThirdPartyService.GoogleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class AdminServiceTest {
    private AdminService adminService;
    private BookRepository bookRepository;
    private GoogleService googleService;
    private UserRepository userRepository;
    private EmailService emailService;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        adminService = new AdminService();
        bookRepository = mock(BookRepository.class);
        googleService = mock(GoogleService.class);
        userRepository = mock(UserRepository.class);
        emailService = mock(EmailService.class);

        Field bookRepositoryField = AdminService.class.getDeclaredField("bookRepository");
        bookRepositoryField.setAccessible(true);
        bookRepositoryField.set(adminService, bookRepository);

        Field googleServiceField = AdminService.class.getDeclaredField("googleService");
        googleServiceField.setAccessible(true);
        googleServiceField.set(adminService, googleService);

        Field userRepositoryField = AdminService.class.getDeclaredField("userRepository");
        userRepositoryField.setAccessible(true);
        userRepositoryField.set(adminService, userRepository);

        Field emailServiceField = AdminService.class.getDeclaredField("emailService");
        emailServiceField.setAccessible(true);
        emailServiceField.set(adminService, emailService);
    }

    @Test
    public void testDeleteBookInStock() {
        String bookId = "123";
        adminService.deleteBookInStock(bookId);
        //Kiem tra xem ham deleteBookByIdSelfLink co duoc goi chua chi dung 1 lan duy nhat
        verify(bookRepository, times(1)).deleteBookByIdSelfLink(bookId);
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
    }

}
