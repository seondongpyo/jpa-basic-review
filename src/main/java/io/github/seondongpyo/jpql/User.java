package io.github.seondongpyo.jpql;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Objects;

@Getter @Setter
@NoArgsConstructor
@Entity
public class User {

    @Id @GeneratedValue
    @Column(name = "USER_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "GROUP_ID")
    private Group group;

    private String name;

    private int age;

    @Embedded
    private Profile profile;

    @Enumerated(EnumType.STRING)
    private UserType userType;

    public User(String name, int age) {
        this.name = name;
        this.age = age;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return getAge() == user.getAge() && Objects.equals(getId(), user.getId()) && Objects.equals(getGroup(), user.getGroup()) && Objects.equals(getName(), user.getName());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getGroup(), getName(), getAge());
    }
}
