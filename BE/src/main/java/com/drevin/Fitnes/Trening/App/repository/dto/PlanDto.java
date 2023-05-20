package com.drevin.Fitnes.Trening.App.repository.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PlanDto {

    private Long id;
    private String name;
    private String description;
    private int durationInWeek;
    private int exercisePerWeek;

    private long userId;
    private int planOrder;

    public PlanDto(Long id, String name, String description, int durationInWeek, int exercisePerWeek, long userId,
                   int planOrder) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.durationInWeek = durationInWeek;
        this.exercisePerWeek = exercisePerWeek;
        this.userId = userId;
        this.planOrder = planOrder;
    }
}
