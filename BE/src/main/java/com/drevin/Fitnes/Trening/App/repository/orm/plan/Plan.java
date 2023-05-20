package com.drevin.Fitnes.Trening.App.repository.orm.plan;


import com.drevin.Fitnes.Trening.App.repository.orm.user.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@Setter
@Getter
@EqualsAndHashCode
public class Plan {

    @OneToMany(cascade = CascadeType.ALL, mappedBy = "plan")
    List<Trening> trenings = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 30)
    private String name;
    @Column(length = 30)
    private String description;
    @Max(value = 99, message = "Maximal values for duration is 99")
    @Column(length = 3)
    private int durationInWeek;
    @Max(value = 99, message = "Maximal value for exercise per week is 99")
    @Column(length = 3)
    private int treningPerWeek;
    private int planOrder;
    private int complete;
    private boolean isGeneratedActual;

    @JsonIgnore
    @ManyToOne
    private User user;

    public Plan(String name, String description, int durationInWeek, int treningPerWeek) {
        this.name = name;
        this.description = description;
        this.durationInWeek = durationInWeek;
        this.treningPerWeek = treningPerWeek;
        this.isGeneratedActual = false;
    }

    public void addTrening(Trening trening) {
        trenings.add(trening);
    }
}
