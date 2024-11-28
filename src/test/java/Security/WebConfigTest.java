package Security;

import com.aclib.aclib_deploy.Security.WebConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.servlet.config.annotation.CorsRegistration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import static org.mockito.Mockito.*;

public class WebConfigTest {
    private WebConfig webConfig;

    @BeforeEach
    public void setUp() {
        webConfig = new WebConfig();
    }

    @Test
    public void testAddCorsMappings() {
        CorsRegistry registry = mock(CorsRegistry.class);
        CorsRegistration registration = mock(CorsRegistration.class);

        when(registry.addMapping("/**")).thenReturn(registration);
        when(registration.allowedOrigins("http://localhost:5173")).thenReturn(registration);
        when(registration.allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS")).thenReturn(registration);
        when(registration.allowedHeaders("*")).thenReturn(registration);
        when(registration.allowCredentials(true)).thenReturn(registration);
        when(registration.exposedHeaders("Authorization", "Content-Type", "Connection", "Cookie", "Origin", "X-Requested-With", "Accept")).thenReturn(registration);

        webConfig.addCorsMappings(registry);
    }
}
