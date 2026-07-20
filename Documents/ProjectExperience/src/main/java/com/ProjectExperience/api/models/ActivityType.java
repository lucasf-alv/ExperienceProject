package com.ProjectExperience.api.models;

import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "activityType")
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
    @OneToMany(mappedBy = "activityType", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<Activity> activityList = new ArrayList<>();
}
