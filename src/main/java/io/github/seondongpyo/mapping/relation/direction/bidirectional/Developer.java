package io.github.seondongpyo.mapping.relation.direction.bidirectional;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@SequenceGenerator(
    name = "DEVELOPER_SEQ_GENERATOR",
    sequenceName = "DEVELOPER_SEQ",
    initialValue = 1,
    allocationSize = 1
)
@Entity
public class Developer {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DEVELOPER_SEQ_GENERATOR")
    @Column(name = "DEVELOPER_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "COMPANY_ID") // 외래 키가 있는 곳을 주인으로 지정
    private Company company;

    private String name;

    @Enumerated(EnumType.STRING)
    private DevelopmentField field;

    public Developer(String name, DevelopmentField field) {
        this.name = name;
        this.field = field;
    }

    public void enter(Company company) {
        this.company = company;
    }
}
