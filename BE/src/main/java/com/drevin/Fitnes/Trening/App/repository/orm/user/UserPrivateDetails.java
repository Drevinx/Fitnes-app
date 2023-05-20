package com.drevin.Fitnes.Trening.App.repository.orm.user;

import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
@Table(name = "user_private_details")
public class UserPrivateDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

/*    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;*/


    private String name;
    private String lastname;
    private int age;

    public UserPrivateDetails(String name, String lastname, int age) {
        // this.user = user;
        this.name = name;
        this.lastname = lastname;
        this.age = age;
    }
}
