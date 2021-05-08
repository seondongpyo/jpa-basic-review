package io.github.seondongpyo.mapping;

import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("객체와 테이블 매핑")
public class EntityMappingTest {

	private EntityManagerFactory emf;
	private EntityManager em;

	@BeforeEach
	void setup() {
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
	@DisplayName("엔티티 클래스에 기본 생성자는 필수이다.")
	void entityRequiresDefaultConstructor() {
		// given
		Person person = new Person("personA", 20);
		em.persist(person);

		// when
		em.flush();
		em.clear();

		// then
		assertThrows(PersistenceException.class, () -> em.find(Person.class, person.getId()));
	}
}
