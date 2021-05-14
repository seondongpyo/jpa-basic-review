package io.github.seondongpyo.mapping.relation.onetoone;

import io.github.seondongpyo.mapping.relation.onetoone.unidirectional.Contact;
import io.github.seondongpyo.mapping.relation.onetoone.unidirectional.Customer;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("일대일 연관관계 연관관계 매핑")
public class OneToOneTest {

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

    @DisplayName("단방향 연관관계 매핑 - 주 테이블에 외래 키를 가지는 경우")
    @Test
    void unidirectional() {
        // given
        Contact contact = new Contact();
        em.persist(contact);

        Customer customer = new Customer();
        customer.setContact(contact);
        em.persist(customer);

        em.flush();
        em.clear();

        // when
        Customer foundCustomer = em.find(Customer.class, customer.getId());

        // then
        assertThat(foundCustomer.getContact().getId()).isEqualTo(contact.getId());
    }

}