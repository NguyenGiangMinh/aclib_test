package Service;

import com.aclib.aclib_deploy.DTO.UserDTO;
import com.aclib.aclib_deploy.Entity.User;
import com.aclib.aclib_deploy.Repository.LoanRepository;
import com.aclib.aclib_deploy.Repository.UserRepository;
import com.aclib.aclib_deploy.Service.UserService;
import com.aclib.aclib_deploy.ThirdPartyService.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceTest {
    private UserService userService;
    private UserRepository userRepository;
    private PasswordEncoder passwordEncoder;
    private LoanRepository loanRepository;
    private EmailService emailService;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        userRepository = mock(UserRepository.class);
        passwordEncoder = mock(PasswordEncoder.class);
        loanRepository = mock(LoanRepository.class);
        emailService = mock(EmailService.class);
        userService = new UserService();
        Field userRepositoryField = userService.getClass().getDeclaredField("userRepository");
        userRepositoryField.setAccessible(true);
        userRepositoryField.set(userService, userRepository);

        Field passwordEncoderField = userService.getClass().getDeclaredField("passwordEncoder");
        passwordEncoderField.setAccessible(true);
        passwordEncoderField.set(userService, passwordEncoder);

        Field loanRepositoryField = userService.getClass().getDeclaredField("loanRepository");
        loanRepositoryField.setAccessible(true);
        loanRepositoryField.set(userService, loanRepository);

        Field emailServiceField = userService.getClass().getDeclaredField("emailService");
        emailServiceField.setAccessible(true);
        emailServiceField.set(userService, emailService);
    }

    @Test
    public void testLoadUserByUsername() {
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        user.setActive(false);

        when(userRepository.findByUsername(username)).thenReturn(user);

        assertThrows(DisabledException.class, () -> userService.loadUserByUsername(username));
    }

    @Test
    public void testLoadUserByUsername2() {
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        user.setActive(true);

        when(userRepository.findByUsername(username)).thenReturn(user);

        userService.loadUserByUsername(username);
    }

    @Test
    public void testLoadUserByUsername3() {
        String username = "testuser";
        User user = new User();
        user.setUsername(username);
        user.setActive(true);

        when(userRepository.findByUsername(username)).thenReturn(null);

        assertThrows(UsernameNotFoundException.class, () -> userService.loadUserByUsername(username));
    }

    @Test
    public void testRegisterNewUser() {
        User user = new User();
        user.setUsername("testuser");
        User user1 = new User();
        user1.setUsername("testuser1");
        User user2 = new User();
        user2.setUsername("testuser2");

        when(userRepository.existsByUsername(user.getUsername())).thenReturn(true);
        when(userRepository.existsByEmail(user1.getEmail())).thenReturn(true);
        assertEquals("Username already taken", assertThrows(RuntimeException.class, () -> userService.registerNewUser(user)).getMessage());
        assertEquals("Email already used", assertThrows(RuntimeException.class, () -> userService.registerNewUser(user1)).getMessage());

        when(userRepository.existsByUsername(user2.getUsername())).thenReturn(false);
        when(userRepository.existsByEmail(user2.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(user2.getPassword())).thenReturn("encodedpassword");

        userService.registerNewUser(user2);

        assertEquals(User.UserRole.ROLE_USER, user2.getRole());
        assertFalse(user2.isActive());
        assertNotNull(user2.getRegistrationId());
    }

    @Test
    public void testFindByRegistrationId() {
        String registrationId = "testregistrationid";
        User user = new User();
        user.setRegistrationId(registrationId);

        when(userRepository.findByRegistrationId(registrationId)).thenReturn(user);

        assertEquals(user, userService.findByRegistrationId(registrationId));
    }

    @Test
    public void testSendOTP() {
        User user = new User();
        user.setEmail("testemail");
        user.setUsername("testusername");

        userService.sendOTP(user);

        assertNotNull(user.getOtp());
        assertNotNull(user.getExpiredTime());
        assertNotNull(user.getLastOTPRequest());
    }

    @Test
    public void testVerifyOtp() {
        String registrationId = "testregistrationid";
        String otpCode = "12345";
        User user = new User();
        user.setRegistrationId(registrationId);

        User user1 = new User();
        user1.setRegistrationId(registrationId);
        user1.setOtp(otpCode);
        user1.setExpiredTime(LocalDateTime.now().plusMinutes(5));

        when(userRepository.findByRegistrationId(registrationId)).thenReturn(null);
        assertEquals("User have" + registrationId + " not found", assertThrows(RuntimeException.class, () -> userService.verifyOtp(registrationId, otpCode)).getMessage());

        when(userRepository.findByRegistrationId(registrationId)).thenReturn(user1);

        assertTrue(userService.verifyOtp(registrationId, otpCode));
        assertTrue(user1.isActive());
        assertNull(user1.getOtp());
        assertNull(user1.getExpiredTime());
    }

    @Test
    public void testCanRequestNewOtp() {
        String registrationId = "testregistrationid";
        User user = new User();
        user.setRegistrationId(registrationId);
        user.setLastOTPRequest(null);

        when(userRepository.findByRegistrationId(registrationId)).thenReturn(user);

        assertTrue(userService.canRequestNewOtp(registrationId));
    }

    @Test
    public void testFindUser() {
        String username = "testusername";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(user);

        assertEquals(user, userService.findUser(username));

        when(userRepository.findByUsername(username)).thenReturn(null);
        assertThrows(UsernameNotFoundException.class, () -> userService.findUser(username));
    }

    @Test
    public void testGetProfile() throws NoSuchFieldException, IllegalAccessException {
        Long userId = 1L;
        String username = "testusername";
        String bio = "testbio";
        String avatarUrl = "testavatarurl";
        String phone = "0123456789";
        String email = "testemail";
        User.UserRole role = User.UserRole.ROLE_USER;
        User user = new User();
        Field idField = User.class.getDeclaredField("id");
        idField.setAccessible(true);
        idField.set(user, userId);
        user.setUsername(username);
        user.setBio(bio);
        user.setAvatarUrl(avatarUrl);
        user.setPhone(phone);
        user.setEmail(email);
        user.setRole(role);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        UserDTO userDTO = userService.getProfile(userId);

        assertEquals(username, userDTO.getUsername());
        assertEquals(bio, userDTO.getBio());
        assertEquals(avatarUrl, userDTO.getAvatarUrl());
        assertEquals(phone, userDTO.getPhone());
        assertEquals(email, userDTO.getEmail());
        assertEquals(userId, userDTO.getUserId());
        assertEquals(role, userDTO.getRole());
    }

    @Test
    public void testMapToUserDTO() {
        User user = new User();
        user.setUsername("testusername");
        user.setBio("testbio");
        user.setAvatarUrl("testavatarurl");
        user.setPhone("0123456789");
        user.setRole(User.UserRole.ROLE_USER);

        UserDTO userDTO = userService.mapToUserDTO(user);

        assertEquals(user.getUsername(), userDTO.getUsername());
        assertEquals(user.getBio(), userDTO.getBio());
        assertEquals(user.getAvatarUrl(), userDTO.getAvatarUrl());
        assertEquals(user.getPhone(), userDTO.getPhone());
        assertEquals(user.getRole(), userDTO.getRole());
    }
}
