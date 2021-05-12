package io.github.seondongpyo.mapping.relation.manytoone.unidirectional;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Student {

    @Id
    @GeneratedValue
    @Column(name = "STUDENT_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "SCHOOL_ID")
    private School school;

    private String name;

    public Student(String name) {
        this.name = name;
    }

    public void enter(School school) {
        this.school = school;
    }

}
