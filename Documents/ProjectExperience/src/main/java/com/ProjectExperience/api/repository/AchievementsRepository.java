package com.ProjectExperience.api.repository;

import com.ProjectExperience.api.models.Achievements;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AchievementsRepository extends JpaRepository<Achievements,Long> {
}
