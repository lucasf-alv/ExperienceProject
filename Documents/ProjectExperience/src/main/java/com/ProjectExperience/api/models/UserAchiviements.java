package com.ProjectExperience.api.models;

import jakarta.persistence.*;

@Entity
@Table(name = "userAchiviements")
public class UserAchiviements {
    @id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "achiviement_id")
    private Achiviements achiviement;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

}
