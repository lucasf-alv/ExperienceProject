package com.ProjectExperience.api.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "achievements")
@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class Achievements {
    @Id
    @GeneratedValue(strategy =  GenerationType.IDENTITY)
    private Long id ;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "criterion",nullable = false)
    private String criterion;
}
