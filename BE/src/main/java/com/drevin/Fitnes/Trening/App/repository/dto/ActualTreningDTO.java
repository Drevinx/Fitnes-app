package com.drevin.Fitnes.Trening.App.repository.dto;

import lombok.Getter;

@Getter
public class ActualTreningDTO {
    private String name;
    private int repeat;
    private int series;
    private double weight;
    private String extension;
    private String descriptionExercise;
    private  long exerciseDetailId;
    private String treningName;
    private int treningCount;

    public ActualTreningDTO(String name, int repeat, int series, double weight, String extension, String descriptionExercise,
                            long exerciseDetailId, String treningName, int treningCount) {
        this.name = name;
        this.repeat = repeat;
        this.series = series;
        this.weight = weight;
        this.extension = extension;
        this.descriptionExercise = descriptionExercise;
        this.exerciseDetailId = exerciseDetailId;
        this.treningName = treningName;
        this.treningCount = treningCount;
    }
}
