package com.ProjectExperience.api.web;

import com.ProjectExperience.api.dto.CheckInDto;
import com.ProjectExperience.api.dto.UpdateActivityDto;
import com.ProjectExperience.api.exceptions.ApiError;
import com.ProjectExperience.api.models.Activity;
import com.ProjectExperience.api.models.ActivityParticipants;
import com.ProjectExperience.api.models.ActivityType;
import com.ProjectExperience.api.models.User;
import com.ProjectExperience.api.security.AuthenticatedUser;
import com.ProjectExperience.api.service.ActivityService;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
            description = "Retorna todos os tipos de atividades disponíveis."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Tipos encontrados"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Autenticação necessária",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":401,
                                  "error":"Unauthorized",
                                  "message":"Autenticação necessária.",
                                  "path":"/activities/types"
                                }
                                """
                            )
                    )
            )
    })
    public ResponseEntity<List<ActivityType>> listActivityTypes(){
        return ResponseEntity.ok(activityService.listActivityTypes());
    }
    //=========================================================
    //                LISTA ATIVIDADES COM TABULAÇÃO
    //=======================================================
    @GetMapping
    @Operation(
            summary = "Listar atividades",
            description = "Retorna uma lista paginada de atividades."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Lista paginada retornada"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Autenticação necessária",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":401,
                                  "error":"Unauthorized",
                                  "message":"Autenticação necessária.",
                                  "path":"/activities"
                                }
                                """
                            )
                    )
            )
    })
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
            summary = "Listar todas as atividades",
            description = "Retorna todas as atividades sem paginação."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Atividades retornadas"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Autenticação necessária",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":401,
                                  "error":"Unauthorized",
                                  "message":"Autenticação necessária.",
                                  "path":"/activities/all"
                                }
                                """
                            )
                    )
            )
    })
    public ResponseEntity<List<Activity>> listAllActivities(){
        return ResponseEntity.ok(activityService.listAllActivities());
    }
    //============================================================
    //        LISTA ATIVIDADES CRIADAS PELO USUÁRIO COM TABULAÇÃO
    //============================================================
    @GetMapping("/user/creator")
    @Operation(
            summary = "Listar atividades criadas pelo usuário",
            description = "Retorna as atividades criadas pelo usuário autenticado com paginação."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Atividades encontradas"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Autenticação necessária",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":401,
                                  "error":"Unauthorized",
                                  "message":"Autenticação necessária.",
                                  "path":"/activities/user/creator"
                                }
                                """
                            )
                    )
            )
    })
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
            summary = "Listar todas as atividades criadas pelo usuário",
            description = "Retorna todas as atividades criadas pelo usuário autenticado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Atividades encontradas"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Autenticação necessária",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":401,
                                  "error":"Unauthorized",
                                  "message":"Autenticação necessária.",
                                  "path":"/activities/user/creator/all"
                                }
                                """
                            )
                    )
            )
    })
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
            summary = "Listar atividades em que o usuário participa",
            description = "Retorna as atividades em que o usuário autenticado está inscrito."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Participações encontradas"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Autenticação necessária",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":401,
                                  "error":"Unauthorized",
                                  "message":"Autenticação necessária.",
                                  "path":"/activities/user/participant"
                                }
                                """
                            )
                    )
            )
    })
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
            summary = "Listar todas as atividades em que o usuário participa",
            description = "Retorna todas as atividades em que o usuário autenticado está inscrito."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Atividades encontradas"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Autenticação necessária",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":401,
                                  "error":"Unauthorized",
                                  "message":"Autenticação necessária.",
                                  "path":"/activities/user/participant/all"
                                }
                                """
                            )
                    )
            )
    })

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
            description = "Retorna todos os usuários inscritos em uma atividade específica."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Participantes encontrados"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Autenticação necessária",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":401,
                                  "error":"Unauthorized",
                                  "message":"Autenticação necessária.",
                                  "path":"/activities/1/participants"
                                }
                                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Atividade não encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":404,
                                  "error":"Not Found",
                                  "message":"Atividade não encontrada.",
                                  "path":"/activities/1/participants"
                                }
                                """
                            )
                    )
            )
    })
    public ResponseEntity<List<ActivityParticipants>> findAllUsersByActivityId(
            @PathVariable Long id
    ){
        return ResponseEntity.ok(
                activityService.findAllUsersByActivityId(id)
        );
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
            description = "Cria uma nova atividade com imagem."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Atividade criada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Imagem inválida ou dados incorretos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":400,
                                  "error":"Bad Request",
                                  "message":"A imagem deve ser um arquivo PNG ou JPG.",
                                  "path":"/activities/new"
                                }
                                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Autenticação necessária",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":401,
                                  "error":"Unauthorized",
                                  "message":"Autenticação necessária.",
                                  "path":"/activities/new"
                                }
                                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Tipo de atividade não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":404,
                                  "error":"Not Found",
                                  "message":"Tipo de atividade não encontrado.",
                                  "path":"/activities/new"
                                }
                                """
                            )
                    )
            )
    })
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
            description = "Realiza a inscrição do usuário autenticado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário inscrito com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Usuário já inscrito ou criador da atividade",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":400,
                                  "error":"Bad Request",
                                  "message":"Você já está inscrito nesta atividade.",
                                  "path":"/activities/1/subscribe"
                                }
                                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Autenticação necessária",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":401,
                                  "error":"Unauthorized",
                                  "message":"Autenticação necessária.",
                                  "path":"/activities/1/subscribe"
                                }
                                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Atividade não encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":404,
                                  "error":"Not Found",
                                  "message":"Atividade não encontrada.",
                                  "path":"/activities/1/subscribe"
                                }
                                """
                            )
                    )
            )
    })
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
            description = "Atualiza os dados e a imagem de uma atividade."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Atividade atualizada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados ou imagem inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":400,
                                  "error":"Bad Request",
                                  "message":"A imagem deve ser um arquivo PNG ou JPG.",
                                  "path":"/activities/1/update"
                                }
                                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Autenticação necessária",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":401,
                                  "error":"Unauthorized",
                                  "message":"Autenticação necessária.",
                                  "path":"/activities/1/update"
                                }
                                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Somente o criador pode editar",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":403,
                                  "error":"Forbidden",
                                  "message":"Apenas o criador da atividade pode editá-la.",
                                  "path":"/activities/1/update"
                                }
                                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Atividade não encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":404,
                                  "error":"Not Found",
                                  "message":"Atividade não encontrada.",
                                  "path":"/activities/1/update"
                                }
                                """
                            )
                    )
            )
    })
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
            description = "Marca uma atividade como concluída."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Atividade concluída com sucesso"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Autenticação necessária",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":401,
                                  "error":"Unauthorized",
                                  "message":"Autenticação necessária.",
                                  "path":"/activities/1/conclude"
                                }
                                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Somente o criador pode concluir",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":403,
                                  "error":"Forbidden",
                                  "message":"Apenas o criador da atividade pode concluí-la.",
                                  "path":"/activities/1/conclude"
                                }
                                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Atividade não encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":404,
                                  "error":"Not Found",
                                  "message":"Atividade não encontrada.",
                                  "path":"/activities/1/conclude"
                                }
                                """
                            )
                    )
            )
    })
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
            description = "Aprova a participação de um usuário em uma atividade. Apenas o criador da atividade pode realizar esta ação."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Participante aprovado com sucesso."
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Autenticação necessária",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":401,
                                  "error":"Unauthorized",
                                  "message":"Autenticação necessária.",
                                  "path":"/activities/1/approve"
                                }
                                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Apenas o criador pode aprovar participantes",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":403,
                                  "error":"Forbidden",
                                  "message":"Apenas o criador da atividade pode aprovar participantes.",
                                  "path":"/activities/1/approve"
                                }
                                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Participante não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":404,
                                  "error":"Not Found",
                                  "message":"Participante não encontrado.",
                                  "path":"/activities/1/approve"
                                }
                                """
                            )
                    )
            )
    })
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
            description = "Realiza o check-in utilizando o código de confirmação da atividade."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Check-in realizado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Código inválido, participante não aprovado ou presença já confirmada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":400,
                                  "error":"Bad Request",
                                  "message":"Código de confirmação incorreto.",
                                  "path":"/activities/1/check-in"
                                }
                                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Autenticação necessária",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":401,
                                  "error":"Unauthorized",
                                  "message":"Autenticação necessária.",
                                  "path":"/activities/1/check-in"
                                }
                                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Atividade não encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":404,
                                  "error":"Not Found",
                                  "message":"Atividade não encontrada.",
                                  "path":"/activities/1/check-in"
                                }
                                """
                            )
                    )
            )
    })
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
            description = "Remove a inscrição do usuário autenticado em uma atividade."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Inscrição cancelada com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Não é possível cancelar após confirmação de presença",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":400,
                                  "error":"Bad Request",
                                  "message":"Não é possível cancelar sua inscrição pois sua presença já foi confirmada.",
                                  "path":"/activities/1/unsubscribe"
                                }
                                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Autenticação necessária",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":401,
                                  "error":"Unauthorized",
                                  "message":"Autenticação necessária.",
                                  "path":"/activities/1/unsubscribe"
                                }
                                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Atividade ou inscrição não encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":404,
                                  "error":"Not Found",
                                  "message":"Atividade ou inscrição não encontrada.",
                                  "path":"/activities/1/unsubscribe"
                                }
                                """
                            )
                    )
            )
    })
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
            description = "Remove uma atividade criada pelo usuário autenticado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Atividade removida com sucesso"
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Autenticação necessária",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":401,
                                  "error":"Unauthorized",
                                  "message":"Autenticação necessária.",
                                  "path":"/activities/1/delete"
                                }
                                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Somente o criador pode remover a atividade",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":403,
                                  "error":"Forbidden",
                                  "message":"Apenas o criador da atividade pode excluí-la.",
                                  "path":"/activities/1/delete"
                                }
                                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Atividade não encontrada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":404,
                                  "error":"Not Found",
                                  "message":"Atividade não encontrada.",
                                  "path":"/activities/1/delete"
                                }
                                """
                            )
                    )
            )
    })
    public ResponseEntity<Void> removeActivity(
            @PathVariable Long id,
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ){
        activityService.removeActivity(id,authenticatedUser.getUser());
        return ResponseEntity.noContent().build();
    }
    @GetMapping("/{id}/participants/count")
    @Operation(
            summary = "Contar participantes da atividade",
            description = "Retorna a quantidade de usuários inscritos em uma atividade."
    )
    public ResponseEntity<Long> countParticipants(
            @PathVariable Long id
    ){

        return ResponseEntity.ok(
                activityService.countParticipants(id)
        );
    }




}
