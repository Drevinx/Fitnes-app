package com.drevin.Fitnes.Trening.App.repository.orm.plan;

import com.drevin.Fitnes.Trening.App.repository.orm.exercise.Exercise;
import com.drevin.Fitnes.Trening.App.repository.orm.exercise.ExerciseDetails;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class TreningExercise {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    public Long id;

    @OneToOne
    Exercise exercise;

    int exerciseOrder;

    int maxWeight;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "trening_id")
    private Trening trening;

    @JsonIgnore
    @OneToMany(mappedBy = "treningExercise", cascade = CascadeType.MERGE)
    private List<ExerciseDetails> exerciseDetails = new ArrayList<>();


    public TreningExercise(Exercise exercise, int exerciseOrder, int maxWeight, Trening trening) {
        this.exercise = exercise;
        this.exerciseOrder = exerciseOrder;
        this.maxWeight = maxWeight;
        this.trening = trening;
    }
}
