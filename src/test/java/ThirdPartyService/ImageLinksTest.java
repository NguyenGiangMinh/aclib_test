package ThirdPartyService;

import com.aclib.aclib_deploy.ThirdPartyService.ImageLinks;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class ImageLinksTest {
    private ImageLinks imageLinks;

    @BeforeEach
    public void setUp() {
        imageLinks = new ImageLinks();
    }

    @Test
    public void testSetAndGetThumbnail() {
        imageLinks.setThumbnail("thumbnail");
        assert (imageLinks.getThumbnail().equals("thumbnail"));
    }
}
