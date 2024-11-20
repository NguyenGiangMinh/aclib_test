package com.aclib.aclib_deploy.Service;

import com.aclib.aclib_deploy.Entity.Book;
import com.aclib.aclib_deploy.DTO.BookDTO;
import com.aclib.aclib_deploy.Entity.User;
import com.aclib.aclib_deploy.DTO.UserDTO;
import com.aclib.aclib_deploy.Repository.BookRepository;
import com.aclib.aclib_deploy.Repository.UserRepository;
import com.aclib.aclib_deploy.ThirdPartyService.GoogleService;
import com.aclib.aclib_deploy.ThirdPartyService.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Service
public class AdminService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private GoogleService googleService;

    @Autowired
    private EmailService emailService;

    public void deleteBookInStock(String bookId) {
        bookRepository.deleteBookByIdSelfLink(bookId);
    }

    public void updateBookCopy(String bookId, int newCopy) {
        Book book = bookRepository.findByIdSelfLink(bookId);

        if (book == null) {
            book = useGoogleApiBooks(bookId, newCopy);
        }

        book.setCopy(newCopy);
        bookRepository.save(book);
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
        book.setStatus(bookDTO.isAvailableForBorrowing() ? "available" : "not available");
        book.setCopy(bookDTO.getCopy());
        return book;
    }

    public UserDTO updateAdminRole(long userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));

        user.setRole(User.UserRole.ROLE_ADMIN);
        userRepository.save(user);

        emailService.sendRoleUpdateNotifications(user.getEmail(), user.getUsername(), "Admin");

        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPhone(),
                user.getRole()
        );
    }

}
