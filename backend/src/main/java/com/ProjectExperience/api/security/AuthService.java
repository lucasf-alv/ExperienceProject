package com.ProjectExperience.api.security;

import com.ProjectExperience.api.config.DefaultProperties;
import com.ProjectExperience.api.dto.JwtResponseDto;
import com.ProjectExperience.api.dto.LoginDto;
import com.ProjectExperience.api.dto.RegisterDto;
import com.ProjectExperience.api.exceptions.*;
import com.ProjectExperience.api.models.User;
import com.ProjectExperience.api.repository.UserRepository;
import com.ProjectExperience.api.security.AuthenticatedUser;
import com.ProjectExperience.api.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final DefaultProperties defaultProperties;
    private final JwtService jwtService;

    // ==========================
    // CADASTRO
    // ==========================

    public void register(RegisterDto dto) {

        // Lança E1: informe os campos obrigatórios corretamente //
        if (dto.name() == null || dto.name().isBlank() ||
                dto.email() == null || dto.email().isBlank() ||
                dto.cpf() == null || dto.cpf().isBlank() ||
                dto.password() == null || dto.password().isBlank()) {

            throw new IncorrectFieldsError("Informe os campos obrigatórios corretamente.");
        }

        // Lança E3 : Email ou Cpf já pertence a outro usuário //
        if (userRepository.existsByEmail(dto.email())
                || userRepository.existsByCpf(dto.cpf())) {

            throw new EmailCpfError("Email ou cpf informado já pertence a outro usuário");

        }

        User user = User.builder()
                .name(dto.name())
                .email(dto.email())
                .cpf(dto.cpf())
                .avatar(defaultProperties.getAvatar())
                .password(passwordEncoder.encode(dto.password()))
                .level(1)
                .xp(0)
                .build();

        userRepository.save(user);
    }

    // ==========================
    // LOGIN
    // ==========================

    public JwtResponseDto login(LoginDto dto) {
        // Lança E5: Senha Incorreta  //
        try {

            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            dto.email(),
                            dto.password()
                    )
            );

        } catch (BadCredentialsException e) {
            throw new IncorrectPasswordError("Senha incorreta.");
        }
        // Lança E4: Usuário não encontrado //
        User user = userRepository.findByEmail(dto.email())
                .orElseThrow(() ->
                        new UserNotFoundError("Usuário não encontrado."));

        String token = jwtService.generateToken(
                new AuthenticatedUser(user)
        );
        // Lança E6: Essa conta foi desativada e não pode ser Usada//
        if(user.getDeletedAt() != null){
            throw new DesactivateAccountError("Essa conta foi desativada e não pode ser usada");
        }

        return new JwtResponseDto(
                token,
                user.getId(),
                user.getName(),
                user.getEmail()
        );
    }

}