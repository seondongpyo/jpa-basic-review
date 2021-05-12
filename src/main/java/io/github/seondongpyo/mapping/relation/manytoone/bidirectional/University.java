package io.github.seondongpyo.mapping.relation.manytoone.bidirectional;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class University {

    @Id
    @GeneratedValue
    @Column(name = "UNIV_ID")
    private Long id;

    /*
        mappedBy
        - The field that owns the relationship.
        - Required unless the relationship is unidirectional.
     */
    @OneToMany(mappedBy = "university")
    private List<Professor> professors = new ArrayList<>();

    private String name;

    public University(String name) {
        this.name = name;
    }
}
