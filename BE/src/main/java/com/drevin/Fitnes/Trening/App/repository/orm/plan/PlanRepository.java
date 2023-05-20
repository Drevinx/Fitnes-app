package com.drevin.Fitnes.Trening.App.repository.orm.plan;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlanRepository extends JpaRepository<Plan, Long> {
    // List<Plan> findAllByUser(User user);

}
