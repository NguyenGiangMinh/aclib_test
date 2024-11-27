package com.aclib.aclib_deploy.Service;

import com.aclib.aclib_deploy.Entity.Book;
import com.aclib.aclib_deploy.DTO.BookDTO;
import com.aclib.aclib_deploy.Entity.User;
import com.aclib.aclib_deploy.DTO.UserDTO;
import com.aclib.aclib_deploy.Exception.BookNotFoundException;
import com.aclib.aclib_deploy.Repository.BookRepository;
import com.aclib.aclib_deploy.Repository.UserRepository;
import com.aclib.aclib_deploy.ThirdPartyService.EmailAsyncService;
import com.aclib.aclib_deploy.ThirdPartyService.GoogleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class AdminService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoogleService googleService;

    @Autowired
    private EmailAsyncService emailAsyncService;

    public void deleteBookInStock(long id) {
        Optional<Book> book = bookRepository.findById(id);


        if (book == null) {
            throw new BookNotFoundException(String.valueOf(id));
        }
        bookRepository.delete(book.get());
    }

    public void updateBookCopy(String bookId, int newCopy) {
        Book book = bookRepository.findByIdSelfLink(bookId);

        if (book == null) {
            book = useGoogleApiBooks(bookId, newCopy);
        }

        book.setCopy(newCopy);
        bookRepository.save(book);
    }

    public void addBookCopyPart2(String category) {
        List<BookDTO> googleBooks = googleService.searchBooksWithCategory(category);

        if (googleBooks == null || googleBooks.isEmpty()) {
            return;
        }

        for (BookDTO bookDTO : googleBooks) {
            Book bookInStock = bookRepository.findByIdSelfLink(bookDTO.getId());
            if (bookInStock == null) {
                bookInStock = mapToBook2(bookDTO, category);
            } else {
                bookInStock.setCopy(bookInStock.getCopy() + 1);
            }

            bookRepository.save(bookInStock);
        }
    }

    private Book mapToBook2(BookDTO bookDTO, String category) {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(String.join(", ", bookDTO.getAuthors()));
        book.setThumbnail(bookDTO.getThumbnail());
        book.setIdSelfLink(bookDTO.getId());
        book.setSelfLink(bookDTO.getSelfLink());
        book.setPublishDate(bookDTO.getPublishedDate());
        book.setCategory(category);
        book.setPublisher(bookDTO.getPublisher());
        book.setCopy(1); // Initial copy count
        return book;
    }


    private Book useGoogleApiBooks (String bookId, int newCopy) {
        BookDTO bookDTO = googleService.searchByIdSelfLink(bookId);
        bookDTO.setCopy(newCopy);

        return mapToBook(bookDTO);
    }

    private Book mapToBook(BookDTO bookDTO) {
        Book book = new Book();
        book.setTitle(bookDTO.getTitle());
        book.setAuthor(String.join(", ", bookDTO.getAuthors()));
        book.setThumbnail(bookDTO.getThumbnail());
        book.setIdSelfLink(bookDTO.getId());
        book.setSelfLink(bookDTO.getSelfLink());
        book.setPublishDate(bookDTO.getPublishedDate());
        book.setPublisher(bookDTO.getPublisher());
        book.setCopy(bookDTO.getCopy());
        return book;
    }

    public UserDTO updateAdminRole(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(User.UserRole.ROLE_ADMIN);
        userRepository.save(user);

        emailAsyncService.sendEmailAsyncSendRoleUpdateNotifications(user.getEmail(), user.getUsername(), "Admin");

        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getRole()
        );
    }

}
