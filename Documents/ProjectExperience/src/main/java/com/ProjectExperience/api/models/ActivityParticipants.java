package com.ProjectExperience.api.models;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "activity_participants")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
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
    @Column(name ="confirmed_at")
    private LocalDateTime confirmed_at;

}
