package io.github.seondongpyo.mapping;

import static org.assertj.core.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("@Transient")
public class TransientTest {

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

	@Test
	@DisplayName("@Transient 애노테이션이 붙은 필드는 데이터베이스와 매핑되지 않는다.")
	void transientField() {
		// given
		Employee employee = new Employee();
		employee.setAge(20);

		// when
		employee.setTemp("this is transient field");
		em.persist(employee);

		em.flush();
		em.clear();

		// then
		Employee foundEmployee = em.find(Employee.class, employee.getId());
		assertThat(foundEmployee.getTemp()).isNotEqualTo("this is transient field").isNull();
	}

}
