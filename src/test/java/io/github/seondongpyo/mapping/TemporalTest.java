package io.github.seondongpyo.mapping;

import static org.assertj.core.api.Assertions.*;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("@Temporal 속성")
public class TemporalTest {

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
	@DisplayName("TemporalType.DATE : 날짜만 저장한다.")
	void temporalTypeDate() {
		// given
		Employee employee = new Employee();
		employee.setAge(20);

		// when
		employee.setJoinDate(new Date(2021 - 1900, Calendar.MAY, 1, 9, 0));
		em.persist(employee);
		em.flush();
		em.clear();

		// then
		Employee foundEmployee = em.find(Employee.class, employee.getId());
		assertThat(foundEmployee.getJoinDate()).isEqualTo("2021-05-01");
	}

	@Test
	@DisplayName("TemporalType.TIME : 시간만 저장한다.")
	void temporalTypeTime() {
		// given
		Employee employee = new Employee();
		employee.setAge(30);

		// when
		employee.setStartWorkTime(new Date(2021 - 1900, Calendar.MAY, 1, 9, 30, 15));
		em.persist(employee);
		em.flush();
		em.clear();

		// then
		Employee foundEmployee = em.find(Employee.class, employee.getId());
		assertThat(foundEmployee.getStartWorkTime().toString()).isEqualTo("09:30:15");
	}

	@Test
	@DisplayName("TemporalType.TIMESTAMP : 날짜와 시간을 함께 저장한다.")
	void temporalTypeTimestamp() {
		// given
		Employee employee = new Employee();
		employee.setAge(40);

		// when
		employee.setLastBusinessTripDateTime(new Date(2021 - 1900, Calendar.MAY, 1, 18, 0, 30));
		em.persist(employee);
		em.flush();
		em.clear();

		// then
		Employee foundEmployee = em.find(Employee.class, employee.getId());
		assertThat(foundEmployee.getLastBusinessTripDateTime().toString()).isEqualTo("2021-05-01 18:00:30.0");
	}
	
}
