package io.github.seondongpyo.mapping;

import static org.assertj.core.api.Assertions.*;
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

	@Test
	@DisplayName("name 속성에 JPA에서 사용할 엔티티 이름을 지정할 수 있다.")
	void nameAttribute() {
		// given
		Employee employee = new Employee();
		employee.setName("홍길동");
		employee.setAge(30);
		
		// when
		em.persist(employee);
		em.flush();
		em.clear();

		// then
		Employee foundEmployee = em.createQuery("select e from EMP e", Employee.class).getSingleResult();
		assertThat(foundEmployee.getName()).isEqualTo("홍길동");
		assertThat(foundEmployee.getAge()).isEqualTo(30);
	}

}
