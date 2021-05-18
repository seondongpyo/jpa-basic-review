package io.github.seondongpyo.mapping.inheritance.tableperclass;

import org.assertj.core.api.Assertions;
import org.h2.jdbc.JdbcSQLSyntaxErrorException;
import org.hibernate.exception.SQLGrammarException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class InheritanceTypeTablePerClassTest {

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

    @DisplayName("상속관계 매핑 전략 - 구현 클래스마다 테이블 생성")
    @Test
    void tablePerClass() {
        // given
        Bus bus = new Bus("88-1");
        bus.setSeat(45);
        em.persist(bus);

        Subway subway = new Subway("분당선");
        subway.setSeat(54);
        em.persist(subway);

        Truck truck = new Truck(5);
        truck.setSeat(2);
        em.persist(truck);

        em.flush();
        em.clear();

        // when
        Vehicle foundBus = em.find(Vehicle.class, bus.getId());
        Vehicle foundSubway = em.find(Vehicle.class, subway.getId());
        Vehicle foundTruck = em.find(Vehicle.class, truck.getId());

        // then
        // 부모 타입으로 조회 시 UNION ALL 쿼리가 실행된다.
        assertThat(foundBus instanceof Bus).isTrue();
        assertThat(foundSubway instanceof Subway).isTrue();
        assertThat(foundTruck instanceof Truck).isTrue();

        // 부모 클래스인 Vehicle 엔티티는 테이블로 생성되지 않는다.
        assertThrows(PersistenceException.class,
                () -> em.createNativeQuery("select * from Vehicle").getResultList());
    }

}
