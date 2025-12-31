package com.jamuara.crs.profile.controller;

import com.jamuara.crs.exceptions.UnauthorizedException;
import com.jamuara.crs.profile.dto.UserProfileDto;
import com.jamuara.crs.profile.dto.UserRegisterDto;
import com.jamuara.crs.profile.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("user-profile")
public class UserProfileController {
    @Autowired
    private UserProfileService userProfileService;

    @GetMapping("")
    public ResponseEntity<?> getUser() {

        try {
            UserProfileDto userProfileDto = userProfileService.getUserProfile();
            return ResponseEntity.ok(userProfileDto);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("something went wrong fetching user details: " + e.getMessage());
        }
    }

    @DeleteMapping("")
    public ResponseEntity<?> deleteUser(@PathVariable String id) {

//        userProfileService.deleteUser(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PutMapping("")
    public ResponseEntity<?> updateUser(
//            @AuthenticationPrincipal Jwt jwt,
            @RequestBody UserProfileDto userProfileDto
    ) {
//        String userId = jwt.getClaimAsString("sub");
//        System.out.println("User ID from JWT: " + userId);
        try {
            userProfileService.updateUser(userProfileDto);
            return ResponseEntity.ok("user updated successfully");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user: " + e.getMessage());
        }
    }

    @GetMapping("check-user-exists")
    public ResponseEntity<?> checkUserExistence() {
        try {
            boolean userExists = userProfileService.checkUserExists();
            System.out.println("user exists flag: " + userExists);
            return ResponseEntity.ok(userExists);
        } catch(UnauthorizedException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized Exception: " + e.getMessage());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user: " + e.getMessage());
        }
    }

    @PostMapping("register")
    public ResponseEntity<?> registerUserDetails(@RequestBody UserRegisterDto userRegisterDto) {
        try {
            userProfileService.registerFullUser(userRegisterDto);
            return ResponseEntity.ok("user registered successfully");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error updating user: " + e.getMessage());
        }
    }
}
