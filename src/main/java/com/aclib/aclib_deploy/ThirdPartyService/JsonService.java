package com.aclib.aclib_deploy.ThirdPartyService;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
public class JsonService {

    public void saveRegistrationIdToJson(String registrationId, String filePath) {
        Map<String, String> data = new HashMap<>();
        data.put("registrationId", registrationId);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.writeValue(new File(filePath), data);
            System.out.println("JSON file created at: " + filePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

