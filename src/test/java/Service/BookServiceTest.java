package Service;

import com.aclib.aclib_deploy.DTO.BookDTO;
import com.aclib.aclib_deploy.Entity.Book;
import com.aclib.aclib_deploy.Exception.BookNotFoundException;
import com.aclib.aclib_deploy.Repository.BookRepository;
import com.aclib.aclib_deploy.Service.BookService;
import com.aclib.aclib_deploy.ThirdPartyService.GoogleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookServiceTest {
    private BookService bookService;
    private GoogleService googleService;
    private BookRepository bookRepository;

    @BeforeEach
    public void setUp() throws NoSuchFieldException, IllegalAccessException {
        googleService = mock(GoogleService.class);
        bookRepository = mock(BookRepository.class);
        bookService = new BookService();

        Field googleServiceField = BookService.class.getDeclaredField("googleService");
        googleServiceField.setAccessible(true);
        googleServiceField.set(bookService, googleService);

        Field bookRepositoryField = BookService.class.getDeclaredField("bookRepository");
        bookRepositoryField.setAccessible(true);
        bookRepositoryField.set(bookService, bookRepository);
    }

    @Test
    public void testSearchByTitle() {
        String title = "Harry Potter";
        List<BookDTO> googleBooks = new ArrayList<>();
        googleBooks.add(new BookDTO("Harry Potter and the Philosopher's Stone", new String[]{"author1", "author2"}, "123", "selfLink", "thumbnail", "description", new String[]{"category1", "category2"}, "publisher", "publishedDate", 300, "en", true));
        when(googleService.searchBooks(title)).thenReturn(googleBooks);
        List<BookDTO> result = bookService.searchByTitle(title);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("Harry Potter and the Philosopher's Stone", result.get(0).getTitle());

    }

    @Test
    public void testSearchByTitleNotFound() {
        String title = "Harry Potter";
        List<BookDTO> googleBooks = new ArrayList<>();
        when(googleService.searchBooks(title)).thenReturn(googleBooks);

        assertThrows(Exception.class, () -> bookService.searchByTitle(title));
    }

    @Test
    public void testGetHomepageBooks() {
        when(bookRepository.findRecentlyAddedBooks()).thenReturn(new ArrayList<>());
        List<BookDTO> result = bookService.getHomepageBooks();

        assertNotNull(result);
        assertEquals(0, result.size());
    }

    @Test
    public void testSearchById() {
        String id = "123";
        Book book = new Book();
        book.setTitle("Harry Potter and the Philosopher's Stone");
        book.setAuthor("author1, author2");
        book.setIdSelfLink("123");
        book.setSelfLink("selfLink");
        book.setThumbnail("thumbnail");
        book.setPublisher("publisher");
        book.setPublishDate("publishedDate");
        book.setCopy(1);
        when(bookRepository.findByIdSelfLink(id)).thenReturn(book);
        BookDTO result = bookService.searchById(id);

        assertNotNull(result);
        assertEquals("Harry Potter and the Philosopher's Stone", result.getTitle());
    }
}
