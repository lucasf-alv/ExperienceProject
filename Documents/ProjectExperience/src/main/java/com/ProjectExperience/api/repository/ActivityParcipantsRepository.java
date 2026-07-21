package com.ProjectExperience.api.repository;

import com.ProjectExperience.api.models.ActivityParticipants;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.SearchResults;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ActivityParcipantsRepository extends JpaRepository<ActivityParticipants,Long> {
    Page<ActivityParticipants> findByUserId(Long userId, Pageable pageable);

    List<ActivityParticipants> findByUserId(Long userId);

    List<ActivityParticipants> findByActivityId(Long activityId);

    Optional<ActivityParticipants> findByActivityIdAndUserId(Long activityId, Long id);
}
