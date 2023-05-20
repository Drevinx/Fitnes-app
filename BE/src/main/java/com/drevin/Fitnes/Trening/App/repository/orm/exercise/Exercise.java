package com.drevin.Fitnes.Trening.App.repository.orm.exercise;

import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode
public class Exercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String description;
    private double koeficient;
    private boolean isWeight;

    @OneToOne
    private ExerciseGroup exerciseGroup;

    @OneToOne
    private Muscle primaryMuscle;


    public Exercise(String name, String description) {
        this.name = name;
        this.description = description;
        this.koeficient = 1;
    }

    public Exercise(String name, String description, double koeficient, boolean isWeight, ExerciseGroup exerciseGroup) {
        this.name = name;
        this.description = description;
        this.koeficient = koeficient;
        this.isWeight = isWeight;
        this.exerciseGroup = exerciseGroup;
    }


}

