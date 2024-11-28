package ThirdPartyService;

import com.aclib.aclib_deploy.DTO.BookDTO;
import com.aclib.aclib_deploy.ThirdPartyService.GoogleBookResponseSelfLink;
import com.aclib.aclib_deploy.ThirdPartyService.GoogleBooksResponse;
import com.aclib.aclib_deploy.ThirdPartyService.GoogleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class GoogleServiceTest {

    private GoogleService googleService;

    private RestTemplate restTemplate;

    @BeforeEach
    public void setUp() {
        googleService = new GoogleService();
        restTemplate = Mockito.mock(RestTemplate.class);
    }

    @Test
    public void testSearchBooks() {
        String searchQuery = "Harry Potter";
        String url = "https://www.googleapis.com/books/v1/volumes?q=" + searchQuery + "&startIndex=0&maxResults=40&key=AIzaSyD9foUUvM-Qo_0OwEPgE6vOY-Mqvpvwi8U";

        GoogleBooksResponse response = new GoogleBooksResponse();

        when(restTemplate.getForEntity(url, GoogleBooksResponse.class))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        List<BookDTO> books = googleService.searchBooks(searchQuery);
        assertNotNull(books);
    }

    @Test
    public void testSearchByIdSelfLink() {
        String id = "123";
        try {
            String url = "https://www.googleapis.com/books/v1/volumes/" + id + "?key=" + "AIzaSyD9foUUvM-Qo_0OwEPgE6vOY-Mqvpvwi8U";
            GoogleBookResponseSelfLink response = new GoogleBookResponseSelfLink();
            when(restTemplate.getForEntity(url, GoogleBookResponseSelfLink.class))
                    .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

            BookDTO book = googleService.searchByIdSelfLink(id);

            assertNotNull(book);

        } catch (HttpServerErrorException e) {
            assertEquals(HttpStatus.SERVICE_UNAVAILABLE, e.getStatusCode());
        }
    }

}
