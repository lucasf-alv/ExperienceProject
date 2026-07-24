package com.ProjectExperience.api.repository;

import com.ProjectExperience.api.models.Preferences;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PreferenceRepository extends JpaRepository<Preferences,Long> {
    List<Preferences> findByUserId(Long id);
}
