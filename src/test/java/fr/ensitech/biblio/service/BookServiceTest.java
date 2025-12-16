package fr.ensitech.biblio.service;

import fr.ensitech.biblio.entity.Author;
import fr.ensitech.biblio.entity.Book;
import fr.ensitech.biblio.repository.IAuthorRepository;
import fr.ensitech.biblio.repository.IBookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private IBookRepository bookRepository;

    @Mock
    private IAuthorRepository authorRepository;

    @InjectMocks
    private BookService bookService;

    private Book book;
    private Author author;

    @BeforeEach
    void setUp() {
        author = new Author();
        author.setId(1L);
        author.setFirstname("Pascal");
        author.setLastname("Lambert");

        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.MARCH, 15, 0, 0, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        book = new Book();
        book.setId(1L);
        book.setTitle("Programmation Java");
        book.setDescription("Cours et exercices");
        book.setIsbn("ABC123");
        book.setEditor("Eyrolles");
        book.setCategory("Informatique");
        book.setNbPages((short) 300);
        book.setLanguage("FR");
        book.setPublished(true);
        book.setPublishedDate(calendar.getTime());
        book.setAuthors(new HashSet<>(Collections.singletonList(author)));
    }

    /* ==================================== getBooks() ==================================== */

    @Test
    @DisplayName("Doit retourner tous les livres")
    void shouldReturnAllBooks() throws Exception {
        // GIVEN
        List<Book> books = Collections.singletonList(book);
        when(bookRepository.findAll()).thenReturn(books);

        // WHEN
        List<Book> result = bookService.getBooks();

        // THEN
        assertThat(result).isNotNull().hasSize(1).containsExactly(book);
        verify(bookRepository).findAll();
    }

    /* ==================================== getBook(long id) ==================================== */

    @Test
    @DisplayName("Doit retourner un livre existant")
    void shouldReturnBookWhenIdExists() throws Exception {
        // GIVEN
        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));

        // WHEN
        Book result = bookService.getBook(1L);

        // THEN
        assertThat(result).isEqualTo(book);
        verify(bookRepository).findById(1L);
    }

    @Test
    @DisplayName("Doit retourner null lorsque le livre n'existe pas")
    void shouldReturnNullWhenBookDoesNotExist() throws Exception {
        // GIVEN
        when(bookRepository.findById(2L)).thenReturn(Optional.empty());

        // WHEN
        Book result = bookService.getBook(2L);

        // THEN
        assertThat(result).isNull();
        verify(bookRepository).findById(2L);
    }

    /* ==================================== getBooksByTitle(String) ==================================== */

    @Test
    @DisplayName("Doit retourner les livres par titre")
    void shouldReturnBooksByTitle() throws Exception {
        // GIVEN
        List<Book> books = Collections.singletonList(book);
        when(bookRepository.findByTitleContainingIgnoreCase("java")).thenReturn(books);

        // WHEN
        List<Book> result = bookService.getBooksByTitle("java");

        // THEN
        assertThat(result).containsExactly(book);
        verify(bookRepository).findByTitleContainingIgnoreCase("java");
    }

    /* ==================================== getBooksByAuthor(Author) ==================================== */

    @Test
    @DisplayName("Retourne null tant que la méthode n'est pas implémentée")
    void shouldReturnNullUntilGetBooksByAuthorIsImplemented() throws Exception {
        // WHEN
        List<Book> result = bookService.getBooksByAuthor(author);

        // THEN
        assertThat(result).isNull();
        verifyNoInteractions(bookRepository, authorRepository);
    }

    /* ==================================== getBooksBetweenYears(int,int) ==================================== */

    @Test
    @DisplayName("Doit retourner les livres publiés entre deux années")
    void shouldReturnBooksBetweenYears() throws Exception {
        // GIVEN
        List<Book> books = Collections.singletonList(book);
        when(bookRepository.findByPublishedDateBetween(any(Date.class), any(Date.class))).thenReturn(books);
        ArgumentCaptor<Date> startCaptor = ArgumentCaptor.forClass(Date.class);
        ArgumentCaptor<Date> endCaptor = ArgumentCaptor.forClass(Date.class);

        // WHEN
        List<Book> result = bookService.getBooksBetweenYears(1999, 2001);

        // THEN
        assertThat(result).containsExactly(book);
        verify(bookRepository).findByPublishedDateBetween(startCaptor.capture(), endCaptor.capture());

        Calendar startCal = Calendar.getInstance();
        startCal.setTime(startCaptor.getValue());
        Calendar endCal = Calendar.getInstance();
        endCal.setTime(endCaptor.getValue());

        assertThat(startCal.get(Calendar.YEAR)).isEqualTo(1999);
        assertThat(startCal.get(Calendar.MONTH)).isEqualTo(Calendar.JANUARY);
        assertThat(startCal.get(Calendar.DAY_OF_MONTH)).isEqualTo(1);

        assertThat(endCal.get(Calendar.YEAR)).isEqualTo(2001);
        assertThat(endCal.get(Calendar.MONTH)).isEqualTo(Calendar.DECEMBER);
        assertThat(endCal.get(Calendar.DAY_OF_MONTH)).isEqualTo(31);
    }

    /* ==================================== getBooksByPublished(boolean) ==================================== */

    @Test
    @DisplayName("Doit retourner les livres par statut de publication")
    void shouldReturnBooksByPublishedFlag() {
        // GIVEN
        List<Book> books = Collections.singletonList(book);
        when(bookRepository.findByPublished(true)).thenReturn(books);

        // WHEN
        List<Book> result = bookService.getBooksByPublished(true);

        // THEN
        assertThat(result).containsExactly(book);
        verify(bookRepository).findByPublished(true);
    }

    /* ==================================== getBookByIsbn(String) ==================================== */

    @Test
    @DisplayName("Doit retourner un livre par ISBN")
    void shouldReturnBookByIsbn() {
        // GIVEN
        when(bookRepository.findByIsbnIgnoreCase("ABC123")).thenReturn(book);

        // WHEN
        Book result = bookService.getBookByIsbn("ABC123");

        // THEN
        assertThat(result).isEqualTo(book);
        verify(bookRepository).findByIsbnIgnoreCase("ABC123");
    }

    /* ==================================== getBooksByTitleOrDescription(String,String) ==================================== */

    @Test
    @DisplayName("Doit retourner les livres par titre ou description")
    void shouldReturnBooksByTitleOrDescription() {
        // GIVEN
        List<Book> books = Collections.singletonList(book);
        when(bookRepository.findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase("Java","exercices"))
                .thenReturn(books);

        // WHEN
        List<Book> result = bookService.getBooksByTitleOrDescription("Java","exercices");

        // THEN
        assertThat(result).containsExactly(book);
        verify(bookRepository).findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase("Java","exercices");
    }
}
