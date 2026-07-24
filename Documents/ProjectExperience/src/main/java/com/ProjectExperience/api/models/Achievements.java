package com.ProjectExperience.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "achievements")
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class Achievements {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "criterion", nullable = false)
    private String criterion;

    @OneToMany(mappedBy = "achievement")
    @JsonIgnore
    private List<UserAchievements> userAchievements = new ArrayList<>();
}
