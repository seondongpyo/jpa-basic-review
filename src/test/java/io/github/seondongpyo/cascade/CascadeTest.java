package io.github.seondongpyo.cascade;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.seondongpyo.cascade.all.Cosmetics;
import io.github.seondongpyo.cascade.all.CosmeticsReview;
import io.github.seondongpyo.cascade.nothing.Child;
import io.github.seondongpyo.cascade.nothing.Parent;
import io.github.seondongpyo.cascade.persist.Lecture;
import io.github.seondongpyo.cascade.persist.LectureReview;
import io.github.seondongpyo.cascade.remove.Hotel;
import io.github.seondongpyo.cascade.remove.HotelReview;

@DisplayName("영속성 전이(CASCADE)")
class CascadeTest {

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

	@DisplayName("영속성 전이를 설정하지 않고 부모 엔티티만 저장 시, 자식 엔티티는 저장되지 않는다.")
	@Test
	void noCascade() {
		// given
		Child child1 = new Child();
		Child child2 = new Child();

		Parent parent = new Parent();
		parent.addChild(child1);
		parent.addChild(child2);
		em.persist(parent); // Parent 엔티티만 저장

		em.flush();
		em.clear();

		// when
		Parent foundParent = em.find(Parent.class, parent.getId());

		// then
		assertThat(foundParent.getChildren()).isEmpty();
	}

	@DisplayName("CascadeType.ALL - 영속성 전이를 모든 영속성 상태 변경 시 적용한다.")
	@Test
	void cascadeTypeAll() {
		// given
		CosmeticsReview review1 = new CosmeticsReview();
		CosmeticsReview review2 = new CosmeticsReview();
		CosmeticsReview review3 = new CosmeticsReview();

		Cosmetics cosmetics = new Cosmetics();
		cosmetics.addReview(review1);
		cosmetics.addReview(review2);
		cosmetics.addReview(review3);
		em.persist(cosmetics); // 부모 엔티티인 Cosmetics 엔티티만 저장

		em.flush();
		em.clear();

		Cosmetics foundCosmetics = em.find(Cosmetics.class, cosmetics.getId());
		em.remove(foundCosmetics); // 부모 엔티티인 Cosmetics 엔티티 삭제

		em.flush();
		em.clear();
		
		// when
		List<CosmeticsReview> reviews = em.createQuery("select cr from CosmeticsReview cr", CosmeticsReview.class)
											.getResultList();

		// then
		assertThat(reviews).isEmpty(); // Cosmetics 엔티티가 삭제되면서 CosmeticsReview 엔티티도 같이 삭제되었음
	}
	
	@DisplayName("CascadeType.PERSIST - 영속성 전이를 저장 시에만 적용")
	@Test
	void cascadeTypePersist() {
		// given
		LectureReview review1 = new LectureReview();
		LectureReview review2 = new LectureReview();
		LectureReview review3 = new LectureReview();

		Lecture lecture = new Lecture();
		lecture.addReview(review1);
		lecture.addReview(review2);
		lecture.addReview(review3);
		em.persist(lecture); // 부모 엔티티인 Lecture 엔티티만 저장

		em.flush();
		em.clear();

		// when
		List<LectureReview> lectureReviews = em.createQuery("select lr from LectureReview lr", LectureReview.class)
												.getResultList();

		// then
		assertThat(lectureReviews).hasSize(3);
	}

	@DisplayName("CascadeType.REMOVE - 영속성 전이를 삭제 시에만 적용")
	@Test
	void cascadeTypeRemove() {
		// given
		HotelReview review1 = new HotelReview();
		HotelReview review2 = new HotelReview();
		HotelReview review3 = new HotelReview();
		em.persist(review1);
		em.persist(review2);
		em.persist(review3);

		Hotel hotel = new Hotel();
		hotel.addReview(review1);
		hotel.addReview(review2);
		hotel.addReview(review3);
		em.persist(hotel);

		em.flush();
		em.clear();

		Hotel foundHotel = em.find(Hotel.class, hotel.getId());

		// when
		em.remove(foundHotel);
		List<HotelReview> reviews = em.createQuery("select hr from HotelReview hr", HotelReview.class)
										.getResultList();
		// then
		assertThat(reviews).isEmpty();
	}

}
