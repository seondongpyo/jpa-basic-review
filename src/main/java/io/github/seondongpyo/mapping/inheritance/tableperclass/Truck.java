package io.github.seondongpyo.mapping.inheritance.tableperclass;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Getter
@NoArgsConstructor
@Entity
public class Truck extends Vehicle {

    private int tonCapacity;

    public Truck(int tonCapacity) {
        this.tonCapacity = tonCapacity;
    }
}
