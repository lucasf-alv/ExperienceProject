package com.ProjectExperience.api.repository;

import com.ProjectExperience.api.models.Preferences;
import com.ProjectExperience.api.models.User;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

}
