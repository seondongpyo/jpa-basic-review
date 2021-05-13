package io.github.seondongpyo.mapping.relation.onetomany.bidirectional;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Airplane {

    @Id
    @GeneratedValue
    @Column(name = "AIRPLANE_ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "AIRPORT_ID", insertable = false, updatable = false) // 공식적으로 지원하지 않는 매핑 방식
    private Airport airport;

    private String model;

    public Airplane(String model) {
        this.model = model;
    }
}
