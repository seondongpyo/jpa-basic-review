package io.github.seondongpyo.embedded;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

	@DisplayName("값을 공유하는 대신 복사해서 사용한다.")
	@Test
	void valueTypeCopy() {
		// given
		Address address = new Address("city", "street", "zipcode");

		Entertainer entertainer1 = new Entertainer("entertainer1");
		entertainer1.setCompanyAddress(address);
		em.persist(entertainer1);

		Entertainer entertainer2 = new Entertainer("entertainer2");
		entertainer2.setCompanyAddress(new Address(address.getCity(), address.getStreet(), address.getZipcode())); // 값을 복사해서 아용
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
		Address entertainer2Address = foundEntertainer2.getCompanyAddress();

		// then
		assertThat(entertainer2Address.getCity()).isEqualTo("city");
		assertThat(entertainer2Address.getStreet()).isEqualTo("street");
		assertThat(entertainer2Address.getZipcode()).isEqualTo("zipcode");
	}

	@DisplayName("값 타입의 비교는 equals(), hashCode()를 적절히 재정의하여 수행한다.")
	@Test
	void valueTypeComparison() {
		// given
		Address address1 = new Address("city", "street", "zipcode");
		Address address2 = new Address("city", "street", "zipcode");

		// when
		boolean identityComparison = address1 == address2;
		boolean equivalenceComparison = address1.equals(address2);

		// then
		assertThat(identityComparison).isFalse();
		assertThat(equivalenceComparison).isTrue();
	}
	
	@DisplayName("값 타입 컬렉션 사용하기")
	@Test
	void valueTypeCollection() {
		// given
		Entertainer entertainer = new Entertainer("entertainer1");
		entertainer.setHomeAddress(new Address("city", "street", "zipcode")); // 임베디드 타입

		// 값 타입 컬렉션
		entertainer.getFavoriteDirectors().add("감독1");
		entertainer.getFavoriteDirectors().add("감독2");
		entertainer.getAddressHistory().add(new Address("oldCity", "oldStreet", "oldZipcode"));
		entertainer.getAddressHistory().add(new Address("previousCity", "previousStreet", "previousZipcode"));

		em.persist(entertainer);
		em.flush();
		em.clear();

		// when
		Entertainer foundEntertainer = em.find(Entertainer.class, entertainer.getId());
		Address homeAddress = foundEntertainer.getHomeAddress();
		List<Address> addressHistory = foundEntertainer.getAddressHistory(); // 지연 로딩
		Set<String> favoriteDirectors = foundEntertainer.getFavoriteDirectors(); // 지연 로딩

		// then
		assertThat(homeAddress).isEqualTo(new Address("city", "street", "zipcode"));
		assertThat(addressHistory).contains(
			new Address("oldCity", "oldStreet", "oldZipcode"),
			new Address("previousCity", "previousStreet", "previousZipcode")
		);
		assertThat(favoriteDirectors).contains("감독1", "감독2");
	}

	@DisplayName("값 타입 컬렉션은 생명주기를 엔티티에 의존한다. (like CascadeType.ALL, orphanRemoval=true)")
	@Test
	void valueTypeCollectionLifeCycle() {
		// given
		Entertainer entertainer = new Entertainer("entertainer");
		entertainer.getAddressHistory().add(new Address("city1", "street1", "zipcode1"));
		entertainer.getAddressHistory().add(new Address("city2", "street2", "zipcode2"));
		entertainer.getFavoriteDirectors().add("director1");
		entertainer.getFavoriteDirectors().add("director2");
		entertainer.getFavoriteDirectors().add("director3");
		em.persist(entertainer);

		em.flush();
		em.clear();

		// when
		Entertainer foundEntertainer = em.find(Entertainer.class, entertainer.getId());
		List<Address> addressHistory1 = new ArrayList<>(foundEntertainer.getAddressHistory()); // 엔티티 제거 전 주소 이력
		Set<String> favoriteDirectors1 = new HashSet<>(foundEntertainer.getFavoriteDirectors()); // 엔티티 제거 전 감독 목록

		em.remove(foundEntertainer); // 엔티티를 제거
		em.flush();
		em.clear();

		List addressHistory2 = em.createNativeQuery("select * from ADDRESS").getResultList(); // 엔티티 제거 후 주소 이력
		List favoriteDirectors2 = em.createNativeQuery("select * from FAVORITE_DIRECTOR").getResultList(); // 엔티티 제거 후 감독 목록

		// then
		assertThat(addressHistory1).hasSize(2);
		assertThat(favoriteDirectors1).hasSize(3);
		assertThat(addressHistory2).hasSize(0);
		assertThat(favoriteDirectors2).hasSize(0);

	}

}
