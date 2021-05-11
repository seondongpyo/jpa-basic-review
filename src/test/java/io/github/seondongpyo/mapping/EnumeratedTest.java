package io.github.seondongpyo.mapping;

import static org.assertj.core.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import io.github.seondongpyo.entity.Employee;
import io.github.seondongpyo.entity.Gender;
import io.github.seondongpyo.entity.Role;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("@Enumerated 속성")
public class EnumeratedTest {

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
	@DisplayName("value = EnumType.ORDINAL 설정 시 enum의 순서 값이 저장된다.")
	void enumTypeOrdinal() {
		// given
		Employee worker = new Employee();
		worker.setAge(20);
		Employee manager = new Employee();
		manager.setAge(40);

		// when
		worker.setRole(Role.WORKER);
		manager.setRole(Role.MANAGER);
		em.persist(worker);
		em.persist(manager);

		em.flush();
		em.clear();

		// then
		Employee foundWorker = em.find(Employee.class, worker.getId());
		Employee foundManager = em.find(Employee.class, manager.getId());

		// <주의> 데이터베이스에만 숫자로 저장됨 (실제 엔티티 조회 시에는 숫자 값이 아님)
		assertThat(foundWorker.getRole()).isEqualTo(Role.WORKER);
		assertThat(foundManager.getRole()).isEqualTo(Role.MANAGER);
	}

	@Test
	@DisplayName("value = EnumType.STRING 설정 시 enum의 이름 값이 저장된다.")
	void enumTypeString() {
		// given
		Employee maleEmployee = new Employee();
		maleEmployee.setAge(20);
		Employee femaleEmployee = new Employee();
		femaleEmployee.setAge(20);

		// when
		maleEmployee.setGender(Gender.MALE);
		femaleEmployee.setGender(Gender.FEMALE);
		em.persist(maleEmployee);
		em.persist(femaleEmployee);

		em.flush();
		em.clear();

		// then
		Employee foundMaleEmployee = em.find(Employee.class, maleEmployee.getId());
		Employee foundFemaleEmployee = em.find(Employee.class, femaleEmployee.getId());
		assertThat(foundMaleEmployee.getGender()).isEqualTo(Gender.MALE);
		assertThat(foundFemaleEmployee.getGender()).isEqualTo(Gender.FEMALE);
	}
}
