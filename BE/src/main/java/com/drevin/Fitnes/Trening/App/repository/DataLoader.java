package com.drevin.Fitnes.Trening.App.repository;


import com.drevin.Fitnes.Trening.App.repository.orm.exercise.*;
import com.drevin.Fitnes.Trening.App.repository.orm.plan.*;
import com.drevin.Fitnes.Trening.App.repository.orm.user.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired
    MuscleRepository muscleRepository;
    @Autowired
    BCryptPasswordEncoder bCryptPasswordEncoder;
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
    private UserPrivateDetailsRepository userDetailsRepository;
    @Autowired
    private TreningRepository treningRepository;
    @Autowired
    private ExerciseGroupRepository exercisePrimaryRepository;
    @Autowired
    private TreningExerciseRepository treningExerciseRepository;

    @Override
    public void run(String... args) throws Exception {

        User user2 = new User("name", bCryptPasswordEncoder.encode("password"), "email@mail.com", true);
        userRepository.save(user2);

        String adm = "SCOPE_ROLE_ADMIN";
        String usr = "SCOPE_ROLE_USER";

        Role admin = new Role(adm);
        Role user = new Role(usr);
        Role userPremium = new Role("SCOPE_ROLE_VIP_USER");

        roleRepository.saveAll(List.of(admin, user, userPremium));

        UserPrivateDetails userDetails = new UserPrivateDetails("firstname", "lastname", 33);


        user2.setUserDetails(userDetails);
        userRepository.save(user2);
        userRepository.save(new User("username2", bCryptPasswordEncoder.encode("password"),
                "email@email.jh", true));

        User userSaved = userRepository.findByUsername("name");
        Plan plan = new Plan("test plan", "test decs", 3, 2);
        plan.setUser(userSaved);
        plan.setPlanOrder(1);
        plan = planRepository.save(plan);

        userSaved.addPlan(plan);
        userRepository.save(userSaved);

        Trening trening = new Trening(plan, "test trening");
        trening.setTreningOrder(1);
        trening.setTreningCount(1);
        trening = treningRepository.save(trening);
        plan.addTrening(trening);
        planRepository.save(plan);

        ExerciseGroup chest = new ExerciseGroup("Chest");
        chest = exercisePrimaryRepository.save(chest);
        ExerciseGroup shoulder = new ExerciseGroup("Shoulder");
        shoulder = exercisePrimaryRepository.save(shoulder);
        ExerciseGroup bicep = new ExerciseGroup("Bicep");
        bicep = exercisePrimaryRepository.save(bicep);
        ExerciseGroup triceps = new ExerciseGroup("Triceps");
        triceps = exercisePrimaryRepository.save(triceps);
        ExerciseGroup leg = new ExerciseGroup("Leg");
        leg = exercisePrimaryRepository.save(leg);
        ExerciseGroup hamstrings = new ExerciseGroup("Hamstrings");
        hamstrings = exercisePrimaryRepository.save(hamstrings);
        ExerciseGroup quadriceps = new ExerciseGroup("Quadriceps");
        quadriceps = exercisePrimaryRepository.save(quadriceps);
        ExerciseGroup back = new ExerciseGroup("Back");
        back = exercisePrimaryRepository.save(back);
        ExerciseGroup glute = new ExerciseGroup("Glute");
        glute = exercisePrimaryRepository.save(glute);
        ExerciseGroup ab = new ExerciseGroup("Ab");
        ab = exercisePrimaryRepository.save(ab);
        ExerciseGroup calves = new ExerciseGroup("Calves");
        calves = exercisePrimaryRepository.save(calves);
        ExerciseGroup forearmFlexors = new ExerciseGroup("Forearm Flexors");
        forearmFlexors = exercisePrimaryRepository.save(forearmFlexors);
        ExerciseGroup forearmExtensor = new ExerciseGroup("Forearm Extensor");
        forearmExtensor = exercisePrimaryRepository.save(forearmExtensor);

        Muscle muscle = new Muscle("quadriceps");
        muscleRepository.save(muscle);

        Muscle pectorals = new Muscle("pectorals");
        pectorals = muscleRepository.save(pectorals);

        Muscle lower_pectorals = new Muscle("lower_pectorals");
        lower_pectorals = muscleRepository.save(lower_pectorals);

        Muscle upper_pectorals = new Muscle("upper_pectorals");
        upper_pectorals = muscleRepository.save(upper_pectorals);

        Muscle rotator_cuff = new Muscle("rotator_cuff");
        rotator_cuff = muscleRepository.save(rotator_cuff);

        Muscle rear_delts = new Muscle("rear_delts");
        rear_delts = muscleRepository.save(rear_delts);

        Muscle front_delts = new Muscle("front_delts");
        front_delts = muscleRepository.save(front_delts);

        Muscle lateral_delts = new Muscle("lateral_delts");
        lateral_delts = muscleRepository.save(lateral_delts);

        Muscle biceps = new Muscle("biceps");
        biceps = muscleRepository.save(biceps);

        Muscle glutes = new Muscle("glutes");
        glutes = muscleRepository.save(glutes);

        Muscle adductors = new Muscle("adductors");
        adductors = muscleRepository.save(adductors);

        Muscle upper_back = new Muscle("upper_back");
        upper_back = muscleRepository.save(upper_back);


        Muscle abdominals = new Muscle("abdominals");
        abdominals = muscleRepository.save(abdominals);

        Muscle forearm_flexors_grip = new Muscle("forearm_flexors_grip");
        forearm_flexors_grip = muscleRepository.save(forearm_flexors_grip);

        Muscle forearm_extensor = new Muscle("forearm_extensor");
        forearm_extensor = muscleRepository.save(forearm_extensor);

        Muscle triceps2 = new Muscle("triceps");
        triceps2 = muscleRepository.save(triceps2);

        Muscle hamstrings2 = new Muscle("hamstrings");
        hamstrings2 = muscleRepository.save(hamstrings2);

        Muscle calves2 = new Muscle("calves2");
        calves2 = muscleRepository.save(calves2);

        Muscle quadriceps2 = new Muscle("quadriceps2");
        quadriceps2 = muscleRepository.save(quadriceps2);

        Muscle delts = new Muscle("delts");
        delts = muscleRepository.save(delts);


        Exercise bench = new Exercise("bench", "bench desc", 1.05, true, chest);
        bench = exerciseRepository.save(bench);
        Exercise squat = new Exercise("squat", "sqaut desc", 1.05, true, quadriceps);
        squat.setPrimaryMuscle(muscle);
        squat = exerciseRepository.save(squat);


        Exercise a1 = new Exercise("Bar Dip", "desc", 1.02, false, chest);
        a1.setPrimaryMuscle(triceps2);
        exerciseRepository.save(a1);
        Exercise a2 = new Exercise("Bench Press", "desc", 1.05, true, chest);
        a2.setPrimaryMuscle(pectorals);
        exerciseRepository.save(a2);
        Exercise a3 = new Exercise("Cable Chest Press", "desc", 1.05, true, chest);
        a3.setPrimaryMuscle(pectorals);
        exerciseRepository.save(a3);
        Exercise a4 = new Exercise("Close-Grip Bench Press", "desc", 1.02, true, chest);
        a4.setPrimaryMuscle(triceps2);
        exerciseRepository.save(a4);
        Exercise a5 = new Exercise("Close-Grip Feet-Up Bench Press", "desc", 1.02, true, chest);
        a5.setPrimaryMuscle(triceps2);
        exerciseRepository.save(a5);
        Exercise a6 = new Exercise("Decline Bench Press", "desc", 1.03, true, chest);
        a6.setPrimaryMuscle(lower_pectorals);
        exerciseRepository.save(a6);
        Exercise a7 = new Exercise("Dumbbell Chest Fly", "desc", 1.05, true, chest);
        a7.setPrimaryMuscle(pectorals);
        exerciseRepository.save(a7);
        Exercise a8 = new Exercise("Dumbbell Chest Press", "desc", 1.05, true, chest);
        a8.setPrimaryMuscle(pectorals);
        exerciseRepository.save(a8);
        Exercise a9 = new Exercise("Dumbbell Decline Chest Press", "desc", 1.03, true, chest);
        a9.setPrimaryMuscle(lower_pectorals);
        exerciseRepository.save(a9);
        Exercise a10 = new Exercise("Dumbbell Floor Press", "desc", 1.02, true, chest);
        a10.setPrimaryMuscle(triceps2);
        exerciseRepository.save(a10);
        Exercise a11 = new Exercise("Dumbbell Pullover", "desc", 1.05, true, chest);
        a11.setPrimaryMuscle(pectorals);
        exerciseRepository.save(a11);
        Exercise a12 = new Exercise("Feet-Up Bench Press", "desc", 1.05, true, chest);
        a12.setPrimaryMuscle(pectorals);
        exerciseRepository.save(a12);
        Exercise a13 = new Exercise("Floor Press", "desc", 1.02, true, chest);
        a13.setPrimaryMuscle(triceps2);
        exerciseRepository.save(a13);
        Exercise a14 = new Exercise("Incline Bench Press", "desc", 1.03, true, chest);
        a14.setPrimaryMuscle(upper_pectorals);
        exerciseRepository.save(a14);
        Exercise a15 = new Exercise("Incline Dumbbell Press", "desc", 1.03, true, chest);
        a15.setPrimaryMuscle(upper_pectorals);
        exerciseRepository.save(a15);
        Exercise a16 = new Exercise("Incline Push-Up", "desc", 1.03, false, chest);
        a16.setPrimaryMuscle(upper_pectorals);
        exerciseRepository.save(a16);
        Exercise a17 = new Exercise("Kneeling Incline Push-Up", "desc", 1.03, false, chest);
        a17.setPrimaryMuscle(upper_pectorals);
        exerciseRepository.save(a17);
        Exercise a18 = new Exercise("Kneeling Push-Up", "desc", 1.05, false, chest);
        a18.setPrimaryMuscle(pectorals);
        exerciseRepository.save(a18);
        Exercise a19 = new Exercise("Machine Chest Fly", "desc", 1.05, true, chest);
        a19.setPrimaryMuscle(pectorals);
        exerciseRepository.save(a19);
        Exercise a20 = new Exercise("Machine Chest Press", "desc", 1.05, true, chest);
        a20.setPrimaryMuscle(pectorals);
        exerciseRepository.save(a20);
        Exercise a21 = new Exercise("Pec Deck", "desc", 1.05, true, chest);
        a21.setPrimaryMuscle(pectorals);
        exerciseRepository.save(a21);
        Exercise a22 = new Exercise("Push-Up", "desc", 1.05, false, chest);
        a22.setPrimaryMuscle(pectorals);
        exerciseRepository.save(a22);
        Exercise a23 = new Exercise("Push-Up Against Wall", "desc", 1.05, false, chest);
        a23.setPrimaryMuscle(pectorals);
        exerciseRepository.save(a23);
        Exercise a24 = new Exercise("Push-Ups With Feet in Rings", "desc", 1.05, false, chest);
        a24.setPrimaryMuscle(pectorals);
        exerciseRepository.save(a24);
        Exercise a25 = new Exercise("Resistance Band Chest Fly", "desc", 1.05, false, chest);
        a25.setPrimaryMuscle(pectorals);
        exerciseRepository.save(a25);
        Exercise a26 = new Exercise("Smith Machine Bench Press", "desc", 1.05, true, chest);
        a26.setPrimaryMuscle(pectorals);
        exerciseRepository.save(a26);
        Exercise a27 = new Exercise("Smith Machine Incline Bench Press", "desc", 1.03, true, chest);
        a27.setPrimaryMuscle(upper_pectorals);
        exerciseRepository.save(a27);
        Exercise a28 = new Exercise("Standing Cable Chest Fly", "desc", 1.05, true, chest);
        a28.setPrimaryMuscle(pectorals);
        exerciseRepository.save(a28);
        Exercise a29 = new Exercise("Standing Resistance Band Chest Fly", "desc", 1.05, true, chest);
        a29.setPrimaryMuscle(pectorals);
        exerciseRepository.save(a29);
        Exercise a30 = new Exercise("Band External Shoulder Rotation", "desc", 1.02, false, shoulder);
        a30.setPrimaryMuscle(rotator_cuff);
        exerciseRepository.save(a30);
        Exercise a31 = new Exercise("Band Internal Shoulder Rotation", "desc", 1.02, false, shoulder);
        a31.setPrimaryMuscle(rotator_cuff);
        exerciseRepository.save(a31);
        Exercise a32 = new Exercise("Band Pull-Apart", "desc", 1.02, false, shoulder);
        a32.setPrimaryMuscle(rear_delts);
        exerciseRepository.save(a32);
        Exercise a33 = new Exercise("Barbell Front Raise", "desc", 1.02, true, shoulder);
        a33.setPrimaryMuscle(front_delts);
        exerciseRepository.save(a33);
        Exercise a34 = new Exercise("Barbell Rear Delt Row", "desc", 1.02, true, shoulder);
        a34.setPrimaryMuscle(rear_delts);
        exerciseRepository.save(a34);
        Exercise a35 = new Exercise("Barbell Upright Row", "desc", 1.02, true, shoulder);
        a35.setPrimaryMuscle(lateral_delts);
        exerciseRepository.save(a35);
        Exercise a36 = new Exercise("Behind the Neck Press", "desc", 1.02, true, shoulder);
        a36.setPrimaryMuscle(delts);
        exerciseRepository.save(a36);
        Exercise a37 = new Exercise("Cable Lateral Raise", "desc", 1.02, false, shoulder);
        a37.setPrimaryMuscle(lateral_delts);
        exerciseRepository.save(a37);
        Exercise a38 = new Exercise("Cable Rear Delt Row", "desc", 1.02, true, shoulder);
        a38.setPrimaryMuscle(rear_delts);
        exerciseRepository.save(a38);
        Exercise a39 = new Exercise("Dumbbell Front Raise", "desc", 1.02, true, shoulder);
        a39.setPrimaryMuscle(front_delts);
        exerciseRepository.save(a39);
        Exercise a40 = new Exercise("Dumbbell Horizontal Internal Shoulder Rot", "desc", 1.02, true, shoulder);
        a40.setPrimaryMuscle(rotator_cuff);
        exerciseRepository.save(a40);
        Exercise a41 = new Exercise("Dumbbell Horizontal External Shoulder Rot", "desc", 1.02, true, shoulder);
        a41.setPrimaryMuscle(rotator_cuff);
        exerciseRepository.save(a41);
        Exercise a42 = new Exercise("Dumbbell Lateral Raise", "desc", 1.02, true, shoulder);
        a42.setPrimaryMuscle(lateral_delts);
        exerciseRepository.save(a42);
        Exercise a43 = new Exercise("Dumbbell Rear Delt Row", "desc", 1.02, true, shoulder);
        a43.setPrimaryMuscle(rear_delts);
        exerciseRepository.save(a43);
        Exercise a44 = new Exercise("Dumbbell Shoulder Press", "desc", 1.02, true, shoulder);
        a44.setPrimaryMuscle(delts);
        exerciseRepository.save(a44);
        Exercise a45 = new Exercise("Face Pull", "desc", 1.02, true, shoulder);
        a45.setPrimaryMuscle(rear_delts);
        exerciseRepository.save(a45);
        Exercise a46 = new Exercise("Front Hold", "desc", 1.02, true, shoulder);
        a46.setPrimaryMuscle(rotator_cuff);
        exerciseRepository.save(a46);
        Exercise a47 = new Exercise("Lying Dumbbell External Shoulder Rotation", "desc", 1.02, true, shoulder);
        a47.setPrimaryMuscle(rotator_cuff);
        exerciseRepository.save(a47);
        Exercise a48 = new Exercise("Lying Dumbbell Internal Shoulder Rotation", "desc", 1.02, true, shoulder);
        a48.setPrimaryMuscle(rotator_cuff);
        exerciseRepository.save(a48);
        Exercise a49 = new Exercise("Machine Lateral Raise", "desc", 1.02, true, shoulder);
        a49.setPrimaryMuscle(lateral_delts);
        exerciseRepository.save(a49);
        Exercise a50 = new Exercise("Machine Shoulder Press", "desc", 1.02, true, shoulder);
        a50.setPrimaryMuscle(delts);
        exerciseRepository.save(a50);
        Exercise a51 = new Exercise("Monkey Row", "desc", 1.02, true, shoulder);
        a51.setPrimaryMuscle(rear_delts);
        exerciseRepository.save(a51);
        Exercise a52 = new Exercise("Overhead Press", "desc", 1.02, true, shoulder);
        a52.setPrimaryMuscle(delts);
        exerciseRepository.save(a52);
        Exercise a53 = new Exercise("Plate Front Raise", "desc", 1.02, true, shoulder);
        a53.setPrimaryMuscle(front_delts);
        exerciseRepository.save(a53);
        Exercise a54 = new Exercise("Power Jerk", "desc", 1.02, true, shoulder);
        a54.setPrimaryMuscle(delts);
        exerciseRepository.save(a54);
        Exercise a55 = new Exercise("Push Press", "desc", 1.02, true, shoulder);
        a55.setPrimaryMuscle(delts);
        exerciseRepository.save(a55);
        Exercise a56 = new Exercise("Reverse Dumbbell Flyes", "desc", 1.02, true, shoulder);
        a56.setPrimaryMuscle(rear_delts);
        exerciseRepository.save(a56);
        Exercise a57 = new Exercise("Reverse Machine Fly", "desc", 1.02, true, shoulder);
        a57.setPrimaryMuscle(rear_delts);
        exerciseRepository.save(a57);
        Exercise a58 = new Exercise("Seated Dumbbell Shoulder Press", "desc", 1.02, true, shoulder);
        a58.setPrimaryMuscle(delts);
        exerciseRepository.save(a58);
        Exercise a59 = new Exercise("Seated Barbell Overhead Press", "desc", 1.02, true, shoulder);
        a59.setPrimaryMuscle(delts);
        exerciseRepository.save(a59);
        Exercise a60 = new Exercise("Seated Smith Machine Shoulder Press", "desc", 1.02, true, shoulder);
        a60.setPrimaryMuscle(delts);
        exerciseRepository.save(a60);
        Exercise a61 = new Exercise("Snatch Grip Behind the Neck Press", "desc", 1.02, true, shoulder);
        a61.setPrimaryMuscle(delts);
        exerciseRepository.save(a61);
        Exercise a62 = new Exercise("Squat Jerk", "desc", 1.02, true, shoulder);
        a62.setPrimaryMuscle(delts);
        exerciseRepository.save(a62);
        Exercise a63 = new Exercise("Split Jerk", "desc", 1.02, true, shoulder);
        a63.setPrimaryMuscle(delts);
        exerciseRepository.save(a63);
        Exercise a64 = new Exercise("Barbell Curl", "desc", 1.01, true, bicep);
        a64.setPrimaryMuscle(biceps);
        exerciseRepository.save(a64);
        Exercise a65 = new Exercise("Barbell Preacher Curl", "desc", 1.01, true, bicep);
        a65.setPrimaryMuscle(biceps);
        exerciseRepository.save(a65);
        Exercise a66 = new Exercise("Bodyweight Curl", "desc", 1.01, true, bicep);
        a66.setPrimaryMuscle(biceps);
        exerciseRepository.save(a66);
        Exercise a67 = new Exercise("Cable Curl With Bar", "desc", 1.01, true, bicep);
        a67.setPrimaryMuscle(biceps);
        exerciseRepository.save(a67);
        Exercise a68 = new Exercise("Cable Curl With Rope", "desc", 1.01, true, bicep);
        a68.setPrimaryMuscle(biceps);
        exerciseRepository.save(a68);
        Exercise a69 = new Exercise("Concentration Curl", "desc", 1.01, true, bicep);
        a69.setPrimaryMuscle(biceps);
        exerciseRepository.save(a69);
        Exercise a70 = new Exercise("Dumbbell Curl", "desc", 1.01, true, bicep);
        a70.setPrimaryMuscle(biceps);
        exerciseRepository.save(a70);
        Exercise a71 = new Exercise("Dumbbell Preacher Curl", "desc", 1.01, true, bicep);
        a71.setPrimaryMuscle(biceps);
        exerciseRepository.save(a71);
        Exercise a72 = new Exercise("Hammer Curl", "desc", 1.01, true, bicep);
        a72.setPrimaryMuscle(biceps);
        exerciseRepository.save(a72);
        Exercise a73 = new Exercise("Incline Dumbbell Curl", "desc", 1.01, true, bicep);
        a73.setPrimaryMuscle(biceps);
        exerciseRepository.save(a73);
        Exercise a74 = new Exercise("Machine Bicep Curl", "desc", 1.01, true, bicep);
        a74.setPrimaryMuscle(biceps);
        exerciseRepository.save(a74);
        Exercise a75 = new Exercise("Spider Curl", "desc", 1.01, true, bicep);
        a75.setPrimaryMuscle(biceps);
        exerciseRepository.save(a75);
        Exercise a76 = new Exercise("Barbell Standing Triceps Extension", "desc", 1.01, true, triceps);
        a76.setPrimaryMuscle(triceps2);
        exerciseRepository.save(a76);
        Exercise a77 = new Exercise("Barbell Lying Triceps Extension", "desc", 1.01, true, triceps);
        a77.setPrimaryMuscle(triceps2);
        exerciseRepository.save(a77);
        Exercise a78 = new Exercise("Bench Dip", "desc", 1.01, true, triceps);
        a78.setPrimaryMuscle(triceps2);
        exerciseRepository.save(a78);
        Exercise a79 = new Exercise("Close-Grip Push-Up", "desc", 1.01, false, triceps);
        a79.setPrimaryMuscle(triceps2);
        exerciseRepository.save(a79);
        Exercise a80 = new Exercise("Dumbbell Lying Triceps Extension", "desc", 1.01, true, triceps);
        a80.setPrimaryMuscle(triceps2);
        exerciseRepository.save(a80);
        Exercise a81 = new Exercise("Dumbbell Standing Triceps Extension", "desc", 1.01, true, triceps);
        a81.setPrimaryMuscle(triceps2);
        exerciseRepository.save(a81);
        Exercise a82 = new Exercise("Overhead Cable Triceps Extension", "desc", 1.01, true, triceps);
        a82.setPrimaryMuscle(triceps2);
        exerciseRepository.save(a82);
        Exercise a83 = new Exercise("Tricep Bodyweight Extension", "desc", 1.01, false, triceps);
        a83.setPrimaryMuscle(triceps2);
        exerciseRepository.save(a83);
        Exercise a84 = new Exercise("Tricep Pushdown With Bar", "desc", 1.01, true, triceps);
        a84.setPrimaryMuscle(triceps2);
        exerciseRepository.save(a84);
        Exercise a85 = new Exercise("Tricep Pushdown With Rope", "desc", 1.01, true, triceps);
        a85.setPrimaryMuscle(triceps2);
        exerciseRepository.save(a85);
        Exercise a86 = new Exercise("Air Squat", "desc", 1.05, false, leg);
        a86.setPrimaryMuscle(quadriceps2); //hamstrings, glutes, calves
        exerciseRepository.save(a86);
        Exercise a87 = new Exercise("Barbell Hack Squat", "desc", 1.05, true, leg);
        a87.setPrimaryMuscle(quadriceps2); //hamstrings, glutes, calves
        exerciseRepository.save(a87);
        Exercise a88 = new Exercise("Barbell Lunge", "desc", 1.05, true, leg);
        a88.setPrimaryMuscle(quadriceps2); //hamstrings, glutes, calves
        exerciseRepository.save(a88);
        Exercise a89 = new Exercise("Barbell Walking Lunge", "desc", 1.05, true, leg);
        a89.setPrimaryMuscle(quadriceps2); //hamstrings, glutes, calves
        exerciseRepository.save(a89);
        Exercise a90 = new Exercise("Belt Squat", "desc", 1.05, true, leg);
        a90.setPrimaryMuscle(quadriceps2); //hamstrings, glutes, calves
        exerciseRepository.save(a90);
        Exercise a91 = new Exercise("Body Weight Lunge", "desc", 1.05, false, leg);
        a91.setPrimaryMuscle(quadriceps2); //hamstrings, glutes, calves
        exerciseRepository.save(a91);
        Exercise a92 = new Exercise("Box Squat", "desc", 1.05, true, leg);
        a92.setPrimaryMuscle(quadriceps2); //hamstrings, glutes, calves
        exerciseRepository.save(a92);
        Exercise a93 = new Exercise("Bulgarian Split Squat", "desc", 1.05, true, leg);
        a93.setPrimaryMuscle(quadriceps2); //hamstrings, glutes, calves
        exerciseRepository.save(a93);
        Exercise a94 = new Exercise("Chair Squat", "desc", 1.05, false, leg);
        a94.setPrimaryMuscle(quadriceps2); //hamstrings, glutes, calves
        exerciseRepository.save(a94);
        Exercise a95 = new Exercise("Dumbbell Lunge", "desc", 1.05, true, leg);
        a95.setPrimaryMuscle(quadriceps2); //hamstrings, glutes, calves
        exerciseRepository.save(a95);
        Exercise a96 = new Exercise("Dumbbell Squat", "desc", 1.05, true, leg);
        a96.setPrimaryMuscle(quadriceps2); //hamstrings, glutes, calves
        exerciseRepository.save(a96);
        Exercise a97 = new Exercise("Front Squat", "desc", 1.05, true, leg);
        a97.setPrimaryMuscle(quadriceps2); //hamstrings, glutes, calves
        exerciseRepository.save(a97);
        Exercise a98 = new Exercise("Goblet Squat", "desc", 1.05, true, leg);
        a98.setPrimaryMuscle(quadriceps2); //hamstrings, glutes, calves
        exerciseRepository.save(a98);
        Exercise a99 = new Exercise("Hack Squat Machine", "desc", 1.05, true, leg);
        a99.setPrimaryMuscle(quadriceps2); //hamstrings, glutes, calves
        exerciseRepository.save(a99);
        Exercise a100 = new Exercise("Half Air Squat", "desc", 1.05, false, leg);
        a100.setPrimaryMuscle(quadriceps2); //hamstrings, glutes, calves
        exerciseRepository.save(a100);
        Exercise a101 = new Exercise("Hip Adduction Machine", "desc", 1.05, true, leg);
        a101.setPrimaryMuscle(adductors);
        exerciseRepository.save(a101);
        Exercise a102 = new Exercise("Landmine Hack Squat", "desc", 1.03, true, leg);
        a102.setPrimaryMuscle(quadriceps2); //hamstrings, glutes, calves
        exerciseRepository.save(a102);
        Exercise a103 = new Exercise("Landmine Squat", "desc", 1.05, true, leg);
        a103.setPrimaryMuscle(quadriceps2); //hamstrings, glutes, calves
        exerciseRepository.save(a103);
        Exercise a104 = new Exercise("Leg Extension", "desc", 1.05, true, leg);
        a104.setPrimaryMuscle(quadriceps2);
        exerciseRepository.save(a104);
        Exercise a105 = new Exercise("Leg Press", "desc", 1.05, true, leg);
        a105.setPrimaryMuscle(quadriceps2); //hamstrings, glutes, calves
        exerciseRepository.save(a105);
        Exercise a106 = new Exercise("Lying Leg Curl", "desc", 1.05, true, leg);
        a106.setPrimaryMuscle(hamstrings2);
        exerciseRepository.save(a106);
        Exercise a107 = new Exercise("Pause Squat", "desc", 1.05, true, leg);
        a107.setPrimaryMuscle(quadriceps2); //hamstrings, glutes, calves
        exerciseRepository.save(a107);
        Exercise a108 = new Exercise("Romanian Deadlift", "desc", 1.05, true, leg);
        a108.setPrimaryMuscle(hamstrings2);
        exerciseRepository.save(a108); // glutes, lower_back
        Exercise a109 = new Exercise("Safety Bar Squat", "desc", 1.05, true, leg);
        a109.setPrimaryMuscle(quadriceps2); //hamstrings, glutes, calves
        exerciseRepository.save(a109);
        Exercise a110 = new Exercise("Seated Leg Curl", "desc", 1.05, true, leg);
        a110.setPrimaryMuscle(hamstrings2);
        exerciseRepository.save(a110);
        Exercise a111 = new Exercise("Shallow Body Weight Lunge", "desc", 1.05, true, leg);
        a111.setPrimaryMuscle(quadriceps2); //hamstrings, glutes, calves
        exerciseRepository.save(a111);
        Exercise a112 = new Exercise("Side Lunges (Bodyweight)", "desc", 1.05, true, leg);
        a112.setPrimaryMuscle(quadriceps2); //hamstrings, glutes, calves
        exerciseRepository.save(a112);
        Exercise a113 = new Exercise("Smith Machine Squat", "desc", 1.05, true, leg);
        a113.setPrimaryMuscle(quadriceps2); //hamstrings, glutes, calves
        exerciseRepository.save(a113);
        Exercise a114 = new Exercise("Squat", "desc", 1.05, true, leg);
        a114.setPrimaryMuscle(quadriceps2); //hamstrings, glutes, calves
        exerciseRepository.save(a114);
        Exercise a115 = new Exercise("Step Up", "desc", 1.05, true, leg);
        a115.setPrimaryMuscle(quadriceps2); //hamstrings, glutes, calves
        exerciseRepository.save(a115);
        Exercise a116 = new Exercise("Back Extension", "desc", 1.03, true, back);
        a116.setPrimaryMuscle(null);
        exerciseRepository.save(a116);
        Exercise a117 = new Exercise("Barbell Row", "desc", 1.03, true, back);
        a117.setPrimaryMuscle(null);
        exerciseRepository.save(a117);
        Exercise a118 = new Exercise("Barbell Shrug", "desc", 1.03, true, back);
        a118.setPrimaryMuscle(null);
        exerciseRepository.save(a118);
        Exercise a119 = new Exercise("Block Snatch", "desc", 1.03, true, back);
        a119.setPrimaryMuscle(null);
        exerciseRepository.save(a119);
        Exercise a120 = new Exercise("Cable Close Grip Seated Row", "desc", 1.03, true, back);
        a120.setPrimaryMuscle(null);
        exerciseRepository.save(a120);
        Exercise a121 = new Exercise("Cable Wide Grip Seated Row", "desc", 1.03, true, back);
        a121.setPrimaryMuscle(null);
        exerciseRepository.save(a121);
        Exercise a122 = new Exercise("Chin-Up", "desc", 1.03, false, back);
        a122.setPrimaryMuscle(null);
        exerciseRepository.save(a122);
        Exercise a123 = new Exercise("Clean", "desc", 1.03, true, back);
        a123.setPrimaryMuscle(null);
        exerciseRepository.save(a123);
        Exercise a124 = new Exercise("Clean and Jerk", "desc", 1.03, true, back);
        a124.setPrimaryMuscle(null);
        exerciseRepository.save(a124);
        Exercise a125 = new Exercise("Deadlift", "desc", 1.05, true, back);
        a125.setPrimaryMuscle(null);
        exerciseRepository.save(a125);
        Exercise a126 = new Exercise("Deficit Deadlift", "desc", 1.05, true, back);
        a126.setPrimaryMuscle(null);
        exerciseRepository.save(a126);
        Exercise a127 = new Exercise("Dumbbell Deadlift", "desc", 1.05, true, back);
        a127.setPrimaryMuscle(null);
        exerciseRepository.save(a127);
        Exercise a128 = new Exercise("Dumbbell Row", "desc", 1.03, true, back);
        a128.setPrimaryMuscle(null);
        exerciseRepository.save(a128);
        Exercise a129 = new Exercise("Dumbbell Shrug", "desc", 1.03, true, back);
        a129.setPrimaryMuscle(null);
        exerciseRepository.save(a129);
        Exercise a130 = new Exercise("Floor Back Extension", "desc", 1.03, false, back);
        a130.setPrimaryMuscle(null);
        exerciseRepository.save(a130);
        Exercise a131 = new Exercise("Good Morning", "desc", 1.03, true, back);
        a131.setPrimaryMuscle(null);
        exerciseRepository.save(a131);
        Exercise a132 = new Exercise("Hang Clean", "desc", 1.03, true, back);
        a132.setPrimaryMuscle(null);
        exerciseRepository.save(a132);
        Exercise a133 = new Exercise("Hang Power Clean", "desc", 1.02, true, back);
        a133.setPrimaryMuscle(null);
        exerciseRepository.save(a133);
        Exercise a134 = new Exercise("Hang Power Snatch", "desc", 1.02, true, back);
        a134.setPrimaryMuscle(null);
        exerciseRepository.save(a134);
        Exercise a135 = new Exercise("Hang Snatch", "desc", 1.02, true, back);
        a135.setPrimaryMuscle(null);
        exerciseRepository.save(a135);
        Exercise a136 = new Exercise("Inverted Row", "desc", 1.02, true, back);
        a136.setPrimaryMuscle(null);
        exerciseRepository.save(a136);
        Exercise a137 = new Exercise("Inverted Row with Underhand Grip", "desc", 1.02, true, back);
        a137.setPrimaryMuscle(null);
        exerciseRepository.save(a137);
        Exercise a138 = new Exercise("Kettlebell Swing", "desc", 1.02, true, back);
        a138.setPrimaryMuscle(null);
        exerciseRepository.save(a138);
        Exercise a139 = new Exercise("Lat Pulldown With Pronated Grip", "desc", 1.02, true, back);
        a139.setPrimaryMuscle(null);
        exerciseRepository.save(a139);
        Exercise a140 = new Exercise("Lat Pulldown With Supinated Grip", "desc", 1.02, true, back);
        a140.setPrimaryMuscle(null);
        exerciseRepository.save(a140);
        Exercise a141 = new Exercise("One-Handed Cable Row", "desc", 1.02, true, back);
        a141.setPrimaryMuscle(null);
        exerciseRepository.save(a141);
        Exercise a142 = new Exercise("One-Handed Lat Pulldown", "desc", 1.02, true, back);
        a142.setPrimaryMuscle(null);
        exerciseRepository.save(a142);
        Exercise a143 = new Exercise("Pause Deadlift", "desc", 1.02, true, back);
        a143.setPrimaryMuscle(null);
        exerciseRepository.save(a143);
        Exercise a144 = new Exercise("Pendlay Row", "desc", 1.02, true, back);
        a144.setPrimaryMuscle(null);
        exerciseRepository.save(a144);
        Exercise a145 = new Exercise("Power Clean", "desc", 1.02, true, back);
        a145.setPrimaryMuscle(null);
        exerciseRepository.save(a145);
        Exercise a146 = new Exercise("Power Snatch", "desc", 1.02, true, back);
        a146.setPrimaryMuscle(null);
        exerciseRepository.save(a146);
        Exercise a147 = new Exercise("Pull-Up", "desc", 1.02, false, back);
        a147.setPrimaryMuscle(null);
        exerciseRepository.save(a147);
        Exercise a148 = new Exercise("Rack Pull", "desc", 1.02, true, back);
        a148.setPrimaryMuscle(null);
        exerciseRepository.save(a148);
        Exercise a149 = new Exercise("Seal Row", "desc", 1.02, true, back);
        a149.setPrimaryMuscle(null);
        exerciseRepository.save(a149);
        Exercise a150 = new Exercise("Seated Machine Row", "desc", 1.02, true, back);
        a150.setPrimaryMuscle(null);
        exerciseRepository.save(a150);
        Exercise a151 = new Exercise("Snatch", "desc", 1.02, true, back);
        a151.setPrimaryMuscle(null);
        exerciseRepository.save(a151);
        Exercise a152 = new Exercise("Snatch Grip Deadlift", "desc", 1.02, true, back);
        a152.setPrimaryMuscle(null);
        exerciseRepository.save(a152);
        Exercise a153 = new Exercise("Stiff-Legged Deadlift", "desc", 1.02, true, back);
        a153.setPrimaryMuscle(null);
        exerciseRepository.save(a153);
        Exercise a154 = new Exercise("Straight Arm Lat Pulldown", "desc", 1.02, true, back);
        a154.setPrimaryMuscle(null);
        exerciseRepository.save(a154);
        Exercise a155 = new Exercise("Sumo Deadlift", "desc", 1.02, true, back);
        a155.setPrimaryMuscle(null);
        exerciseRepository.save(a155);
        Exercise a156 = new Exercise("T-Bar Row", "desc", 1.02, true, back);
        a156.setPrimaryMuscle(upper_back);
        exerciseRepository.save(a156);
        Exercise a157 = new Exercise("Trap Bar Deadlift With High Handles", "desc", 1.02, true, back);
        a157.setPrimaryMuscle(hamstrings2);
        exerciseRepository.save(a157);
        Exercise a158 = new Exercise("Trap Bar Deadlift With Low Handles", "desc", 1.02, true, back);
        a158.setPrimaryMuscle(hamstrings2);
        exerciseRepository.save(a158);
        Exercise a159 = new Exercise("Banded Side Kicks", "desc", 1.02, true, glute);
        a159.setPrimaryMuscle(glutes);
        exerciseRepository.save(a159);
        Exercise a160 = new Exercise("Cable Pull Through", "desc", 1.02, true, glute);
        a160.setPrimaryMuscle(glutes);
        exerciseRepository.save(a160);
        Exercise a161 = new Exercise("Clamshells", "desc", 1.02, true, glute);
        a161.setPrimaryMuscle(glutes);
        exerciseRepository.save(a161);
        Exercise a162 = new Exercise("Dumbbell Romanian Deadlift", "desc", 1.04, true, glute);
        a162.setPrimaryMuscle(hamstrings2);
        exerciseRepository.save(a162);
        Exercise a163 = new Exercise("Dumbbell Frog Pumps", "desc", 1.03, true, glute);
        a163.setPrimaryMuscle(glutes);
        exerciseRepository.save(a163);
        Exercise a164 = new Exercise("Fire Hydrants", "desc", 1.03, false, glute);
        a164.setPrimaryMuscle(glutes);
        exerciseRepository.save(a164);
        Exercise a165 = new Exercise("Frog Pumps", "desc", 1.03, false, glute);
        a165.setPrimaryMuscle(glutes);
        exerciseRepository.save(a165);
        Exercise a166 = new Exercise("Glute Bridge", "desc", 1.03, false, glute);
        a166.setPrimaryMuscle(glutes);
        exerciseRepository.save(a166);
        Exercise a167 = new Exercise("Hip Abduction Against Band", "desc", 1.03, false, glute);
        a167.setPrimaryMuscle(glutes);
        exerciseRepository.save(a167);
        Exercise a168 = new Exercise("Hip Abduction Machine", "desc", 1.03, true, glute);
        a168.setPrimaryMuscle(glutes);
        exerciseRepository.save(a168);
        Exercise a169 = new Exercise("Hip Thrust", "desc", 1.03, true, glute);
        a169.setPrimaryMuscle(glutes);
        exerciseRepository.save(a169);
        Exercise a170 = new Exercise("Hip Thrust Machine", "desc", 1.03, true, glute);
        a170.setPrimaryMuscle(glutes);
        exerciseRepository.save(a170);
        Exercise a171 = new Exercise("Hip Thrust With Band Around Knees", "desc", 1.03, true, glute);
        a171.setPrimaryMuscle(glutes);
        exerciseRepository.save(a171);
        Exercise a172 = new Exercise("Lateral Walk With Band", "desc", 1.03, false, glute);
        a172.setPrimaryMuscle(glutes);
        exerciseRepository.save(a172);
        Exercise a173 = new Exercise("Machine Glute Kickbacks", "desc", 1.03, true, glute);
        a173.setPrimaryMuscle(glutes);
        exerciseRepository.save(a173);
        Exercise a174 = new Exercise("One-Legged Glute Bridge", "desc", 1.03, false, glute);
        a174.setPrimaryMuscle(glutes);
        exerciseRepository.save(a174);
        Exercise a175 = new Exercise("One-Legged Hip Thrust", "desc", 1.03, false, glute);
        a175.setPrimaryMuscle(glutes);
        exerciseRepository.save(a175);
        Exercise a176 = new Exercise("Romanian Deadlift", "desc", 1.04, true, glute);
        a176.setPrimaryMuscle(hamstrings2);
        exerciseRepository.save(a176);
        Exercise a177 = new Exercise("Single Leg Romanian Deadlift", "desc", 1.04, true, glute);
        a177.setPrimaryMuscle(hamstrings2);
        exerciseRepository.save(a177);
        Exercise a178 = new Exercise("Standing Glute Kickback in Machine", "desc", 1.03, true, glute);
        a178.setPrimaryMuscle(glutes);
        exerciseRepository.save(a178);
        Exercise a179 = new Exercise("Step Up", "desc", 1.03, false, glute);
        a179.setPrimaryMuscle(glutes);
        exerciseRepository.save(a179);
        Exercise a180 = new Exercise("Cable Crunch", "desc", 1.01, false, ab);
        a180.setPrimaryMuscle(abdominals);
        exerciseRepository.save(a180);
        Exercise a181 = new Exercise("Crunch", "desc", 1.01, false, ab);
        a181.setPrimaryMuscle(abdominals);
        exerciseRepository.save(a181);
        Exercise a182 = new Exercise("Dead Bug", "desc", 1.01, false, ab);
        a182.setPrimaryMuscle(abdominals);
        exerciseRepository.save(a182);
        Exercise a183 = new Exercise("Hanging Leg Raise", "desc", 1.01, false, ab);
        a183.setPrimaryMuscle(abdominals);
        exerciseRepository.save(a183);
        Exercise a184 = new Exercise("Hanging Knee Raise", "desc", 1.01, false, ab);
        a184.setPrimaryMuscle(abdominals);
        exerciseRepository.save(a184);
        Exercise a185 = new Exercise("Hanging Sit-Up", "desc", 1.01, false, ab);
        a185.setPrimaryMuscle(abdominals);
        exerciseRepository.save(a185);
        Exercise a186 = new Exercise("High to Low Wood Chop", "desc", 1.01, false, ab);
        a186.setPrimaryMuscle(abdominals);
        exerciseRepository.save(a186);
        Exercise a187 = new Exercise("Horizontal Wood Chop", "desc", 1.01, false, ab);
        a187.setPrimaryMuscle(abdominals);
        exerciseRepository.save(a187);
        Exercise a188 = new Exercise("Kneeling Ab Wheel Roll-Out", "desc", 1.01, false, ab);
        a188.setPrimaryMuscle(abdominals);
        exerciseRepository.save(a188);
        Exercise a189 = new Exercise("Kneeling Plank", "desc", 1.01, false, ab);
        a189.setPrimaryMuscle(abdominals);
        exerciseRepository.save(a189);
        Exercise a190 = new Exercise("Kneeling Side Plank", "desc", 1.01, false, ab);
        a190.setPrimaryMuscle(abdominals);
        exerciseRepository.save(a190);
        Exercise a191 = new Exercise("Lying Leg Raise", "desc", 1.01, false, ab);
        a191.setPrimaryMuscle(abdominals);
        exerciseRepository.save(a191);
        Exercise a192 = new Exercise("Lying Windshield Wiper", "desc", 1.01, false, ab);
        a192.setPrimaryMuscle(abdominals); // obliques
        exerciseRepository.save(a192);
        Exercise a193 = new Exercise("Lying Windshield Wiper with Bent Knees", "desc", 1.01, false, ab);
        a193.setPrimaryMuscle(abdominals); // obliques
        exerciseRepository.save(a193);
        Exercise a194 = new Exercise("Machine Crunch", "desc", 1.01, true, ab);
        a194.setPrimaryMuscle(abdominals);
        exerciseRepository.save(a194);
        Exercise a195 = new Exercise("Mountain Climbers", "desc", 1.01, false, ab);
        a195.setPrimaryMuscle(abdominals);
        exerciseRepository.save(a195);
        Exercise a196 = new Exercise("Oblique Crunch", "desc", 1.01, false, ab);
        a196.setPrimaryMuscle(abdominals); // obliques
        exerciseRepository.save(a196);
        Exercise a197 = new Exercise("Oblique Sit-Up", "desc", 1.01, false, ab);
        a197.setPrimaryMuscle(abdominals); // obliques
        exerciseRepository.save(a197);
        Exercise a198 = new Exercise("Plank", "desc", 1.01, false, ab);
        a198.setPrimaryMuscle(abdominals);
        exerciseRepository.save(a198);
        Exercise a199 = new Exercise("Side Plank", "desc", 1.01, false, ab);
        a199.setPrimaryMuscle(abdominals); // obliques
        exerciseRepository.save(a199);
        Exercise a200 = new Exercise("Sit-Up", "desc", 1.01, false, ab);
        a200.setPrimaryMuscle(abdominals);
        exerciseRepository.save(a200);
        Exercise a201 = new Exercise("Eccentric Heel Drop", "desc", 1.02, false, calves);
        a201.setPrimaryMuscle(calves2);
        exerciseRepository.save(a201);
        Exercise a202 = new Exercise("Heel Raise", "desc", 1.02, true, calves);
        a202.setPrimaryMuscle(calves2);
        exerciseRepository.save(a202);
        Exercise a203 = new Exercise("Seated Calf Raise", "desc", 1.02, true, calves);
        a203.setPrimaryMuscle(calves2);
        exerciseRepository.save(a203);
        Exercise a204 = new Exercise("Standing Calf Raise", "desc", 1.02, true, calves);
        a204.setPrimaryMuscle(calves2);
        exerciseRepository.save(a204);
        Exercise a205 = new Exercise("Barbell Wrist Curl", "desc", 1.02, true, forearmFlexors);
        a205.setPrimaryMuscle(forearm_flexors_grip);
        exerciseRepository.save(a205);
        Exercise a206 = new Exercise("Barbell Wrist Curl Behind the Back", "desc", 1.02, true, forearmFlexors);
        a206.setPrimaryMuscle(forearm_flexors_grip);
        exerciseRepository.save(a206);
        Exercise a207 = new Exercise("Bar Hang", "desc", 1.02, false, forearmFlexors);
        a207.setPrimaryMuscle(forearm_flexors_grip);
        exerciseRepository.save(a207);
        Exercise a208 = new Exercise("Dumbbell Wrist Curl", "desc", 1.02, true, forearmFlexors);
        a208.setPrimaryMuscle(forearm_flexors_grip);
        exerciseRepository.save(a208);
        Exercise a209 = new Exercise("Farmers Walk", "desc", 1.02, true, forearmFlexors);
        a209.setPrimaryMuscle(forearm_flexors_grip);
        exerciseRepository.save(a209);
        Exercise a210 = new Exercise("Fat Bar Deadlift", "desc", 1.02, true, forearmFlexors);
        a210.setPrimaryMuscle(forearm_flexors_grip);
        exerciseRepository.save(a210);
        Exercise a211 = new Exercise("Gripper", "desc", 1.02, false, forearmFlexors);
        a211.setPrimaryMuscle(forearm_flexors_grip);
        exerciseRepository.save(a211);
        Exercise a212 = new Exercise("One-Handed Bar Hang", "desc", 1.02, false, forearmFlexors);
        a212.setPrimaryMuscle(forearm_flexors_grip);
        exerciseRepository.save(a212);
        Exercise a213 = new Exercise("Plate Pinch", "desc", 1.02, true, forearmFlexors);
        a213.setPrimaryMuscle(forearm_flexors_grip);
        exerciseRepository.save(a213);
        Exercise a214 = new Exercise("Plate Wrist Curl", "desc", 1.02, true, forearmFlexors);
        a214.setPrimaryMuscle(forearm_flexors_grip);
        exerciseRepository.save(a214);
        Exercise a215 = new Exercise("Towel Pull-Up", "desc", 1.02, false, forearmFlexors);
        a215.setPrimaryMuscle(forearm_flexors_grip);
        exerciseRepository.save(a215);
        Exercise a216 = new Exercise("Barbell Wrist Extension", "desc", 1.02, true, forearmExtensor);
        a216.setPrimaryMuscle(forearm_extensor);
        exerciseRepository.save(a216);
        Exercise a217 = new Exercise("Dumbbell Wrist Extension", "desc", 1.02, true, forearmExtensor);
        a217.setPrimaryMuscle(forearm_extensor);
        exerciseRepository.save(a217);


        TreningExercise treningExercise = new TreningExercise(bench, 1, 20, trening);
        trening.addTreningExercise(treningExercise);

        treningExerciseRepository.save(treningExercise);

    }
}