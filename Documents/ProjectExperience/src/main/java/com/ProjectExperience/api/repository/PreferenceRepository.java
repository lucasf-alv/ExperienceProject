package com.ProjectExperience.api.repository;

import com.ProjectExperience.api.models.Preferences;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PreferenceRepository extends JpaRepository<Preferences,Long> {
}
