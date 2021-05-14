package io.github.seondongpyo.mapping.relation.onetoone.unidirectional;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@NoArgsConstructor
@Entity
public class Traveler {

    @Id @GeneratedValue
    @Column(name = "TRAVELER_ID")
    private Long id;

    @OneToOne(mappedBy = "traveler")
    private Passport passport;

    private String name;

}
