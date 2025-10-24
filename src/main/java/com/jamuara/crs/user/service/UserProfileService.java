/*
package com.jamuara.crs.user.service;
import com.jamuara.crs.user.Repository.UserProfileRepository;
import com.jamuara.crs.user.entity.UserProfile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserProfileService {

    @Autowired
    private UserProfileRepository userProfileRepository;




   */
/* public List<UserProfile> getAllUsers() {
        return userProfileRepository.findAll();
    }
*//*


    public Optional<UserProfile> getUserById(Long id) {
        return userProfileRepository.findById(id);
    }


    public UserProfile createUser(UserProfile userProfile) {
        return userProfileRepository.save(userProfile);
    }


    public Optional<UserProfile> updateUser(Long id, UserProfile userDetails) {
        return userProfileRepository.findById(id).map(user -> {
            user.setFirstName(userDetails.getFirstName());
            user.setLastName(userDetails.getLastName());
            user.setEmail(userDetails.getEmail());
            user.setPhone(userDetails.getPhone());
            user.setPassword(userDetails.getPassword());
            user.setUserName(userDetails.getUserName());
            user.setAddress(userDetails.getAddress());
            return userProfileRepository.save(user);
        });
    }


   */
/* public boolean deleteUser(Long id) {
        return userProfileRepository.findById(id).map(user -> {
            userProfileRepository.delete(user);
            return true;
        }).orElse(false);
    }*//*


}*/
