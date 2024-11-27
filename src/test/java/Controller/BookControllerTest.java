package Controller;

import com.aclib.aclib_deploy.Controller.BookController;
import com.aclib.aclib_deploy.DTO.BookDTO;
import com.aclib.aclib_deploy.Service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class BookControllerTest {
    private BookController bookController;
    private BookService bookService;

    @BeforeEach
    public void setUp() {
        bookService = mock(BookService.class);
        bookController = new BookController(bookService);
    }

    @Test
    public void testGetBookByTitle() {
        String title = "Harry Potter";
        BookDTO bookDTO = new BookDTO(
                "1", new String[]{"J.K. Rowling"}, title, "Fantasy", "Publisher", "Description",
                new String[]{"tag1", "tag2"}, "ISBN", "Language", 2000, "CoverUrl", true
        );
        when(bookService.searchByTitle(title)).thenReturn(List.of(bookDTO));

        ResponseEntity<List<BookDTO>> result = bookController.getBookByTitle(title);

        assertEquals(ResponseEntity.ok(List.of(bookDTO)), result);
    }

    @Test
    public void testGetHomepageData() {
        List<BookDTO> bookDTO = List.of(new BookDTO(
                        "1", new String[]{"J.K. Rowling"}, "Harry Potter", "Fantasy", "Publisher", "Description",
                        new String[]{"tag1", "tag2"}, "ISBN", "Language", 2000, "CoverUrl", true
                ), new BookDTO(
                        "2", new String[]{"J.K. Rowling"}, "Harry Potter 2", "Fantasy", "Publisher", "Description",
                        new String[]{"tag1", "tag2"}, "ISBN", "Language", 2000, "CoverUrl", true
                )
        );
        when(bookService.getHomepageBooks()).thenReturn(bookDTO);

        ResponseEntity result = bookController.getHomepageData();

        assertEquals(ResponseEntity.ok(Map.of("featuredBooks", bookDTO)), result);
    }

    @Test
    public void testGetSearchedBookByTitle() {
        String id = "1";
        BookDTO bookDTO = new BookDTO(
                id, new String[]{"J.K. Rowling"}, "Harry Potter", "Fantasy", "Publisher", "Description",
                new String[]{"tag1", "tag2"}, "ISBN", "Language", 2000, "CoverUrl", true
        );
        when(bookService.searchById(id)).thenReturn(bookDTO);

        ResponseEntity<BookDTO> result = bookController.getSearchedBookByTitle(id);

        assertEquals(ResponseEntity.ok(bookDTO), result);
    }
}

