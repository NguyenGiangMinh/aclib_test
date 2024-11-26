package ThirdPartyService;

import com.aclib.aclib_deploy.ThirdPartyService.GoogleBooksResponse;
import com.aclib.aclib_deploy.ThirdPartyService.VolumeInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

public class GoogleBookResponseTest {
    private GoogleBooksResponse googleBooksResponse;
    private VolumeInfo volumeInfo;
    private GoogleBooksResponse.Item item;

    @BeforeEach
    public void setUp() {
        googleBooksResponse = new GoogleBooksResponse();
        volumeInfo = new VolumeInfo();
        item = new GoogleBooksResponse.Item();
    }

    @Test
    public void testSetAndGetItems() {
        List<GoogleBooksResponse.Item> items = new ArrayList<>();
        items.add(item);
        googleBooksResponse.setItems(items);
        assertEquals(items, googleBooksResponse.getItems());
    }

    @Test
    public void testSetAndGetVolumeInfo() {
        item.setVolumeInfo(volumeInfo);
        assertEquals(volumeInfo, item.getVolumeInfo());
    }

    @Test
    public void testGetId() {
        assertNull(item.getId());
    }

    @Test
    public void testSetAndGetSelfLink() {
        item.setSelfLink("selfLink");
        assertEquals("selfLink", item.getSelfLink());
    }

}
