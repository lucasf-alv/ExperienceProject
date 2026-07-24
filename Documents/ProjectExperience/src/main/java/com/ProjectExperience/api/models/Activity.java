package com.ProjectExperience.api.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "activities")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Activity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "title", nullable = false)
    private String title;
    @Column(name = "description", nullable = false)
    private String description;
    @Column(name = "confirmation_code",nullable = false)
    private String confirmation_code;
    @Column(name = "image", nullable = false)
    private String image ;
    @Column(name ="scheduled_date", nullable = false)
    private LocalDateTime scheduled_Date;
    @Column(name = "created_at", nullable = false)
    private LocalDateTime criated_At;
    @Column(name = "deleted_at", nullable = false)
    private LocalDateTime deleted_At;
    @Column(name = "completed_at", nullable = false)
    private LocalDateTime completed_At ;
    @Column(name = "private", nullable = false)
    private Boolean Private;
    @ManyToOne
    @JoinColumn(name = "creator_id")
    private User creator;
    @OneToOne(mappedBy = "activity", cascade = CascadeType.ALL)
    private ActivityAddress activityAddress;
    @OneToMany(mappedBy = "activity",cascade = CascadeType.ALL)
    private List<ActivityParticipants> participants = new ArrayList<>();
    @ManyToOne
    @JoinColumn(name = "activity_type")
    private ActivityType activityType;

}
