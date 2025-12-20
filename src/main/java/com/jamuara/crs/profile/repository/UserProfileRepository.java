package com.jamuara.crs.profile.repository;

import com.jamuara.crs.model.Payment;
import com.jamuara.crs.model.UserProfile;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    @Query("""
            SELECT u
            FROM UserProfile u
            WHERE u.kcUserId=:kcUserId
            """)
    UserProfile findByKcUserId(@Param("kcUserId") String kcUserId);
}
