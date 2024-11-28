package Entity;

import com.aclib.aclib_deploy.Entity.Book;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BookTest {
    private Book book;

    @BeforeEach
    public void setUp() {
        book = new Book();
    }

    @Test
    void testSetAndGetBookId() {
        book.setId(12340);
        assertEquals(12340, book.getId());
    }

    @Test
    void testSetAndGetBookId1() {
        book.setId(000010);
        assertEquals(000010, book.getId());
    }

    @Test
    void testSetAndGetIdselfLink() {
        book.setIdSelfLink("12340abc");
        assertEquals("12340abc", book.getIdSelfLink());
    }

    @Test
    void testSetAndGetIdselfLink1() {
        book.setIdSelfLink("abcdef");
        assertEquals("abcdef", book.getIdSelfLink());
    }

    @Test
    void testSetAndGetBookTitle() {
        book.setTitle("Harry Potter");
        assertEquals("Harry Potter", book.getTitle());
    }

    @Test
    void testSetAndGetBookTitle1() {
        book.setTitle("The Platform");
        assertEquals("The Platform", book.getTitle());
    }

    @Test
    void testSetAndGetBookTitle2() {
        book.setTitle("The Platform 2");
        assertEquals("The Platform 2", book.getTitle());
    }

    @Test
    void testSetAndGetAuthor() {
        book.setAuthor("LHNam2005");
        assertEquals("LHNam2005", book.getAuthor());
    }

    @Test
    void testSetAndGetAuthor1() {
        book.setAuthor("Minh");
        assertEquals("Minh", book.getAuthor());
    }

    @Test
    void testSetAndGetPublishDate() {
        book.setPublishDate("19-05-1890");
        assertEquals("19-05-1890", book.getPublishDate());
    }

    @Test
    void testSetAndGetPublishDate1() {
        book.setPublishDate("15-09-2005");
        assertEquals("15-09-2005", book.getPublishDate());
    }

    @Test
    void testSetAndGetPublishDate2() {
        book.setPublishDate("15-09-2005");
        assertEquals("15-09-2005", book.getPublishDate());
    }

    @Test
    void testSetAndGetThumbnail() {
        book.setThumbnail("LouisVN.png");
        assertEquals("LouisVN.png", book.getThumbnail());
    }

    @Test
    void testSetAndGetThumbnail1() {
        book.setThumbnail("Namdz.png");
        assertEquals("Namdz.png", book.getThumbnail());
    }

    @Test
    void testSetAndGetThumbnail2() {
        book.setThumbnail("Namdz2.png");
        assertEquals("Namdz2.png", book.getThumbnail());
    }

    @Test
    void testSetAndGetCopy() {
        book.setCopy(10);
        assertEquals(10, book.getCopy());
    }

    @Test
    void testSetAndGetCopy1() {
        book.setCopy(0);
        assertEquals(0, book.getCopy());
    }

    @Test
    void testSetAndGetPublisher() {
        book.setPublisher("ACCode");
        assertEquals("ACCode", book.getPublisher());
    }

    @Test
    void testSetAndGetPublisher1() {
        book.setPublisher("AC");
        assertEquals("AC", book.getPublisher());
    }

    @Test
    void testSetAndGetSeltLink() {
        book.setSelfLink("accode.com");
        assertEquals("accode.com", book.getSelfLink());
    }

    @Test
    void testSetAndGetSeltLink2() {
        book.setSelfLink("accode2.com");
        assertEquals("accode2.com", book.getSelfLink());
    }

    @Test
    void testSetAndGetSeltLink1() {
        book.setSelfLink("accodehehe.com");
        assertEquals("accodehehe.com", book.getSelfLink());
    }


    @Test
    void testSetAndGetCategory() {
        book.setCategory("Science");
        assertEquals("Science", book.getCategory());
    }

    @Test
    void testSetAndGetCategory1() {
        book.setCategory("Math");
        assertEquals("Math", book.getCategory());
    }

    @Test
    void testSetAndGetCategory2() {
        book.setCategory("History");
        assertEquals("History", book.getCategory());
    }
}
