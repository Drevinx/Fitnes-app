package com.drevin.Fitnes.Trening.App.orm;

import com.drevin.Fitnes.Trening.App.repository.orm.exercise.ExerciseDetailsRepository;
import com.drevin.Fitnes.Trening.App.repository.orm.exercise.ExerciseRepository;
import com.drevin.Fitnes.Trening.App.repository.orm.plan.Plan;
import com.drevin.Fitnes.Trening.App.repository.orm.plan.PlanRepository;
import com.drevin.Fitnes.Trening.App.repository.orm.plan.Trening;
import com.drevin.Fitnes.Trening.App.repository.orm.user.RoleRepository;
import com.drevin.Fitnes.Trening.App.repository.orm.user.UserRepository;
import com.drevin.Fitnes.Trening.App.repository.orm.user.*;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureTestEntityManager;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Base64;


import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

@ActiveProfiles("dev")
@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestEntityManager

class ApiOrmTest {

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
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;



    @Test
    public void testPlanCreate() throws Exception {

        String planName = "Test plan";
        String planDecs = "description plna";

        Plan plan = new Plan(planName,planDecs, 6,2);

        HttpHeaders headers = new HttpHeaders();
         headers.add(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().
                 encodeToString("username2:password".getBytes()));

        HttpEntity<Plan> request = new HttpEntity<>(plan, headers);

        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Plan> response = restTemplate.exchange("http://localhost:8080/api/v1/plans",
                HttpMethod.POST, request, Plan.class);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Plan createdPlan = response.getBody();
        assertNotNull(createdPlan);
        assertEquals(planName, createdPlan.getName());
        assertEquals(planDecs, createdPlan.getDescription());

    }


    @Test
    void testPlanPatch() throws Exception {

        Plan plan = new Plan("Test plan patch","description plan patch",
                6,2);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().
                encodeToString("username2:password".getBytes()));

        HttpEntity<Plan> request = new HttpEntity<>(plan, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Plan> response = restTemplate.exchange("http://localhost:8080/api/v1/plans",
                HttpMethod.POST, request, Plan.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Plan createdPlan = response.getBody();
        assertNotNull(createdPlan);

         Plan updatePlan = response.getBody();
        System.out.println(response.getBody().getId());
        updatePlan.setName("patch");
        updatePlan.setDescription("patch");
        updatePlan.setTreningPerWeek(99);
        updatePlan.setDurationInWeek(88);

        HttpEntity<Plan> requestPatch = new HttpEntity<>(updatePlan, headers);
        RestTemplate restTemplate2 = new RestTemplate(new HttpComponentsClientHttpRequestFactory());


        ResponseEntity<Plan> responsePatch = restTemplate2.exchange("http://localhost:8080/api/v1/plans/"+updatePlan.getId(),
                HttpMethod.PATCH, requestPatch, Plan.class);
        assertEquals(HttpStatus.OK, responsePatch.getStatusCode());
        Plan updPlan = response.getBody();
        assertNotNull(createdPlan);


        ResponseEntity<Plan> responseGet = restTemplate2.exchange("http://localhost:8080/api/v1/plans/"+updatePlan.getId(),
                HttpMethod.GET, requestPatch, Plan.class);
        assertEquals(HttpStatus.OK, responsePatch.getStatusCode());
        Plan getPlan = responseGet.getBody();
        assertNotNull(getPlan);


        Plan updatedFromDb = getPlan;
        assertThat(updatedFromDb.getDescription()).isEqualTo("patch");
        assertThat(updatedFromDb.getName()).isEqualTo("patch");
        assertThat(updatedFromDb.getTreningPerWeek()==99);
        assertThat(updatedFromDb.getDurationInWeek()==88);
    }


    @Test
    void testPlanDelete() throws Exception {

        Plan plan = new Plan("Test plan delete","description plan delete",
                6,2);

        HttpHeaders headers = new HttpHeaders();
        headers.add(HttpHeaders.AUTHORIZATION, "Basic " + Base64.getEncoder().
                encodeToString("username2:password".getBytes()));

        HttpEntity<Plan> request = new HttpEntity<>(plan, headers);
        RestTemplate restTemplate = new RestTemplate();

        ResponseEntity<Plan> response = restTemplate.exchange("http://localhost:8080/api/v1/plans",
                HttpMethod.POST, request, Plan.class);
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        Plan createdPlan = response.getBody();
        assertNotNull(createdPlan);


        assertThat(createdPlan.getDescription()).isEqualTo("description plan delete");
        assertThat(createdPlan.getName()).isEqualTo("Test plan delete");
        assertThat(createdPlan.getTreningPerWeek()==2);
        assertThat(createdPlan.getDurationInWeek()==6);

        HttpEntity<Plan> requestDelete = new HttpEntity<>(headers);
        RestTemplate restTemplate2 = new RestTemplate(new HttpComponentsClientHttpRequestFactory());


        ResponseEntity<Void> responseDelete = restTemplate2.exchange("http://localhost:8080/api/v1/plans/"+createdPlan.getId(),
                HttpMethod.DELETE, requestDelete, Void.class);
        assertEquals(HttpStatus.OK, responseDelete.getStatusCode());


        try {
            ResponseEntity<Void> responseDelete404 = restTemplate2.exchange("http://localhost:8080/api/v1/plans/"+createdPlan.getId(),
                    HttpMethod.DELETE, requestDelete, Void.class);

        } catch (HttpClientErrorException ex){
            assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
            assertTrue(ex.getResponseBodyAsString().contains("Plan id:" + createdPlan.getId()));
        }
    }




}
