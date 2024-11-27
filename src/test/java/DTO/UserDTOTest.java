package DTO;

import com.aclib.aclib_deploy.DTO.UserDTO;
import com.aclib.aclib_deploy.Entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class UserDTOTest {
    private UserDTO userDTO;

    @BeforeEach
    void setUp() {
        userDTO = new UserDTO(1234L, "username123", "email123", "phone123", User.UserRole.ROLE_USER);
    }

    @Test
    public void testGetUserId() {
        assertEquals(1234L, userDTO.getUserId());
    }

    @Test
    public void testSetUserId() {
        userDTO.setUserId(5678L);
        assertEquals(5678L, userDTO.getUserId());
    }

    @Test
    public void testGetEmail() {
        assertEquals("email123", userDTO.getEmail());
    }

    @Test
    public void testSetEmail() {
        userDTO.setEmail("newEmail123");
        assertEquals("newEmail123", userDTO.getEmail());
    }

    @Test
    public void testGetAvatarUrl() {
        assertNull(userDTO.getAvatarUrl());
    }

    @Test
    public void testSetAvatarUrl() {
        userDTO.setAvatarUrl("avatarUrl123");
        assertEquals("avatarUrl123", userDTO.getAvatarUrl());
    }

    @Test
    public void testGetUsername() {
        assertEquals("username123", userDTO.getUsername());
    }

    @Test
    public void testSetUsername() {
        userDTO.setUsername("newUsername123");
        assertEquals("newUsername123", userDTO.getUsername());
    }

    @Test
    public void testGetPhone() {
        assertEquals("phone123", userDTO.getPhone());
    }

    @Test
    public void testSetPhone() {
        userDTO.setPhone("newPhone123");
        assertEquals("newPhone123", userDTO.getPhone());
    }

    @Test
    public void testGetRole() {
        assertEquals(User.UserRole.ROLE_USER, userDTO.getRole());
    }

    @Test
    public void testSetRole() {
        userDTO.setRole(User.UserRole.ROLE_ADMIN);
        assertEquals(User.UserRole.ROLE_ADMIN, userDTO.getRole());
    }

    @Test
    public void testGetBio() {
        assertNull(userDTO.getBio());
    }

    @Test
    public void testSetBio() {
        userDTO.setBio("bio123");
        assertEquals("bio123", userDTO.getBio());
    }
}
