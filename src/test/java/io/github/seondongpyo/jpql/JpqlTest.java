package io.github.seondongpyo.jpql;

import io.github.seondongpyo.mapping.relation.direction.unidirectional.Team;
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
        String selectUserQuery = "select user_id, group_id, name, age, email, website, twitterUsername, userType from User where name = 'Kim'";
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

    @DisplayName("ON 절 - 조인 대상 필터링")
    @Test
    void joinOn() {
        // given
        Group group1 = new Group("group1");
        Group group2 = new Group("group2");
        em.persist(group1);
        em.persist(group2);

        User user1 = new User("user1", 10);
        User user2 = new User("user2", 20);
        User user3 = new User("user3", 30);
        user1.setGroup(group1);
        user2.setGroup(group1);
        user3.setGroup(group2);
        em.persist(user1);
        em.persist(user2);
        em.persist(user3);

        em.flush();
        em.clear();

        // when
        String selectGroup1Users = "select u from User u join u.group g on g.name = 'group1'";
        List<User> users = em.createQuery(selectGroup1Users, User.class)
                                .getResultList();

        // then
        assertThat(users).hasSize(2);
    }

    @DisplayName("ON 절 - 연관관계가 없는 엔티티의 조인")
    @Test
    void joinOnEntity() {
        // given
        Team team = new Team("team");
        em.persist(team);

        User user1 = new User("team", 10);
        User user2 = new User("team", 20);
        User user3 = new User("group", 30);
        User user4 = new User("party", 40);
        em.persist(user1);
        em.persist(user2);
        em.persist(user3);
        em.persist(user4);

        // when
        List<User> users = em.createQuery("select u from User u join Team t on u.name = t.name", User.class)
                                .getResultList();

        // then
        assertThat(users).hasSize(2);
    }

    @DisplayName("서브 쿼리")
    @Test
    void subQuery() {
        // given
        User user1 = new User("user1", 10);
        User user2 = new User("user2", 21);
        User user3 = new User("user3", 28);
        User user4 = new User("user4", 32);
        User user5 = new User("user5", 45);
        em.persist(user1);
        em.persist(user2);
        em.persist(user3);
        em.persist(user4);
        em.persist(user5);

        em.flush();
        em.clear();

        // when
        // 전체 회원의 중 평균 나이보다 많은 회원들만 조회
        List<User> users = em.createQuery("select u from User u where u.age > (select avg(u2.age) from User u2)", User.class)
                                .getResultList();
        // then
        assertThat(users).hasSize(3);
    }

    @DisplayName("JPQL 타입 표현")
    @Test
    void jpqlTypeExpression() {
        // given
        User user = new User("user1", 20);
        user.setUserType(UserType.ADMIN);
        em.persist(user);

        em.flush();
        em.clear();

        String query =  "select 'Hello', 10L, TRUE from User u where u.userType = :userType";
        List<Object[]> resultList = em.createQuery(query, Object[].class)
                .setParameter("userType", UserType.ADMIN)
                .getResultList();

        Object[] result = resultList.get(0);

        // then
        assertThat(result[0]).isEqualTo("Hello");
        assertThat(result[1]).isEqualTo(10L);
        assertThat(result[2]).isEqualTo(true);
    }

    @DisplayName("CASE 조건식 - 기본")
    @Test
    void caseDefault() {
        // given
        User user1 = new User("user1", 20);
        User user2 = new User("user2", 40);
        User user3 = new User("user3", 60);
        User user4 = new User("user4", 70);
        User user5 = new User("user5", 100);
        User user6 = new User("user6", 35);
        em.persist(user1);
        em.persist(user2);
        em.persist(user3);
        em.persist(user4);
        em.persist(user5);
        em.persist(user6);

        // when
        String query =
                "select " +
                "   case when u.age = 20 then '약관' " +
                "        when u.age = 40 then '지천명' " +
                "        when u.age = 60 then '환갑' " +
                "        when u.age = 70 then '고희' " +
                "        when u.age = 100 then '상수' " +
                "        else '일반' " +
                "   end " +
                "from User u";

        List<String> resultList = em.createQuery(query, String.class).getResultList();

        // then
        assertThat(resultList.get(0)).isEqualTo("약관");
        assertThat(resultList.get(1)).isEqualTo("지천명");
        assertThat(resultList.get(2)).isEqualTo("환갑");
        assertThat(resultList.get(3)).isEqualTo("고희");
        assertThat(resultList.get(4)).isEqualTo("상수");
        assertThat(resultList.get(5)).isEqualTo("일반");
    }

    @DisplayName("CASE 조건식 - 단순")
    @Test
    void caseSimple() {
        // given
        User user1 = new User("고길동", 50);
        em.persist(user1);

        User user2 = new User("둘리", 10);
        em.persist(user2);

        User user3 = new User("마이콜", 20);
        em.persist(user3);

        // when
        String query =
                "select " +
                "   case u.name " +
                "       when '고길동' then '집주인' " +
                "       when '둘리' then '세입자' " +
                "       else '동네주민' " +
                "   end " +
                "from User u";

        List<String> resultList = em.createQuery(query, String.class).getResultList();

        // then
        assertThat(resultList.get(0)).isEqualTo("집주인");
        assertThat(resultList.get(1)).isEqualTo("세입자");
        assertThat(resultList.get(2)).isEqualTo("동네주민");
    }

    @DisplayName("조건식 - COALESCE")
    @Test
    void coalesce() {
        // given
        User user1 = new User(null, 10);
        em.persist(user1);

        User user2 = new User("", 20);
        em.persist(user2);

        User user3 = new User("홍길동", 30);
        em.persist(user3);

        // when
        // coalesce : 값이 null이면 지정한 값을 반환
        String query = "select coalesce(u.name, '익명회원') from User u";
        List<String> resultList = em.createQuery(query, String.class)
                                    .getResultList();

        // then
        assertThat(resultList.get(0)).isEqualTo("익명회원");
        assertThat(resultList.get(1)).isEqualTo("");
        assertThat(resultList.get(2)).isEqualTo("홍길동");
    }

    @DisplayName("조건식 - NULLIF")
    @Test
    void nullif() {
        // given
        User user1 = new User("고길동", 40);
        User user2 = new User("둘리", 10);
        User user3 = new User("도우너", 10);
        User user4 = new User("또치", 10);
        em.persist(user1);
        em.persist(user2);
        em.persist(user3);
        em.persist(user4);

        // when
        String query = "select nullif(u.name, '고길동') from User u";
        List<String> resultList = em.createQuery(query, String.class)
                                    .getResultList();

        // then
        assertThat(resultList.get(0)).isNull();
        assertThat(resultList.get(1)).isEqualTo("둘리");
        assertThat(resultList.get(2)).isEqualTo("도우너");
        assertThat(resultList.get(3)).isEqualTo("또치");
    }

}
