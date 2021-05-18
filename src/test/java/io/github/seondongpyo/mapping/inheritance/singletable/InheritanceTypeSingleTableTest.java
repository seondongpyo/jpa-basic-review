package io.github.seondongpyo.mapping.inheritance.singletable;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.assertj.core.api.Assertions.assertThat;

public class InheritanceTypeSingleTableTest {

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

    @DisplayName("상속관계 매핑 전략 - 단일 테이블")
    @Test
    void singleTable() {
        // given
        Can can = new Can("꽁치");
        can.setPrice(5000);
        em.persist(can);

        Sauce sauce = new Sauce("토마토");
        sauce.setPrice(3000);
        em.persist(sauce);

        em.flush();
        em.clear();

        // when
        String canContentQuery = "select content from Grocery where DTYPE = 'Can'";
        String canIngredientQuery = "select ingredient from Grocery where DTYPE = 'Can'";
        String sauceIngredientQuery = "select ingredient from Grocery where DTYPE = 'Sauce'";
        String sauceContentQuery = "select content from Grocery where DTYPE = 'Sauce'";

        Object canContent = em.createNativeQuery(canContentQuery).getSingleResult();
        Object canIngredient = em.createNativeQuery(canIngredientQuery).getSingleResult();
        Object sauceIngredient = em.createNativeQuery(sauceIngredientQuery).getSingleResult();
        Object sauceContent = em.createNativeQuery(sauceContentQuery).getSingleResult();

        // then
        assertThat(canContent).isEqualTo("꽁치");
        assertThat(sauceIngredient).isEqualTo("토마토");
        assertThat(canIngredient).isNull(); // 통조림 저장 시 소스 관련 컬럼엔 null 저장
        assertThat(sauceContent).isNull(); // 소스 저장 시 통조림 관련 컬럼엔 null 저장
    }

}
