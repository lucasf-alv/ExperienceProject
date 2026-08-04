package com.ProjectExperience.api.repository;

import com.ProjectExperience.api.models.ActivityType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ActivityTypeRepository extends JpaRepository<ActivityType,Long> {
    Optional<ActivityType> findByName(String type);
}
