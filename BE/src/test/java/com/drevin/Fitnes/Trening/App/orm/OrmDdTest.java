package com.drevin.Fitnes.Trening.App.orm;

import com.drevin.Fitnes.Trening.App.repository.orm.exercise.Exercise;
import com.drevin.Fitnes.Trening.App.repository.orm.exercise.ExerciseDetails;
import com.drevin.Fitnes.Trening.App.repository.orm.exercise.ExerciseDetailsRepository;
import com.drevin.Fitnes.Trening.App.repository.orm.exercise.ExerciseRepository;
import com.drevin.Fitnes.Trening.App.repository.orm.plan.*;
import com.drevin.Fitnes.Trening.App.repository.orm.user.RoleRepository;
import com.drevin.Fitnes.Trening.App.repository.orm.user.User;
import com.drevin.Fitnes.Trening.App.repository.orm.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("application-dev.properties")
@DataJpaTest
class OrmDdTest {

    @Autowired
    private TestEntityManager entityManager;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private ExerciseRepository exerciseRepository;
    @Autowired
    private PlanRepository planRepository;
    @Autowired
    private ExerciseDetailsRepository exerciseDetailsRepository;

    @Autowired
    private TreningRepository treningRepository;

    private String username = "usrname";


    @Test
    public void addUser(){

        User user = new User(username,"heslo","email",true);
        entityManager.persist(user);

        User user2 = new User("usrname2","heslo2","email2",true);
        entityManager.persist(user2);

        entityManager.flush();

        List<User> entities = userRepository.findAll();
        assertThat(entities).hasSize(2);

        List<String> usernames = entities.stream()
                        .map(User::getUsername)
                        .collect(Collectors.toList());

        assertThat(usernames).containsExactly(user.getUsername(), user2.getUsername());

    }

    @Test
    public void addPlan(){
        User user = new User("name Plan","password","mm",true);

        user = entityManager.persist(user);

        Plan plan = new Plan("planName","planDesc",20,2);
        plan = entityManager.persist(plan);
        entityManager.flush();

        user.addPlan(plan);
        entityManager.persist(user);

        entityManager.flush();


        assertEquals(plan,userRepository.findByUsername("name Plan").getPlans().get(0));
    }

    @Test
    public void addTrening(){
        User user = new User("name Trening","password","mm",true);

        user = entityManager.persist(user);

        Plan plan = new Plan("planName TR","planDesc TR",20,2);
        plan = entityManager.persist(plan);
        entityManager.flush();

        user.addPlan(plan);
        entityManager.persist(user);

        entityManager.flush();

        Trening trening = new Trening(plan, "Trening name");
        Trening savedTrening = entityManager.persist(trening);

        plan.addTrening(savedTrening);
        entityManager.persist(plan);
        entityManager.flush();


        System.out.println(userRepository.findByUsername("name Trening").getUsername());
        System.out.println(userRepository.findByUsername("name Trening").getPlans().get(0).getName());
        System.out.println(userRepository.findByUsername("name Trening").getPlans().get(0)
                .getTrenings().get(0).getName());

        assertEquals(trening,userRepository.findByUsername("name Trening").
                getPlan().get(0).getTrenings().get(0));
    }

    @Test
    public void addExercise(){

        User userCreate = new User("User EX","pass","mail",true);
        userCreate = entityManager.persist(userCreate);

        Plan planCreate = new Plan("Plan Ex","Desc EX",20,3);
         planCreate = entityManager.persist(planCreate);

        Trening trening = new Trening(planCreate, "Trening name EX");
        trening = entityManager.persist(trening);

        Exercise exercise = new Exercise("exc name","exc desc");
        exercise = entityManager.persist(exercise);

        TreningExercise treningExercise = new TreningExercise(exercise,1,50,trening);
        treningExercise = entityManager.persist(treningExercise);

        trening.addTreningExercise(treningExercise);
        trening = entityManager.persist(trening);

        userCreate.addPlan(planCreate);
        userCreate = entityManager.persist(userCreate);

        planCreate.addTrening(trening);
        trening = entityManager.persist(trening);


        entityManager.flush();

        assertEquals(exercise,userRepository.findByUsername("User EX").getPlan().get(0).getTrenings()
                .get(0).getTreningExercises().get(0).getExercise());
    }
    @Test
    public void exerciseDetails(){
        User userCreate = new User("User EXD","pass","mail",true);
        userCreate = entityManager.persist(userCreate);

        Plan planCreate = new Plan("Plan ExD","Desc EX",20,3);
        planCreate = entityManager.persist(planCreate);

        Trening trening = new Trening(planCreate, "Trening name EXD");
        trening = entityManager.persist(trening);

        Exercise exercise = new Exercise("excD name","exc desc");
        exercise = entityManager.persist(exercise);

        TreningExercise treningExercise = new TreningExercise(exercise,1,50,trening);
        treningExercise = entityManager.persist(treningExercise);

        userCreate.addPlan(planCreate);
        userCreate = entityManager.persist(userCreate);

        planCreate.addTrening(trening);
        trening = entityManager.persist(trening);

        ExerciseDetails exerciseDetails = new ExerciseDetails(1,30,3,7,treningExercise);
        exerciseDetails = entityManager.persist(exerciseDetails);

        entityManager.flush();

        assertEquals(exerciseDetails,exerciseDetailsRepository.findByTreningExercise(treningExercise).get(0));
    }

}