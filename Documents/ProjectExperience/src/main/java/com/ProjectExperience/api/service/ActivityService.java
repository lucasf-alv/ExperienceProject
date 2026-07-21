package com.ProjectExperience.api.service;

import com.ProjectExperience.api.dto.CheckInDto;
import com.ProjectExperience.api.dto.UpdateActivityDto;
import com.ProjectExperience.api.exceptions.UserNotFound;
import com.ProjectExperience.api.models.*;
import com.ProjectExperience.api.repository.ActivityParcipantsRepository;
import com.ProjectExperience.api.repository.ActivityRepository;
import com.ProjectExperience.api.repository.ActivityTypeRepository;
import com.ProjectExperience.api.repository.UserRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

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

    // ==========================
    // LISTAGENS
    // ==========================

    public List<ActivityType> listActivityTypes() {
        return activityTypeRepository.findAll();
    }

    public Page<Activity> listActivities(Pageable pageable) {
        return activityRepository.findAll(pageable);
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

    // ==========================
    // PARTICIPANTES
    // ==========================

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

    public void createActivity(Activity activity, User loggedUser) {

        activity.setCreator(loggedUser);
        activity.setCriated_At(LocalDateTime.now());
        activity.setPrivate(false);
        activity.setConfirmation_code(UUID.randomUUID().toString().substring(0,8));
    }

    // ==========================
    // INSCRIÇÃO
    // ==========================

    public ActivityParticipants subscribeActivity(Long activityId,
                                                  User loggedUser) {

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() ->
                        new RuntimeException("Atividade não encontrada"));

        ActivityParticipants participant = new ActivityParticipants();

        participant.setActivity(activity);
        participant.setUser(loggedUser);

        return activityParticipantsRepository.save(participant);
    }


    // ==========================
    // ATUALIZAÇÃO
    // ==========================

    public Activity updateActivity(Long activityId,
                                   UpdateActivityDto dto) {

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() ->
                        new RuntimeException("Atividade não encontrada"));

        ActivityType type = activityTypeRepository
                .findByName(dto.type())
                .orElseThrow(() ->
                        new RuntimeException("Tipo não encontrado"));

        ActivityAddress address = activity.getActivityAddress();

        address.setLatitude(dto.latitute());
        address.setLongitude(dto.longitude());

        activity.setTitle(dto.title());
        activity.setDescription(dto.description());
        activity.setScheduled_Date(dto.scheduleDate());
        activity.setPrivate(dto.Private());
        activity.setImage(dto.image());
        activity.setActivityType(type);

        return activityRepository.save(activity);
    }

    // ==========================
    // CONCLUIR
    // ==========================

    public Activity concludeActivity(Long activityId) {

        Activity activity = activityRepository.findById(activityId)
                .orElseThrow(() ->
                        new RuntimeException("Atividade não encontrada"));

        activity.setCompleted_At(LocalDateTime.now());
        activity.setPrivate(Boolean.TRUE);

        return activityRepository.save(activity);
    }

    // ==========================
    // APROVAR PARTICIPANTE
    // ==========================

    public void approveParticipant(Long participantId) {

        ActivityParticipants participant =
                activityParticipantsRepository.findById(participantId)
                        .orElseThrow(() ->
                                new RuntimeException("Participante não encontrado"));

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

        // E13 - Não pode fazer check-in em atividade concluída
        if (activity.getCompleted_At() != null) {
            throw new RuntimeException("Não é possível confirmar presença em uma atividade concluída.");
        }

        // Busca a inscrição do usuário nessa atividade
        ActivityParticipants participant =
                activityParticipantsRepository
                        .findByActivityIdAndUserId(activityId, loggedUser.getId())
                        .orElseThrow(() ->
                                new RuntimeException("Usuário não está inscrito na atividade"));

        // E9 - Participante precisa estar aprovado
        if (!participant.getApproved()) {
            throw new RuntimeException(
                    "Apenas participantes aprovados na atividade podem fazer check-in.");
        }


        // E10 - Código incorreto
        if (!activity.getConfirmation_code()
                .equals(dto.confirmationCode())) {

            throw new RuntimeException("Código de confirmação incorreto.");
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

        // E18
        if (participant.getConfirmed_at() != null) {
            throw new RuntimeException(
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

        // E15
        if (!activity.getCreator().getId().equals(loggedUser.getId())) {
            throw new RuntimeException(
                    "Apenas o criador da atividade pode excluí-la.");
        }

        activity.setDeleted_At(LocalDateTime.now());

        activityRepository.save(activity);
    }
}