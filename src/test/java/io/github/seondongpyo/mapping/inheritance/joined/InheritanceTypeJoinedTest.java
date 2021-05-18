package io.github.seondongpyo.mapping.inheritance.joined;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

public class InheritanceTypeJoinedTest {

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

    @DisplayName("상속관계 매핑 전략 - JOINED")
    @Test
    void joined() {
        // given
        Album album = new Album("IU");
        album.setName("LILAC");
        em.persist(album);

        Book book = new Book("이상민", "978-89-97924-83-7");
        book.setName("누가 IT 시장 취업에 성공하는가");
        em.persist(book);

        Movie movie = new Movie("정이삭", "윤여정");
        movie.setName("미나리");
        em.persist(movie);

        em.flush();
        em.clear();

        // when
        Item foundAlbum = em.find(Item.class, album.getId());
        Item foundBook = em.find(Item.class, book.getId());
        Item foundMovie = em.find(Item.class, movie.getId());

        // then
        assertThat(foundAlbum instanceof Album).isTrue();
        assertThat(foundAlbum.getName()).isEqualTo("LILAC");
        assertThat(foundBook instanceof Book).isTrue();
        assertThat(foundBook.getName()).isEqualTo("누가 IT 시장 취업에 성공하는가");
        assertThat(foundMovie instanceof Movie).isTrue();
        assertThat(foundMovie.getName()).isEqualTo("미나리");
    }

}
