package fr.ensitech.biblio.service;

import fr.ensitech.biblio.entity.Author;
import fr.ensitech.biblio.entity.Book;
import fr.ensitech.biblio.repository.IBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
public class BookService implements IBookService {

    @Autowired
    private IBookRepository bookRepository;

    @Override
    public void addOrUpdateBook(Book book) throws Exception {
        if (book.getId() < 0) {
            throw new Exception("Book id must be greater than 0 !");
        }

        if (book.getId() == 0) {
            bookRepository.save(book);
        } else {
            Book _book = bookRepository.findById(book.getId())
                    .orElseThrow(() -> new Exception("Book not found"));

            _book.setIsbn(book.getIsbn());
            _book.setTitle(book.getTitle());
            _book.setDescription(book.getDescription());
            _book.setEditor(book.getEditor());
            _book.setPublicationDate(book.getPublicationDate());
            _book.setCategory(book.getCategory());
            _book.setLanguage(book.getLanguage());
            _book.setNbPages(book.getNbPages());
            _book.setPublished(book.getPublished());

            bookRepository.save(_book);
        }
    }

    @Override
    public void deleteBook(long id) throws Exception {
        bookRepository.deleteById(id);
    }

    @Override
    public List<Book> getBooks() throws Exception {
        return bookRepository.findAll();
    }

    @Override
    public Book getBook(long id) throws Exception {
        Optional<Book> optional = bookRepository.findById(id);
        return optional.orElse(null);
    }

    @Override
    public List<Book> getBooksByTitle(String title) throws Exception {
        return bookRepository.findByTitleContainingIgnoreCase(title);
    }

    @Override
    public List<Book> getBooksByAuthor(Author author) throws Exception {
        return bookRepository.findByAuthorsContaining(author);
    }

    @Override
    public List<Book> getBooksBetweenYears(int startYear, int endYear) throws Exception {
        LocalDate startDate = LocalDate.of(startYear, 1, 1);
        LocalDate endDate = LocalDate.of(endYear, 12, 31);
        return bookRepository.findByPublicationDateBetween(startDate, endDate);
    }

    // ✅ BOOLEAN ALIGNÉ
    @Override
    public List<Book> getBooksByPublished(boolean published) {
        return bookRepository.findByPublished(published);
    }

    @Override
    public Book getBookByIsbn(String isbn) {
        return bookRepository.findByIsbnIgnoreCase(isbn);
    }

    @Override
    public List<Book> getBooksByTitleOrDescription(String title, String description) {
        return bookRepository
                .findByTitleContainingIgnoreCaseOrDescriptionContainingIgnoreCase(title, description);
    }
}
