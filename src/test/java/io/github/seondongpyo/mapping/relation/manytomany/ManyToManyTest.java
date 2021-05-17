package io.github.seondongpyo.mapping.relation.manytomany;

import static org.assertj.core.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.seondongpyo.mapping.relation.manytomany.unidirectional.Bank;
import io.github.seondongpyo.mapping.relation.manytomany.unidirectional.Client;

@DisplayName("다대다(N:M) 연관관계 매핑")
public class ManyToManyTest {

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

	@DisplayName("단방향 연관관계 매핑")
	@Test
	void unidirectional() {
		// given
		Bank shinhanBank = new Bank("신한은행");
		Bank wooriBank = new Bank("우리은행");
		em.persist(shinhanBank);
		em.persist(wooriBank);

		Client client = new Client();
		client.getBanks().add(shinhanBank);
		client.getBanks().add(wooriBank);
		em.persist(client);

		em.flush();
		em.clear();

		// when
		Client foundClient = em.find(Client.class, client.getId());

		// then
		assertThat(foundClient.getBanks()).hasSize(2);
	}

}
