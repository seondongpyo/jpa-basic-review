package io.github.seondongpyo.entity;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("Member 엔티티 테스트")
class MemberTest {

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
    @DisplayName("저장")
    void save() {
        // given
        Member member = new Member("memberA", 20);
        em.persist(member);
        em.flush();
        em.clear();

        // when
        Member foundMember = em.find(Member.class, member.getId());

        // then
        assertThat(member.getId()).isEqualTo(foundMember.getId());
        assertThat(member.getName()).isEqualTo(foundMember.getName());
        assertThat(member.getAge()).isEqualTo(foundMember.getAge());
    }

    @Test
    @DisplayName("수정")
    void update() {
        // given
        Member member = new Member("memberA", 30);
        em.persist(member);

        // when
        member.setName("memberB");
        member.setAge(40);
        em.flush();
        em.clear();

        Member foundMember = em.find(Member.class, member.getId());

        // then
        assertThat(foundMember.getName()).isEqualTo("memberB");
        assertThat(foundMember.getAge()).isEqualTo(40);
    }

    @Test
    @DisplayName("삭제")
    void delete() {
        // given
        Member member = new Member("memberA", 10);
        em.persist(member);
        em.flush();
        em.clear();

        // when
        Member savedMember = em.find(Member.class, member.getId());
        em.remove(savedMember);
        em.flush();
        em.clear();

        Member foundMember = em.find(Member.class, member.getId());

        // then
        assertThat(foundMember).isNull();
    }

}