package com.ProjectExperience.api.web;

import com.ProjectExperience.api.dto.JwtResponseDto;
import com.ProjectExperience.api.dto.LoginDto;
import com.ProjectExperience.api.dto.RegisterDto;
import com.ProjectExperience.api.security.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(
        name = "Autenticação",
        description = "Cadastro e login de usuários"
)
public class AuthController {

    private final AuthService authService;

    // ==========================
    // CADASTRO
    // ==========================

    @PostMapping("/register")
    @Operation(
            summary = "Cadastrar usuário",
            description = "Cria uma nova conta de usuário"
    )
    @ApiResponse(
            responseCode = "201",
            description = "Usuário cadastrado com sucesso"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Campos obrigatórios inválidos"
    )
    @ApiResponse(
            responseCode = "409",
            description = "Email ou CPF já cadastrado"
    )
    public ResponseEntity<Void> register(
            @Valid @RequestBody RegisterDto dto) {

        authService.register(dto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // ==========================
    // LOGIN
    // ==========================

    @PostMapping("/login")
    @Operation(
            summary = "Realizar login",
            description = "Autentica o usuário e retorna um token JWT"
    )
    @ApiResponse(
            responseCode = "200",
            description = "Login realizado com sucesso"
    )
    @ApiResponse(
            responseCode = "401",
            description = "Senha incorreta"
    )
    @ApiResponse(
            responseCode = "404",
            description = "Usuário não encontrado"
    )
    @ApiResponse(
            responseCode = "403",
            description = "Conta desativada"
    )
    @ApiResponse(
            responseCode = "400",
            description = "Dados inválidos"
    )
    public ResponseEntity<JwtResponseDto> login(
            @Valid @RequestBody LoginDto dto) {

        return ResponseEntity.ok(
                authService.login(dto)
        );
    }
}