package com.aclib.aclib_deploy.ThirdPartyService;

import com.aclib.aclib_deploy.DTO.BookDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class GoogleService {
    private static final String GOOGLE_BOOKS_API_URL_1 = "https://www.googleapis.com/books/v1/volumes?q=";
    private static final String GOOGLE_BOOKS_API_URL_2 = "https://www.googleapis.com/books/v1/volumes/";
    private static final String API_KEY = "AIzaSyD9foUUvM-Qo_0OwEPgE6vOY-Mqvpvwi8U";
    private static final String resultCount = "&startIndex=0&maxResults=40";
    private static final String resultCount1 = "&startIndex=0&maxResults=20";
    private static final String default_thumbnail = "https://st.quantrimang.com/photos/image/2018/12/18/Anh-Sorry-Pix-2.jpg";

    //Search with key
    public List<BookDTO> searchBooks(String searchQuery) {
        String url = GOOGLE_BOOKS_API_URL_1 + searchQuery + resultCount + "&key=" + API_KEY;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<GoogleBooksResponse> response = restTemplate.getForEntity(url, GoogleBooksResponse.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return mapGoogleBooksToBooks(response.getBody());
        }

        return Collections.emptyList();
    }

    //Search with category
    public List<BookDTO> searchBooksWithCategory(String searchQuery) {
        String url = GOOGLE_BOOKS_API_URL_1 + "subject:" + searchQuery + resultCount1 + "&key=" + API_KEY;
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<GoogleBooksResponse> response = restTemplate.getForEntity(url, GoogleBooksResponse.class);

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            return mapGoogleBooksToBooks(response.getBody());
        }

        return Collections.emptyList();
    }

    private List<BookDTO> mapGoogleBooksToBooks(GoogleBooksResponse googleBooksResponse) {
        return googleBooksResponse.getItems().stream()
                .map(item -> {
                    String title = item.getVolumeInfo().getTitle();
                    String[] authors = item.getVolumeInfo().getAuthors() != null ? item.getVolumeInfo().getAuthors() : new String[]{};
                    String id = item.getId();
                    String selfLink = item.getSelfLink();
                    String thumbnail = (item.getVolumeInfo().getImageLinks() != null
                            && item.getVolumeInfo().getImageLinks().getThumbnail() != null)
                            ? item.getVolumeInfo().getImageLinks().getThumbnail() : default_thumbnail;
                    String description = item.getVolumeInfo().getDescription();
                    String publisher = item.getVolumeInfo().getPublisher();
                    String publishedDate = item.getVolumeInfo().getPublishedDate();
                    String[] categories = item.getVolumeInfo().getCategories();
                    int pageCount = 0;
                    try {
                        pageCount = item.getVolumeInfo().getPageCount();
                    } catch (NumberFormatException | NullPointerException e) {
                        // safety purpose
                    }

                    String language = item.getVolumeInfo().getLanguage();
                    boolean availableForBorrowing = item.getVolumeInfo().isAvailable();

                    return new BookDTO(title, authors, id, selfLink, thumbnail,
                            description, categories ,publisher, publishedDate, pageCount, language, availableForBorrowing);
                })
                .collect(Collectors.toList());
    }


    //search with id_self_Link
    public BookDTO searchByIdSelfLink(String searchQuery) {
        try {
            String url = GOOGLE_BOOKS_API_URL_2 + searchQuery + "?key=AIzaSyD9foUUvM-Qo_0OwEPgE6vOY-Mqvpvwi8U";
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<GoogleBookResponseSelfLink> response = restTemplate.getForEntity(url, GoogleBookResponseSelfLink.class);

            if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
                return mapGoogleBooksToBookDTO(response.getBody());
            }

            return null;
        } catch (HttpServerErrorException e) {
            if (e.getStatusCode() == HttpStatus.SERVICE_UNAVAILABLE) {
                // log if 503 occurs
                System.out.println("503 Service Unavailable. Retrying...");
            }
            throw e;
        }
    }

    private BookDTO mapGoogleBooksToBookDTO(GoogleBookResponseSelfLink item) {
        String title = item.getVolumeInfo().getTitle();
        String[] authors = item.getVolumeInfo().getAuthors();
        String idSelfLink = item.getId();
        String selfLink = item.getSelfLink();
        String thumbnail = (item.getVolumeInfo().getImageLinks() != null) ?
                item.getVolumeInfo().getImageLinks().getThumbnail() : null;
        String publisher = item.getVolumeInfo().getPublisher();
        String description = item.getVolumeInfo().getDescription();
        int pageCount = item.getVolumeInfo().getPageCount();
        String publishDate = item.getVolumeInfo().getPublishedDate();
        String language = item.getVolumeInfo().getLanguage();
        String previewLink = item.getVolumeInfo().getPreviewLink();
        String infoLink = item.getVolumeInfo().getInfoLink();

        return new BookDTO(title, authors, idSelfLink, selfLink, thumbnail,
                publisher, pageCount, description ,publishDate, language, previewLink, infoLink, 1);
    }
}
