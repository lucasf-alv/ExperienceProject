package com.ProjectExperience.api.service;

import com.ProjectExperience.api.config.S3Properties;
import com.ProjectExperience.api.dto.UpdateUserDto;
import com.ProjectExperience.api.exceptions.PhotoError;
import com.ProjectExperience.api.models.ActivityType;
import com.ProjectExperience.api.models.Preferences;
import com.ProjectExperience.api.models.User;
import com.ProjectExperience.api.repository.ActivityTypeRepository;
import com.ProjectExperience.api.repository.PreferenceRepository;
import com.ProjectExperience.api.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PreferenceRepository preferenceRepository;
    private final ActivityTypeRepository activityTypeRepository;
    private final S3Client s3Client;
    private final S3Properties s3Properties;

    // ==========================
    // BUSCAR USUÁRIO
    // ==========================

    public User findDataUser(User loggedUser) {
        return loggedUser;

    }

    // ==========================
    // PREFERÊNCIAS
    // ==========================

    public List<Preferences> listPreferences(User loggedUser) {

        return preferenceRepository.findByUserId(loggedUser.getId());
    }

    public User updatePreferences(User loggedUser,
                                  List<Long> preferenceIds) {

        List<Preferences> preferences =
                preferenceRepository.findAllById(preferenceIds);

        loggedUser.setPreferences(preferences);

        return userRepository.save(loggedUser);
    }

    // ==========================
    // DADOS DO USUÁRIO
    // ==========================

    public User updateData(User loggedUser,
                           UpdateUserDto dto) {

        loggedUser.setName(dto.name());
        loggedUser.setEmail(dto.email());

        if (dto.password() != null && !dto.password().isBlank()) {

            loggedUser.setPassword(
                    dto.password()
            );
        }

        return userRepository.save(loggedUser);
    }

    // ==========================
    // FOTO
    // ==========================

    public User updateAvatar(User loggedUser,
                             MultipartFile file) {

        uploadPhoto(loggedUser, file);

        return loggedUser;
    }

    private void uploadPhoto(User user,
                             MultipartFile file) {

        validateImage(file);

        try {

            String extension = getExtension(file.getOriginalFilename());

            String photoId =
                    "users/"
                            + user.getId()
                            + "/"
                            + UUID.randomUUID()
                            + extension;

            PutObjectRequest request =
                    PutObjectRequest.builder()
                            .bucket(s3Properties.getBucket())
                            .key(photoId)
                            .contentType(file.getContentType())
                            .build();

            s3Client.putObject(
                    request,
                    RequestBody.fromBytes(file.getBytes())
            );

            String url =
                    s3Properties.getEndpoint()
                            + "/"
                            + s3Properties.getBucket()
                            + "/"
                            + photoId;

            user.setAvatar(url);

            userRepository.save(user);

        } catch (IOException e) {

            throw new PhotoError(
                    "Falha ao enviar imagem."
            );
        }
    }

    // ==========================
    // DESATIVAR CONTA
    // ==========================

    public void desactivateAccount(User loggedUser) {

        loggedUser.setDeletedAt(LocalDateTime.now());

        userRepository.save(loggedUser);
    }

    // ==========================
    // AUXILIARES
    // ==========================

    private void validateImage(MultipartFile file) {

        if (file == null || file.isEmpty()) {

            throw new PhotoError("Arquivo inválido.");
        }

        String extension =
                getExtension(file.getOriginalFilename())
                        .toLowerCase();

        if (!extension.equals(".png")
                && !extension.equals(".jpg")
                && !extension.equals(".jpeg")) {

            throw new PhotoError(
                    "A imagem deve ser um arquivo PNG ou JPG."
            );
        }
    }

    private String getExtension(String fileName) {

        if (fileName == null || !fileName.contains(".")) {

            return "";
        }

        return fileName.substring(
                fileName.lastIndexOf(".")
        );
    }

    public User findUser(Long userId) {
        return userRepository.findById(userId).orElseThrow(()->new RuntimeException("Usuario não encontrado"));
    }
}