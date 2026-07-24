package com.ProjectExperience.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "user_achievements")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserAchievements {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "achievement_id")
    private Achievements achievement;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
