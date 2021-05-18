package io.github.seondongpyo.mapping.inheritance.tableperclass;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@Entity
public abstract class Vehicle {

    @Id @GeneratedValue
    private Long id;

    private int seat;

}
