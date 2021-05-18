package io.github.seondongpyo.mapping.inheritance.singletable;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@NoArgsConstructor
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Entity
public abstract class Grocery {

    @Id @GeneratedValue
    private Long id;

    private int price;

}
