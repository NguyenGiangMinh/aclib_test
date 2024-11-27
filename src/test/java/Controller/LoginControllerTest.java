package Controller;

import com.aclib.aclib_deploy.Controller.LoginController;
import com.aclib.aclib_deploy.DTO.UserDTO;
import com.aclib.aclib_deploy.Entity.User;
import com.aclib.aclib_deploy.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.apache.catalina.UserDatabase;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class LoginControllerTest {
    private LoginController loginController;
    private UserService userService;
    private AuthenticationManager authenticationManager;
    private HttpSession session;
    private HttpServletRequest request;

    @BeforeEach
    public void setUp() throws IllegalAccessException, NoSuchFieldException {
        userService = mock(UserService.class);
        session = mock(HttpSession.class);
        request = mock(HttpServletRequest.class);
        authenticationManager = mock(AuthenticationManager.class);
        loginController = new LoginController(authenticationManager);

        Field userServiceField = LoginController.class.getDeclaredField("userService");
        userServiceField.setAccessible(true);
        userServiceField.set(loginController, userService);
    }

    @Test
    public void testLogin() {
        LoginController.LoginRequest loginRequest = new LoginController.LoginRequest("username", "password");
        Authentication authenticationRequest = new UsernamePasswordAuthenticationToken("username", "password");
        Authentication authenticationResponse = mock(Authentication.class);

        User user = new User();
        UserDTO userDTO = new UserDTO();

        when(authenticationManager.authenticate(authenticationRequest)).thenReturn(authenticationResponse);
        when(authenticationResponse.getName()).thenReturn("username");
        when(request.getSession()).thenReturn(session);
        when(session.getAttribute("authUsername")).thenReturn("username");
        when(userService.findUser("username")).thenReturn(user);
        when(userService.mapToUserDTO(user)).thenReturn(userDTO);

        ResponseEntity<UserDTO> response = loginController.login(loginRequest, request, session);

        assertEquals(ResponseEntity.ok(userDTO), response);
    }

}
