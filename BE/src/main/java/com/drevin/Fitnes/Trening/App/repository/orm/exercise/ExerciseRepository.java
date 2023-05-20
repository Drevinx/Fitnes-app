package com.drevin.Fitnes.Trening.App.repository.orm.exercise;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {

    Exercise findByName(String name);

}
