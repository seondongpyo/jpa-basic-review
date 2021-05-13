package io.github.seondongpyo.mapping.relation.onetomany;

import io.github.seondongpyo.mapping.relation.onetomany.unidirectional.Car;
import io.github.seondongpyo.mapping.relation.onetomany.unidirectional.ParkingLot;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("일대다(1:N) 연관관계 매핑")
public class OneToManyTest {

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

    @DisplayName("단방향 연관관계 - 연관관계의 주인이 외래 키를 관리한다.")
    @Test
    void unidirectional() {
        // given
        Car car = new Car("자동차");
        em.persist(car);

        ParkingLot parkingLot = new ParkingLot("성남");
        parkingLot.getCars().add(car);
        em.persist(parkingLot);

        em.flush();
        em.clear();

        // when
        ParkingLot foundParkingLot = em.find(ParkingLot.class, parkingLot.getId());

        // then
        assertThat(foundParkingLot.getCars()).hasSize(1);
    }


}
