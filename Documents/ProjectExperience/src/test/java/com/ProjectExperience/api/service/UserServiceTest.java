package com.ProjectExperience.api.service;

import com.ProjectExperience.api.config.S3Properties;
import com.ProjectExperience.api.dto.UpdateActivityDto;
import com.ProjectExperience.api.dto.UpdateUserDto;
import com.ProjectExperience.api.exceptions.PhotoError;
import com.ProjectExperience.api.exceptions.UserNotFoundError;
import com.ProjectExperience.api.models.Preferences;
import com.ProjectExperience.api.models.User;
import com.ProjectExperience.api.repository.PreferenceRepository;
import com.ProjectExperience.api.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import software.amazon.awssdk.core.sync.RequestBody;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UpdateActivityDto updateActivityDto;

    @Mock
    private PreferenceRepository preferenceRepository;

    @Mock
    private UserProgressService userProgressService;

    @Mock
    private S3Client s3Client;

    @Mock
    private S3Properties s3Properties;

    @InjectMocks
    private UserService userService;
  //================================================================
  //                      Testes do FindDataUser
  //================================================================
    @Test
    void shouldReturnUserWhenUserExists() {

        // Arrange
        User loggedUser = new User();
        loggedUser.setId(1L);

        User userFound = new User();
        userFound.setId(1L);
        userFound.setName("Lucas");

        when(userRepository.findByIdWithAchievements(1L))
                .thenReturn(Optional.of(userFound));

        // Act
        User result = userService.findDataUser(loggedUser);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Lucas", result.getName());

        verify(userRepository).findByIdWithAchievements(1L);
    }
    @Test
    void shouldThrowExceptionWhenUserDoesNotExist() {

        // Arrange
        User loggedUser = new User();
        loggedUser.setId(1L);

        when(userRepository.findByIdWithAchievements(1L))
                .thenReturn(Optional.empty());

        // Act + Assert
        assertThrows(UserNotFoundError.class, () -> {
            userService.findDataUser(loggedUser);
        });

        verify(userRepository).findByIdWithAchievements(1L);
    }
    //================================================================
    //                      Testes do listPreferences
    //================================================================
    @Test
    void shouldReturnUserPreferences() {

        // Arrange
        User user = new User();
        user.setId(1L);

        Preferences p1 = new Preferences();
        p1.setId(1L);

        Preferences p2 = new Preferences();
        p2.setId(2L);

        List<Preferences> preferences = List.of(p1, p2);

        when(preferenceRepository.findByUserId(1L))
                .thenReturn(preferences);

        // Act
        List<Preferences> result = userService.listPreferences(user);

        // Assert
        assertEquals(2, result.size());
        assertEquals(preferences, result);

        verify(preferenceRepository).findByUserId(1L);
    }
    //========================================================
    //               Testes do updatePreferences
    //=======================================================
    @Test
    void shouldUpdatePreferences() {

        // Arrange
        User loggedUser = new User();
        loggedUser.setId(1L);

        Preferences p1 = new Preferences();
        p1.setId(1L);

        Preferences p2 = new Preferences();
        p2.setId(2L);

        List<Long> ids = List.of(1L, 2L);

        User userAfterUpdate = new User();
        userAfterUpdate.setId(1L);
        userAfterUpdate.setPreferences(List.of(p1, p2));

        when(preferenceRepository.findAllById(ids))
                .thenReturn(List.of(p1, p2));

        when(userRepository.findByIdWithAchievements(1L))
                .thenReturn(Optional.of(userAfterUpdate));

        // Act
        User result = userService.updatePreferences(loggedUser, ids);

        // Assert
        assertEquals(2, result.getPreferences().size());
        assertTrue(result.getPreferences().contains(p1));
        assertTrue(result.getPreferences().contains(p2));

        verify(preferenceRepository).findAllById(ids);
        verify(preferenceRepository).saveAll(anyList());
        verify(userRepository).findByIdWithAchievements(1L);
    }
        //============================================================
        //                    Testes do updateData
        //============================================================
    @Test
    void shouldUpdateDataUser() {
        // Arrange
        User loggedUser = new User();
        loggedUser.setId(1L);
        loggedUser.setName("Lucas");
        loggedUser.setPassword("123456");

        UpdateUserDto dto = new UpdateUserDto("Goku", "654321");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = userService.updateData(loggedUser, dto);

        // Assert
        assertEquals("Goku", result.getName());
        assertEquals("654321", result.getPassword());

        verify(userRepository).save(loggedUser);
    }
    @Test
    void shouldUpdateOnlyNameWhenPasswordIsBlank() {
        // Arrange
        User loggedUser = new User();
        loggedUser.setId(1L);
        loggedUser.setName("Lucas");
        loggedUser.setPassword("123456");

        UpdateUserDto dto = new UpdateUserDto("Goku", "");

        when(userRepository.save(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        User result = userService.updateData(loggedUser, dto);

        // Assert
        assertEquals("Goku", result.getName());
        assertEquals("123456", result.getPassword()); // continua a antiga

        verify(userRepository).save(loggedUser);
    }
    //==========================================================================
    //                    Testes do updateAvatar
    //=========================================================================
    @Test
    void shouldUpdateAvatar() throws IOException {
        // Arrange
        User user = new User();
        user.setId(1L);

        MultipartFile file = mock(MultipartFile.class);

        when(file.getOriginalFilename()).thenReturn("foto.png");
        when(file.getContentType()).thenReturn("image/png");
        when(file.getBytes()).thenReturn(new byte[]{1,2,3});

        when(s3Properties.getBucket()).thenReturn("bucket");
        when(s3Properties.getEndpoint()).thenReturn("http://localhost:4566");

        when(userRepository.findByIdWithAchievements(1L))
                .thenReturn(Optional.of(user));
        // Act
        User result = userService.updateAvatar(user, file);
        // Assert
        assertNotNull(result);

        verify(s3Client).putObject(
                any(PutObjectRequest.class),
                any(RequestBody.class)
        );

        verify(userRepository).save(user);

        verify(userProgressService)
                .grantAchievement(user, "Primeira Foto de Perfil");
    }
    @Test
    void shouldThrowWhenFileIsEmpty() {
        User user = new User();
        user.setId(1L);

        MultipartFile file = mock(MultipartFile.class);

        when(file.isEmpty()).thenReturn(true);

        assertThrows(
                PhotoError.class,
                () -> userService.updateAvatar(user, file)
        );
    }
    @Test
    void shouldThrowWhenExtensionIsInvalid() throws Exception {
        User user = new User();
        user.setId(1L);

        MultipartFile file = mock(MultipartFile.class);

        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn("foto.pdf");

        assertThrows(
                PhotoError.class,
                () -> userService.updateAvatar(user, file)
        );
    }
    @Test
    void shouldThrowWhenUploadFails() throws Exception {
        User user = new User();
        user.setId(1L);

        MultipartFile file = mock(MultipartFile.class);

        when(file.isEmpty()).thenReturn(false);
        when(file.getOriginalFilename()).thenReturn("foto.png");
        when(file.getContentType()).thenReturn("image/png");
        when(file.getBytes()).thenThrow(new IOException());

        assertThrows(
                PhotoError.class,
                () -> userService.updateAvatar(user, file)
        );
    }
    //=============================================================================
    //                     Testes desactivateAccount
    //============================================================================
    @Test
    void shouldDeactivateAccount() {

        // Arrange
        User loggedUser = new User();
        loggedUser.setId(1L);

        // Act
        userService.desactivateAccount(loggedUser);

        // Assert
        assertNotNull(loggedUser.getDeletedAt());

        verify(userRepository).save(loggedUser);
    }

}