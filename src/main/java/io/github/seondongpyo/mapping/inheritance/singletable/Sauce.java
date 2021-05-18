package io.github.seondongpyo.mapping.inheritance.singletable;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Getter
@NoArgsConstructor
@Entity
public class Sauce extends Grocery {

    private String ingredient;

    public Sauce(String ingredient) {
        this.ingredient = ingredient;
    }
}
