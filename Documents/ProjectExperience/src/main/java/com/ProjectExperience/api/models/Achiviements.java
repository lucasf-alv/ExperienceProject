package com.ProjectExperience.api.models;

import jakarta.persistence.*;

@Entity
@Table(name = "achiviements")
public class Achiviements {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id ;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "criterion",nullable = false)
    private String criterion;
}
