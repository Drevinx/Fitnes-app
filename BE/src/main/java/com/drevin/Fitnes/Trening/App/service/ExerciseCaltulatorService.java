package com.drevin.Fitnes.Trening.App.service;

import com.drevin.Fitnes.Trening.App.repository.orm.exercise.Exercise;
import com.drevin.Fitnes.Trening.App.repository.orm.exercise.ExerciseDetails;
import com.drevin.Fitnes.Trening.App.repository.orm.plan.TreningExercise;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
public class ExerciseCaltulatorService {

    private static final HashMap<Integer, Double> diffKoef;

    static {
        diffKoef = new HashMap<>();
        diffKoef.put(3, -2.5);
        diffKoef.put(2, 0.1);
        diffKoef.put(1, 0.3);
        diffKoef.put(0, 0.6);
        diffKoef.put(-1, 0.9);
    }


    public ExerciseDetails calculateNextExerciseDetail(ExerciseDetails exerciseDetailLast, ExerciseDetails exeDetUpdate) {
        Exercise exercise = exerciseDetailLast.getTreningExercise().getExercise();
        TreningExercise treningExercise = exerciseDetailLast.getTreningExercise();

        ExerciseDetails addExeDet = getExeDetToUpdateOrCreate(exeDetUpdate, treningExercise, exerciseDetailLast);

        int series = 0;
        int repeat = 0;
        double weight = 0;

        int newWorkValue = workValueWithDiff(exerciseDetailLast);

        if (treningExercise.getExercise().isWeight()) {
            if (isNewRepeatOver12(newWorkValue, exerciseDetailLast)) {
                if (exerciseDetailLast.getSeries() >= 4) {
                    series = 4;
                    repeat = 12;
                    weight = validateWeight(newWorkValue / 4 / 12, exerciseDetailLast);
                } else {
                    series = exerciseDetailLast.getSeries() + 1;
                    repeat = 7;
                    weight = validateWeight((int) (exercise.getKoeficient() * exerciseDetailLast.getWeight()), exerciseDetailLast);
                }
            } else {
                series = exerciseDetailLast.getSeries();
                if (exerciseDetailLast.getDifficulty() == 3) {
                    repeat = (int) Math.floor(newWorkValue / exerciseDetailLast.getWeight() / series);
                } else {
                    repeat = (int) Math.ceil(newWorkValue / exerciseDetailLast.getWeight() / series);
                }

                weight = exerciseDetailLast.getWeight();
            }
            addExeDet.setWeight(Math.ceil(weight));
            addExeDet.setRepeat(repeat);
            addExeDet.setSeries(series);

            if (addExeDet.getSeries() == exerciseDetailLast.getSeries() && addExeDet.getWeight() == exerciseDetailLast.getWeight()
                    && addExeDet.getRepeat() == exerciseDetailLast.getRepeat()) {
                addExeDet = validateChanges(addExeDet, exerciseDetailLast);
            }

            return addExeDet;
        } else {
            repeat = exerciseDetailLast.getRepeat() + 1;
            addExeDet.setRepeat(repeat);
            addExeDet.setSeries(exerciseDetailLast.getSeries());
            return addExeDet;
        }
    }

    private ExerciseDetails getExeDetToUpdateOrCreate(ExerciseDetails exeDetUpdate, TreningExercise treningExercise,
                                                      ExerciseDetails exerciseDetailLast) {
        ExerciseDetails addExeDet;
        if (exeDetUpdate != null) {
            addExeDet = exeDetUpdate;
        } else {
            addExeDet = new ExerciseDetails();
            addExeDet.setTreningExercise(treningExercise);
            addExeDet.setCount(exerciseDetailLast.getCount() + 1);
            addExeDet.setComplete(false);
        }
        return addExeDet;
    }

    private double validateWeight(int newWeight, ExerciseDetails exerciseDetailLast) {
        double maxWeight = exerciseDetailLast.getTreningExercise().getMaxWeight();

        if (maxWeight * 0.70 < newWeight) {
            if (exerciseDetailLast.getDifficulty() == 3) {
                return exerciseDetailLast.getWeight() * 0.9;
            } else if (exerciseDetailLast.getDifficulty() == 2) {
                return exerciseDetailLast.getWeight() + 1;
            }

        } else if (maxWeight * 0.8 < newWeight) {
            if (exerciseDetailLast.getDifficulty() == 3) {
                return exerciseDetailLast.getWeight() * 0.9;
            } else if (exerciseDetailLast.getDifficulty() == 2) {
                return exerciseDetailLast.getWeight();
            }

        } else if (maxWeight * 0.88 < maxWeight) {
            if (exerciseDetailLast.getDifficulty() == 3) {
                return exerciseDetailLast.getWeight() * 0.9;
            }
            return exerciseDetailLast.getWeight();
        }

        return newWeight;
    }


    private ExerciseDetails validateChanges(ExerciseDetails addExeDet, ExerciseDetails old) {
        if (old.getDifficulty() != 3) {
            if (addExeDet.getRepeat() == 12 && addExeDet.getSeries() == 4) {
                addExeDet.setWeight(addExeDet.getWeight() + 1);
                return addExeDet;
            } else if (addExeDet.getRepeat() == 12) {
                addExeDet.setSeries(addExeDet.getSeries() + 1);
                addExeDet.setRepeat(7);
                addExeDet.setWeight(addExeDet.getWeight() + 1);
                return addExeDet;
            } else {
                addExeDet.setRepeat(addExeDet.getRepeat() + 1);
                return addExeDet;
            }
        } else if (old.getDifficulty() == 3 && old.getWeight() == addExeDet.getWeight()) {
            addExeDet.setWeight(addExeDet.getWeight() * 0.92);
        }
        return addExeDet;
    }


    private boolean isNewRepeatOver12(int newWorkValue, ExerciseDetails exeDetLast) {
        return (newWorkValue / exeDetLast.getWeight()) / exeDetLast.getSeries() >= 12;
    }

    private int workValueWithDiff(ExerciseDetails exDeLa) {
        double lastWorkValue = (exDeLa.getRepeat() * exDeLa.getSeries() * exDeLa.getWeight());
        double newWorkValue = exDeLa.getTreningExercise().getExercise().getKoeficient() * lastWorkValue;

        return (int) (diffKoef.get(exDeLa.getDifficulty()) * (newWorkValue - lastWorkValue) + lastWorkValue);
    }

    public ExerciseDetails calculateFirstDetail(ExerciseDetails exDetail) {
        exDetail.setCount(1);
        exDetail.setDifficulty(0);

        if (exDetail.getTreningExercise().getExercise().isWeight()) {
            double weight = exDetail.getTreningExercise().getMaxWeight();
            System.out.println("weightmax from calc first:" + weight);
            exDetail.setSeries(2);
            exDetail.setRepeat(7);
            exDetail.setWeight(weight * 0.6);
        } else {
            exDetail.setSeries(3);
            exDetail.setRepeat(7);
            exDetail.setWeight(0);
        }
        return exDetail;
    }


}
