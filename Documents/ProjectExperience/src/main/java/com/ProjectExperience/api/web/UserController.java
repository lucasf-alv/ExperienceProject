package com.ProjectExperience.api.web;

import com.ProjectExperience.api.dto.UpdateUserDto;
import com.ProjectExperience.api.models.Preferences;
import com.ProjectExperience.api.models.User;
import com.ProjectExperience.api.security.AuthenticatedUser;
import com.ProjectExperience.api.service.UserService;
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
            description = "Retorna os dados do usuário logado através do token JWT"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Usuário encontrado"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Usuário não encontrado"
    )
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
            description = "Retorna as preferências associadas ao usuário autenticado"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Preferências encontradas"
    )
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
            description = "Define as preferências selecionadas pelo usuário"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Preferências atualizadas"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Usuário não encontrado"
    )
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
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    @Operation(
            summary = "Atualizar avatar",
            description = "Envia uma imagem PNG ou JPG para o perfil do usuário"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Foto atualizada"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Arquivo inválido"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Usuário não encontrado"
    )
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
    @ApiResponse(
            responseCode = "200",
            description = "Dados atualizados"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Usuário não encontrado"
    )
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
            description = "Marca a conta do usuário como desativada"
    )
    @ApiResponse(
            responseCode = "204",
            description = "Conta desativada com sucesso"
    )
    public ResponseEntity<Void> desactivateAccount(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ){
        User user = authenticatedUser.getUser();
        userService.desactivateAccount(user);
        return ResponseEntity.noContent().build();
    }

}









