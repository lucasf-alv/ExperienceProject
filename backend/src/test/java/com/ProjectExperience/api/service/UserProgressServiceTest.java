package com.ProjectExperience.api.service;

import com.ProjectExperience.api.models.Achievements;
import com.ProjectExperience.api.models.User;
import com.ProjectExperience.api.models.UserAchievements;
import com.ProjectExperience.api.repository.AchievementsRepository;
import com.ProjectExperience.api.repository.UserAchievementsRepository;
import com.ProjectExperience.api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserProgressServiceTest {
    @Mock
    private UserRepository userRepository;
    @Mock
    private AchievementsRepository achievementsRepository;
    @Mock
    private UserAchievementsRepository userAchievementsRepository;
    @InjectMocks
    private UserProgressService userProgressService;

    //===================================================================
    //                Testes do addXp
    //===================================================================
    @Test
    void shouldAddXp() {

        // Arrange
        User user = new User();
        user.setId(1L);
        user.setXp(0);
        user.setLevel(1);

        // Act
        userProgressService.addXp(user, 40);

        // Assert
        assertEquals(40, user.getXp());
        assertEquals(1, user.getLevel());

        verify(userRepository).save(user);
    }
    //========================================================================
    //                 Testes do CheckLevelUp
    //========================================================================
    @Test
    void shouldLevelUpWhenXpReaches100() {

        // Arrange
        User user = new User();
        user.setId(1L);
        user.setXp(90);
        user.setLevel(1);

        Achievements achievement = new Achievements();
        achievement.setId(1L);
        achievement.setName("Nível 2");

        when(achievementsRepository.findByName("Nível 2"))
                .thenReturn(achievement);

        when(userAchievementsRepository.existsByUserIdAndAchievementId(1L, 1L))
                .thenReturn(false);

        // Act
        userProgressService.addXp(user, 10);

        // Assert
        assertEquals(100, user.getXp());
        assertEquals(2, user.getLevel());

        verify(userAchievementsRepository)
                .save(any(UserAchievements.class));

        verify(userRepository).save(user);
    }
    //==================================================================================
    //                         Testes do grantAchiviement
    //==================================================================================
    @Test
    void shouldGrantAchievement() {

        // Arrange
        User user = new User();
        user.setId(1L);

        Achievements achievement = new Achievements();
        achievement.setId(1L);
        achievement.setName("Primeira Atividade");

        when(achievementsRepository.findByName("Primeira Atividade"))
                .thenReturn(achievement);

        when(userAchievementsRepository
                .existsByUserIdAndAchievementId(1L, 1L))
                .thenReturn(false);

        // Act
        userProgressService.grantAchievement(
                user,
                "Primeira Atividade"
        );

        // Assert
        verify(userAchievementsRepository)
                .save(any(UserAchievements.class));
    }
    @Test
    void shouldNotGrantAchievementWhenUserAlreadyHasIt() {

        // Arrange
        User user = new User();
        user.setId(1L);

        Achievements achievement = new Achievements();
        achievement.setId(1L);

        when(achievementsRepository.findByName("Primeira Atividade"))
                .thenReturn(achievement);

        when(userAchievementsRepository
                .existsByUserIdAndAchievementId(1L, 1L))
                .thenReturn(true);

        // Act
        userProgressService.grantAchievement(
                user,
                "Primeira Atividade"
        );

        // Assert
        verify(userAchievementsRepository, never())
                .save(any());
    }

}