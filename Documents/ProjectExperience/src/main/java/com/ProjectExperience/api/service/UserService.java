package com.ProjectExperience.api.service;

import com.ProjectExperience.api.config.S3Properties;
import com.ProjectExperience.api.dto.UpdateUserDto;
import com.ProjectExperience.api.exceptions.PhotoError;
import com.ProjectExperience.api.exceptions.UserNotFound;
import com.ProjectExperience.api.models.Preferences;
import com.ProjectExperience.api.models.User;
import com.ProjectExperience.api.repository.PreferenceRepository;
import com.ProjectExperience.api.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.List;
import java.util.UUID;

@Service
@AllArgsConstructor
public class UserService{
    private final  UserRepository userRepository;
    private final PreferenceRepository preferenceRepository;
    private final  S3Client s3Client;
    private final S3Properties s3Properties;


    // Buscar usuário //
    public User findUser(Long id){
        return userRepository.findById(id).orElseThrow(()-> new UserNotFound("User not found"));

    }
    // Listar preferencias //
    public List<Preferences> listPreferences(Long id){
        User user_find = findUser(id);
        return user_find.getPreferences();

    }
    // Adicionar preferencias//
    public User updatePreferences(Long userId, List<Long> preferencesIds) {

        User user = findUser(userId);

        List<Preferences> preferences =
                preferenceRepository.findAllById(preferencesIds);

        user.setPreferences(preferences);

        return userRepository.save(user);
    }
    // Atualizar dados do usuário//
    public User updateDataUser(Long id, UpdateUserDto dto){
        User user = findUser(id);
        user.setName((dto.name()));
        user.setEmail(dto.email());
        return userRepository.save(user);

    }
    // Atualizar foto do usuário //
    private void uploadPhotoUser(User user, MultipartFile file) {

        if (file == null || file.isEmpty()) {
            throw new PhotoError("Arquivo inválido.");
        }
        String extension_ = getExtension(file.getOriginalFilename()).toLowerCase();

        if (!extension_.equals(".png")
                && !extension_.equals(".jpg")
                && !extension_.equals(".jpeg")) {

            throw new PhotoError("A foto deve ser PNG ou JPG.");
        }

        try {

            String extension = getExtension(file.getOriginalFilename());

            String photoId =
                    "users/"
                            + user.getId()
                            + "/"
                            + UUID.randomUUID()
                            + extension;

            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(s3Properties.getBucket())
                    .key(photoId)
                    .contentType(file.getContentType())
                    .build();

            s3Client.putObject(
                    request,
                    RequestBody.fromBytes(file.getBytes())
            );

            String photoUrl =
                    s3Properties.getEndpoint()
                            + "/"
                            + s3Properties.getBucket()
                            + "/"
                            + photoId;

            user.setAvatar(photoUrl);

            userRepository.save(user);

        } catch (IOException e) {
            throw new PhotoError("Falha ao enviar foto: " + e.getMessage());
        }
    }
    public User updateAvatar(Long id, MultipartFile file){

        User user = findUser(id);

        uploadPhotoUser(user, file);

        return user;
    }


    private String getExtension(String fileName){

        if(fileName == null || !fileName.contains(".")){
            return "";
        }

        return fileName.substring(
                fileName.lastIndexOf(".")
        );
    }
    // Deletar usuário //
    public  void deleteUser(Long id){
        userRepository.deleteById(id);
    }
}



