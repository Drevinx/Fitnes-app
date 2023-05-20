package com.drevin.Fitnes.Trening.App.controller;

import com.drevin.Fitnes.Trening.App.repository.dto.ActualTreningDTO;
import com.drevin.Fitnes.Trening.App.repository.dto.ExerciseTreningDto;
import com.drevin.Fitnes.Trening.App.repository.dto.PlanDto;
import com.drevin.Fitnes.Trening.App.repository.orm.exercise.Exercise;
import com.drevin.Fitnes.Trening.App.repository.orm.exercise.ExerciseDetails;
import com.drevin.Fitnes.Trening.App.repository.orm.plan.Plan;
import com.drevin.Fitnes.Trening.App.repository.orm.plan.Trening;
import com.drevin.Fitnes.Trening.App.repository.orm.plan.TreningExercise;
import com.drevin.Fitnes.Trening.App.service.ApiService;
import com.drevin.Fitnes.Trening.App.service.ExerciseDetailService;
import com.drevin.Fitnes.Trening.App.service.TreningService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController()
@RequestMapping("api/v1")
public class ApiRestController {

    private ApiService apiService;
    private ExerciseDetailService exerciseDetailService;
    private TreningService treningService;

    public ApiRestController(ApiService apiService, ExerciseDetailService exerciseDetailService, TreningService treningService) {
        this.apiService = apiService;
        this.exerciseDetailService = exerciseDetailService;
        this.treningService = treningService;
    }

    @GetMapping("/exercises")
    public List<Exercise> getAllExercises () {
        return apiService.getAllExersises();
    }
    @GetMapping("/exercises/{exerciseId}")
    public Exercise getExerciseById(@PathVariable long exerciseId){
        return apiService.getExerciseById(exerciseId);
    }





    @GetMapping("/plans")
    public List<PlanDto> getAllUserPlans () {
            return apiService.getAllPlansDto();
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/plans")
    public PlanDto createPlan(@Valid @RequestBody Plan plan){
        return apiService.createPlan( plan);
    }

    @GetMapping("/plans/{planId}")
    public PlanDto getUserPlan(@PathVariable long planId){
        return apiService.getPlanById(planId);
    }

    @DeleteMapping("/plans/{planId}")
    public String deletePlanById(@PathVariable long planId){
        apiService.deletePlanById(planId);
        return "redirect:plans";
    }

    @PatchMapping("/plans/{planId}")
    public PlanDto updateList(@PathVariable long planId,@Valid @RequestBody Plan plan){
        return apiService.updatePlanById(planId, plan);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/plans/{planId}/generate")
    public String generateExerciseDetails(@PathVariable long planId){
        exerciseDetailService.GenerateExerciseDetails(planId);
        return "Generated";
    }

    @GetMapping("/plans/{planId}/lets-go")
    public List<ActualTreningDTO> getNextActualTrening(@PathVariable long planId){
        return treningService.nextTreningFromPlan(planId);
    }




    @GetMapping("/plans/{planId}/trenings")
    public List<Trening> getTrenings (@PathVariable long planId){
        return apiService.getTrainingsByPlan(planId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/plans/{planId}/trenings")
    public Trening createTraining(@PathVariable long planId, @Valid @RequestBody Trening trening){
        return apiService.createTrening(planId, trening);
    }
    @GetMapping("/plans/{planId}/trenings/{treningId}")
    public Trening getTreningById(@PathVariable long planId, @PathVariable long treningId){
        return apiService.getTreningById(planId,treningId);
    }

    @DeleteMapping("/plans/{planId}/trenings/{treningId}")
    public String deleteTreningById(@PathVariable long planId, @PathVariable long treningId){
        apiService.deleteTreningById(planId,treningId);
        return "redirect:trainigs";
    }
    @PatchMapping("/plans/{planId}/trenings/{treningId}")
    public Trening updateTrening (@PathVariable long planId, @PathVariable long treningId,
                                  @Valid @RequestBody Trening trening){
        return apiService.updateTreningById(planId ,treningId, trening);
    }


    @GetMapping("/plans/{planId}/trenings/{treningId}/lets-go")
    public List<ActualTreningDTO> getTreningStartBySelectedPlan(@PathVariable long planId, @PathVariable long treningId){
        return treningService.selectedTreningStart(planId, treningId);
    }





    @GetMapping("/plans/{planId}/trenings/{treningId}/exercises")
    public List<TreningExercise> getExerciseByTrening(@PathVariable long planId, @PathVariable long treningId){
        return apiService.getExerciseByTrening(planId, treningId);
    }


    @GetMapping("/plans/{planId}/trenings/{treningId}/exercises/{treningExerciseId}")
    public TreningExercise getExerciseByTrening(@PathVariable long planId, @PathVariable long treningId,
                                                @PathVariable long treningExerciseId){
        return apiService.getTreningExerciseById(planId, treningId, treningExerciseId);
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/plans/{planId}/trenings/{treningId}/exercises")
    public TreningExercise addExerciseToTraining(@PathVariable long planId, @PathVariable long treningId,
                                          @Valid @RequestBody ExerciseTreningDto exerciseTreningDto){
        return apiService.addExerciseTraining(planId, treningId, exerciseTreningDto);
    }

    @DeleteMapping("/plans/{planId}/trenings/{treningId}/exercises/{exerciseId}")
    public void deleteExerciseFromTrainingById(@PathVariable long planId, @PathVariable long treningId,
                                   @PathVariable long exerciseId){
        apiService.deleteExerciseById(planId, treningId, exerciseId);
    }

    @PatchMapping("/plans/{planId}/trenings/{treningId}/exercises/{exerciseId}")
    public ExerciseTreningDto updateExerciseById(@PathVariable long planId, @PathVariable long treningId,
                                                    @PathVariable long exerciseId,
                                                     @RequestBody ExerciseTreningDto exerciseTreningDto){
        return apiService.updateExerciseById(planId, treningId, exerciseId, exerciseTreningDto);
    }





    @GetMapping("/plans/{planId}/trenings/{treningId}/exercises/{exerciseId}/exercise-details")
    public List<ExerciseDetails> getExerciseDetails(@PathVariable long planId, @PathVariable long treningId,
                                                    @PathVariable long exerciseId){
        return apiService.getDetailsExerciseByTreningAndExercise(planId, treningId, exerciseId);
    }


    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping("/plans/{planId}/trenings/{treningId}/exercises/{exerciseId}/exercise-details")
    public ExerciseDetails saveExerciseDetails(@PathVariable long planId, @PathVariable long treningId,
                                               @PathVariable long exerciseId,
                                               @Valid @RequestBody ExerciseDetails exerciseDetails){
        return apiService.addDetailsExerciseByTreningAndExercise(planId, treningId, exerciseId, exerciseDetails);
    }




    @GetMapping("/plans/{planId}/trenings/{treningId}/exercises/{exerciseId}/exercise-details/{exeDetailId}")
    public ExerciseDetails getExerciseDetailById(@PathVariable long planId, @PathVariable long treningId,
                                                 @PathVariable long exerciseId, @PathVariable long exeDetailId){
        return apiService.getExerciseDetailById(planId, treningId, exerciseId, exeDetailId);
    }


    @DeleteMapping("/plans/{planId}/trenings/{treningId}/exercises/{exerciseId}/exercise-details/{exeDetailId}")
    public void deleteExerciseDetailById(@PathVariable long planId, @PathVariable long treningId,
                                         @PathVariable long exerciseId, @PathVariable long exeDetailId){
        apiService.deleteExerciseDetailById(planId, treningId, exerciseId, exeDetailId);
    }

    @PatchMapping("/plans/{planId}/trenings/{treningId}/exercises/{exerciseId}/exercise-details/{exeDetailId}")
    public ExerciseDetails updateExerciseDetailById(@PathVariable long planId, @PathVariable long treningId,
                                                    @PathVariable long exerciseId, @PathVariable long exeDetailId,
                                                    @Valid @RequestBody ExerciseDetails exerciseDetails){
        return apiService.updateExerciseDetailById(planId, treningId, exerciseId, exeDetailId, exerciseDetails);
    }




}
