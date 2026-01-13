package fr.ensitech.biblio.repository;

import fr.ensitech.biblio.entity.Author;
import fr.ensitech.biblio.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface IBookRepository extends JpaRepository<Book, Long> {

    List<Book> findByTitleContainingIgnoreCase(String title);

    List<Book> findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            String title,
            String description
    );

    List<Book> findByAuthorsContaining(Author author);

    List<Book> findByPublicationDateBetween(LocalDate startDate, LocalDate endDate);

    List<Book> findByPublished(boolean published);

    Book findByIsbnIgnoreCase(String isbn);
}
