package com.jamuara.crs.common.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Map;
import java.util.Optional;

@Service
@Slf4j
public class RestService {
    private final RestTemplate restTemplate;

    public RestService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public <T> ResponseEntity<T> sendRequest(
            String url,
            HttpMethod method,
            Map<String, String> headers,
//            Optional<String> username,
//            Optional<String> password,
            Object body,
            ParameterizedTypeReference<T> responseType
        ) {
        log.info("Rest Template request body: " + body.toString());
        HttpHeaders httpHeaders = new HttpHeaders();

        if(headers != null) {
            headers.forEach(httpHeaders::set);
        }

//        if(username.isPresent() && password.isPresent()) {
//            String auth = username + ":" + password;
//            String encodedAuth = Base64.getEncoder().encodeToString(auth.getBytes(StandardCharsets.UTF_8));
//            httpHeaders.set("Authorization", "Basic " + encodedAuth);
//        }

        if(!httpHeaders.containsKey(HttpHeaders.CONTENT_TYPE)) {
            httpHeaders.setContentType(MediaType.APPLICATION_JSON);
        }

        HttpEntity<?> entity = new HttpEntity<>(body, httpHeaders);

        try {
            var res = restTemplate.exchange(url, method, entity, responseType);
            log.info("rest request completed successfully with status: {}", res.getStatusCode());
            return res;
        } catch (RestClientException e) {
            log.error("Error in restTemplate exchange", e);
            throw new RuntimeException(e);
        }
    }
}
