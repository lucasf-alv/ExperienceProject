package com.ProjectExperience.api.models;

import jakarta.persistence.*;

@Entity
@Table(name ="activityAddress")
public class ActivityAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;
    @Column(name = "latitude")
    private Double latitude ;
    @Column(name = "longitude")
    private Double longitude;
}
