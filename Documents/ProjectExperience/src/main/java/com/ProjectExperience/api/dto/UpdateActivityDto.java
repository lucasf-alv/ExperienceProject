package com.ProjectExperience.api.dto;

import com.ProjectExperience.api.models.ActivityType;

import java.time.LocalDateTime;

public record UpdateActivityDto(String title, String description , LocalDateTime scheduleDate, String type,  Boolean Private, Double latitute, Double longitude) {
}
