package com.aclib.aclib_deploy.Service;

import com.aclib.aclib_deploy.Entity.Book;
import com.aclib.aclib_deploy.DTO.BookDTO;
import com.aclib.aclib_deploy.Exception.BookNotFoundException;
import com.aclib.aclib_deploy.Repository.BookRepository;
import com.aclib.aclib_deploy.ThirdPartyService.GoogleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private GoogleService googleService;

    public List<BookDTO> searchByTitle(String title) {
        List<BookDTO> googleBooks = googleService.searchBooks(title);
        List<BookDTO> result = new ArrayList<>();

        if (!googleBooks.isEmpty()) {
            for (BookDTO book : googleBooks) {
                result.add(new BookDTO(book.getTitle(),
                        book.getAuthors(),
                        book.getId(),
                        book.getSelfLink(),
                        book.getThumbnail(),
                        book.getDescription(),
                        book.getPublisher(),
                        book.getPublishedDate(),
                        book.getPageCount(),
                        book.getLanguage(),
                        book.isAvailableForBorrowing()));
            }
        } else {
            throw new BookNotFoundException("Cannot find the book with title: " + title);
        }
        System.out.println("Books found by title: " + googleBooks);
        return result;
    }

    public List<BookDTO> getHomepageBooks() {
        // Fetch books from the repository
        List<Book> books = bookRepository.findRecentlyAddedBooks();
        return books.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private BookDTO convertToDTO(Book book) {
        return new BookDTO(
                book.getTitle(),
                new String[]{book.getAuthor()},
                book.getIdSelfLink(),
                book.getSelfLink(),
                book.getThumbnail(),
                book.getPublisher(),
                book.getPublishDate(),
                book.getStatus(),
                book.getCopy()
        );
    }
}

