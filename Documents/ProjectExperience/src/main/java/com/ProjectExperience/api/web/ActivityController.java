package com.ProjectExperience.api.web;

import com.ProjectExperience.api.dto.CheckInDto;
import com.ProjectExperience.api.dto.UpdateActivityDto;
import com.ProjectExperience.api.models.Activity;
import com.ProjectExperience.api.models.ActivityParticipants;
import com.ProjectExperience.api.models.ActivityType;
import com.ProjectExperience.api.models.User;
import com.ProjectExperience.api.security.AuthenticatedUser;
import com.ProjectExperience.api.service.ActivityService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/activities")
@RequiredArgsConstructor
public class ActivityController {
    private final ActivityService activityService;
    //=========================================================
    //                 LISTA TIPOS DE ATIVIDADE
    //========================================================
    @GetMapping("/types")
    public ResponseEntity<List<ActivityType>> listActivityTypes(){
        return ResponseEntity.ok(activityService.listActivityTypes());
    }
    //=========================================================
    //                LISTA ATIVIDADES COM TABULAÇÃO
    //=======================================================
    @GetMapping("/activities")
    public ResponseEntity<Page<Activity>> listActivities(Pageable pageable) {

        return ResponseEntity.ok(
                activityService.listActivities(pageable)
        );
    }
    //===========================================================
    //                 LISTA TODAS AS ATIVIDADES
    //==========================================================
    @GetMapping("/all")
    public ResponseEntity<List<Activity>> listAllActivities(){
        return ResponseEntity.ok(activityService.listAllActivities());
    }
    //============================================================
    //        LISTA ATIVIDADES CRIADAS PELO USUÁRIO COM TABULAÇÃO
    //============================================================
    @GetMapping("/user/creator")
    public ResponseEntity<Page<Activity>> findActivitiesCreatedByUser(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            Pageable pageable
            ){
        User user = authenticatedUser.getUser();
        return ResponseEntity.ok(activityService.findActivitiesCreatedByUser(user,pageable));

    }
    //=============================================================
    //              LISTA TODAS AS ATIVIDADES
    //============================================================
    @GetMapping("/user/creator/all")
    public ResponseEntity<List<Activity>> findAllActivitiesCreatedByUser(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ){
        return ResponseEntity.ok(activityService.findAllActivitiesCreatedByUser(authenticatedUser.getUser()));
    }
    //==============================================================
    //            LISTA PARTICIPANTES DE UMA ATIVIDADE COM TABULAÇÃO
    //=============================================================
    @GetMapping("/user/participants")
    public ResponseEntity<Page<ActivityParticipants>> findActivityParticipantsByUser(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            Pageable pageable
    ){
        return ResponseEntity.ok(activityService.findActivitiesParticipatingByUser(
                authenticatedUser.getUser(),pageable
        ));
    }
    //=================================================================
    //              LISTA TODOS OS PARTICIPANTES DE UMA ATIVIDADE
    //=================================================================
    @GetMapping("/user/participants/all")
    public ResponseEntity<List<ActivityParticipants>> findAllActitivityPartipantsByUser(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ){
        return ResponseEntity.ok(activityService.findAllActivitiesParticipatingByUser(
                authenticatedUser.getUser()
        ));

    }
    //=============================================================
    //          LISTA TODOS OS USUARIOS PELO ID DE UMA ATIVIDADE
    //===============================================================
    @GetMapping("/{id}/participants")
    public ResponseEntity<List<User>> findAllUsersByActivityId(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(activityService.findAllUsersByActivityId(id));
    }
    //======================================================================
    //                         CRIA UMA ATIVIDADE
    //=====================================================================
    @PostMapping(
            value = "/new",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Void> createActivity(
            @ModelAttribute UpdateActivityDto dto,
            @RequestPart("image") MultipartFile file,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {

        activityService.createActivity(
                dto,
                authenticatedUser.getUser(),
                file
        );

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
    //===================================================================
    //                  INSCREVE O USUÁRIO EM UMA ATIVIDADE
    //==================================================================
    @PostMapping("/{id}/subscribe")
    public ResponseEntity<ActivityParticipants> subscribeActivity(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @PathVariable Long id
    ){
        return ResponseEntity.ok(activityService.subscribeActivity(id,authenticatedUser.getUser()));
    }
    //==========================================================================
    //                ATUALIZA OS DADOS DE UMA ATIVIDADE
    //=======================================================================
    @PutMapping(
            value = "/{activityId}",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ResponseEntity<Activity> updateActivity(
            @PathVariable Long activityId,
            @ModelAttribute UpdateActivityDto dto,
            @RequestPart("image") MultipartFile file,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {

        Activity activity = activityService.updateActivity(
                activityId,
                dto,
                file
        );

        return ResponseEntity.ok(activity);
    }
    //==============================================================
    //                    CONCLUI UMA ATIVIDADE
    //============================================================
    @PutMapping("/{id}/conclude")
    public ResponseEntity<Activity>  concludeActivity(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ){
        return ResponseEntity.ok(activityService.concludeActivity(id,authenticatedUser.getUser()));
    }
    //===============================================================
    //                APROVA UMA ATIVIDADE
    //===============================================================
    @PutMapping("/participants/{participantId}/approve")
    public ResponseEntity<Void> approveParticipant(
            @PathVariable Long participantId,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ) {

        activityService.approveParticipant(
                participantId,
                authenticatedUser.getUser()
        );

        return ResponseEntity.noContent().build();
    }
    //================================================================
    //                   FAZ CHECK-IN EM UMA ATIVIDADE
    //===============================================================
    @PutMapping("/{id}/check-in")
    public ResponseEntity<Void> checkInActivity(
            @PathVariable Long id,
            @RequestBody CheckInDto dto,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
            ){
        activityService.checkInActivity(id,dto,authenticatedUser.getUser());
        return ResponseEntity.noContent().build();

    }
    //==========================================================================
    //                     SE DESINSCREVE DE UMA ATIVIDADE
    //=========================================================================
    @DeleteMapping("{id}/unsubscribe")
    public ResponseEntity<Void> unsubscribeActivity(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ){
        activityService.unsubscribeActivity(id,authenticatedUser.getUser());
        return ResponseEntity.noContent().build();
    }
    //==========================================================================
    //                      REMOVE UMA ATIVIDADE
    //==========================================================================
    @DeleteMapping("{id}/delete")
    public ResponseEntity<Void> removeActivity(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ){
        activityService.removeActivity(id,authenticatedUser.getUser());
        return ResponseEntity.noContent().build();
    }




}
