package ThirdPartyService;

import com.aclib.aclib_deploy.ThirdPartyService.JsonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.File;

import java.util.Map;


import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JsonServiceTest {
    @Test
    public void testSaveRegistrationIdToJson() {
        JsonService jsonService = new JsonService();
        String registrationId = "registrationId";
        String filepath = "filePath";

        jsonService.saveRegistrationIdToJson("registrationId", "filePath");

        File file = new File(filepath);
        assertTrue(file.exists());

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> data = objectMapper.readValue(file, Map.class);
            assertTrue(data.containsKey("registrationId"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testSaveRegistrationIdToJson1() {
        JsonService jsonService = new JsonService();
        String registrationId = "registrationId1";
        String filepath = "filePath1";

        jsonService.saveRegistrationIdToJson(registrationId, filepath);

        File file = new File(filepath);
        assertTrue(file.exists());

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, String> data = objectMapper.readValue(file, Map.class);
            assertTrue(data.containsKey("registrationId"));
            assertEquals(registrationId, data.get("registrationId"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
