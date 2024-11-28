package Entity;

import com.aclib.aclib_deploy.Entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    //    private UserRepository userRepository;
    private User user;

    @BeforeEach
    public void setUp() {
        user = new User();
    }

    /**
     * Id mac dinh la null truoc khi set
     */
    @Test
    public void testGetId() {
        assertNull(user.getId());
    }

    @Test
    public void testsetUsername() {
        user.setUsername("LHNam2005");
        assertEquals("LHNam2005", user.getUsername());
    }

    @Test
    public void testsetUsername1() {
        user.setUsername("Louis Trieu");
        assertEquals("Louis Trieu", user.getUsername());
    }

    @Test
    public void testsetUsername2() {
        user.setUsername("NGM");
        assertEquals("NGM", user.getUsername());
    }

    @Test
    public void testsetPassword() {
        user.setPassword("123456abc@");
        assertEquals("123456abc@", user.getPassword());
    }

    @Test
    public void testsetPassword1() {
        user.setPassword("123456abc@");
        assertEquals("123456abc@", user.getPassword());
    }

    @Test
    public void testsetRole() {
        user.setRole(User.UserRole.ROLE_ADMIN);
        assertEquals(User.UserRole.ROLE_ADMIN, user.getRole());
    }

    @Test
    public void testsetRole1() {
        user.setRole(User.UserRole.ROLE_USER);
        assertEquals(User.UserRole.ROLE_USER, user.getRole());
    }

    @Test
    public void testsetEmail() {
        user.setEmail("23021629@vnu.edu.vn");
        assertEquals("23021629@vnu.edu.vn", user.getEmail());
    }

    @Test
    public void testsetEmail1() {
        user.setEmail("23021641@vnu.edu.vn");
        assertEquals("23021641@vnu.edu.vn", user.getEmail());
    }

    @Test
    public void testsetEmail2() {
        user.setEmail("email");
        assertEquals("email", user.getEmail());
    }

    @Test
    public void testsetPhone() {
        user.setPhone("0987654321");
        assertEquals("0987654321", user.getPhone());
    }

    @Test
    public void testsetPhone1() {
        user.setPhone("0123456789");
        assertEquals("0123456789", user.getPhone());
    }

    @Test
    public void testsetOtp() {
        user.setOtp("123456");
        assertEquals("123456", user.getOtp());
    }

    @Test
    public void testsetOtp1() {
        user.setOtp("654321");
        assertEquals("654321", user.getOtp());
    }

    @Test
    public void testsetOtp2() {
        user.setOtp("otp");
        assertEquals("otp", user.getOtp());
    }

    @Test
    public void testsetExpiredTime() {
        LocalDateTime time = LocalDateTime.now();
        user.setExpiredTime(time);
        assertEquals(time, user.getExpiredTime());
    }

    @Test
    public void testsetExpiredTime1() {
        LocalDateTime time = LocalDateTime.of(2021, 12, 31, 23, 59, 59);
        user.setExpiredTime(time);
        assertEquals(time, user.getExpiredTime());
    }

    @Test
    public void testsetExpiredTime2() {
        LocalDateTime time = LocalDateTime.of(2021, 12, 31, 23, 59, 59);
        user.setExpiredTime(time);
        assertEquals(time, user.getExpiredTime());
    }

    @Test
    public void testSetLastOTPRequest() {
        LocalDateTime time = LocalDateTime.now();
        user.setLastOTPRequest(time);
        assertEquals(time, user.getLastOTPRequest());
    }

    @Test
    public void testSetLastOTPRequest1() {
        LocalDateTime time = LocalDateTime.of(2021, 12, 31, 23, 59, 59);
        user.setLastOTPRequest(time);
        assertEquals(time, user.getLastOTPRequest());
    }

    @Test
    public void testSetLastOTPRequest2() {
        LocalDateTime time = LocalDateTime.of(2021, 12, 31, 23, 59, 59);
        user.setLastOTPRequest(time);
        assertEquals(time, user.getLastOTPRequest());
    }

    @Test
    public void testsetActive() {
        user.setActive(true);
        assertTrue(user.isActive());
    }

    @Test
    public void testsetActive1() {
        user.setActive(false);
        assertFalse(user.isActive());
    }

    @Test
    public void testRegistrationId() {
        user.setRegistrationId("123456");
        assertEquals("123456", user.getRegistrationId());
    }

    @Test
    public void testRegistrationId1() {
        user.setRegistrationId("654321");
        assertEquals("654321", user.getRegistrationId());
    }

}
