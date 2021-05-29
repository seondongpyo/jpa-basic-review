package io.github.seondongpyo.jpql;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.*;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
        String selectUserQuery = "select user_id, group_id, name, age, email, website, twitterUsername from User where name = 'Kim'";
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

    @DisplayName("결과 조회 API getSingleResult() - 결과가 정확히 하나인 경우")
    @Test
    void getSingleResult() {
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
        String selectUserQuery = "select u from User u where u.name = :name";
        User user = em.createQuery(selectUserQuery, User.class)
                        .setParameter("name", "Kim")
                        .getSingleResult();

        // then
        assertThat(user.getName()).isEqualTo("Kim");
        assertThat(user.getAge()).isEqualTo(10);
    }

    @DisplayName("getSingleResult() - 결과가 없으면 NoResultException 발생")
    @Test
    void noResultException() {
        // given
        User user = new User("Kim", 10);
        em.persist(user);

        em.flush();
        em.clear();

        // when
        String selectUserQuery = "select u from User u where u.name = :name";

        // then
        assertThrows(NoResultException.class, () ->
            em.createQuery(selectUserQuery, User.class)
                .setParameter("name", "Lee")
                .getSingleResult());
    }

    @DisplayName("getSingleResult() - 결과가 둘 이상이면 NonUniqueResultException 발생")
    @Test
    void nonUniqueResultException() {
        // given
        User user1 = new User("Kim", 10);
        User user2 = new User("Kim", 20);
        em.persist(user1);
        em.persist(user2);

        em.flush();
        em.clear();

        // when
        String selectUserQuery = "select u from User u where u.name = :name";

        // then
        assertThrows(NonUniqueResultException.class, () ->
                em.createQuery(selectUserQuery, User.class)
                        .setParameter("name", "Kim")
                        .getSingleResult());
    }

    @DisplayName("파라미터 바인딩 - 이름 기준")
    @Test
    void parameterBindingByName() {
        // given
        User user1 = new User("user1", 20);
        User user2 = new User("user2", 30);
        em.persist(user1);
        em.persist(user2);

        em.flush();
        em.clear();

        // when
        String selectUserQuery = "select u from User u where u.name = :name";
        User foundUser = em.createQuery(selectUserQuery, User.class)
                            .setParameter("name", "user1")
                            .getSingleResult();

        // then
        assertThat(foundUser.getName()).isEqualTo("user1");
        assertThat(foundUser.getAge()).isEqualTo(20);
    }

    @DisplayName("파라미터 바인딩 - 위치 기준")
    @Test
    void parameterBindingByPosition() {
        // given
        User user1 = new User("Kim", 10);
        User user2 = new User("Kim", 20);
        User user3 = new User("Lee", 30);
        em.persist(user1);
        em.persist(user2);
        em.persist(user3);

        em.flush();
        em.clear();

        // when
        String selectUserQuery = "select u from User u where u.name = ?1 and u.age = ?2";
        User foundUser = em.createQuery(selectUserQuery, User.class)
                            .setParameter(1, "Kim")
                            .setParameter(2, 20)
                            .getSingleResult();

        // then
        assertThat(foundUser.getName()).isEqualTo("Kim");
        assertThat(foundUser.getAge()).isEqualTo(20);
    }
    
    @DisplayName("프로젝션 - 엔티티")
    @Test
    void entityProjection() {
        // given
        User user = new User("user", 10);
        em.persist(user);

        em.flush();
        em.clear();

        // when
        User foundUser = em.createQuery("select u from User u", User.class)
                            .getSingleResult();

        // then
        assertThat(foundUser).isEqualTo(user);
    }

    @DisplayName("프로젝션 - 연관관계 엔티티")
    @Test
    void relatedEntityProjection() {
        // given
        Group group = new Group("A");
        em.persist(group);

        User user = new User("user", 20);
        user.setGroup(group);
        em.persist(user);

        em.flush();
        em.clear();

        // when
        Group foundGroup = em.createQuery("select u.group from User u", Group.class)
                                .getSingleResult();

        // then
        assertThat(foundGroup.getName()).isEqualTo(group.getName());
    }

    @DisplayName("프로젝션 - 임베디드 타입")
    @Test
    void embeddedTypeProjection() {
        // given
        Profile profile = new Profile("abc@gmail.com", "http://www.aaa.com", "abc");

        User user = new User("user", 20);
        user.setProfile(profile);
        em.persist(user);

        em.flush();
        em.clear();

        // when
        Profile foundProfile = em.createQuery("select u.profile from User u", Profile.class)
                                    .getSingleResult();

        // then
        assertThat(foundProfile).isEqualTo(profile);
    }

    @DisplayName("프로젝션 - 스칼라 타입")
    @Test
    void scalaTypeProjection() {
        // given
        User user = new User("user", 30);
        em.persist(user);

        em.flush();
        em.clear();

        // when
        List result = em.createQuery("select u.name, u.age from User u").getResultList();
        Object[] results = (Object[]) result.get(0);

        // then
        assertThat(results).contains(user.getName(), user.getAge());
    }

    @DisplayName("여러 값 조회 - new 명령어")
    @Test
    void newDtoProjection() {
        // given
        User user = new User("user", 10);
        em.persist(user);

        em.flush();
        em.clear();

        // when
        String newDtoQuery = "select new io.github.seondongpyo.jpql.UserDto(u.name, u.age) from User u";
        UserDto userDto = em.createQuery(newDtoQuery, UserDto.class).getSingleResult();

        // then
        assertThat(userDto.getName()).isEqualTo(user.getName());
        assertThat(userDto.getAge()).isEqualTo(user.getAge());
    }

    @DisplayName("페이징 API")
    @Test
    void pagingQuery() {
        // given
        for (int i = 1; i <= 20; i++) {
            User user = new User("user" + i, i);
            em.persist(user);
        }

        em.flush();
        em.clear();

        // when
        List<User> users = em.createQuery("select u from User u", User.class)
                                .setFirstResult(0)
                                .setMaxResults(10)
                                .getResultList();

        // then
        assertThat(users).hasSize(10);
    }

    @DisplayName("내부 조인")
    @Test
    void innerJoin() {
        // given
        Group group = new Group();
        em.persist(group);

        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        user1.setGroup(group);
        user2.setGroup(null);
        user3.setGroup(null);
        em.persist(user1);
        em.persist(user2);
        em.persist(user3);

        em.flush();
        em.clear();

        // when
        List<User> users = em.createQuery("select u from User u join u.group", User.class)
                                .getResultList();

        // then
        assertThat(users).hasSize(1);
    }

    @DisplayName("외부 조인")
    @Test
    void outerJoin() {
        // given
        Group group = new Group();
        em.persist(group);

        User user1 = new User();
        User user2 = new User();
        User user3 = new User();
        user1.setGroup(group);
        user2.setGroup(null);
        user3.setGroup(null);
        em.persist(user1);
        em.persist(user2);
        em.persist(user3);

        em.flush();
        em.clear();

        // when
        List<User> users = em.createQuery("select u from User u left outer join u.group", User.class)
                                .getResultList();

        // then
        assertThat(users).hasSize(3);
    }

}
