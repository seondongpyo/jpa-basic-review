package io.github.seondongpyo.mapping.relation.onetomany.bidirectional;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class Airport {

    @Id
    @GeneratedValue
    @Column(name = "AIRPORT_ID")
    private Long id;

    @OneToMany
    @JoinColumn(name = "AIRPORT_ID")
    private List<Airplane> airplanes = new ArrayList<>();

    private String name;

    public Airport(String name) {
        this.name = name;
    }
}
