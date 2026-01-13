package fr.ensitech.biblio.service;

import fr.ensitech.biblio.entity.Book;
import fr.ensitech.biblio.repository.IBookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private IBookRepository bookRepository;

    @InjectMocks
    private BookService bookService;

    private Book book;

    @BeforeEach
    void setUp() {
        book = new Book();
        book.setId(1L);
        book.setTitle("Test Book");
        book.setDescription("Description");
        book.setIsbn("ISBN123");
        book.setEditor("Editor");
        book.setCategory("Category");
        book.setLanguage("FR");
        book.setNbPages((short) 200);
        book.setPublished(true);
        book.setPublicationDate(LocalDate.of(2020, 1, 1));
    }

    @Test
    void shouldReturnBooksBetweenYears() throws Exception {
        LocalDate start = LocalDate.of(2019, 1, 1);
        LocalDate end = LocalDate.of(2021, 12, 31);

        when(bookRepository.findByPublicationDateBetween(start, end))
                .thenReturn(List.of(book));

        List<Book> result = bookService.getBooksBetweenYears(2019, 2021);

        assertEquals(1, result.size());
        verify(bookRepository).findByPublicationDateBetween(start, end);
    }

    @Test
    void shouldReturnBooksByPublished() {
        when(bookRepository.findByPublished(true))
                .thenReturn(List.of(book));

        List<Book> result = bookService.getBooksByPublished(true);

        assertEquals(1, result.size());
        verify(bookRepository).findByPublished(true);
    }
}
