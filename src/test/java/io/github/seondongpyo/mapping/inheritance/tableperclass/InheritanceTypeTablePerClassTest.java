package io.github.seondongpyo.mapping.inheritance.tableperclass;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class InheritanceTypeTablePerClassTest {

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
        Query query = em.createNativeQuery("select * from Vehicle");

        // then
        // 부모 타입으로 조회 시 UNION ALL 쿼리가 실행된다.
        assertThat(foundBus).isInstanceOf(Bus.class);
        assertThat(foundSubway).isInstanceOf(Subway.class);
        assertThat(foundTruck).isInstanceOf(Truck.class);

        // 부모 클래스인 Vehicle 엔티티는 테이블로 생성되지 않는다.
        assertThrows(PersistenceException.class, query::getResultList);
    }

}
