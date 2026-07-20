package com.ProjectExperience.api.models;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "activityPartipants")
public class ActivityParticipants {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "activity_id")
    private Activity activity;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @Column(name = "Approved")
    private Boolean approved;
    @Column(name ="confirmated_At")
    private LocalDateTime confirmated_At;

}
