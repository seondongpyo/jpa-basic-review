package io.github.seondongpyo;

import io.github.seondongpyo.entity.Member;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("영속성 관리")
public class PersistenceTest {

    private EntityManagerFactory emf;
    private EntityManager em;

    @BeforeEach
    void beforeEach() {
        emf = Persistence.createEntityManagerFactory("basic");
        em = emf.createEntityManager();
        em.getTransaction().begin();
    }

    @AfterEach
    void afterEach() {
        em.close();
        emf.close();
    }

    @Test
    @DisplayName("비영속 - 객체를 생성한 시점엔 영속 상태가 아니다.")
    void newInstance() {
        // given
        // when
        Member member = new Member("memberA", 20);

        // then
        assertThat(em.contains(member)).isFalse();
    }

    @Test
    @DisplayName("영속 - 영속성 컨텍스트에 의해 관리되고 있는 상태이다.")
    void managed() {
        // given
        Member member = new Member("memberA", 30);

        // when
        em.persist(member);

        // then
        assertThat(em.contains(member)).isTrue();
    }

    @Test
    @DisplayName("준영속 - 영속성 컨텍스트에 저장되었다가 분리된 상태이다.")
    void detached() {
        // given
        Member member = new Member("memberA", 10);
        em.persist(member);

        // when
        em.detach(member);

        // then
        assertThat(em.contains(member)).isFalse();
    }

    @Test
    @DisplayName("삭제 - 엔티티가 삭제된 상태이다.")
    void removed() {
        // given
        Member member = new Member("memberA", 30);
        em.persist(member);

        // when
        em.remove(member);
        Member foundMember = em.find(Member.class, member.getId());

        // then
        assertThat(foundMember).isNull();
    }

    @Test
    @DisplayName("영속 엔티티의 동일성 보장")
    void equals() {
        // given
        Member member = new Member("memberA", 40);
        em.persist(member);

        // when
        Member foundMember = em.find(Member.class, member.getId());

        // then
        assertThat(member).isEqualTo(foundMember);
    }

    @Test
    @DisplayName("엔티티 수정 - 변경 감지")
    void dirtyChecking() {
        // given
        Member member = new Member("memberA", 20);
        em.persist(member);

        // when
        member.setName("memberB");
        member.setAge(30);
        em.flush();
        em.clear();

        Member foundMember = em.find(Member.class, member.getId());

        // then
        assertThat(foundMember.getName()).isEqualTo("memberB");
        assertThat(foundMember.getAge()).isEqualTo(30);
    }
    
}
