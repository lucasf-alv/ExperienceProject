package com.ProjectExperience.api.web;

import com.ProjectExperience.api.dto.JwtResponseDto;
import com.ProjectExperience.api.dto.LoginDto;
import com.ProjectExperience.api.dto.RegisterDto;
import com.ProjectExperience.api.exceptions.ApiError;
import com.ProjectExperience.api.security.AuthService;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
            description = "Cria uma nova conta de usuário."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Usuário cadastrado com sucesso"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Campos obrigatórios inválidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":400,
                                  "error":"Bad Request",
                                  "message":"Os campos obrigatórios não foram preenchidos corretamente.",
                                  "path":"/auth/register"
                                }
                                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Email ou CPF já cadastrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":409,
                                  "error":"Conflict",
                                  "message":"O email ou CPF informado já pertence a outro usuário.",
                                  "path":"/auth/register"
                                }
                                """
                            )
                    )
            )
    })
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
            description = "Autentica o usuário e retorna um token JWT."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Login realizado com sucesso"
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
                                  "path":"/auth/login"
                                }
                                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Senha incorreta",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":401,
                                  "error":"Unauthorized",
                                  "message":"Senha incorreta.",
                                  "path":"/auth/login"
                                }
                                """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "403",
                    description = "Conta desativada",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ApiError.class),
                            examples = @ExampleObject(
                                    value = """
                                {
                                  "timestamp":"2026-07-28T01:29:16.089",
                                  "status":403,
                                  "error":"Forbidden",
                                  "message":"Essa conta foi desativada.",
                                  "path":"/auth/login"
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
                                  "path":"/auth/login"
                                }
                                """
                            )
                    )
            )
    })
    public ResponseEntity<JwtResponseDto> login(
            @Valid @RequestBody LoginDto dto) {

        return ResponseEntity.ok(
                authService.login(dto)
        );
    }
}