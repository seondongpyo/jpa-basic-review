package io.github.seondongpyo.mapping.relation.manytomany;

import static org.assertj.core.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.seondongpyo.mapping.relation.manytomany.bidirectional.Task;
import io.github.seondongpyo.mapping.relation.manytomany.bidirectional.Worker;
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

	@DisplayName("양방향 연관관계 매핑")
	@Test
	void bidirectional() {
		// given
		Task task1 = new Task("미장");
		Task task2 = new Task("도배");
		em.persist(task1);
		em.persist(task2);

		Worker worker1 = new Worker("작업자1");
		Worker worker2 = new Worker("작업자2");
		Worker worker3 = new Worker("작업자3");
		worker1.getTasks().add(task1);
		worker2.getTasks().add(task1);
		worker3.getTasks().add(task2);
		em.persist(worker1);
		em.persist(worker2);
		em.persist(worker3);

		em.flush();
		em.clear();

		// when
		Task foundTask1 = em.find(Task.class, task1.getId());
		Task foundTask2 = em.find(Task.class, task2.getId());

		// then
		assertThat(foundTask1.getWorkers()).hasSize(2);
		assertThat(foundTask2.getWorkers()).hasSize(1);
	}

}
