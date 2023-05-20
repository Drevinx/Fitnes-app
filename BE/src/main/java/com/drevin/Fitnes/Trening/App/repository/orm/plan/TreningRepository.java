package com.drevin.Fitnes.Trening.App.repository.orm.plan;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TreningRepository extends JpaRepository<Trening, Long> {
    List<Trening> findByPlan(Plan plan);

}
