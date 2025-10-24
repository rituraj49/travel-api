/*
package com.jamuara.crs.user.controller;

import com.jamuara.crs.user.entity.UserProfile;
import com.jamuara.crs.user.service.UserProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserProfileController {

    @Autowired
    private UserProfileService userProfileService;




   */
/* @GetMapping
    public List<UserProfile> getAllUsers() {
        return userProfileService.getAllUsers();
    }*//*


    // Get user by ID
    @GetMapping("/{id}")
    public ResponseEntity<UserProfile> getUserById(@PathVariable Long id) {
        return userProfileService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Create user
    @PostMapping
    public UserProfile createUser(@RequestBody UserProfile userProfile) {
        return userProfileService.createUser(userProfile);
    }

    // Update user
    @PutMapping("/{id}")
    public ResponseEntity<UserProfile> updateUser(
            @PathVariable Long id,
            @RequestBody UserProfile userDetails) {
        return userProfileService.updateUser(id, userDetails)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // Delete user
*/
/*    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        boolean deleted = userProfileService.deleteUser(id);
        return deleted ? ResponseEntity.noContent().<Void>build()
                : ResponseEntity.notFound().build();
    }*//*

}
*/
