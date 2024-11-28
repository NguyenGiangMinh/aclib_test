package Security;

import com.aclib.aclib_deploy.Security.SecurityConfig;
import com.aclib.aclib_deploy.Service.UserService;
import jakarta.servlet.Registration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;

import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SecurityConfigTest {
    private SecurityConfig securityConfig;

    @BeforeEach
    public void setUp() {
        securityConfig = new SecurityConfig();
    }

    @Test
    public void testSecurityFilterChain() {
        UserService userService = mock(UserService.class);
        HttpSecurity http = mock(HttpSecurity.class);

        try {
            securityConfig.securityFilterChain(http, userService);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAuthenticationManager() {
        UserDetailsService userDetailsService = mock(UserDetailsService.class);
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

        securityConfig.authenticationManager(userDetailsService, passwordEncoder);
    }

    @Test
    public void TestCorsConfigurationSource() {
        securityConfig.corsConfigurationSource();
    }
}
