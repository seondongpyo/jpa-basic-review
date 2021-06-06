package io.github.seondongpyo.mapping.common;

import static org.assertj.core.api.Assertions.*;

import java.time.LocalDateTime;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class MappedSuperClassTest {

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

	@DisplayName("@MappedSuperClass - 공통 매핑 정보를 모으는 용도로 사용")
	@Test
	void mappedSuperClass() {
		// given
		LocalDateTime createdAt = LocalDateTime.of(2021, 5, 9, 19, 15, 30);

		Post post = new Post();
		post.setCreatedBy("글 작성자");
		post.setCreatedAt(createdAt);
		em.persist(post);

		Comment comment = new Comment();
		comment.setCreatedBy("댓글 작성자");
		comment.setCreatedAt(createdAt.plusHours(1));
		comment.setPost(post);
		em.persist(comment);

		em.flush();
		em.clear();

		// when
		Comment foundComment = em.find(Comment.class, comment.getId());

		// then
		assertThat(foundComment.getPost().getCreatedAt()).isEqualTo(createdAt);
		assertThat(foundComment.getPost().getCreatedBy()).isEqualTo("글 작성자");
		assertThat(foundComment.getCreatedAt()).isEqualTo(createdAt.plusHours(1));
		assertThat(foundComment.getCreatedBy()).isEqualTo("댓글 작성자");
	}

}
