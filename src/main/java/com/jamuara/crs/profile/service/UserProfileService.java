package com.jamuara.crs.profile.service;

import com.jamuara.crs.profile.dto.UserProfileDto;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class UserProfileService {
    @Value("${app.keycloak.realm}")
    private String realm;

    @Autowired
    private Keycloak keycloak;

    public void updateUser(String userId, UserProfileDto dto) throws Exception {
        UserResource existingUser = keycloak.realm(realm).users().get(userId);
        UserRepresentation userRep = existingUser.toRepresentation();

        if(dto.getUsername() != null) {
            userRep.setUsername(dto.getUsername());
        }

        if (dto.getFirstName() != null) {
            userRep.setFirstName(dto.getFirstName());
        }

        if (dto.getLastName() != null) {
            userRep.setLastName(dto.getLastName());
        }

        if (dto.getEmail() != null) {
            userRep.setEmail(dto.getEmail());
        }

        Map<String, List<String>> attrs = userRep.getAttributes();
        if (attrs == null) attrs = new HashMap<>();

        if (dto.getPhone() != null) {
            attrs.put("phone", List.of(dto.getPhone()));
        }

        if (dto.getAddress() != null) {
            UserProfileDto.AddressDto address = dto.getAddress();

            if (address.getLine1() != null) attrs.put("address.line1", List.of(address.getLine1()));
            if (address.getLine2() != null) attrs.put("address.line2", List.of(address.getLine2()));
            if (address.getCity() != null) attrs.put("address.city", List.of(address.getCity()));
            if (address.getState() != null) attrs.put("address.state", List.of(address.getState()));
            if (address.getCountry() != null) attrs.put("address.country", List.of(address.getCountry()));
            if (address.getZipCode() != null) attrs.put("address.zip", List.of(address.getZipCode()));
        }

        userRep.setAttributes(attrs);
        existingUser.update(userRep);
    }
}
