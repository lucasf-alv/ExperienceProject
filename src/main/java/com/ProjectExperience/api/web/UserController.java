package com.ProjectExperience.api.web;

import com.ProjectExperience.api.dto.UpdateUserDto;
import com.ProjectExperience.api.exceptions.ApiError;
import com.ProjectExperience.api.models.Preferences;
import com.ProjectExperience.api.models.User;
import com.ProjectExperience.api.security.AuthenticatedUser;
import com.ProjectExperience.api.service.UserService;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
@Tag(
        name = "Usuário",
        description = "Endpoints para gerenciamento dos dados do usuário"
)
public class UserController {

    private final UserService userService;
//=============================================================================
//                          PEGAR DADOS DO USUÁRIO
//=============================================================================

    @GetMapping
    @Operation(
            summary = "Buscar dados do usuário autenticado",
            description = "Retorna os dados do usuário autenticado através do token JWT."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário encontrado"
            ),

            // COLOCA AQUI
            @ApiResponse(
                    responseCode = "401",
                    description = "Autenticação necessária",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16",
                                  "status":401,
                                  "error":"Unauthorized",
                                  "message":"Autenticação necessária.",
                                  "path":"/user"
                                }
                                """
                            )
                    )
            ),

            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16",
                                  "status":404,
                                  "error":"Not Found",
                                  "message":"Usuário não encontrado.",
                                  "path":"/user"
                                }
                                """
                            )
                    )
            )
    })
    public ResponseEntity<User> findDataUser(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {


        return ResponseEntity.ok(
                userService.findDataUser(authenticatedUser.getUser())
        );
    }
//=============================================================================
//                          LISTAR PREFERENCIAS
//============================================================================
    @GetMapping("/preferences")
    @Operation(
            summary = "Listar preferências",
            description = "Retorna as preferências do usuário autenticado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Preferências encontradas"
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
                                  "path":"/user/preferences"
                                }
                                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":404,
                                  "error":"Not Found",
                                  "message":"Usuário não encontrado.",
                                  "path":"/user/preferences"
                                }
                                """
                            )
                    )
            )
    })
    public ResponseEntity<List<Preferences>> listPreferences(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        return ResponseEntity.ok(
                userService.listPreferences(authenticatedUser.getUser())
        );
    }
//===============================================================================
//                                 AUTALIZAR PREFERENCIAS
//==============================================================================
    @PostMapping("/preferences/define")
    @Operation(
            summary = "Atualizar preferências",
            description = "Atualiza as preferências do usuário autenticado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Preferências atualizadas"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Lista de preferências inválida",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":400,
                                  "error":"Bad Request",
                                  "message":"Lista de preferências inválida.",
                                  "path":"/user/preferences/define"
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
                                  "path":"/user/preferences/define"
                                }
                                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":404,
                                  "error":"Not Found",
                                  "message":"Usuário não encontrado.",
                                  "path":"/user/preferences/define"
                                }
                                """
                            )
                    )
            )
    })
    public ResponseEntity<User> updatePreferences(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @RequestBody List<Long> preferencesIds) {

        return ResponseEntity.ok(
                userService.updatePreferences(
                        authenticatedUser.getUser(),
                        preferencesIds
                )
        );
    }
    //=================================================================================
    //                             ATUALIZAR FOTO
    //=================================================================================
    @PutMapping(
            value = "/avatar",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Atualizar avatar",
            description = "Envia uma imagem PNG ou JPG para atualizar a foto de perfil do usuário autenticado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Usuário atualizado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                            {
                              "timestamp": "2026-07-28T01:29:16.089",
                              "status": 400,
                              "error": "Bad Request",
                              "message": "O email informado já está em uso.",
                              "path": "/user/update"
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
                            examples = @ExampleObject(
                                    value = """
                            {
                              "timestamp": "2026-07-28T01:29:16.089",
                              "status": 401,
                              "error": "Unauthorized",
                              "message": "Autenticação necessária.",
                              "path": "/user/update"
                            }
                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                            {
                              "timestamp": "2026-07-28T01:29:16.089",
                              "status": 404,
                              "error": "Not Found",
                              "message": "Usuário não encontrado.",
                              "path": "/user/update"
                            }
                            """
                            )
                    )
            )
    })
    public ResponseEntity<User> updateAvatar(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @RequestParam("file") MultipartFile file) {

        return ResponseEntity.ok(
                userService.updateAvatar(
                        authenticatedUser.getUser(),
                        file
                )
        );
    }
    //=================================================================================
    //                              ATUALIZAR DADOS
    //===============================================================================
    @PutMapping("/update")
    @Operation(
            summary = "Atualizar dados",
            description = "Atualiza os dados do usuário autenticado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Dados atualizados com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dados inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":400,
                                  "error":"Bad Request",
                                  "message":"Os dados informados são inválidos.",
                                  "path":"/user/update"
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
                                  "path":"/user/update"
                                }
                                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":404,
                                  "error":"Not Found",
                                  "message":"Usuário não encontrado.",
                                  "path":"/user/update"
                                }
                                """
                            )
                    )
            )
    })
    public ResponseEntity<User> updateData(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser,
            @RequestBody UpdateUserDto dto
            ){
        User user= authenticatedUser.getUser();
        return ResponseEntity.ok(userService.updateData(user,dto));

    }
    //===================================================================================
    //                               DESATIVAR CONTA
    //==================================================================================
    @DeleteMapping("/desactivate")
    @Operation(
            summary = "Desativar conta",
            description = "Desativa a conta do usuário autenticado."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "204",
                    description = "Conta desativada com sucesso"
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
                                  "path":"/user/desactivate"
                                }
                                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Usuário não encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":404,
                                  "error":"Not Found",
                                  "message":"Usuário não encontrado.",
                                  "path":"/user/desactivate"
                                }
                                """
                            )
                    )
            )
    })
    public ResponseEntity<Void> desactivateAccount(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ){
        User user = authenticatedUser.getUser();
        userService.desactivateAccount(user);
        return ResponseEntity.noContent().build();
    }

}









