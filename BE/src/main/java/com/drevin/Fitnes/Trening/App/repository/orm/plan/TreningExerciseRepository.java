package com.drevin.Fitnes.Trening.App.repository.orm.plan;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TreningExerciseRepository extends JpaRepository<TreningExercise, Long> {
    //   @Query("SELECT e FROM ExerciseDetails e ORDER BY e.exerciseOrder")
    List<TreningExercise> findByTrening(Trening trening);
}
