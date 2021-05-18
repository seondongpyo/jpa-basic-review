package io.github.seondongpyo.mapping.inheritance.tableperclass;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Getter
@NoArgsConstructor
@Entity
public class Subway extends Vehicle {

    private String line;

    public Subway(String line) {
        this.line = line;
    }
}
