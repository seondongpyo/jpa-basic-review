package io.github.seondongpyo.mapping.relation.manytomany;

import static org.assertj.core.api.Assertions.*;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import io.github.seondongpyo.mapping.relation.manytomany.alternative.Attendee;
import io.github.seondongpyo.mapping.relation.manytomany.alternative.Course;
import io.github.seondongpyo.mapping.relation.manytomany.alternative.CourseRegistration;
import io.github.seondongpyo.mapping.relation.manytomany.bidirectional.Task;
import io.github.seondongpyo.mapping.relation.manytomany.bidirectional.Worker;
import io.github.seondongpyo.mapping.relation.manytomany.unidirectional.Bank;
import io.github.seondongpyo.mapping.relation.manytomany.unidirectional.Client;

@DisplayName("다대다(N:M) 연관관계 매핑")
class ManyToManyTest {

	private EntityManagerFactory emf;
	private EntityManager em;
	private EntityTransaction tx;

	@BeforeEach
	void setup() {
		emf = Persistence.createEntityManagerFactory("basic");
		em = emf.createEntityManager();
		tx = em.getTransaction();
		tx.begin();
	}

	@AfterEach
	void close() {
		em.close();
		emf.close();
	}

	@DisplayName("단방향 연관관계 매핑")
	@Test
	void unidirectional() {
		// given
		Bank shinhanBank = new Bank("신한은행");
		Bank wooriBank = new Bank("우리은행");
		em.persist(shinhanBank);
		em.persist(wooriBank);

		Client client = new Client();
		client.getBanks().add(shinhanBank);
		client.getBanks().add(wooriBank);
		em.persist(client);

		em.flush();
		em.clear();

		// when
		Client foundClient = em.find(Client.class, client.getId());

		// then
		assertThat(foundClient.getBanks()).hasSize(2);
	}

	@DisplayName("양방향 연관관계 매핑")
	@Test
	void bidirectional() {
		// given
		Task task1 = new Task("미장");
		Task task2 = new Task("도배");
		em.persist(task1);
		em.persist(task2);

		Worker worker1 = new Worker("작업자1");
		Worker worker2 = new Worker("작업자2");
		Worker worker3 = new Worker("작업자3");
		worker1.getTasks().add(task1);
		worker2.getTasks().add(task1);
		worker3.getTasks().add(task2);
		em.persist(worker1);
		em.persist(worker2);
		em.persist(worker3);

		em.flush();
		em.clear();

		// when
		Task foundTask1 = em.find(Task.class, task1.getId());
		Task foundTask2 = em.find(Task.class, task2.getId());

		// then
		assertThat(foundTask1.getWorkers()).hasSize(2);
		assertThat(foundTask2.getWorkers()).hasSize(1);
	}
	
	@DisplayName("대안 : @ManyToMany를 @OneToMany, @ManyToOne으로 풀어내라.")
	@Test
	void alternative() {
		// given
		Course java = new Course("Java");
		Course spring = new Course("Spring");
		em.persist(java);
		em.persist(spring);

		Attendee attendee1 = new Attendee("학생1");
		Attendee attendee2 = new Attendee("학생2");
		Attendee attendee3 = new Attendee("학생3");
		Attendee attendee4 = new Attendee("학생4");
		Attendee attendee5 = new Attendee("학생5");
		em.persist(attendee1);
		em.persist(attendee2);
		em.persist(attendee3);
		em.persist(attendee4);
		em.persist(attendee5);

		CourseRegistration javaCourseRegistration1 = new CourseRegistration(java, attendee1);
		CourseRegistration javaCourseRegistration2 = new CourseRegistration(java, attendee2);
		CourseRegistration javaCourseRegistration3 = new CourseRegistration(java, attendee3);
		CourseRegistration springCourseRegistration1 = new CourseRegistration(spring, attendee4);
		CourseRegistration springCourseRegistration2 = new CourseRegistration(spring, attendee5);
		em.persist(javaCourseRegistration1);
		em.persist(javaCourseRegistration2);
		em.persist(javaCourseRegistration3);
		em.persist(springCourseRegistration1);
		em.persist(springCourseRegistration2);

		tx.commit();

		// when
		Course javaCourse = em.createQuery("select c from Course c where c.name = :name", Course.class)
								.setParameter("name", "Java")
								.getSingleResult();

		Course springCourse = em.createQuery("select c from Course c where c.name = :name", Course.class)
								.setParameter("name", "Spring")
								.getSingleResult();

		List<Attendee> javaAttendees
			= em.createQuery("select cr.attendee from CourseRegistration cr where cr.course.id = :id", Attendee.class)
				.setParameter("id", javaCourse.getId())
				.getResultList();

		List<Attendee> springAttendees
			= em.createQuery("select cr.attendee from CourseRegistration cr where cr.course.id = :id", Attendee.class)
				.setParameter("id", springCourse.getId())
				.getResultList();

		// then
		assertThat(javaAttendees).hasSize(3);
		assertThat(springAttendees).hasSize(2);
	}

}
