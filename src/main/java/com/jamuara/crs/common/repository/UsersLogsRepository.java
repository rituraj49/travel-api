package com.jamuara.crs.common.repository;

import com.jamuara.crs.model.UserLogs;
import org.springframework.context.annotation.Profile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

@Profile("!nodb")
public interface UsersLogsRepository extends JpaRepository<UserLogs,Long> {
    List<UserLogs> findByLogTimestampBetween(LocalDateTime start, LocalDateTime end);
}
