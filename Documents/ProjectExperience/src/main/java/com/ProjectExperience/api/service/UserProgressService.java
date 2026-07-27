package com.ProjectExperience.api.service;

import com.ProjectExperience.api.models.Achievements;
import com.ProjectExperience.api.models.User;
import com.ProjectExperience.api.models.UserAchievements;
import com.ProjectExperience.api.repository.AchievementsRepository;
import com.ProjectExperience.api.repository.UserAchievementsRepository;
import com.ProjectExperience.api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProgressService {

    private final UserRepository userRepository;
    private final AchievementsRepository achievementRepository;
    private final UserAchievementsRepository userAchievementsRepository;

    private static final int XP_PER_LEVEL = 100;

    public void addXp(User user, int xp) {

        user.setXp(user.getXp() + xp);

        checkLevelUp(user);

        userRepository.save(user);
    }

    private void checkLevelUp(User user) {

        int newLevel = (user.getXp() / XP_PER_LEVEL) + 1;

        if (newLevel > user.getLevel()) {
            user.setLevel(newLevel);

            grantAchievement(user, "Nível 2");
        }
    }

    public void grantAchievement(User user, String achievementName) {

        Achievements achievement =
                achievementRepository.findByName(achievementName);

        boolean alreadyHas =
                userAchievementsRepository
                        .existsByUserIdAndAchievementId(
                                user.getId(),
                                achievement.getId());

        if (alreadyHas) {
            return;
        }

        UserAchievements ua = new UserAchievements();

        ua.setUser(user);
        ua.setAchievement(achievement);

        userAchievementsRepository.save(ua);
    }

}