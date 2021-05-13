package io.github.seondongpyo.mapping.relation.onetomany.unidirectional;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
public class ParkingLot {

    @Id
    @GeneratedValue
    @Column(name = "LOT_ID")
    private Long id;

    @OneToMany
    @JoinColumn(name = "LOT_ID")
    private List<Car> cars = new ArrayList<>();

    private String location;

    public ParkingLot(String location) {
        this.location = location;
    }
}
