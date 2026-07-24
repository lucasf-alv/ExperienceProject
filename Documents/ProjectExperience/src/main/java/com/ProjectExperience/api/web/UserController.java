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

import java.util.List;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
//=============================================================================
//                          PEGAR DADOS DO USUÁRIO
//=============================================================================

    @GetMapping
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
    public ResponseEntity<List<Preferences>> listPreferences(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser) {
        System.out.println("ENTROU NO CONTROLLER");
        return ResponseEntity.ok(
                userService.listPreferences(authenticatedUser.getUser())
        );
    }
//===============================================================================
//                                 AUTALIZAR PREFERENCIAS
//==============================================================================
    @PostMapping("/preferences/define")
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
    public ResponseEntity<Void> desactivateAccount(
            @AuthenticationPrincipal AuthenticatedUser authenticatedUser
    ){
        User user = authenticatedUser.getUser();
        userService.desactivateAccount(user);
        return ResponseEntity.noContent().build();
    }

}









