package com.ProjectExperience.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "activity_type")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ActivityType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name ;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "image",nullable = false)
    private String image;
    @OneToMany(mappedBy = "activityType", cascade = CascadeType.ALL)
    private List<Activity> activityList = new ArrayList<>();
}
