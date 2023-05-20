package com.drevin.Fitnes.Trening.App.repository.dto;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
@EqualsAndHashCode
public class ExerciseTreningDto {

    long exerciseId;

    int exerciseOrder;

    int weight;

    public ExerciseTreningDto(long exerciseId, int exerciseOrder, int weight) {
        this.exerciseId = exerciseId;
        this.exerciseOrder = exerciseOrder;
        this.weight = weight;
    }
}
