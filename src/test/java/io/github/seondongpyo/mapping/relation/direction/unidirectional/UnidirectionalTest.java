package io.github.seondongpyo.mapping.relation.direction.unidirectional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.assertj.core.api.Assertions.assertThat;

class UnidirectionalTest {

    private EntityManagerFactory emf;
    private EntityManager em;

    @BeforeEach
    void setup() {
        emf = Persistence.createEntityManagerFactory("basic");
        em = emf.createEntityManager();
        em.getTransaction().begin();
    }

    @AfterEach
    void close() {
        em.close();
        emf.close();
    }

    @DisplayName("단방향 연관관계")
    @Test
    void unidirectional() {
        // given
        Team team = new Team("teamA");
        em.persist(team);

        Player player = new Player("홍길동");
        player.join(team);
        em.persist(player);

        em.flush();
        em.clear();

        // when
        Player foundPlayer = em.find(Player.class, player.getId());

        // then
        assertThat(foundPlayer.getTeam().getName()).isEqualTo(team.getName());
    }

}
