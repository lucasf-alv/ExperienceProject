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
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

@RestController
@RequestMapping("/activities")
@RequiredArgsConstructor
@Tag(
        name = "Atividades",
        description = "Gerenciamento de atividades, inscrições e check-ins"
)
public class ActivityController {
    private final ActivityService activityService;
    //=========================================================
    //                 LISTA TIPOS DE ATIVIDADE
    //========================================================
    @GetMapping("/types")
    @Operation(
            summary = "Listar tipos de atividade",
            description = "Retorna todos os tipos de atividades disponíveis"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Tipos encontrados"
    )
    public ResponseEntity<List<ActivityType>> listActivityTypes(){
        return ResponseEntity.ok(activityService.listActivityTypes());
    }
    //=========================================================
    //                LISTA ATIVIDADES COM TABULAÇÃO
    //=======================================================
    @GetMapping
    @Operation(
            summary = "Listar atividades",
            description = "Retorna atividades paginadas"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Lista paginada retornada"
    )
    public ResponseEntity<Page<Activity>> listActivities(Pageable pageable) {

        return ResponseEntity.ok(
                activityService.listActivities(pageable)
        );
    }
    //===========================================================
    //                 LISTA TODAS AS ATIVIDADES
    //==========================================================
    @GetMapping("/all")
    @Operation(
            summary = "Listar todas atividades",
            description = "Retorna todas as atividades sem paginação"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Atividades retornadas"
    )
    public ResponseEntity<List<Activity>> listAllActivities(){
        return ResponseEntity.ok(activityService.listAllActivities());
    }
    //============================================================
    //        LISTA ATIVIDADES CRIADAS PELO USUÁRIO COM TABULAÇÃO
    //============================================================
    @GetMapping("/user/creator")
    @Operation(
            summary = "Listar atividades criadas pelo usuário",
            description = "Retorna atividades criadas pelo usuário autenticado com paginação"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Atividades encontradas"
    )
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
    @Operation(
            summary = "Listar todas atividades criadas pelo usuário",
            description = "Retorna todas as atividades criadas pelo usuário autenticado"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Atividades encontradas"
    )
    public ResponseEntity<List<Activity>> findAllActivitiesCreatedByUser(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ){
        return ResponseEntity.ok(activityService.findAllActivitiesCreatedByUser(authenticatedUser.getUser()));
    }
    //==============================================================
    //            LISTA PARTICIPANTES DE UMA ATIVIDADE COM TABULAÇÃO
    //=============================================================
    @GetMapping("/user/participant")
    @Operation(
            summary = "Listar atividades participando",
            description = "Retorna atividades onde o usuário está inscrito"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Participações encontradas"
    )
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
    @GetMapping("/user/participant/all")
    @Operation(
            summary = "Listar todas atividades que o usuário participa",
            description = "Retorna todas as participações do usuário autenticado sem paginação"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Participações encontradas"
    )
    @ApiResponse(
            responseCode = "401",
            description = "Usuário não autenticado"
    )

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
    @Operation(
            summary = "Listar participantes da atividade",
            description = "Retorna todos os usuários inscritos em uma atividade específica"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Participantes encontrados"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Atividade não encontrada"
    )
    @ApiResponse(
            responseCode = "401",
            description = "Usuário não autenticado"
    )
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
    @Operation(
            summary = "Criar atividade",
            description = "Cria uma nova atividade com imagem"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Atividade criada"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Imagem inválida ou dados incorretos"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Tipo de atividade não encontrado"
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
    @Operation(
            summary = "Inscrever usuário na atividade",
            description = "Realiza a inscrição do usuário autenticado"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Usuário inscrito"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Usuário já inscrito ou criador da atividade"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Atividade não encontrada"
    )
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
            value = "/{activityId}/update",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(
            summary = "Atualizar atividade",
            description = "Atualiza dados e imagem de uma atividade"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Atividade atualizada"
    )
    @ApiResponse(
            responseCode = "403",
            description = "Somente o criador pode editar"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Atividade não encontrada"
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
                file,
                authenticatedUser.getUser()
        );

        return ResponseEntity.ok(activity);
    }
    //==============================================================
    //                    CONCLUI UMA ATIVIDADE
    //============================================================
    @PutMapping("/{id}/conclude")
    @Operation(
            summary = "Concluir atividade",
            description = "Marca uma atividade como concluída"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Atividade concluída"
    )
    @ApiResponse(
            responseCode = "403",
            description = "Somente o criador pode concluir"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Atividade não encontrada"
    )
    public ResponseEntity<Activity>  concludeActivity(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ){
        return ResponseEntity.ok(activityService.concludeActivity(id,authenticatedUser.getUser()));
    }
    //===============================================================
    //                APROVA UMA ATIVIDADE
    //===============================================================
    @PutMapping("/{participantId}/approve")
    @Operation(
            summary = "Aprovar participante",
            description = "Aprova um usuário para participar da atividade"
    )
    @ApiResponse(
            responseCode = "204",
            description = "Participante aprovado"
    )
    @ApiResponse(
            responseCode = "403",
            description = "Somente o criador pode aprovar"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Participante não encontrado"
    )
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
    @Operation(
            summary = "Confirmar presença",
            description = "Realiza check-in usando código de confirmação"
    )
    @ApiResponse(
            responseCode = "204",
            description = "Check-in realizado"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Código inválido, participante não aprovado ou presença já confirmada"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Atividade não encontrada"
    )
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
    @Operation(
            summary = "Cancelar inscrição",
            description = "Remove o usuário da atividade"
    )
    @ApiResponse(
            responseCode = "204",
            description = "Inscrição removida"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Presença já confirmada"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Atividade ou inscrição não encontrada"
    )
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
    @Operation(
            summary = "Remover atividade",
            description = "Desativa uma atividade criada pelo usuário"
    )
    @ApiResponse(
            responseCode = "204",
            description = "Atividade removida"
    )
    @ApiResponse(
            responseCode = "403",
            description = "Somente o criador pode remover"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Atividade não encontrada"
    )
    public ResponseEntity<Void> removeActivity(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ){
        activityService.removeActivity(id,authenticatedUser.getUser());
        return ResponseEntity.noContent().build();
    }




}
