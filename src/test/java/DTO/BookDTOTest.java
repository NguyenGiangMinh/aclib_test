package DTO;

import com.aclib.aclib_deploy.DTO.BookDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class BookDTOTest {
    private BookDTO bookDTO;
    private BookDTO bookDTO1;

    @BeforeEach
    void setUp() {
        bookDTO = new BookDTO("Title", new String[]{"Author1", "Author2"}, "id123", "selfLink123", "thumbnail123", "description123", new String[]{"Category1"}, "Publisher123", "2023-01-01", 300, "en", true);
        bookDTO1 = new BookDTO("Title", new String[]{"Author1", "Author2"}, "id123", "selfLink123", "thumbnail123", "Publisher123", "2023-01-01", "Available", 1);
    }

    @Test
    public void testGetTitle() {
        assertEquals("Title", bookDTO.getTitle());
    }

    @Test
    public void testSetTitle() {
        bookDTO.setTitle("New Title");
        assertEquals("New Title", bookDTO.getTitle());
    }

    @Test
    public void testGetAuthors() {
        assertArrayEquals(new String[]{"Author1", "Author2"}, bookDTO.getAuthors());
    }

    @Test
    public void testSetAuthors() {
        bookDTO.setAuthors(new String[]{"New Author1", "New Author2"});
        assertArrayEquals(new String[]{"New Author1", "New Author2"}, bookDTO.getAuthors());
    }

    @Test
    public void testgetDescription() {
        assertEquals("description123", bookDTO.getDescription());
    }

    @Test
    public void testSetDescription() {
        bookDTO.setDescription("newDescription123");
        assertEquals("newDescription123", bookDTO.getDescription());
    }

    @Test
    public void testisAvailableForBorrowing() {
        assertTrue(bookDTO.isAvailableForBorrowing());
    }

    @Test
    public void testSetAvailableForBorrowing() {
        bookDTO.setAvailableForBorrowing(false);
        assertEquals(false, bookDTO.isAvailableForBorrowing());
    }

    @Test
    public void testGetId() {
        assertEquals("id123", bookDTO.getId());
    }

    @Test
    public void testSetId() {
        bookDTO.setId("newId123");
        assertEquals("newId123", bookDTO.getId());
    }

    @Test
    public void testGetPublisher() {
        assertEquals("Publisher123", bookDTO.getPublisher());
    }

    @Test
    public void testSetPublisher() {
        bookDTO.setPublisher("newPublisher123");
        assertEquals("newPublisher123", bookDTO.getPublisher());
    }

    @Test
    public void testGetPublishedDate() {
        assertEquals("2023-01-01", bookDTO.getPublishedDate());
    }

    @Test
    public void testSetPublishedDate() {
        bookDTO.setPublishedDate("2023-01-02");
        assertEquals("2023-01-02", bookDTO.getPublishedDate());
    }

    @Test
    public void testGetPageCount() {
        assertEquals(300, bookDTO.getPageCount());
    }

    @Test
    public void testSetPageCount() {
        bookDTO.setPageCount(400);
        assertEquals(400, bookDTO.getPageCount());
    }

    @Test
    public void testGetLanguage() {
        assertEquals("en", bookDTO.getLanguage());
    }

    @Test
    public void testSetLanguage() {
        bookDTO.setLanguage("fr");
        assertEquals("fr", bookDTO.getLanguage());
    }

    @Test
    public void testGetSelfLink() {
        assertEquals("selfLink123", bookDTO.getSelfLink());
    }

    @Test
    public void testSetSelfLink() {
        bookDTO.setSelfLink("newSelfLink123");
        assertEquals("newSelfLink123", bookDTO.getSelfLink());
    }

    @Test
    public void testGetThumbnail() {
        assertEquals("thumbnail123", bookDTO.getThumbnail());
    }

    @Test
    public void testSetThumbnail() {
        bookDTO.setThumbnail("newThumbnail123");
        assertEquals("newThumbnail123", bookDTO.getThumbnail());
    }

    @Test
    public void testGetCopy() {
        assertEquals(1, bookDTO1.getCopy());
    }

    @Test
    public void testSetCopy() {
        bookDTO.setCopy(2);
        assertEquals(2, bookDTO.getCopy());
    }

    @Test
    public void testGetStatus() {
        assertEquals("Available", bookDTO1.getStatus());
    }

    @Test
    public void testSetStatus() {
        bookDTO.setStatus("Borrowed");
        assertEquals("Borrowed", bookDTO.getStatus());
    }

    @Test
    public void testGetCategory() {
        assertArrayEquals(new String[]{"Category1"}, bookDTO.getCategory());
    }

    @Test
    public void testSetCategory() {
        bookDTO.setCategory(new String[]{"Category2"});
        assertArrayEquals(new String[]{"Category2"}, bookDTO.getCategory());
    }

}
