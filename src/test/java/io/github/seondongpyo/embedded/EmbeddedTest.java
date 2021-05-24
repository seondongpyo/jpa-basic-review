package io.github.seondongpyo.embedded;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("임베디드 타입")
public class EmbeddedTest {

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

	@DisplayName("임베디드 타입 사용하기")
	@Test
	void embedded() {
		// given
		LocalDate startDate = LocalDate.of(2021, 5, 20);
		LocalDate expireDate = startDate.plusYears(2);

		Entertainer entertainer = new Entertainer("홍길동");
		entertainer.setHomeAddress(new Address("서울", "종로3가", "12345"));
		entertainer.setContract(new Contract(startDate, expireDate));
		em.persist(entertainer);

		em.flush();
		em.clear();

		// when
		Address address = em.createQuery("select e.homeAddress from Entertainer e", Address.class)
							.getSingleResult();
		Contract contract = em.createQuery("select e.contract from Entertainer e", Contract.class)
								.getSingleResult();

		// then
		assertThat(address.getCity()).isEqualTo("서울");
		assertThat(address.getStreet()).isEqualTo("종로3가");
		assertThat(address.getZipcode()).isEqualTo("12345");
		assertThat(contract.getStartDate()).isEqualTo(startDate);
		assertThat(contract.getExpireDate()).isEqualTo(expireDate);
	}

	@DisplayName("@AttributeOverrides : 하나의 엔티티에서 같은 임베디드 타입을 사용할 경우")
	@Test
	void attributeOverrides() {
		// given
		Entertainer entertainer = new Entertainer("홍길동");
		entertainer.setHomeAddress(new Address("성남", "시민로260번길", "12345"));
		entertainer.setCompanyAddress(new Address("서울", "강남대로", "11223"));
		em.persist(entertainer);

		em.flush();
		em.clear();

		// when
		String homeAddressQuery = "select city, street, zipcode from Entertainer";
		String companyAddressQuery = "select COMPANY_CITY, COMPANY_STREET, COMPANY_ZIPCODE from Entertainer";
		Object[] homeAddress = (Object[]) em.createNativeQuery(homeAddressQuery).getSingleResult();
		Object[] companyAddress = (Object[]) em.createNativeQuery(companyAddressQuery).getSingleResult();

		// then
		assertThat(homeAddress).contains("성남", "시민로260번길", "12345");
		assertThat(companyAddress).contains("서울", "강남대로", "11223");
	}

	@DisplayName("임베디드 타입의 값이 null이면 매핑된 컬럼의 값들도 모두 null이다.")
	@Test
	void embeddedNull() {
		// given
		Entertainer entertainer = new Entertainer("홍길동");
		em.persist(entertainer);

		em.flush();
		em.clear();

		// when
		String contractQuery = "select startDate, expireDate from Entertainer";
		String homeAddressQuery = "select city, street, zipcode from Entertainer";
		String companyAddressQuery = "select COMPANY_CITY, COMPANY_STREET, COMPANY_ZIPCODE from Entertainer";
		Object[] contract = (Object[]) em.createNativeQuery(contractQuery).getSingleResult();
		Object[] homeAddress = (Object[]) em.createNativeQuery(homeAddressQuery).getSingleResult();
		Object[] companyAddress = (Object[]) em.createNativeQuery(companyAddressQuery).getSingleResult();

		// then
		assertThat(contract).containsOnlyNulls();
		assertThat(homeAddress).containsOnlyNulls();
		assertThat(companyAddress).containsOnlyNulls();
	}

	@DisplayName("값 타입은 여러 엔티티가 공유하면 안 된다.")
	@Test
	void valueTypeSharing() {
		// given
		Address address = new Address("city", "street", "zipcode");

		Entertainer entertainer1 = new Entertainer("entertainer1");
		entertainer1.setCompanyAddress(address);
		em.persist(entertainer1);

		Entertainer entertainer2 = new Entertainer("entertainer2");
		entertainer2.setCompanyAddress(address);
		em.persist(entertainer2);

		// entertainer1의 주소 내용을 변경
		entertainer1.getCompanyAddress().setCity("newCity");
		entertainer1.getCompanyAddress().setStreet("newStreet");
		entertainer1.getCompanyAddress().setZipcode("newZipcode");

		em.flush();
		em.clear();

		// when
		String selectEntertainer2Query = "select e from Entertainer e where e.name = 'entertainer2'";
		Entertainer foundEntertainer2 = em.createQuery(selectEntertainer2Query, Entertainer.class).getSingleResult();

		// then
		assertThat(foundEntertainer2.getCompanyAddress().getCity()).isEqualTo("newCity");
		assertThat(foundEntertainer2.getCompanyAddress().getStreet()).isEqualTo("newStreet");
		assertThat(foundEntertainer2.getCompanyAddress().getZipcode()).isEqualTo("newZipcode");
	}


}
