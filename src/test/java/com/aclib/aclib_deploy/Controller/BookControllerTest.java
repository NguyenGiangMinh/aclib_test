package com.aclib.aclib_deploy.Controller;

import com.aclib.aclib_deploy.DTO.BookDTO;
import com.aclib.aclib_deploy.Service.BookService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookControllerTest {

    @Mock
    private BookService bookService;

    @InjectMocks
    private BookController bookController;

    private BookDTO testBookDTO;
    private List<BookDTO> testBookList;

    @BeforeEach
    void setUp() {
        testBookDTO = new BookDTO(
            "Test Book",
            new String[]{"Author 1", "Author 2"},
            "book123",
            "http://example.com/book123",
            "http://example.com/thumbnail.jpg",
            "Test Description",
            new String[]{"Fiction"},
            "Test Publisher",
            "2024-01-01",
            300,
            "en",
            true
        );

        testBookList = new ArrayList<>();
        testBookList.add(testBookDTO);
    }

    @Test
    void testGetBookByTitle_Success() {
        // Arrange
        String title = "Test Book";
        when(bookService.searchByTitle(title)).thenReturn(testBookList);

        // Act
        ResponseEntity<List<BookDTO>> response = bookController.getBookByTitle(title);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Test Book", response.getBody().get(0).getTitle());
        verify(bookService).searchByTitle(title);
    }

    @Test
    void testGetBookByTitle_NotFound() {
        // Arrange
        String title = "Nonexistent Book";
        when(bookService.searchByTitle(title)).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<List<BookDTO>> response = bookController.getBookByTitle(title);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(bookService).searchByTitle(title);
    }

    @Test
    void testGetBookByTitle_MultipleResults() {
        // Arrange
        String title = "Test";
        BookDTO book2 = new BookDTO(
            "Test Book 2",
            new String[]{"Author 3"},
            "book456",
            "http://example.com/book456",
            "http://example.com/thumbnail2.jpg",
            "Another Test",
            new String[]{"Science"},
            "Publisher 2",
            "2024-02-01",
            400,
            "en",
            true
        );
        testBookList.add(book2);
        when(bookService.searchByTitle(title)).thenReturn(testBookList);

        // Act
        ResponseEntity<List<BookDTO>> response = bookController.getBookByTitle(title);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetSearchedBookByTitle_Success() {
        // Arrange
        String id = "book123";
        when(bookService.searchById(id)).thenReturn(testBookDTO);

        // Act
        ResponseEntity<BookDTO> response = bookController.getSearchedBookByTitle(id);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("Test Book", response.getBody().getTitle());
        assertEquals(id, response.getBody().getId());
        verify(bookService).searchById(id);
    }

    @Test
    void testGetSearchedBookByTitle_NotFound() {
        // Arrange
        String id = "nonexistent";
        when(bookService.searchById(id)).thenReturn(null);

        // Act
        ResponseEntity<BookDTO> response = bookController.getSearchedBookByTitle(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(bookService).searchById(id);
    }

    @Test
    void testGetSearchedBookByTitle_InvalidId() {
        // Arrange
        String id = "";
        when(bookService.searchById(id)).thenReturn(null);

        // Act
        ResponseEntity<BookDTO> response = bookController.getSearchedBookByTitle(id);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetBookByCategory_Success() {
        // Arrange
        String category = "Fiction";
        when(bookService.searchByCategory(category)).thenReturn(testBookList);

        // Act
        ResponseEntity<List<BookDTO>> response = bookController.getBookByCategory(category);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals(1, response.getBody().size());
        assertEquals("Test Book", response.getBody().get(0).getTitle());
        verify(bookService).searchByCategory(category);
    }

    @Test
    void testGetBookByCategory_NotFound() {
        // Arrange
        String category = "Unknown";
        when(bookService.searchByCategory(category)).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<List<BookDTO>> response = bookController.getBookByCategory(category);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertNull(response.getBody());
        verify(bookService).searchByCategory(category);
    }

    @Test
    void testGetBookByCategory_MultipleBooks() {
        // Arrange
        String category = "Fiction";
        BookDTO book2 = new BookDTO(
            "Fiction Book 2",
            new String[]{"Author 4"},
            "book789",
            "http://example.com/book789",
            "http://example.com/thumbnail3.jpg",
            "Fiction Description",
            new String[]{"Fiction"},
            "Publisher 3",
            "2024-03-01",
            250,
            "en",
            true
        );
        testBookList.add(book2);
        when(bookService.searchByCategory(category)).thenReturn(testBookList);

        // Act
        ResponseEntity<List<BookDTO>> response = bookController.getBookByCategory(category);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(2, response.getBody().size());
    }

    @Test
    void testGetHomepageData_Success() {
        // Arrange
        when(bookService.getHomepageBooks()).thenReturn(testBookList);

        // Act
        ResponseEntity response = bookController.getHomepageData();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        verify(bookService).getHomepageBooks();
    }

    @Test
    void testGetBookByTitle_EmptyTitle() {
        // Arrange
        String title = "";
        when(bookService.searchByTitle(title)).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<List<BookDTO>> response = bookController.getBookByTitle(title);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetBookByCategory_EmptyCategory() {
        // Arrange
        String category = "";
        when(bookService.searchByCategory(category)).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<List<BookDTO>> response = bookController.getBookByCategory(category);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void testGetBookByTitle_ServiceThrowsException() {
        // Arrange
        String title = "Test Book";
        when(bookService.searchByTitle(title))
            .thenThrow(new RuntimeException("Google Books API error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
            bookController.getBookByTitle(title));
    }

    @Test
    void testGetBookByCategory_ServiceThrowsException() {
        // Arrange
        String category = "Fiction";
        when(bookService.searchByCategory(category))
            .thenThrow(new RuntimeException("Database connection error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
            bookController.getBookByCategory(category));
    }

    @Test
    void testGetSearchedBookByTitle_ServiceThrowsException() {
        // Arrange
        String id = "book123";
        when(bookService.searchById(id))
            .thenThrow(new RuntimeException("Book service error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
            bookController.getSearchedBookByTitle(id));
    }

    @Test
    void testGetBookByTitle_NullTitle() {
        // Arrange
        when(bookService.searchByTitle(null))
            .thenThrow(new IllegalArgumentException("Title cannot be null"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
            bookController.getBookByTitle(null));
    }

    @Test
    void testGetBookByCategory_NullCategory() {
        // Arrange
        when(bookService.searchByCategory(null))
            .thenThrow(new IllegalArgumentException("Category cannot be null"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
            bookController.getBookByCategory(null));
    }

    @Test
    void testGetSearchedBookByTitle_NullId() {
        // Arrange
        when(bookService.searchById(null))
            .thenThrow(new IllegalArgumentException("ID cannot be null"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () ->
            bookController.getSearchedBookByTitle(null));
    }

    @Test
    void testGetHomepageData_EmptyResult() {
        // Arrange
        when(bookService.getHomepageBooks()).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity response = bookController.getHomepageData();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        verify(bookService).getHomepageBooks();
    }

    @Test
    void testGetHomepageData_ServiceThrowsException() {
        // Arrange
        when(bookService.getHomepageBooks())
            .thenThrow(new RuntimeException("Database error"));

        // Act & Assert
        assertThrows(RuntimeException.class, () ->
            bookController.getHomepageData());
    }

    @Test
    void testGetBookByTitle_SpecialCharacters() {
        // Arrange
        String title = "Test@#$%";
        when(bookService.searchByTitle(title)).thenReturn(new ArrayList<>());

        // Act
        ResponseEntity<List<BookDTO>> response = bookController.getBookByTitle(title);

        // Assert
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }
}
