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
		employee.setAge(20);
		em.persist(employee);

		em.flush();
		em.clear();

		// when
		Employee foundEmployee = em.find(Employee.class, employee.getId());

		// then
		assertThat(foundEmployee.getAddress()).isNull();
	}

	@Test
	@DisplayName("updatable : false 설정 시 데이터베이스에 값이 수정되지 않는다.")
	void updatable() {
		// given
		Employee employee = new Employee();
		employee.setName("홍길동");
		employee.setAge(20);
		em.persist(employee);

		em.flush();
		em.clear();

		// when
		Employee foundEmployee = em.find(Employee.class, employee.getId());
		foundEmployee.setName("김길동");

		em.flush();
		em.clear();

		// then
		String name = em.find(Employee.class, employee.getId()).getName();
		assertThat(name).isEqualTo("홍길동").isNotEqualTo("김길동");
	}

	@Test
	@DisplayName("nullable : false 설정 시 해당 필드에 매핑되는 컬럼은 not null 제약 조건이 된다.")
	void nullable() {
		// given
		Employee employee = new Employee();

		// when
		// nullable = false인 age 값을 넣지 않음
		employee.setName("홍길동");
		employee.setAddress("서울특별시 송파구");
		employee.setGender(Gender.MALE);
		
		// then
		assertThrows(PersistenceException.class, () -> em.persist(employee));
	}

	@Test
	@DisplayName("length : 문자열 필드의 길이 제약 조건을 설정한다. (String 타입에만 사용)")
	void length() {
		// given
		Employee employee = new Employee();
		employee.setAge(20);

		// when
		employee.setResidentNumber("900101-1234567"); // 13자리가 아닌 14자리를 입력할 경우
		em.persist(employee);

		// then
		assertThrows(PersistenceException.class, () -> em.flush()); // 실제 DB로 쿼리가 실행될 때 예외 발생
	}

}
