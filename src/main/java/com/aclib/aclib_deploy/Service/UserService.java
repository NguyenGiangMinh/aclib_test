package com.aclib.aclib_deploy.Service;

import com.aclib.aclib_deploy.DTO.LoanDTO;
import com.aclib.aclib_deploy.Entity.Loans;
import com.aclib.aclib_deploy.Entity.User;
import com.aclib.aclib_deploy.Entity.UserInfo;
import com.aclib.aclib_deploy.DTO.UserDTO;
import com.aclib.aclib_deploy.Repository.LoanRepository;
import com.aclib.aclib_deploy.Repository.UserRepository;
import com.aclib.aclib_deploy.ThirdPartyService.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@EnableTransactionManagement
public class UserService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private EmailService emailService;

    @Override
    public UserDetails loadUserByUsername(String username) {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            throw new UsernameNotFoundException("User " + username + " not found");
        }

        if (!user.isActive()) {
            throw new DisabledException("User " + username + " is not active"
                    + "\nPlease verify the OTP code");
        }
        return new UserInfo(user);
    }

    // Register a new user with password encoding
    public User registerNewUser(User user) {
        if (userRepository.existsByUsername(user.getUsername())) {
            throw new RuntimeException("Username already taken");
        }

        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already used");
        }

        if (user.getRole() == null) {
            user.setRole(User.UserRole.ROLE_USER);
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setActive(false);
        // fix here
        String registrationId = UUID.randomUUID().toString();
        user.setRegistrationId(registrationId);

        sendOTP(user);
        return userRepository.save(user);
    }

    public User findByRegistrationId(String registrationId) {
        return userRepository.findByRegistrationId(registrationId);
    }

    public void sendOTP(User user) {
        String otpCode = generateOTPCode();
        user.setOtp(otpCode);
        user.setExpiredTime(LocalDateTime.now().plusMinutes(3));
        user.setLastOTPRequest(LocalDateTime.now());
        userRepository.save(user);

        emailService.sendOTPNotifications(user.getEmail(),
                user.getUsername() ,user.getOtp(), user.getRegistrationId() ,user.getExpiredTime());
    }

    public boolean verifyOtp(String registrationId, String otpCode) {
        User user = userRepository.findByRegistrationId(registrationId);

        if (user == null) {
            throw new RuntimeException("User have" + registrationId + " not found");
        }

        if (otpCode.equals(user.getOtp()) && LocalDateTime.now().isBefore(user.getExpiredTime())) {
            user.setActive(true);
            user.setOtp(null);
            user.setExpiredTime(null);
            userRepository.save(user);
            return true;
        }
        return false;
    }

    public boolean canRequestNewOtp(String registrationId) {
        User user = userRepository.findByRegistrationId(registrationId);
        return user.getLastOTPRequest() == null || LocalDateTime.now().isAfter(user.getLastOTPRequest().plusMinutes(1));
    }


    private String generateOTPCode() {
        Random random = new Random();
        return String.valueOf(10000 + random.nextInt(90000));  // Generate a 5-digit OTP String
    }

    // get user first way
    public User findUser(String userName) {
        User user = userRepository.findByUsername(userName);
        if (user == null) {
            throw new UsernameNotFoundException("User " + userName + " not found");
        }

        return user;
    }

    //get profile
    public UserDTO getProfile(long userId) {
        Optional<User> userOptional = userRepository.findById(userId);
        User user = userOptional.get();
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setPhone(user.getPhone());
        userDTO.setEmail(user.getEmail());
        userDTO.setUserId(userId);
        userDTO.setBio(user.getBio());
        userDTO.setRole(user.getRole());
        return userDTO;
    }

    //get loans deal
    public List<LoanDTO> getLoans(long userId) {
        List<Loans> loans = loanRepository.findByUserId(userId);

        return loans.stream().map(loan -> new LoanDTO(
                loan.getLoansId(),
                loan.getUser().getId(),
                loan.getBook().getId(),
                loan.getIdSelfLink(),
                loan.getBookTitle(),
                loan.getBorrowDate(),
                loan.getLoanStatus().name(),
                loan.getReturnDate(),
                loan.getDueDate(),
                loan.getRenewalCount()
        )).collect(Collectors.toList());
    }


    //edit
    public UserDTO updateDetails(User user, String phone, String bio, MultipartFile filePath) {
        if (phone != null && !phone.isBlank()) {
            user.setPhone(phone);
        }

        if (bio != null && !bio.isEmpty()) {
            user.setBio(bio);
        }


        userRepository.save(user);

        return mapToUserDTO(user);
    }


    public UserDTO mapToUserDTO(User user) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUsername(user.getUsername());
        userDTO.setPhone(user.getPhone());
        userDTO.setBio(user.getBio());
        userDTO.setRole(user.getRole());
        return userDTO;
    }
}

