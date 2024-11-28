package Controller;

import com.aclib.aclib_deploy.Controller.AdminController;
import com.aclib.aclib_deploy.DTO.UserDTO;
import com.aclib.aclib_deploy.Entity.Book;
import com.aclib.aclib_deploy.Service.AdminService;
import com.aclib.aclib_deploy.Service.LoanService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class AdminControllerTest {
    private AdminController adminController;

    private AdminService adminService;

    private HttpSession session;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        adminController = new AdminController();
        adminService = mock(AdminService.class);
        session = mock(HttpSession.class);

        Field adminServiceField = AdminController.class.getDeclaredField("adminService");
        adminServiceField.setAccessible(true);
        adminServiceField.set(adminController, adminService);

    }

    @Test
    public void testDeleteBookFromStock() {
        Long bookId = 1L;
        when(session.getAttribute("authUsername")).thenReturn("admin");

        ResponseEntity<Void> result = adminController.deleteBookFromStock(bookId, session);

        assertEquals(ResponseEntity.ok().build(), result);
    }

    @Test
    public void testUpdateBookIntoStock() {
        AdminController.updateBook book = new AdminController.updateBook("123", 5);
        when(session.getAttribute("authUsername")).thenReturn("admin");

        ResponseEntity<String> result = adminController.updateBookIntoStock(book, session);

        assertEquals(ResponseEntity.ok().build(), result);
    }

    @Test
    public void testAddBookCopies() {
        String category = "category";
        when(session.getAttribute("authUsername")).thenReturn("admin");

        ResponseEntity<Void> result = adminController.addBookCopies(category);

        assertEquals(ResponseEntity.ok().build(), result);
    }

    @Test
    public void testUpdatingNewAdmin() {
        AdminController.updateNewAdmin newAdmin = new AdminController.updateNewAdmin(1L);
        UserDTO userDTO = new UserDTO();
        when(session.getAttribute("authUsername")).thenReturn("admin");
        when(adminService.updateAdminRole(newAdmin.userId())).thenReturn(userDTO);

        ResponseEntity<UserDTO> result = adminController.updatingNewAdmin(newAdmin, session);

        assertEquals(ResponseEntity.ok(userDTO), result);
    }
}
