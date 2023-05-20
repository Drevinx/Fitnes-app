package com.drevin.Fitnes.Trening.App.service;

import com.drevin.Fitnes.Trening.App.repository.dto.ActualTreningDTO;
import com.drevin.Fitnes.Trening.App.repository.orm.exercise.ExerciseDetails;
import com.drevin.Fitnes.Trening.App.repository.orm.exercise.ExerciseDetailsRepository;
import com.drevin.Fitnes.Trening.App.repository.orm.plan.Plan;
import com.drevin.Fitnes.Trening.App.repository.orm.plan.Trening;
import com.drevin.Fitnes.Trening.App.repository.orm.plan.TreningExercise;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Service
public class TreningService {

    private ExerciseDetailsRepository exerciseDetailsRepository;
    private ApiService apiService;
    private ExerciseDetailService exerciseDetailService;

    public TreningService(ExerciseDetailsRepository exerciseDetailsRepository, ApiService apiService, ExerciseDetailService exerciseDetailService) {
        this.exerciseDetailsRepository = exerciseDetailsRepository;
        this.apiService = apiService;
        this.exerciseDetailService = exerciseDetailService;
    }

    public List<ActualTreningDTO> nextTreningFromPlan(long planId) {

        Plan plan = apiService.getPlanByIdF(planId);
        List<ActualTreningDTO> actualTreningDTOS = new ArrayList<>();

        // check if exercise details is not 0 or generate
        if (!plan.isGeneratedActual()) {
            exerciseDetailService.GenerateExerciseDetails(planId);
        }

        Trening nextTrening = null;
        List<Trening> treningList = plan.getTrenings();
        treningList.sort(Comparator.comparingInt(Trening::getTreningCount));

        for (Trening trening : plan.getTrenings()) {
            if (nextTrening == null || nextTrening.getTreningCount() < trening.getTreningCount()) {
                nextTrening = trening;
            }
        }
        return returnActualTreningWithAllExercises(planId, nextTrening.getId());
    }

    private List<ActualTreningDTO> returnActualTreningWithAllExercises(long planId, long treningId) {
        List<ActualTreningDTO> actualTreningDTOS = new ArrayList<>();
        Trening nextTrening = apiService.getTreningById(planId, treningId);

        for (TreningExercise treningExercise : nextTrening.getTreningExercises()) {

            ExerciseDetails exerciseDetails = exerciseDetailsRepository.findByCountAndTreningExercise(nextTrening.getTreningCount(), treningExercise);

            actualTreningDTOS.add(new ActualTreningDTO(
                    treningExercise.getExercise().getName(),
                    exerciseDetails.getRepeat(),
                    exerciseDetails.getSeries(),
                    exerciseDetails.getWeight(),
                    exerciseDetails.getExtension(),
                    treningExercise.getExercise().getDescription(),
                    exerciseDetails.getId(),
                    nextTrening.getName(),
                    nextTrening.getTreningCount()));
        }

        return actualTreningDTOS;
    }


    public List<ActualTreningDTO> selectedTreningStart(long planId, long treningId) {

        if (!apiService.getPlanByIdF(planId).isGeneratedActual()) {
            exerciseDetailService.GenerateExerciseDetails(planId);
        }
        return returnActualTreningWithAllExercises(planId, treningId);
    }
}
