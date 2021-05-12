package io.github.seondongpyo.mapping.relation.direction.unidirectional;

import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Player {

    @Id
    @GeneratedValue
    @Column(name = "PLAYER_ID")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    private String name;

    public Player(String name) {
        this.name = name;
    }

    public void join(Team team) {
        this.team = team;
    }
}
