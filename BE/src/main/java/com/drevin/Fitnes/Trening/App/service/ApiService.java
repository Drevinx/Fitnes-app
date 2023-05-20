package com.drevin.Fitnes.Trening.App.service;

import com.drevin.Fitnes.Trening.App.exeption.DbNotFoundExeption;
import com.drevin.Fitnes.Trening.App.repository.dto.ExerciseTreningDto;
import com.drevin.Fitnes.Trening.App.repository.dto.PlanDto;
import com.drevin.Fitnes.Trening.App.repository.orm.exercise.Exercise;
import com.drevin.Fitnes.Trening.App.repository.orm.exercise.ExerciseDetails;
import com.drevin.Fitnes.Trening.App.repository.orm.exercise.ExerciseDetailsRepository;
import com.drevin.Fitnes.Trening.App.repository.orm.exercise.ExerciseRepository;
import com.drevin.Fitnes.Trening.App.repository.orm.plan.*;
import com.drevin.Fitnes.Trening.App.repository.orm.user.RoleRepository;
import com.drevin.Fitnes.Trening.App.repository.orm.user.User;
import com.drevin.Fitnes.Trening.App.repository.orm.user.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Service
public class ApiService {

    public static final String DATE_FORMAT = "dd/MM/yyyy";
    private UserRepository userRepository;
    private RoleRepository roleRepository;
    private ExerciseRepository exerciseRepository;
    private PlanRepository planRepository;
    private ExerciseDetailsRepository exerciseDetailsRepository;
    private TreningRepository treningRepository;
    private TreningExerciseRepository treningExerciseRepository;
    private ExerciseCaltulatorService exerciseCaltulatorService;


    public ApiService(UserRepository userRepository, RoleRepository roleRepository, ExerciseRepository exerciseRepository,
                      PlanRepository planRepository, ExerciseDetailsRepository exerciseDetailsRepository, TreningRepository treningRepository,
                      TreningExerciseRepository treningExerciseRepository, ExerciseCaltulatorService exerciseCaltulatorService) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.exerciseRepository = exerciseRepository;
        this.planRepository = planRepository;
        this.exerciseDetailsRepository = exerciseDetailsRepository;
        this.treningRepository = treningRepository;
        this.treningExerciseRepository = treningExerciseRepository;
        this.exerciseCaltulatorService = exerciseCaltulatorService;

    }

    private PlanDto planToPlanDto(Plan plan) {
        return new PlanDto(plan.getId(), plan.getName(), plan.getDescription(),
                plan.getDurationInWeek(), plan.getDurationInWeek(), plan.getUser().getId(), plan.getPlanOrder());
    }


    public List<PlanDto> getAllPlansDto() {
        List<Plan> plans = getUser().getPlans();
        List<PlanDto> planDtoList = transferPlanToPlanDto(plans);
        return planDtoList;
    }


    private List<PlanDto> transferPlanToPlanDto(List<Plan> plans) {
        List<PlanDto> planDtoList = new ArrayList<>();
        for (Plan plan : plans) {
            planDtoList.add(new PlanDto(plan.getId(), plan.getName(), plan.getDescription(),
                    plan.getDurationInWeek(), plan.getDurationInWeek(), plan.getUser().getId(), plan.getPlanOrder()));
        }
        return planDtoList;
    }


    public Integer getUserId() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username).getId();
    }


    private User getUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByUsername(username);
    }


    public PlanDto getPlanById(long planId) {
        Plan plan = getPlanByIdF(planId);

        if (plan.getUser().getId() == getUserId()) {
            return planToPlanDto(plan);
        } else {
            throw new DbNotFoundExeption("Plan id:" + planId);
        }
    }


    public Exercise getExerciseById(long exerciseId) {
        return exerciseRepository.findById(exerciseId).orElse(null);
    }


    public List<Exercise> getAllExersises() {
        return exerciseRepository.findAll();
    }


    public PlanDto createPlan(Plan plan) {
        plan.setPlanOrder(getUser().getPlans().size() + 1);

        User saveUser = getUser();
        plan.setUser(saveUser);

        Plan savedPlan = planRepository.save(plan);
        saveUser.addPlan(savedPlan);
        userRepository.save(saveUser);

        return planToPlanDto(plan);
    }

    public void deletePlanById(long planId) {
        Plan delePlan = getPlanByIdF(planId);
        User user = getUser();

        if (delePlan.getUser().getId() == user.getId()) {
            deleteAllExerciseDetailByTrenings(delePlan.getTrenings());

            delePlan.getTrenings().forEach(trening -> {
                treningExerciseRepository.deleteAll(treningExerciseRepository.findByTrening(trening));
            });

            treningRepository.deleteAll(treningRepository.findByPlan(delePlan));

            user.getPlans().remove(delePlan);
            userRepository.save(getUser());

            planRepository.delete(delePlan);
            updateOrderAfterDeletePlan(delePlan, user.getPlan());

        } else {
            throw new DbNotFoundExeption("Plan id:" + planId);
        }
    }


    private void deleteAllExerciseDetailByTrenings(List<Trening> trenings) {
        trenings.forEach(
                t -> deleteAllExerciseDetailByTreningExercise(t.getTreningExercises())
        );
    }

    private void deleteAllExerciseDetailByTreningExercise(List<TreningExercise> treningExercises) {
        treningExercises.forEach(
                treningExercise -> {
                    exerciseDetailsRepository.deleteAll(exerciseDetailsRepository.findByTreningExercise(treningExercise));
                });
    }


    private void updateOrderAfterDeletePlan(Plan deletePlan, List<Plan> planList) {
        planList.forEach(plan -> {
            if (plan.getPlanOrder() > deletePlan.getPlanOrder()) {
                plan.setPlanOrder(plan.getPlanOrder() - 1);
                planRepository.save(plan);
            }
        });
    }


    public PlanDto updatePlanById(long planId, Plan plan) {
        Plan existingPlan = getPlanByIdF(planId);
        User user = getUser();

        if (existingPlan.getUser().getId() == user.getId()) {
            existingPlan = copyPlanToExistingPlan(existingPlan, plan);

            if (existingPlan.getPlanOrder() != plan.getPlanOrder()) {
                changePlanOrders(existingPlan.getPlanOrder(), plan.getPlanOrder(), user.getPlan(), existingPlan);

            } else {
                planRepository.save(existingPlan);
            }

            return planToPlanDto(existingPlan);

        } else {
            throw new DbNotFoundExeption("Plan id:" + planId);
        }
    }


    private Plan copyPlanToExistingPlan(Plan existingPlan, Plan plan) {
        existingPlan.setName(plan.getName());
        existingPlan.setDescription(plan.getDescription());
        existingPlan.setDurationInWeek(plan.getDurationInWeek());
        existingPlan.setTreningPerWeek(plan.getTreningPerWeek());

        return existingPlan;
    }


    private void changePlanOrders(int orderOld, int orderNew, List<Plan> planList, Plan plan) {
        plan.setPlanOrder(orderNew);
        planRepository.save(plan);

        if (orderOld > orderNew) {

            for (int i = 0; i < planList.size(); i++) {
                Plan planIterate = planList.get(i);

                if (planIterate.getId() != plan.getId() && planIterate.getPlanOrder() >= orderNew
                        && planIterate.getPlanOrder() <= orderOld) {
                    planIterate.setPlanOrder(planIterate.getPlanOrder() + 1);
                    planRepository.save(planIterate);
                }
            }
        } else {

            for (int i = 0; i < planList.size(); i++) {
                Plan planIterate = planList.get(i);

                if (planIterate.getId() != plan.getId() && planIterate.getPlanOrder() >= orderOld
                        && planIterate.getPlanOrder() <= orderNew) {
                    planIterate.setPlanOrder(planIterate.getPlanOrder() - 1);
                    planRepository.save(planIterate);
                }
            }
        }
    }

    public Trening createTrening(long planId, Trening trening) {
        Plan plan = getPlanByIdF(planId);

        if (plan.getUser().getId() == getUserId()) {
            trening.setTreningOrder(
                    plan.getTrenings().size() + 1
            );
            plan.setGeneratedActual(false);
            savePlan(plan);

            trening.setPlan(plan);
            return treningRepository.save(trening);
        } else {
            throw new DbNotFoundExeption("Plan id:" + planId);
        }
    }


    public List<Trening> getTrainingsByPlan(long planId) {
        Plan plan = getPlanByIdF(planId);

        if (plan.getUser().getId() == getUserId()) {
            return plan.getTrenings();
        } else {
            throw new DbNotFoundExeption("Plan id:" + planId);
        }
    }

    public Trening getTreningById(long planId, long treningId) {
        Plan plan = getPlanByIdF(planId);

        if ((plan.getUser().getId() == getPlanByTreningIdRetUserId(treningId)) &&
                plan.getUser().getId() == getUserId()) {
            return getTreningIdF(treningId);

        } else {
            throw new DbNotFoundExeption("Trening id:" + treningId);
        }
    }

    public Plan getPlanByIdF(long planId) {
        return planRepository.findById(planId).orElseThrow(
                () -> new DbNotFoundExeption("Plan id:" + planId));
    }

    private Trening getTreningIdF(long treningId) {
        return treningRepository.findById(treningId).orElseThrow(
                () -> new DbNotFoundExeption("Plan id:" + treningId));
    }

    private long getPlanByTreningIdRetUserId(long treningId) {
        return treningRepository.findById(treningId).orElseThrow(
                        () -> new DbNotFoundExeption("Plan id:" + treningId))
                .getPlan().getUser().getId();
    }

    public void deleteTreningById(long planId, long treningId) {
        Plan plan = (getPlanByIdF(planId));
        if (plan.getUser().getId() == getPlanByTreningIdRetUserId(treningId) &&
                plan.getUser().getId() == getUserId()) {

            Trening deleteTrening = getTreningIdF(treningId);

            deleteAllExerciseDetailByTreningExercise(deleteTrening.getTreningExercises());

            treningExerciseRepository.deleteAll(treningExerciseRepository.findByTrening(deleteTrening));

            treningRepository.deleteById(treningId);

            updateOrderAfterDeleteTr(deleteTrening, plan);

            plan.setGeneratedActual(false);
            savePlan(plan);

        } else {
            throw new DbNotFoundExeption("Plan id:" + planId + " or Trening id:" + treningId);
        }
    }


    private void updateOrderAfterDeleteTr(Trening deleteTrening, Plan plan) {
        List<Trening> treningListByPlan = plan.getTrenings();
        treningListByPlan.forEach(trening -> {
            if (trening.getTreningOrder() > deleteTrening.getTreningOrder()) {
                trening.setTreningOrder(trening.getTreningOrder() - 1);
                treningRepository.save(trening);
            }
        });
    }

    public Trening updateTreningById(long planId, long treningId, Trening trening) {
        Plan plan = getPlanByIdF(planId);

        if ((plan.getUser().getId() == getPlanByTreningIdRetUserId(treningId)) &&
                plan.getUser().getId() == getUserId()) {

            Trening treningExist = getTreningIdF(treningId);
            treningExist.setName(trening.getName());

            if (treningExist.getTreningOrder() != trening.getTreningOrder()) {
                changeTreningOrders(treningExist.getTreningOrder(), trening.getTreningOrder(), plan, treningExist);
            }

            return treningRepository.save(treningExist);
        } else {
            throw new DbNotFoundExeption("Plan id:" + planId + " or Trening id:" + treningId);
        }
    }


    private void changeTreningOrders(int orderOld, int orderNew, Plan plan, Trening trening) {
        trening.setTreningOrder(orderNew);
        treningRepository.save(trening);

        List<Trening> treningList = plan.getTrenings();

        if (orderOld > orderNew) {

            for (int i = 0; i < treningList.size(); i++) {
                Trening treningIterate = treningList.get(i);

                if (treningIterate.getId() != trening.getId() && treningIterate.getTreningOrder() >= orderNew
                        && treningIterate.getTreningOrder() <= orderOld) {
                    treningIterate.setTreningOrder(treningIterate.getTreningOrder() + 1);
                    treningRepository.save(treningIterate);
                }
            }
        } else {

            for (int i = 0; i < treningList.size(); i++) {
                Trening treningIterate = treningList.get(i);

                if (treningIterate.getId() != trening.getId() && treningIterate.getTreningOrder() >= orderOld
                        && treningIterate.getTreningOrder() <= orderNew) {
                    treningIterate.setTreningOrder(treningIterate.getTreningOrder() - 1);
                    treningRepository.save(treningIterate);
                }
            }
        }
    }


    public List<TreningExercise> getExerciseByTrening(long planId, long treningId) {
        long userId = getUserId();
        Trening trening = getTreningIdF(treningId);

        if (userId == getPlanByIdF(planId).getUser().getId() &&
                userId == trening.getPlan().getUser().getId()) {
            return trening.getTreningExercises();

        } else {
            throw new DbNotFoundExeption("Plan id:" + planId + " or Trening id:" + treningId);
        }
    }


    public TreningExercise addExerciseTraining(long planId, long treningId, ExerciseTreningDto exerciseTreningDto) {
        long userId = getUserId();
        Plan plan = getPlanByIdF(planId);
        Trening trening = getTreningIdF(treningId);

        if (userId == getPlanByIdF(planId).getUser().getId() &&
                userId == trening.getPlan().getUser().getId() &&
                plan.getTrenings().contains(trening)) {

            Exercise addExercise = getExerciseByIdF(exerciseTreningDto.getExerciseId());

            TreningExercise treningExercise = new TreningExercise(addExercise,
                    calcNextOrderNumber(trening), exerciseTreningDto.getWeight(), trening);

            treningExercise = treningExerciseRepository.save(treningExercise);

            trening.addTreningExercise(treningExercise);
            treningRepository.save(trening);

            plan.setGeneratedActual(false);
            savePlan(plan);

            return treningExercise;

        } else {
            throw new DbNotFoundExeption("Plan id:" + planId + " or Trening id:" + treningId);
        }
    }


    private Exercise getExerciseByIdF(long exerciseId) {
        return exerciseRepository.findById(exerciseId).
                orElseThrow(() -> new DbNotFoundExeption("Exercise id:" + exerciseId));
    }

    private int calcNextOrderNumber(Trening trening) {
        List<TreningExercise> treningExerciseList = loadTreningExerciseListByTrening(trening);


        System.out.println(treningExerciseList.size());
        return (treningExerciseList.size() + 1);
    }


    private List<TreningExercise> loadTreningExerciseListByTrening(Trening trening) {
        return treningExerciseRepository.findByTrening(trening);
    }


    public void deleteExerciseById(long planId, long treningId, long exerciseId) {
        long userId = getUserId();
        Plan plan = getPlanByIdF(planId);
        Trening trening = getTreningIdF(treningId);

        if (userId == plan.getUser().getId() &&
                userId == trening.getPlan().getUser().getId()) {

            TreningExercise deleteTreningExercise = getTreningExerciseByIdF(exerciseId);

            exerciseDetailsRepository.deleteAll(exerciseDetailsRepository.findByTreningExercise(deleteTreningExercise));

            treningExerciseRepository.deleteById(exerciseId);

            updateOrderAfterDelete(deleteTreningExercise, trening);

            trening.getTreningExercises().remove(deleteTreningExercise);
            treningRepository.save(trening);

            plan.setGeneratedActual(false);
            savePlan(plan);
        } else {
            throw new DbNotFoundExeption("Plan id:" + planId + " or Trening id:" + treningId
                    + " Exercise id:" + exerciseId);
        }
    }


    private void updateOrderAfterDelete(TreningExercise deleteTreningExercise, Trening trening) {
        List<TreningExercise> exerciseListByTrening = loadTreningExerciseListByTrening(trening);
        exerciseListByTrening.forEach(exTr -> {
            if (exTr.getExerciseOrder() > deleteTreningExercise.getExerciseOrder()) {
                exTr.setExerciseOrder(exTr.getExerciseOrder() - 1);
                treningExerciseRepository.save(exTr);
            }
        });
    }

    public ExerciseTreningDto updateExerciseById(long planId, long treningId, long exerciseId, ExerciseTreningDto exerciseTreningDto) {
        Trening trening = getTreningIdF(treningId);
        Plan plan = getPlanByIdF(planId);
        long userId = getUserId();

        if (userId == plan.getUser().getId() &&
                userId == trening.getPlan().getUser().getId()) {

            TreningExercise treningExercise = getTreningExerciseByIdF(exerciseId);

            if (treningExercise.getExercise().getId() != exerciseTreningDto.getExerciseId()) {
                treningExercise.setExercise(getExerciseByIdF(exerciseTreningDto.getExerciseId()));
            }

            if (treningExercise.getMaxWeight() != exerciseTreningDto.getWeight()) {
                treningExercise.setMaxWeight(exerciseTreningDto.getWeight());
                treningExerciseRepository.save(treningExercise);
                recalculateTreningExerciseDetails(exerciseId, exerciseTreningDto.getWeight());
            }

            if (treningExercise.getExerciseOrder() != exerciseTreningDto.getExerciseOrder()) {
                changeExerciseOrders(treningExercise.getExerciseOrder(), exerciseTreningDto.getExerciseOrder(),
                        treningExercise, trening);
            }

            treningExerciseRepository.save(treningExercise);

            plan.setGeneratedActual(false);
            savePlan(plan);

            return new ExerciseTreningDto(treningExercise.getExercise().getId(),
                    treningExercise.getExerciseOrder(), treningExercise.getMaxWeight());

        } else {
            throw new DbNotFoundExeption("Plan id:" + planId + " or Trening id:" + treningId
                    + " Exercise id:" + exerciseId);
        }
    }

    private void recalculateTreningExerciseDetails(long exerciseId, double maxWeight) {
        TreningExercise treningExercise = getTreningExerciseByIdF(exerciseId);
        List<ExerciseDetails> exerciseDetails = exerciseDetailsRepository.findByTreningExercise(treningExercise);

        if (exerciseDetails.isEmpty()) {
            return;
        }

        int maxCount = 0;
        for (ExerciseDetails TreningExeDet : exerciseDetails) {
            if (TreningExeDet.isComplete() && TreningExeDet.getCount() > maxCount) {
                maxCount = TreningExeDet.getCount();
            }
        }

        Collections.sort(exerciseDetails, new Comparator<ExerciseDetails>() {
            @Override
            public int compare(ExerciseDetails o1, ExerciseDetails o2) {
                return Integer.compare(o1.getCount(), o2.getCount());
            }
        });

        System.out.println("max count:" + maxCount + "   exerList:" + exerciseDetails.size());
        for (int i = maxCount; i < exerciseDetails.size(); i++) {

            if (i == 0) {
                System.out.println(maxWeight);
                exerciseDetails.get(i).setWeight(0.6 * maxWeight);
                exerciseDetailsRepository.save(exerciseDetails.get(0));
                continue;
            }
/*            if (i == maxCount && maxCount != exerciseDetails.size()){
                System.out.println("weight set 0,6 * max");
                exerciseDetails.get(i).setWeight(0.6 * maxWeight);
                exerciseDetailsRepository.save(exerciseDetails.get(i+1));
                continue;

            }*/

            System.out.println("i:" + (i) + " exDetCount:" + exerciseDetails.get(i).getCount());
            //exerciseDetailsRepository.delete(exerciseDetailsList.get(i));

            ExerciseDetails updatedExeDet = exerciseDetailsRepository.save(
                    exerciseCaltulatorService.calculateNextExerciseDetail(exerciseDetails.get((i - 1)),
                            exerciseDetails.get(i)));
            System.out.println("last weight:" + exerciseDetails.get(i - 1).getWeight() + " updateweight:" + exerciseDetails.get(i).getWeight());
            System.out.println(updatedExeDet.getWeight());
            updatedExeDet.setId(exerciseDetails.get(i).getId());
            exerciseDetails.remove(i);
            exerciseDetails.add(updatedExeDet);
            exerciseDetails.sort(Comparator.comparingInt(ExerciseDetails::getCount));
        }

        treningExercise.setExerciseDetails(exerciseDetails);
        treningExerciseRepository.save(treningExercise);

    }


    private void changeExerciseOrders(int orderOld, int orderNew, TreningExercise treningExercise, Trening trening) {
        treningExercise.setExerciseOrder(orderNew);
        treningExerciseRepository.save(treningExercise);

        List<TreningExercise> exerciseListByTrening = loadTreningExerciseListByTrening(trening);

        if (orderOld > orderNew) {

            for (int i = 0; i < exerciseListByTrening.size(); i++) {
                TreningExercise trenExercise = exerciseListByTrening.get(i);

                if (trenExercise.getId() != treningExercise.getId() && trenExercise.getExerciseOrder() >= orderNew
                        && trenExercise.getExerciseOrder() <= orderOld) {
                    trenExercise.setExerciseOrder(trenExercise.getExerciseOrder() + 1);
                    treningExerciseRepository.save(trenExercise);
                }
            }
        } else {

            for (int i = 0; i < exerciseListByTrening.size(); i++) {
                TreningExercise trenExercise = exerciseListByTrening.get(i);

                if (trenExercise.getId() != treningExercise.getId() && trenExercise.getExerciseOrder() >= orderOld
                        && trenExercise.getExerciseOrder() <= orderNew) {
                    trenExercise.setExerciseOrder(trenExercise.getExerciseOrder() - 1);
                    treningExerciseRepository.save(trenExercise);
                }
            }
        }
    }


    public List<ExerciseDetails> getDetailsExerciseByTreningAndExercise(long planId, long treningId, long exerciseId) {
        long useId = getUserId();

        if (useId == getPlanByIdF(planId).getUser().getId() &&
                useId == getTreningIdF(treningId).getPlan().getUser().getId()) {

            TreningExercise treningExercise = getTreningExerciseByIdF(exerciseId);

            return exerciseDetailsRepository.findByTreningExercise(treningExercise);

        }
        throw new DbNotFoundExeption("Plan id:" + planId + " Trening id:" + treningId + " Exercise id:" + exerciseId);
    }


    private TreningExercise getTreningExerciseByIdF(long exerciseId) {
        return treningExerciseRepository.findById(exerciseId).orElseThrow(
                () -> new DbNotFoundExeption("Exercise id:" + exerciseId)
        );
    }


    public ExerciseDetails addDetailsExerciseByTreningAndExercise(long planId, long treningId, long exerciseId,
                                                                  ExerciseDetails exerciseDetails) {
        long userId = getUserId();
        Plan plan = getPlanByIdF(planId);

        if (userId == plan.getUser().getId() &&
                userId == getTreningIdF(treningId).getPlan().getUser().getId()) {

            TreningExercise treningExercise = getTreningExerciseByIdF(exerciseId);
            ExerciseDetails exDetail = new ExerciseDetails();
            List<ExerciseDetails> exerciseDetailsList = exerciseDetailsRepository.findByTreningExercise(treningExercise);

            if (exerciseDetailsList.size() == 0) {
                throw new RuntimeException("Exercise Details ware not generated!");

            } else {
                ExerciseDetails exerciseDetailLast = exerciseDetailsList.stream()
                        .max(Comparator.comparingInt(ExerciseDetails::getCount))
                        .orElseThrow(() -> new RuntimeException(" Last Exercise Details not found"));
                //  exDetail = exerciseCaltulatorService.calculateNextExerciseDetail(exerciseDetailLast);
            }
            plan.setGeneratedActual(false);
            savePlan(plan);

            return exerciseDetailsRepository.save(exDetail);
        } else {
            throw new DbNotFoundExeption("Plan id:" + planId + " Trening id:" + treningId + " Exercise id:" + exerciseId);
        }
    }


    public ExerciseDetails getExerciseDetailById(long planId, long treningId, long exerciseId, long exeDetailId) {
        long userId = getUserId();

        if (userId == getPlanByIdF(planId).getUser().getId() &&
                userId == getTreningIdF(treningId).getPlan().getUser().getId()) {

            return getExerciseDetailByIdF(exeDetailId);

        }
        throw new DbNotFoundExeption("Exercise Details id:" + exeDetailId);
    }


    private ExerciseDetails getExerciseDetailByIdF(long exeDetailId) {
        return exerciseDetailsRepository.findById(exeDetailId).orElseThrow(
                () -> new DbNotFoundExeption("Exercise details id:" + exeDetailId)
        );
    }

    public void deleteExerciseDetailById(long planId, long treningId, long exerciseId, long exeDetailId) {
        long userId = getUserId();
        Plan plan = getPlanByIdF(planId);

        if (userId == plan.getUser().getId() &&
                userId == getTreningIdF(treningId).getPlan().getUser().getId()) {

            ExerciseDetails delExerciseDetail = getExerciseDetailByIdF(exeDetailId);
            exerciseDetailsRepository.delete(delExerciseDetail);

            plan.setGeneratedActual(false);
            savePlan(plan);

        } else {
            throw new DbNotFoundExeption("Plan id:" + planId + " Trening id:" + treningId + " Exercise id:" + exerciseId
                    + "Exercise Detail id:" + exeDetailId);
        }
    }


    public ExerciseDetails updateExerciseDetailById(long planId, long treningId, long exerciseId, long exeDetailId,
                                                    ExerciseDetails exeDetPost) {
        long userId = getUserId();

        if (userId == getPlanByIdF(planId).getUser().getId() &&
                userId == getTreningIdF(treningId).getPlan().getUser().getId()) {


            TreningExercise treningExercise = getTreningExerciseByIdF(exerciseId);

            ExerciseDetails existingExeDettail = getExerciseDetailByIdF(exeDetailId);

            if (exeDetPost.isComplete() && !existingExeDettail.isComplete()) {
                LocalDate currentDate = LocalDate.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
                String formattedDate = currentDate.format(formatter);

                existingExeDettail.setLocalDate(formattedDate);
                existingExeDettail.setComplete(true);
            }
            if (!exeDetPost.isComplete()) {
                existingExeDettail.setLocalDate(null);
            }

            if (existingExeDettail.getSeries() == exeDetPost.getSeries() &&
                    existingExeDettail.getRepeat() == exeDetPost.getRepeat() &&
                    existingExeDettail.getWeight() == exeDetPost.getWeight() &&
                    existingExeDettail.getDifficulty() == exeDetPost.getDifficulty() &&
                    existingExeDettail.isComplete() == exeDetPost.isComplete()) {

                existingExeDettail.setExtension(exeDetPost.getExtension());

                existingExeDettail = exerciseDetailsRepository.save(existingExeDettail);

                return existingExeDettail;
            }
            if (treningExercise.getExercise().isWeight()) {
                existingExeDettail.setWeight(exeDetPost.getWeight());
            }
            existingExeDettail.setRepeat(exeDetPost.getRepeat());
            existingExeDettail.setSeries(exeDetPost.getSeries());
            existingExeDettail.setDifficulty(exeDetPost.getDifficulty());
            existingExeDettail = exerciseDetailsRepository.save(existingExeDettail);
            validateCompleteTrening(existingExeDettail);

            List<ExerciseDetails> exerciseDetailsList = exerciseDetailsRepository.findByTreningExercise(treningExercise);

            Collections.sort(exerciseDetailsList, new Comparator<ExerciseDetails>() {
                @Override
                public int compare(ExerciseDetails o1, ExerciseDetails o2) {
                    return Integer.compare(o1.getCount(), o2.getCount());
                }
            });

            System.out.println("existingdet count:" + existingExeDettail.getCount() + "   exerList:" + exerciseDetailsList.size());
            for (int i = existingExeDettail.getCount(); i < exerciseDetailsList.size(); i++) {
                System.out.println("i:" + i + " exDetCount:" + exerciseDetailsList.get(i - 1).getCount());
                //exerciseDetailsRepository.delete(exerciseDetailsList.get(i));


                ExerciseDetails updatedExeDet = exerciseDetailsRepository.save(
                        exerciseCaltulatorService.calculateNextExerciseDetail(exerciseDetailsList.get((i - 1)),
                                exerciseDetailsList.get(i)));
                updatedExeDet.setId(exerciseDetailsList.get(i).getId());
                exerciseDetailsList.remove(i);
                exerciseDetailsList.add(updatedExeDet);
                exerciseDetailsList.sort(Comparator.comparingInt(ExerciseDetails::getCount));
            }

            return existingExeDettail;

        } else {
            throw new DbNotFoundExeption("Plan id:" + planId + " Trening id:" + treningId + " Exercise id:" + exerciseId
                    + "Exercise Detail id:" + exeDetailId);
        }
    }

    private void validateCompleteTrening(ExerciseDetails existingExeDettail) {

        boolean isComplete = true;

        for (TreningExercise trEx : existingExeDettail.getTreningExercise().getTrening().getTreningExercises()) {
            for (ExerciseDetails exDet : trEx.getExerciseDetails()) {
                if (exDet.getCount() == existingExeDettail.getCount()) {
                    if (!exDet.isComplete()) {
                        isComplete = false;
                    }
                }
            }
        }

        if (isComplete) {
            Trening trening = existingExeDettail.getTreningExercise().getTrening();
            trening.setTreningCount(existingExeDettail.getTreningExercise().getTrening().getTreningCount() + 1);
            treningRepository.save(trening);
        }

    }


    public TreningExercise getTreningExerciseById(long planId, long treningId, long treningExerciseId) {
        return treningExerciseRepository.findById(treningExerciseId).orElseThrow(
                () -> new DbNotFoundExeption("tr exercise id:" + treningExerciseId + " not found")
        );
    }

    public void savePlan(Plan plan) {
        planRepository.save(plan);
    }
}

