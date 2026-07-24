package com.ProjectExperience.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="users")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column( name = "email", nullable = false)
    private String email;
    @Column(name = "cpf")
    private String cpf;
    @Column(name = "password")
    private String password;
    @Column(name = "avatar")
    private String avatar;
    @Column(name = "xp")
    private Integer xp ;
    @Column(name = "level")
    private Integer level;
    @Column(name = "deletedAt")
    private LocalDateTime deletedAt;
    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Preferences> preferences = new ArrayList<>();
    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL,fetch = FetchType.EAGER)
    private List<UserAchievements> userAchiviements = new ArrayList<>();
    @OneToMany(mappedBy = "creator" , cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Activity> activities = new ArrayList<>();
    @OneToMany(mappedBy = "user" , cascade = CascadeType.ALL)
    @JsonIgnore
    private List<ActivityParticipants> activityParticipants= new ArrayList<>();




}
