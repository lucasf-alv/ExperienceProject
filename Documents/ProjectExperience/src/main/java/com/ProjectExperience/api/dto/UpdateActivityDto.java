package com.ProjectExperience.api.dto;

import com.ProjectExperience.api.models.ActivityType;

import java.time.LocalDateTime;

public record UpdateActivityDto(String title, String description , LocalDateTime scheduleDate, String type, String address, Boolean Private, String image, Double latitute, Double longitude) {
}
