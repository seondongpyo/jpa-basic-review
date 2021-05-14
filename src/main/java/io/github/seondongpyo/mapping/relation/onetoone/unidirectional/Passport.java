package io.github.seondongpyo.mapping.relation.onetoone.unidirectional;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@NoArgsConstructor
@Entity
public class Passport {

    @Id @GeneratedValue
    @Column(name = "PASSPORT_ID")
    private Long id;

    // 대상 테이블에 외래 키 양방향으로 매핑 (단방향은 지원 X)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TRAVELER_ID")
    private Traveler traveler;

}
