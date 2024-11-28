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
        bookDTO1 = new BookDTO(1L ,"Title", new String[]{"Author1", "Author2"}, "id123", "selfLink123", "thumbnail123", "Publisher123", "2023-01-01",  1);
    }

    @Test
    public void testGetTitle() {
        assertEquals("Title", bookDTO.getTitle());
    }

    @Test
    public void testGetTitle1() {
        assertEquals("Title", bookDTO1.getTitle());
    }

    @Test
    public void testSetTitle() {
        bookDTO.setTitle("New Title");
        assertEquals("New Title", bookDTO.getTitle());
    }

    @Test
    public void testSetTitle1() {
        bookDTO1.setTitle("New Title");
        assertEquals("New Title", bookDTO1.getTitle());
    }

    @Test
    public void testGetAuthors1() {
        assertArrayEquals(new String[]{"Author1", "Author2"}, bookDTO1.getAuthors());
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
    public void testSetAuthors1() {
        bookDTO1.setAuthors(new String[]{"New Author1", "New Author2"});
        assertArrayEquals(new String[]{"New Author1", "New Author2"}, bookDTO1.getAuthors());
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
    public void testSetDescription1() {
        bookDTO1.setDescription("newDescription123");
        assertEquals("newDescription123", bookDTO1.getDescription());
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
    public void testSetAvailableForBorrowing1() {
        bookDTO1.setAvailableForBorrowing(false);
        assertEquals(false, bookDTO1.isAvailableForBorrowing());
    }

    @Test
    public void testGetId() {
        assertEquals("id123", bookDTO.getId());
    }

    @Test
    public void testGetId1() {
        assertEquals("id123", bookDTO1.getId());
    }

    @Test
    public void testSetId() {
        bookDTO.setId("newId123");
        assertEquals("newId123", bookDTO.getId());
    }

    @Test
    public void testSetId1() {
        bookDTO1.setId("newId123");
        assertEquals("newId123", bookDTO1.getId());
    }

    @Test
    public void testGetPublisher() {
        assertEquals("Publisher123", bookDTO.getPublisher());
    }

    @Test
    public void testGetPublisher1() {
        assertEquals("Publisher123", bookDTO1.getPublisher());
    }

    @Test
    public void testSetPublisher() {
        bookDTO.setPublisher("newPublisher123");
        assertEquals("newPublisher123", bookDTO.getPublisher());
    }

    @Test
    public void testSetPublisher1() {
        bookDTO1.setPublisher("newPublisher123");
        assertEquals("newPublisher123", bookDTO1.getPublisher());
    }

    @Test
    public void testGetPublishedDate() {
        assertEquals("2023-01-01", bookDTO.getPublishedDate());
    }

    @Test
    public void testGetPublishedDate1() {
        assertEquals("2023-01-01", bookDTO1.getPublishedDate());
    }

    @Test
    public void testSetPublishedDate() {
        bookDTO.setPublishedDate("2023-01-02");
        assertEquals("2023-01-02", bookDTO.getPublishedDate());
    }

    @Test
    public void testSetPublishedDate1() {
        bookDTO1.setPublishedDate("2023-01-02");
        assertEquals("2023-01-02", bookDTO1.getPublishedDate());
    }

    @Test
    public void testGetPageCount() {
        assertEquals(300, bookDTO.getPageCount());
    }

    @Test
    public void testGetPageCount1() {
        assertEquals(0, bookDTO1.getPageCount());
    }

    @Test
    public void testSetPageCount() {
        bookDTO.setPageCount(400);
        assertEquals(400, bookDTO.getPageCount());
    }

    @Test
    public void testSetPageCount1() {
        bookDTO1.setPageCount(400);
        assertEquals(400, bookDTO1.getPageCount());
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
    public void testSetLanguage1() {
        bookDTO1.setLanguage("fr");
        assertEquals("fr", bookDTO1.getLanguage());
    }

    @Test
    public void testGetSelfLink() {
        assertEquals("selfLink123", bookDTO.getSelfLink());
    }

    @Test
    public void testGetSelfLink1() {
        assertEquals("selfLink123", bookDTO1.getSelfLink());
    }

    @Test
    public void testSetSelfLink() {
        bookDTO.setSelfLink("newSelfLink123");
        assertEquals("newSelfLink123", bookDTO.getSelfLink());
    }

    @Test
    public void testSetSelfLink1() {
        bookDTO1.setSelfLink("newSelfLink123");
    }

    @Test
    public void testGetThumbnail() {
        assertEquals("thumbnail123", bookDTO.getThumbnail());
    }

    @Test
    public void testGetThumbnail1() {
        assertEquals("thumbnail123", bookDTO1.getThumbnail());
    }

    @Test
    public void testSetThumbnail() {
        bookDTO.setThumbnail("newThumbnail123");
        assertEquals("newThumbnail123", bookDTO.getThumbnail());
    }

    @Test
    public void testSetThumbnail1() {
        bookDTO1.setThumbnail("newThumbnail123");
        assertEquals("newThumbnail123", bookDTO1.getThumbnail());
    }

    @Test
    public void testGetCopy() {
        assertEquals(1, bookDTO1.getCopy());
    }

    @Test
    public void testGetCopy1() {
        assertEquals(1, bookDTO1.getCopy());
    }

    @Test
    public void testSetCopy() {
        bookDTO.setCopy(2);
        assertEquals(2, bookDTO.getCopy());
    }

    @Test
    public void testSetCopy1() {
        bookDTO1.setCopy(2);
        assertEquals(2, bookDTO1.getCopy());
    }

    @Test
    public void testGetAndSetInfoLink() {
        bookDTO.setInfoLink("infoLink123");
        assertEquals("infoLink123", bookDTO.getInfoLink());
    }

    @Test
    public void testGetAndSetInfoLink1() {
        bookDTO1.setInfoLink("infoLink123");
        assertEquals("infoLink123", bookDTO1.getInfoLink());
    }


    @Test
    public void testGetAndSetPreviewLink() {
        bookDTO.setPreviewLink("previewLink123");
        assertEquals("previewLink123", bookDTO.getPreviewLink());
    }

    @Test
    public void testGetAndSetId_in_db() {
        bookDTO.setId_in_db(123);
        assertEquals(123, bookDTO.getId_in_db());
    }

    @Test
    public void testGetAndSetId_in_db1() {
        bookDTO1.setId_in_db(123);
        assertEquals(123, bookDTO1.getId_in_db());
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

    @Test
    public void testSetCategory1() {
        bookDTO1.setCategory(new String[]{"Category2"});
        assertArrayEquals(new String[]{"Category2"}, bookDTO1.getCategory());
    }

}
