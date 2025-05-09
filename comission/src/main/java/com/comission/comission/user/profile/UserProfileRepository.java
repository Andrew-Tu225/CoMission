package com.comission.comission.repository;

import com.comission.comission.user.profile.UserProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserProfileRepository extends JpaRepository<UserProfile, Long> {

    Optional<UserProfile> findById(Long userId);
}
