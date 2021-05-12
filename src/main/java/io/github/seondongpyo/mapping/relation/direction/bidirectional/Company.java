package io.github.seondongpyo.mapping.relation.direction.bidirectional;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@SequenceGenerator(
    name = "COMPANY_SEQ_GENERATOR",
    sequenceName = "COMPANY_SEQ",
    initialValue = 1,
    allocationSize = 1
)
@Entity
public class Company {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "COMPANY_SEQ_GENERATOR")
    @Column(name = "COMPANY_ID")
    private Long id;

    @OneToMany(mappedBy = "company") // 외래 키의 주인이 아니면 mappedBy 속성으로 주인을 지정
    private List<Developer> developers = new ArrayList<>();

    private String name;

    public Company(String name) {
        this.name = name;
    }
}
