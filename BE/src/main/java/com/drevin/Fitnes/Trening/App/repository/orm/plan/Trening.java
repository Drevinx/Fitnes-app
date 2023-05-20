package com.drevin.Fitnes.Trening.App.repository.orm.plan;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
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
public class Trening {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(length = 30)
    private String name;

    private int treningCount;

    private boolean isTreningcountComplete;

    private int treningOrder;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "plan_id")
    private Plan plan;

    @JsonIgnore
    @OneToMany(mappedBy = "trening", cascade = CascadeType.MERGE)
    private List<TreningExercise> treningExercises = new ArrayList<>();

    public Trening(Plan plan, String name) {
        this.name = name;
        this.plan = plan;
        this.treningCount = 0;
        this.isTreningcountComplete = false;
    }

    public void addTreningExercise(TreningExercise treningExercise) {
        treningExercises.add(treningExercise);
    }
}
