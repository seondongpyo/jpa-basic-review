package io.github.seondongpyo.mapping.relation.bidirectional;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.assertj.core.api.Assertions.assertThat;

public class BidirectionalTest {

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
        emf.close();
        em.close();
    }

    @DisplayName("양방향 연관관계")
    @Test
    void bidirectional() {
        // given
        Company company = new Company("woowabros");
        em.persist(company);

        Developer backendDeveloper = new Developer("홍길동", DevelopmentField.BACKEND);
        backendDeveloper.enter(company);
        em.persist(backendDeveloper);

        Developer frontendDeveloper = new Developer("김길동", DevelopmentField.FRONTEND);
        frontendDeveloper.enter(company);
        em.persist(frontendDeveloper);

        Developer devOpsDeveloper = new Developer("서길동", DevelopmentField.DEVOPS);
        devOpsDeveloper.enter(company);
        em.persist(devOpsDeveloper);

        em.flush();
        em.clear();

        // when
        Company foundCompany = em.find(Company.class, company.getId());

        // then
        assertThat(foundCompany.getDevelopers()).hasSize(3);
    }

    @Test
    @DisplayName("양방향 매핑 시 주의사항 - 연관관계의 주인에 값을 입력하지 않는 경우 null")
    void ownerHasNoValue() {
        // given
        Company company = new Company("kakao");
        em.persist(company);

        // when
        Developer backendDeveloper = new Developer("홍길동", DevelopmentField.BACKEND);
        company.getDevelopers().add(backendDeveloper);
        em.persist(backendDeveloper);

        em.flush();
        em.clear();

        // then
        Developer foundDeveloper = em.find(Developer.class, backendDeveloper.getId());
        assertThat(foundDeveloper.getCompany()).isNull();
    }

    @Test
    @DisplayName("양방향 매핑 시 주의사항 - 순수한 객체 상태를 고려하여 항상 양쪽 다 값을 입력해야 한다.")
    void setValueBothSide() {
        // given
        Company company = new Company("Naver");
        em.persist(company);

        // when
        Developer frontendDeveloper = new Developer("김길동", DevelopmentField.FRONTEND);
        company.getDevelopers().add(frontendDeveloper);
        frontendDeveloper.enter(company);
        em.persist(frontendDeveloper);

        em.flush();
        em.clear();

        // then
        Developer foundDeveloper = em.find(Developer.class, frontendDeveloper.getId());
        assertThat(foundDeveloper.getCompany()).isNotNull();
        assertThat(foundDeveloper.getCompany().getId()).isEqualTo(1);
    }

}
