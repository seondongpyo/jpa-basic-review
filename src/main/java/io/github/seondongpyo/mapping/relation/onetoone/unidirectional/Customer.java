package io.github.seondongpyo.mapping.relation.onetoone.unidirectional;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@NoArgsConstructor
@Entity
public class Customer {

    @Id @GeneratedValue
    @Column(name = "CUSTOMER_ID")
    private Long id;

    private String name;

    // 주(main) 테이블(Customer)에 외래 키 단방향으로 매핑
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "CONTACT_ID")
    private Contact contact;

}
