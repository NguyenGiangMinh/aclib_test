package com.aclib.aclib_deploy.Controller;

import com.aclib.aclib_deploy.DTO.UserDTO;
import com.aclib.aclib_deploy.Entity.User;
import com.aclib.aclib_deploy.Service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;
import org.springframework.web.bind.annotation.*;

@RestController
public class LoginController {

    @Autowired
    private UserService userService;

    private final AuthenticationManager authenticationManager;

    public LoginController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request, HttpSession session) {
        try {
            // Create an unauthenticated token
            Authentication authenticationRequest =
                    UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(), loginRequest.password());

            // Authenticate the user
            Authentication authenticationResponse =
                    authenticationManager.authenticate(authenticationRequest);

            session.setAttribute("authUsername", authenticationResponse.getName());
            SecurityContextHolder.getContext().setAuthentication(authenticationResponse);
            request.getSession().setAttribute(HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());

            String authUsername = session.getAttribute("authUsername").toString();
            User user = userService.findUser(authUsername);

            // Return a successful response
            return ResponseEntity.ok(userService.mapToUserDTO(user));

        } catch (AuthenticationException e) {
            // If authentication fails, return Unauthorized
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new UserDTO("Authentication failed: Invalid username or password."));
        }
    }

    public record LoginRequest(String username, String password) {
    }
}