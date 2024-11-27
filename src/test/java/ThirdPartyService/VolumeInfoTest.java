package ThirdPartyService;

import com.aclib.aclib_deploy.ThirdPartyService.ImageLinks;
import com.aclib.aclib_deploy.ThirdPartyService.VolumeInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class VolumeInfoTest {
    private VolumeInfo volumeInfo;

    @BeforeEach
    public void setUp() {
        volumeInfo = new VolumeInfo();
    }

    @Test
    public void testSetAndGetTitle() {
        volumeInfo.setTitle("title");
        assertEquals("title", volumeInfo.getTitle());
    }

    @Test
    public void testSetAndGetAuthors() {
        String[] authors = {"author1", "author2"};
        volumeInfo.setAuthors(authors);
        assertArrayEquals(authors, volumeInfo.getAuthors());
    }

    @Test
    public void testSetAndGetImageLinks() {
        ImageLinks imageLinks = new ImageLinks();
        volumeInfo.setImageLinks(imageLinks);
        assertEquals(imageLinks, volumeInfo.getImageLinks());
    }

    @Test
    public void testSetAndGetDescription() {
        volumeInfo.setDescription("description");
        assertEquals("description", volumeInfo.getDescription());
    }

    @Test
    public void testSetAndGetPublisher() {
        volumeInfo.setPublisher("publisher");
        assertEquals("publisher", volumeInfo.getPublisher());
    }

    @Test
    public void testSetAndGetPublishedDate() {
        volumeInfo.setPublishedDate("publishedDate");
        assertEquals("publishedDate", volumeInfo.getPublishedDate());
    }

    @Test
    public void testSetAndGetPageCount() {
        volumeInfo.setPageCount(10);
        assertEquals(10, volumeInfo.getPageCount());
    }

    @Test
    public void testSetAndGetLanguage() {
        volumeInfo.setLanguage("en");
        assertEquals("en", volumeInfo.getLanguage());
    }

    @Test
    public void testSetAndGetisAvailble() {
        volumeInfo.setAvailble(true);
        assertTrue(volumeInfo.isAvailble());
    }

    @Test
    public void testSetAndGetCategories() {
        String[] categories = {"category1", "category2"};
        volumeInfo.setCategories(categories);
        assertArrayEquals(categories, volumeInfo.getCategories());
    }

}
