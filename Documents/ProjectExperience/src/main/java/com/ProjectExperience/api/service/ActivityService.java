package com.ProjectExperience.api.service;

import com.ProjectExperience.api.config.S3Properties;
import com.ProjectExperience.api.dto.CheckInDto;
import com.ProjectExperience.api.dto.UpdateActivityDto;
import com.ProjectExperience.api.exceptions.*;
import com.ProjectExperience.api.models.*;
import com.ProjectExperience.api.repository.ActivityParcipantsRepository;
import com.ProjectExperience.api.repository.ActivityRepository;
import com.ProjectExperience.api.repository.ActivityTypeRepository;
import com.ProjectExperience.api.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
public class ActivityService {

    private final ActivityRepository activityRepository;
    private final ActivityTypeRepository activityTypeRepository;
    private final ActivityParcipantsRepository activityParticipantsRepository;
    private final UserRepository userRepository;
    private final S3Client s3Client;
    private final S3Properties s3Properties;


    // ==========================
    // LISTAGENS
    // ==========================

    public List<ActivityType> listActivityTypes() {
        return activityTypeRepository.findAll();
    }

    public Page<Activity> listActivities(Pageable pageable) {
        return activityRepository.findAll(pageable);
    }
    public List<Activity> listAllActivities() {
        return activityRepository.findAll();
    }

    public Page<Activity> findActivitiesCreatedByUser(User loggedUser,
                                                      Pageable pageable) {

        return activityRepository.findByCreatorId(
                loggedUser.getId(),
                pageable
        );
    }

    public List<Activity> findAllActivitiesCreatedByUser(User loggedUser) {

        return activityRepository.findByCreatorId(
                loggedUser.getId()
        );
    }

    public Page<ActivityParticipants> findActivitiesParticipatingByUser(
            User loggedUser,
            Pageable pageable) {

        return activityParticipantsRepository.findByUserId(
                loggedUser.getId(),
                pageable
        );
    }

    public List<ActivityParticipants> findAllActivitiesParticipatingByUser(
            User loggedUser) {

        return activityParticipantsRepository.findByUserId(
                loggedUser.getId()
        );
    }

    public List<User> findAllUsersByActivityId(Long activityId) {

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() ->
                        new RuntimeException("Atividade não encontrada"));

        return activityParticipantsRepository
                .findByActivityId(activity.getId())
                .stream()
                .map(ActivityParticipants::getUser)
                .toList();
    }

    // ==========================
    // CRIAR
    // ==========================

    public Activity createActivity(
            UpdateActivityDto dto,
            User loggedUser,
            MultipartFile file
    ) {

        Activity activity = new Activity();

        ActivityType type = activityTypeRepository.findByName(dto.type())
                .orElseThrow(() ->
                        new RuntimeException("Tipo de atividade não encontrado"));


        ActivityAddress address = new ActivityAddress();
        address.setLatitude(dto.latitute());
        address.setLongitude(dto.longitude());
        address.setActivity(activity);

        activity.setTitle(dto.title());
        activity.setDescription(dto.description());
        activity.setScheduled_Date(dto.scheduleDate());
        activity.setActivityType(type);
        activity.setActivityAddress(address);

        activity.setCreator(loggedUser);
        activity.setCriated_At(LocalDateTime.now());

        activity.setPrivate(
                dto.Private() != null ? dto.Private() : false
        );

        activity.setConfirmation_code(
                UUID.randomUUID()
                        .toString()
                        .substring(0, 8)
                        .toUpperCase()
        );

        uploadPhoto(activity, file);
        return activityRepository.save(activity);


    }


    // ==========================
    // INSCRIÇÃO
    // ==========================

    public ActivityParticipants subscribeActivity(Long activityId,
                                                  User loggedUser) {

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() ->
                        new RuntimeException("Atividade não encontrada"));

        // Lança E7: Usuário já é participante da atividade
        ActivityParticipants participant = new ActivityParticipants();
        if (activityParticipantsRepository.existsByActivityIdAndUserId(
                activityId,
                loggedUser.getId())) {

            throw new ActivityRegisterError(
                    "Você já se registrou nessa atividade."
            );
        }
        // Lança E8: O criador não pode se inscrever como participante
        if (activity.getCreator().getId().equals(loggedUser.getId())) {
            throw new CreatorParticipantError(
                    "O criador da atividade não pode se inscrever como participante."
            );
        }
        // Lança E12: Não é possível se inscrever em uma atividade concluida
        if(activity.getCompleted_At() != null){
            throw new ActivityCompletedError("Não é possível se inscrever em uma atividade concluida");
        }

        participant.setActivity(activity);
        participant.setUser(loggedUser);
        participant.setApproved(false);

        return activityParticipantsRepository.save(participant);
    }


    // ==========================
    // ATUALIZAÇÃO
    // ==========================

    public Activity updateActivity(Long activityId,
                                   UpdateActivityDto dto,MultipartFile file,User loggedUser) {

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() ->
                        new RuntimeException("Atividade não encontrada"));

        ActivityType type = activityTypeRepository
                .findByName(dto.type())
                .orElseThrow(() ->
                        new RuntimeException("Tipo não encontrado"));
        // Lança E14: Apenas o criador da atividade pode edita-lá
        if (!activity.getCreator().getId().equals(loggedUser.getId())) {
            throw new EditActivityError(
                    "Apenas o criador da atividade pode edita-la"
            );
        }

        ActivityAddress address = activity.getActivityAddress();

        address.setLatitude(dto.latitute());
        address.setLongitude(dto.longitude());

        activity.setTitle(dto.title());
        activity.setDescription(dto.description());
        activity.setScheduled_Date(dto.scheduleDate());
        activity.setPrivate(dto.Private());
        activity.setActivityType(type);
        uploadPhoto(activity,file);

        return activityRepository.save(activity);
    }

    // ==========================
    // CONCLUIR
    // ==========================

    public Activity concludeActivity(Long activityId, User loggedUser) {

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() ->
                        new RuntimeException("Atividade não encontrada"));
        //Lança E17: Apenas o criador da atividade pode conclui-lá
        if(!activity.getCreator().getId().equals(loggedUser.getId())){
            throw new ConcludeActivityError("Apenas o criador da atividade pode concluir atividades");
        }

        activity.setCompleted_At(LocalDateTime.now());
        activity.setPrivate(Boolean.TRUE);

        return activityRepository.save(activity);
    }

    // ==========================
    // APROVAR PARTICIPANTE
    // ==========================

    public void approveParticipant(Long participantId,User loggedUser) {

        ActivityParticipants participant =
                activityParticipantsRepository.findById(participantId)
                        .orElseThrow(() ->
                                new RuntimeException("Participante não encontrado"));
        //Lança E16: Apenas o criador da atividade pode aprovar participantes
        if(!participant.getActivity().getCreator().getId().equals(loggedUser.getId())){
            throw new ApproveParticipantsError("Apenas o criador da atividade pode aprovar participantes");
        }

        participant.setApproved(true);

        activityParticipantsRepository.save(participant);
    }

    // ==========================
    // CHECK-IN
    // ==========================

    public void checkInActivity(Long activityId,
                                CheckInDto dto,
                                User loggedUser) {

        // Busca a atividade
        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() ->
                        new RuntimeException("Atividade não encontrada"));

        // Lança E13: Não é possível confirmar presença em uma atividade concluida //
        if (activity.getCompleted_At() != null) {
            throw new ConfirmationConcludeActivityError("Não é possível confirmar presença em uma atividade concluída.");
        }

        // Busca a inscrição do usuário nessa atividade
        ActivityParticipants participant =
                activityParticipantsRepository
                        .findByActivityIdAndUserId(activityId, loggedUser.getId())
                        .orElseThrow(() ->
                                new RuntimeException("Usuário não está inscrito na atividade"));

        // lança E9: Participante precisa ser aprovado //
        if (!participant.getApproved()) {
            throw new CheckInError(
                    "Apenas participantes aprovados na atividade podem fazer check-in.");
        }


        // lança  E10: Código incorreto //
        if (!activity.getConfirmation_code()
                .equals(dto.confirmationCode())) {

            throw new IncorrectConfirmationCodeError( "Código de confirmação incorreto.");
        }
        // lança E11: Confirmação duplicada na atividade//
        if(participant.getConfirmed_at() != null){
            throw new ConfirmationActivityError("Você já confirmou sua presença na atividade");
        }

        // Marca presença
        participant.setConfirmed_at(LocalDateTime.now());

        // Dá XP para o participante
        User participantUser = participant.getUser();
        participantUser.setXp(participantUser.getXp() + 50);

        // Dá XP para o criador
        User creator = activity.getCreator();
        creator.setXp(creator.getXp() + 20);

        // Salva tudo
        activityParticipantsRepository.save(participant);
        userRepository.save(participantUser);
        userRepository.save(creator);
    }
    //=========================
    // UNSUBSCRIBE
    //====================
    public void unsubscribeActivity(Long activityId, User loggedUser) {

        // Verifica se a atividade existe
        activityRepository.findById(activityId)
                .orElseThrow(() ->
                        new RuntimeException("Atividade não encontrada"));

        // Procura a inscrição do usuário
        ActivityParticipants participant =
                activityParticipantsRepository
                        .findByActivityIdAndUserId(activityId, loggedUser.getId())
                        .orElseThrow(() ->
                                new RuntimeException("Usuário não está inscrito nesta atividade"));

        // Lança E18: Não é possível confirmar cancelar sua inscrição pois sua presença já foi confirmada
        if (participant.getConfirmed_at() != null) {
            throw new CancelSubscribeError(
                    "Não é possível cancelar sua inscrição, pois sua presença já foi confirmada."
            );
        }

        // Remove a inscrição
        activityParticipantsRepository.delete(participant);
    }
    //=========================
    // REMOVE UMA ATIVIDADE
    //========================
    public void removeActivity(Long activityId, User loggedUser) {

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() ->
                        new RuntimeException("Atividade não encontrada"));

        // Lança E15: Apenas o criador da atividade pode exclui-lá
        if (!activity.getCreator().getId().equals(loggedUser.getId())) {
            throw new DeleteActivityError(
                    "Apenas o criador da atividade pode excluí-la.");
        }

        activity.setDeleted_At(LocalDateTime.now());

        activityRepository.save(activity);
    }
    //======================
    // VALIDAÇÃO DA FOTO
    // =====================
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
    // =========================
    //       FOTO
    //========================
    private void uploadPhoto(Activity activity,
                             MultipartFile file) {

        validateImage(file);

        try {

            String extension = getExtension(file.getOriginalFilename());

            String photoId =
                    "activities/"
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

            activity.setImage(url);

        } catch (IOException e) {

            throw new PhotoError("Falha ao enviar imagem.");
        }
    }

}