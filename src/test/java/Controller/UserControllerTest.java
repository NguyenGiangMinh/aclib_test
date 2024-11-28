package Controller;

import com.aclib.aclib_deploy.Controller.UserController;
import com.aclib.aclib_deploy.DTO.UserDTO;
import com.aclib.aclib_deploy.Entity.User;
import com.aclib.aclib_deploy.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class UserControllerTest {
    private UserController userController;
    private UserService userService;
    private HttpSession session;

    @BeforeEach
    public void setUp() throws IllegalAccessException, NoSuchFieldException {
        userController = new UserController();
        userService = mock(UserService.class);
        session = mock(HttpSession.class);

        Field userServiceField = UserController.class.getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(userController, userService);
    }

    @Test
    public void testGetProfile() throws Exception {
        User user1 = new User();
        Field user1Field = User.class.getDeclaredField("id");
        user1Field.setAccessible(true);
        user1Field.set(user1, 1L);

        UserDTO userDTO = new UserDTO();

        when(session.getAttribute("authUsername")).thenReturn("username");
        when(userService.findUser("username")).thenReturn(user1);
        when(userService.getProfile(1L)).thenReturn(userDTO);

        ResponseEntity<UserDTO> result = userController.getProfile(session);

        assertEquals(ResponseEntity.ok(userDTO), result);

        when(userService.findUser("username")).thenReturn(null);

        ResponseEntity<UserDTO> result2 = userController.getProfile(session);

        assertEquals(ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null), result2);
    }

}
