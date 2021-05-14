package io.github.seondongpyo.mapping.relation.onetoone.unidirectional;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@NoArgsConstructor
@Entity
public class Contact {

    @Id @GeneratedValue
    @Column(name = "CONTACT_ID")
    private Long id;

    // 양방향 연관관계 설정
    @OneToOne(mappedBy = "contact")
    private Customer customer;

    private String address;
    private String phoneNumber;
    private String email;

}
