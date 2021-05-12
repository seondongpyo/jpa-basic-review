package io.github.seondongpyo.mapping.relation.manytoone.unidirectional;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter
@NoArgsConstructor
@Entity
public class School {

    @Id
    @GeneratedValue
    @Column(name = "SCHOOL_ID")
    private Long id;

    private String name;

    public School(String name) {
        this.name = name;
    }
}
