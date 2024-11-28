package com.aclib.aclib_deploy.Controller;


import com.aclib.aclib_deploy.DTO.RegistrationResponse;
import com.aclib.aclib_deploy.Entity.User;
import com.aclib.aclib_deploy.Service.UserService;
import com.aclib.aclib_deploy.ThirdPartyService.JsonService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RegisterController {

    private final UserService userService;

    private final JsonService jsonService;

    // Define the admin code
    private static final String ADMIN_REGISTRATION_CODE = "STAFF_SECRET_CODE";

    public RegisterController(UserService userService, JsonService jsonService) {
        this.userService = userService;
        this.jsonService = jsonService;
    }

    @PostMapping("/register")
    public ResponseEntity<RegistrationResponse> register(@RequestBody RegisterRequest registerRequest) {
        try {
            User newUser = new User();
            newUser.setUsername(registerRequest.username);
            newUser.setPassword(registerRequest.password);
            newUser.setEmail(registerRequest.email);
            newUser.setPhone(registerRequest.phone);

            if (registerRequest.role() != null) {
                String role = registerRequest.role().trim().toUpperCase();

                if ("ADMIN".equals(role) && !ADMIN_REGISTRATION_CODE.equals(registerRequest.adminCode())) {
                    return ResponseEntity.status(HttpStatus.FORBIDDEN)
                            .body(new RegistrationResponse(null, "Invalid admin code. "
                                    + "Only authorized staff can register as admin."));
                }

                try {
                    newUser.setRole(User.UserRole.valueOf("ROLE_" + role));
                } catch (IllegalArgumentException e) {
                    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                            .body(new RegistrationResponse(null, "Invalid role type."
                                    + " Please use 'user' or 'admin'."));
                }
            } else {
                newUser.setRole(User.UserRole.ROLE_USER);
            }

            userService.registerNewUser(newUser);

            String registrationId = newUser.getRegistrationId();
            String filePath = "user_registration.json";
            jsonService.saveRegistrationIdToJson(registrationId, filePath);

            // Create the response with registrationId and notifications message
            RegistrationResponse response = new RegistrationResponse(
                    registrationId,
                    "please check the OTP sent to your email and verify!"
            );

            return ResponseEntity.status(HttpStatus.CREATED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(new RegistrationResponse(null,
                            "Registration failed: " + e.getMessage()));
        }
    }

    @PostMapping("/verifying-otp")
    public ResponseEntity<String> verifingOTP (@RequestBody VerifyOtpRequest verifyOtpRequest) {
        if (userService.verifyOtp(verifyOtpRequest.registrationId, verifyOtpRequest.otpCode)) {
            return ResponseEntity.ok("Registration complete! Your account is now active."
                    + "Please login again and enjoy the service");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid or expired OTP."
                    + "\nYou have to wait five minutes to get a new OTP code");
        }
    }

    @PostMapping("/resenting-otp")
    public ResponseEntity<String> resentingOTP (@RequestBody ResentingRequest resentingRequest) {
        if (userService.canRequestNewOtp(resentingRequest.registrationId)) {
            User user = userService.findByRegistrationId(resentingRequest.registrationId);
            userService.sendOTP(user);

            return ResponseEntity.ok("We sent OTP code to your email. Please check");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("We will send the OTP code after 5 minutes."
                    + "\nPlease wait.");
        }
    }

    public record RegisterRequest(String username, String password, String email, String phone,
                                  String role, String adminCode) {}

    public record VerifyOtpRequest(String otpCode, String registrationId) {}
    public record ResentingRequest(String registrationId) {}
}
