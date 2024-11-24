package DTO;

import com.aclib.aclib_deploy.DTO.RegistrationResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class RegistrationResponseTest {
    private RegistrationResponse registrationResponse;

    @BeforeEach
    void setUp() {
        registrationResponse = new RegistrationResponse("id123", "notifications123");
    }

    @Test
    public void testGetRegistrationId() {
        assertEquals("id123", registrationResponse.getRegistrationId());
    }

    @Test
    public void testSetRegistrationId() {
        registrationResponse.setRegistrationId("id456");
        assertEquals("id456", registrationResponse.getRegistrationId());
    }

    @Test
    public void testGetNotifications() {
        assertEquals("notifications123", registrationResponse.getNotifications());
    }

    @Test
    public void testSetNotifications() {
        registrationResponse.setNotifications("newNotifications123");
        assertEquals("newNotifications123", registrationResponse.getNotifications());
    }
}
