package io.github.seondongpyo.mapping.inheritance.joined;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter @Setter
@Inheritance(strategy = InheritanceType.JOINED)
@DiscriminatorColumn
@Entity
public abstract class Item {

    @Id @GeneratedValue
    @Column(name = "ITEM_ID")
    private Long id;

    private String name;

}
