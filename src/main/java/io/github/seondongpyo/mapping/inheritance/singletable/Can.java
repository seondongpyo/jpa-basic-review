package io.github.seondongpyo.mapping.inheritance.singletable;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;

@Getter
@NoArgsConstructor
@Entity
public class Can extends Grocery {

    private String content;

    public Can(String content) {
        this.content = content;
    }
}
