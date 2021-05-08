package io.github.seondongpyo.mapping;

import static org.assertj.core.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("@Column 속성")
public class ColumnTest {

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
	@DisplayName("insertable : false 설정 시 데이터베이스에 값이 저장되지 않는다.")
	void insertable() {
		// given
		Employee employee = new Employee();
		employee.setAddress("서울특별시 송파구");
		em.persist(employee);
		
		em.flush();
		em.clear();

		// when
		Employee foundEmployee = em.find(Employee.class, employee.getId());

		// then
		assertThat(foundEmployee.getAddress()).isNull();
	}

}
