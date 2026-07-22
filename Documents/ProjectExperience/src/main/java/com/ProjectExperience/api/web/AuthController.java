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

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    // ==========================
    // CADASTRO
    // ==========================

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @Valid @RequestBody RegisterDto dto) {

        authService.register(dto);

        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    // ==========================
    // LOGIN
    // ==========================

    @PostMapping("/login")
    public ResponseEntity<JwtResponseDto> login(
            @Valid @RequestBody LoginDto dto) {

        return ResponseEntity.ok(
                authService.login(dto)
        );
    }
}