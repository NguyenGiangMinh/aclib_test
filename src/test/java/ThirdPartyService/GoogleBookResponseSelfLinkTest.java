package ThirdPartyService;

import com.aclib.aclib_deploy.ThirdPartyService.GoogleBookResponseSelfLink;
import com.aclib.aclib_deploy.ThirdPartyService.VolumeInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GoogleBookResponseSelfLinkTest {
    private GoogleBookResponseSelfLink googleBookResponseSelfLink;
    private VolumeInfo volumeInfo;

    @BeforeEach
    public void setUp() {
        googleBookResponseSelfLink = new GoogleBookResponseSelfLink();
        volumeInfo = new VolumeInfo();
    }

    @Test
    public void testSetAndGetId() {
        googleBookResponseSelfLink.setId("id");
        assertEquals("id", googleBookResponseSelfLink.getId());
    }

    @Test
    public void testSetAndGetSelfLink() {
        googleBookResponseSelfLink.setSelfLink("selfLink");
        assertEquals("selfLink", googleBookResponseSelfLink.getSelfLink());
    }

    @Test
    public void testSetAndGetVolumeInfo() {
        googleBookResponseSelfLink.setVolumeInfo(volumeInfo);
        assertEquals(volumeInfo, googleBookResponseSelfLink.getVolumeInfo());
    }

}
