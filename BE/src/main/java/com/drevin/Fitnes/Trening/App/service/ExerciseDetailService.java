package com.drevin.Fitnes.Trening.App.service;

import com.drevin.Fitnes.Trening.App.repository.orm.exercise.ExerciseDetails;
import com.drevin.Fitnes.Trening.App.repository.orm.exercise.ExerciseDetailsRepository;
import com.drevin.Fitnes.Trening.App.repository.orm.plan.Plan;
import com.drevin.Fitnes.Trening.App.repository.orm.plan.Trening;
import com.drevin.Fitnes.Trening.App.repository.orm.plan.TreningExercise;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;

@Service
public class ExerciseDetailService {

    ApiService apiService;
    ExerciseDetailsRepository exerciseDetailsRepository;
    ExerciseCaltulatorService exerciseCaltulatorService;

    public ExerciseDetailService(ApiService apiService, ExerciseDetailsRepository exerciseDetailsRepository, ExerciseCaltulatorService exerciseCaltulatorService) {
        this.apiService = apiService;
        this.exerciseDetailsRepository = exerciseDetailsRepository;
        this.exerciseCaltulatorService = exerciseCaltulatorService;
    }

    public void GenerateExerciseDetails(long planId) {
        Plan plan = apiService.getPlanByIdF(planId);
        if (plan.getUser().getId() == apiService.getUserId()) {
            createDetails(plan);
            plan.setGeneratedActual(true);
            apiService.savePlan(plan);
        } else {
            throw new RuntimeException("User or plan id:" + planId + " not match");
        }
    }

    private void createDetails(Plan plan) {

        List<Trening> trenings = plan.getTrenings();

        for (int i = 0; i < plan.getTreningPerWeek() * plan.getDurationInWeek(); ) {
            for (int j = 0; j < trenings.size(); j++) {
                if (i < plan.getTreningPerWeek() * plan.getDurationInWeek()) {
                    createDetailsByExercise(trenings.get(j), i);
                    i++;
                }
            }
        }
    }


    private void createDetailsByExercise(Trening trening, int i) {
        List<TreningExercise> exercisesTrening = trening.getTreningExercises();

        for (TreningExercise exeTren : exercisesTrening) {
            createOrUpdateExerciseDetails(exeTren, i);
        }
    }

    private void createOrUpdateExerciseDetails(TreningExercise treningExercise, int i) {
        List<ExerciseDetails> exerciseDetails = exerciseDetailsRepository.findByTreningExercise(treningExercise);

        int maxCount = 0;
        for (ExerciseDetails TreningExeDet : exerciseDetails) {
            if (TreningExeDet.isComplete() && TreningExeDet.getCount() > maxCount) {
                maxCount = TreningExeDet.getCount();
            }
        }


        if (i == 0 && maxCount == 0 && exerciseDetails.size() == 0) {
            ExerciseDetails firstExerciseDetail = new ExerciseDetails(1, 0, 0, 0, treningExercise);
            exerciseDetailsRepository.save(exerciseCaltulatorService.calculateFirstDetail(firstExerciseDetail));
            return;
        }

        if (exerciseDetails.size() == i) {
            ExerciseDetails exerciseDetailLast = exerciseDetails.stream()
                    .max(Comparator.comparingInt(ExerciseDetails::getCount))
                    .orElseThrow(() -> new RuntimeException("Last Exercise Details not found"));
            ExerciseDetails newExeDet = exerciseCaltulatorService.calculateNextExerciseDetail(exerciseDetailLast, null);
            exerciseDetailsRepository.save(newExeDet);

        } else if (exerciseDetails.size() > i) {
            ExerciseDetails exeDetI = exerciseDetails.get(i);

            if (i >= maxCount + 1) {
                exerciseDetailsRepository.save(
                        exerciseCaltulatorService.calculateNextExerciseDetail(exerciseDetails.get(i - 1), exerciseDetails.get(i)));
            }

        } else {
            throw new RuntimeException("Error in exercise details");
        }
    }



}
