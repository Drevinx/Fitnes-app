package com.drevin.Fitnes.Trening.App.repository.orm.exercise;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class ExerciseGroup {
    @JsonIgnore
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nameGroupOfBodies;

    public ExerciseGroup(String nameGroupOfBodies) {
        this.nameGroupOfBodies = nameGroupOfBodies;
    }
}
