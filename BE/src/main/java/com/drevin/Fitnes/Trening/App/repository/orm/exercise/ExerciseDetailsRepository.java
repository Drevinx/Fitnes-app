package com.drevin.Fitnes.Trening.App.repository.orm.exercise;

import com.drevin.Fitnes.Trening.App.repository.orm.plan.TreningExercise;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ExerciseDetailsRepository extends JpaRepository<ExerciseDetails, Long> {

    List<ExerciseDetails> findByTreningExercise(TreningExercise treningExercise);

    ExerciseDetails findByCountAndTreningExercise(int count, TreningExercise treningExercise);

}
