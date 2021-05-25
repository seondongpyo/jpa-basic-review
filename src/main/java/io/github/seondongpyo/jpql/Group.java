package io.github.seondongpyo.jpql;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Getter @Setter
@NoArgsConstructor
@Table(name = "GROUPS")
@Entity
public class Group {

    @Id @GeneratedValue
    @Column(name = "GROUP_ID")
    private Long id;

    @OneToMany(mappedBy = "group")
    private List<User> users = new ArrayList<>();

    private String name;

    public Group(String name) {
        this.name = name;
    }
}
