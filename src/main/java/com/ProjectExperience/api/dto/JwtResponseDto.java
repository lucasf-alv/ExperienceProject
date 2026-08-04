package com.ProjectExperience.api.dto;

public record JwtResponseDto(
        String token,
        Long id,
        String name,
        String email
) {
}