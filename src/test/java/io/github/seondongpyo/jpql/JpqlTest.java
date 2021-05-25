package io.github.seondongpyo.jpql;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("JPQL - 객체 지향 쿼리 언어")
public class JpqlTest {

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

    @DisplayName("JPQL : JPA에서 제공하는 객체 지향 쿼리 언어")
    @Test
    void jpql() {
        // given
        for (int index = 1; index <= 100; index++) {
            User user = new User("user" + index, index);
            em.persist(user);
        }

        em.flush();
        em.clear();

        // when
        // 나이가 50살보다 많은 유저만 조회
        List<User> users = em.createQuery("select u from User u where u.age > 50", User.class).getResultList();

        // then
        assertThat(users).hasSize(50);
    }

}
