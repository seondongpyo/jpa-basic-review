package io.github.seondongpyo.mapping;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceException;

import io.github.seondongpyo.mapping.id.GenerateIdByIdentity;
import io.github.seondongpyo.mapping.id.GenerateIdBySequence;
import io.github.seondongpyo.mapping.id.GenerateIdByTable;
import io.github.seondongpyo.mapping.id.GenerateIdByUser;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("기본 키 생성 전략")
class IdGenerationTest {

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
	@DisplayName("기본 키를 사용자가 직접 할당한다.")
	void generatedByUser() {
		// given
		GenerateIdByUser user = new GenerateIdByUser();

		// when
		user.setId("ID_USER"); // 사용자가 id 값을 직접 세팅
		em.persist(user);
		em.flush();
		em.clear();

		// then
		GenerateIdByUser foundUser = em.find(GenerateIdByUser.class, user.getId());
		assertThat(foundUser.getId()).isEqualTo("ID_USER");
	}

	@Test
	@DisplayName("기본 키가 할당되어 있지 않으면 해당 엔티티를 영속 상태로 관리할 수 없다.")
	void notAssignedId() {
		// given
		GenerateIdByUser user = new GenerateIdByUser();

		// when
		// then
		assertThrows(PersistenceException.class, () -> em.persist(user));
		assertThat(em.contains(user)).isFalse();
	}

	@Test
	@DisplayName("IDENTITY - 기본 키 생성을 데이터베이스에 위임한다.")
	void generationTypeIdentity() {
		// given
		GenerateIdByIdentity identity = new GenerateIdByIdentity();

		// when
		em.persist(identity); // em.persist() 시점에 즉시 INSERT 쿼리를 실행하고 DB에서 id를 조회
		
		// then
		assertThat(identity.getId()).isEqualTo(1L);
	}

	@Test
	@DisplayName("SEQUENCE - 데이터베이스의 시퀀스 오브젝트를 사용한다.")
	void generationTypeSequence() {
		// given
		GenerateIdBySequence sequence = new GenerateIdBySequence();

		// when
		em.persist(sequence); // 다음 시퀀스 값을 가져와서 id에 세팅

		// then
		assertThat(em.contains(sequence)).isTrue();
		assertThat(sequence.getId()).isEqualTo(1L);
	}

	@Test
	@DisplayName("TABLE - 키 생성 전용 테이블을 만들어서 데이터베이스 시퀀스를 흉내낸다.")
	void generationTypeTable() {
		// given
		GenerateIdByTable table = new GenerateIdByTable();

		// when
		em.persist(table);	// 테이블로 생성한 시퀀스 값을 가져와서 id에 세팅

		// then
		assertThat(em.contains(table)).isTrue();
		assertThat(table.getId()).isEqualTo(1L);
	}

}
