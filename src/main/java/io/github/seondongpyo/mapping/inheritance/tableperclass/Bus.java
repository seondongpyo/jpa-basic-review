package io.github.seondongpyo.mapping.inheritance.tableperclass;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Getter
@NoArgsConstructor
@Entity
public class Bus extends Vehicle {

    private String number;

    public Bus(String number) {
        this.number = number;
    }
}
