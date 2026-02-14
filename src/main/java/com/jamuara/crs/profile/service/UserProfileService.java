package com.jamuara.crs.profile.service;

import com.jamuara.crs.common.Helper;
import com.jamuara.crs.exceptions.UnauthorizedException;
import com.jamuara.crs.model.UserProfile;
import com.jamuara.crs.profile.dto.UserProfileDto;
import com.jamuara.crs.profile.dto.UserRegisterDto;
import com.jamuara.crs.profile.repository.UserProfileRepository;
import jakarta.ws.rs.NotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
public class UserProfileService {
    @Value("${app.keycloak.realm}")
    private String realm;

    @Autowired
    private Keycloak keycloak;

    @Autowired
    private UserProfileRepository userProfileRepository;

    public UserProfileDto getUserProfile() throws Exception {
        String userId = "";
        if(Helper.isUserAuthenticated()) {
            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(jwt != null) userId = jwt.getClaim("sub");
            else userId = "";
        } else {
            throw new Exception("user unauthorized");
        }
        UserResource existingUser = keycloak.realm(realm).users().get(userId);
        System.out.println("existing user: " + existingUser.toString());
        UserRepresentation userRep = existingUser.toRepresentation();
        UserProfile userProfile = fetchUserDetails(userId);

        log.info("kc user id: {}", userId);
//        log.info("user attributes: {}", userRep.getAttributes().toString());
        Map<String, List<String>> attrs = userRep.getAttributes();

        UserProfileDto userProfileDto = new UserProfileDto();
        userProfileDto.setUsername(userRep.getUsername());
        userProfileDto.setFirstName(userRep.getFirstName());
        userProfileDto.setLastName(userRep.getLastName());
        userProfileDto.setEmail(userRep.getEmail());
        userProfileDto.setPhone(attrs.get("phone").get(0));

//        UserProfileDto.AddressDto addressDto = new UserProfileDto.AddressDto();
//        addressDto.setLine1(attrs.get("address.line1").get(0));
//        addressDto.setLine2(attrs.get("address.line2").get(0));
//        addressDto.setCity(attrs.get("address.city").get(0));
//        addressDto.setState(attrs.get("address.state").get(0));
//        addressDto.setCountry(attrs.get("address.country").get(0));
//        addressDto.setZipCode(attrs.get("address.zip").get(0));

        UserProfileDto.AddressDto addressDto = new UserProfileDto.AddressDto();
        addressDto.setLine1(userProfile.getAddress().getLine1());
        addressDto.setLine2(userProfile.getAddress().getLine2());
        addressDto.setCity(userProfile.getAddress().getCity());
        addressDto.setState(userProfile.getAddress().getState());
        addressDto.setCountry(userProfile.getAddress().getCountry());
        addressDto.setZipCode(userProfile.getAddress().getZipCode());

        userProfileDto.setAddress(addressDto);

        userProfileDto.setReservations(userProfile.getReservations());

        return userProfileDto;
    }

    public void updateUser(UserProfileDto dto) throws Exception {
        String userId = "";
        Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if(jwt != null) userId = jwt.getClaim("sub");
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
            UserProfile userProfile = findUserByKcUserId(userId);
            if (address.getLine1() != null) userProfile.getAddress().setLine1(address.getLine1());
            if (address.getLine1() != null) userProfile.getAddress().setLine2(address.getLine2());
            if (address.getLine1() != null) userProfile.getAddress().setCity(address.getCity());
            if (address.getLine1() != null) userProfile.getAddress().setCountry(address.getCountry());
            if (address.getLine1() != null) userProfile.getAddress().setZipCode(address.getZipCode());

            userProfileRepository.save(userProfile);
//            if (address.getLine1() != null) attrs.put("address.line1", List.of(address.getLine1()));
//            if (address.getLine2() != null) attrs.put("address.line2", List.of(address.getLine2()));
//            if (address.getCity() != null) attrs.put("address.city", List.of(address.getCity()));
//            if (address.getState() != null) attrs.put("address.state", List.of(address.getState()));
//            if (address.getCountry() != null) attrs.put("address.country", List.of(address.getCountry()));
//            if (address.getZipCode() != null) attrs.put("address.zip", List.of(address.getZipCode()));
        }

        userRep.setAttributes(attrs);
        existingUser.update(userRep);
    }

    public void registerFullUser(UserRegisterDto dto) throws UnauthorizedException, NotFoundException {
        String userId = Helper.getAuthenticatedUserId();
        UserProfile userProfile = new UserProfile();

        userProfile.setKcUserId(userId);
        UserProfile.Address address = new UserProfile.Address();
            address.setState(dto.getState());
            address.setCountry(dto.getCountry());
            address.setLine1(dto.getLine1());
            address.setLine2(dto.getLine2());
            address.setCity(dto.getCity());
            address.setZipCode(dto.getZipCode());

        userProfile.setAddress(address);

        userProfileRepository.save(userProfile);
    }

    @Transactional
    public UserProfile fetchUserDetails(String userId) {
        UserProfile userProfile = userProfileRepository.findByKcUserIdWithAllRelations(userId)
                .orElseThrow(() -> new NotFoundException("no user found in database for the kc user id"));
        userProfile.getReservations().forEach(r -> r.getTravelers().size());
        userProfile.getReservations().forEach(r -> r.getFlightLegs().size());

        return userProfile;
    }

    public boolean checkUserExists() throws UnauthorizedException {
        String userId = "";
        if(Helper.isUserAuthenticated()) {
            Jwt jwt = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            if(jwt != null) userId = jwt.getClaim("sub");
        } else {
            throw new UnauthorizedException("user is not unauthorized");
        }

        UserProfile userProfile = userProfileRepository.findByKcUserId(userId).orElse(null);
        return userProfile != null;
    }

//    public void ensureUserExists(String kcUserId) {
//       UserProfile userProfile = userProfileRepository.findByKcUserId(kcUserId)
//               .orElseGet(() -> userProfileRepository.save(new UserProfile(kcUserId)));
//    }

    public UserProfile findUserByKcUserId(String kcUserId) {
        return userProfileRepository.findByKcUserId(kcUserId)
                .orElseThrow(() -> new NotFoundException("user with the given kc user id not found: " + kcUserId));
    }
}
