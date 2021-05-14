package io.github.seondongpyo.mapping.relation.onetoone.unidirectional;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

@Getter @Setter
@NoArgsConstructor
@Entity
public class Contact {

    @Id @GeneratedValue
    @Column(name = "CONTACT_ID")
    private Long id;

    private String address;
    private String phoneNumber;
    private String email;

}
