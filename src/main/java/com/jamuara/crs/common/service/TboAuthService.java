package com.jamuara.crs.common.service;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class TboAuthService {
    @Autowired
    private RestService restService;

    @Getter
    private static volatile String token = "";

    @Value("${tbo.client.username}")
    private String username;

    @Value("${tbo.client.password}")
    private String password;

    public void authenticate() {
        Map<String, String> requestBody = new HashMap<>();
        requestBody.put("ClientId", "ApiIntegrationNew");
        requestBody.put("UserName", username);
        requestBody.put("Password", password);
        requestBody.put("EndUserIp", "192.168.197.1");

        ResponseEntity<Map<String, Object>> authResponse = restService.sendRequest(
                "http://Sharedapi.tektravels.com/SharedData.svc/rest/Authenticate",
                HttpMethod.POST,
                new HashMap<>(),
                requestBody,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );

        token = (String) authResponse.getBody().get("TokenId");
    }
}
