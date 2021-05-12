package io.github.seondongpyo.mapping.relation.manytoone.bidirectional;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Professor {

    @Id
    @GeneratedValue
    @Column(name = "PROF_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "UNIV_ID")
    private University university;

    private String name;

    public Professor(String name) {
        this.name = name;
    }

    public void appoint(University university) {
        this.university = university;
    }
}
