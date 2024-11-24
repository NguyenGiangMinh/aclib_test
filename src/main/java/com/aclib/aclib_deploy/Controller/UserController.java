package com.aclib.aclib_deploy.Controller;

import com.aclib.aclib_deploy.DTO.UserDTO;
import com.aclib.aclib_deploy.Entity.User;
import com.aclib.aclib_deploy.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

@RestController
@RequestMapping("/mls_user")
public class UserController {

    @Autowired
    private UserService userService;

    // see the user's profile
    @GetMapping("/profile")
    public ResponseEntity<UserDTO> getProfile(HttpSession session) {
        String authUsername = (String) session.getAttribute("authUsername");
        User user1 = userService.findUser(authUsername);

        if (user1 == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        UserDTO userDTO = userService.getProfile(user1.getId());
        return new ResponseEntity<>(userDTO, HttpStatus.OK);
    }

    @PutMapping("/profile")
    public ResponseEntity<UserDTO> editUserProfile(@RequestBody UserProfileUpdateDTO profileUpdate, HttpSession session) {
        String authUsername = (String) session.getAttribute("authUsername");
        User user1 = userService.findUser(authUsername);

        if (user1 == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        UserDTO updatedUser = userService.updateDetails(user1, profileUpdate.phone,
                profileUpdate.bio, profileUpdate.file);
        return ResponseEntity.ok(updatedUser);
    }

    public record UserProfileUpdateDTO(User user1, String phone, String bio, String file) {}

}
