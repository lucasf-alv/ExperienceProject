package com.ProjectExperience.api.repository;

import com.ProjectExperience.api.models.Activity;
import com.ProjectExperience.api.models.ActivityParticipants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ActivityRepository extends JpaRepository<Activity, Long> {

    Page<Activity> findByActivityTypeId(Long typeId, Pageable pageable);

    Page<Activity> findByCreatorId(Long creatorId, Pageable pageable);

    List<Activity> findByCreatorId(Long creatorId);
}