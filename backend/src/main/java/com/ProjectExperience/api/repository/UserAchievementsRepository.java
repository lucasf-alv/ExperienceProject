package com.ProjectExperience.api.repository;

import com.ProjectExperience.api.models.UserAchievements;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserAchievementsRepository extends JpaRepository<UserAchievements,Long> {

    boolean existsByUserIdAndAchievementId(
            Long userId,
            Long achievementId
    );
}
