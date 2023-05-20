package com.drevin.Fitnes.Trening.App.repository.orm.exercise;

import com.drevin.Fitnes.Trening.App.repository.orm.plan.TreningExercise;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@NoArgsConstructor
@Setter
@Getter
@ToString
public class ExerciseDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Max(99)
    @Column(length = 2)
    private int count;
    @DecimalMax(value = "999.99", inclusive = true, message = "Maximal value for weight is 999.99")
    @DecimalMin(value = "0.00", message = "Minimal value for weight is 0.00")
    @Column(length = 5)
    private double weight;
    @Max(value = 99, message = "Maximal values for series is 99")
    @Column(length = 2)
    private int series;
    @Max(value = 99, message = "Maximal values for repeat is 99")
    @Column(length = 2)
    private int repeat;

    private boolean complete;
    @Max(value = 3, message = "Maximal value for Difficulty is 3")
    @Min(value = -1, message = "Maximal value for Difficulty is -1")
    private int difficulty;
    @Column(length = 10)
    private String localDate;
    @Size(max = 50, message = "Extension should have max 50 digits")
    @Column(length = 50)
    private String extension;


    @JsonIgnore
    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "trening_exercise_id")
    private TreningExercise treningExercise;


    public ExerciseDetails(int count, double weight, int series, int repeat, TreningExercise treningExercise) {
        this.count = count;
        this.weight = weight;
        this.series = series;
        this.repeat = repeat;
        this.treningExercise = treningExercise;
        this.complete = false;
        this.localDate = null;
        this.extension = null;
    }

}
