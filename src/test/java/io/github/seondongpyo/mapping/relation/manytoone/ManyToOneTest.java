package io.github.seondongpyo.mapping.relation.manytoone;

import io.github.seondongpyo.mapping.relation.manytoone.unidirectional.School;
import io.github.seondongpyo.mapping.relation.manytoone.unidirectional.Student;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.assertj.core.api.Assertions.assertThat;

public class ManyToOneTest {

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

    @DisplayName("다대일 단방향 연관관계 매핑")
    @Test
    void manyToOne() {
        // given
        School school = new School("한국대학교");
        em.persist(school);

        Student student = new Student("홍길동");
        student.enter(school);
        em.persist(student);

        em.flush();
        em.clear();

        // when
        Student foundStudent = em.find(Student.class, student.getId());

        // then
        assertThat(foundStudent.getSchool().getName()).isEqualTo("한국대학교");
    }

}
