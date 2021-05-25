package io.github.seondongpyo.jpql;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
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

    @DisplayName("Criteria : 문자가 아닌 자바 코드로 JPQL을 작성할 수 있다.")
    @Test
    void criteria() {
        // given
        User user1 = new User("Kim", 10);
        User user2 = new User("Kim", 20);
        User user3 = new User("Lee", 30);
        User user4 = new User("Park", 40);
        em.persist(user1);
        em.persist(user2);
        em.persist(user3);
        em.persist(user4);

        em.flush();
        em.clear();

        // Criteria 사용 준비
        CriteriaBuilder cb = em.getCriteriaBuilder();
        CriteriaQuery<User> query = cb.createQuery(User.class);

        // 루트 클래스 (조회를 시작할 클래스)
        Root<User> u = query.from(User.class);

        // when
        // 이름이 'Kim'인 유저만 조회
        CriteriaQuery<User> cq = query.select(u).where(cb.equal(u.get("name"), "Kim"));
        List<User> users = em.createQuery(cq).getResultList();

        // then
        assertThat(users).hasSize(2);
    }

    @DisplayName("네이티브 쿼리")
    @Test
    void nativeQuery() {
        // given
        User user1 = new User("Kim", 10);
        User user2 = new User("Kim", 20);
        User user3 = new User("Lee", 30);
        User user4 = new User("Park", 40);
        em.persist(user1);
        em.persist(user2);
        em.persist(user3);
        em.persist(user4);

        em.flush();
        em.clear();

        // when
        String selectUserQuery = "select user_id, group_id, name, age from User where name = 'Kim'";
        List<User> users = em.createNativeQuery(selectUserQuery, User.class).getResultList();

        // then
        assertThat(users).hasSize(2);
    }

    @DisplayName("결과 조회 API getResultList() - 결과가 하나 이상일 경우")
    @Test
    void getResultList() {
        // given
        User user1 = new User("Kim", 10);
        User user2 = new User("Lee", 20);
        User user3 = new User("Park", 30);
        em.persist(user1);
        em.persist(user2);
        em.persist(user3);

        em.flush();
        em.clear();

        // when
        List<User> users = em.createQuery("select u from User u", User.class).getResultList();

        // then
        assertThat(users).hasSize(3);
    }

}
