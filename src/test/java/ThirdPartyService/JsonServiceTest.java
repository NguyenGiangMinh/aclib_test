package ThirdPartyService;

import com.aclib.aclib_deploy.ThirdPartyService.JsonService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;
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
}
