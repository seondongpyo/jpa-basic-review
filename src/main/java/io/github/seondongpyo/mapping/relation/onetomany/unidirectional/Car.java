package io.github.seondongpyo.mapping.relation.onetomany.unidirectional;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class Car {

    @Id
    @GeneratedValue
    @Column(name = "CAR_ID")
    private Long id;

    private String name;

    public Car(String name) {
        this.name = name;
    }
}
