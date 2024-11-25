package com.aclib.aclib_deploy.Service;

import com.aclib.aclib_deploy.DTO.LoanDTO;
import com.aclib.aclib_deploy.Entity.Book;
import com.aclib.aclib_deploy.Entity.Loans;
import com.aclib.aclib_deploy.Entity.User;
import com.aclib.aclib_deploy.Exception.BookNotAvailableException;
import com.aclib.aclib_deploy.Exception.UserNotFoundException;
import com.aclib.aclib_deploy.Repository.UserRepository;
import com.aclib.aclib_deploy.Repository.LoanRepository;
import com.aclib.aclib_deploy.Repository.BookRepository;
import com.aclib.aclib_deploy.ThirdPartyService.EmailService;
import com.aclib.aclib_deploy.Exception.BookNotFoundException;
import com.aclib.aclib_deploy.Exception.LoanNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class LoanService {

    @Autowired
    private LoanRepository loanRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private EmailService emailService;

    private static final int BORROW_PERIOD_DATE = 150;
    private static final int BORROW_PERIOD_DATE_NEW = 30;
    private static final int MAX_RENEWALS = 1;
    private static final int MAX_DATE_AFTER_NOTIFY = 10;
    private static final int BORROW_PERIOD_MINUTES = 3;  // For testing purposes
    private static final int MAX_MINUTES_AFTER_NOTIFY = 2;
    @Autowired
    private UserService userService;

    public LoanDTO searchByLoansId(long loansId) {
        return convertToDTO(loanRepository.findById(loansId).get());
    }

    public String getUsernameWithLoans(long loanId){
        Optional<Loans> loan = loanRepository.findById(loanId);
        if (loan.isPresent()){
            Optional<User> user = userRepository.findById(loan.get().getUser().getId());
            if (user.isPresent()){
                return user.get().getUsername();
            }
        }

        return null;
    }

    //Process borrowing a book
    public LoanDTO borrowBook(String bookId, Long userId) {
        Book book = bookRepository.findByIdSelfLink(bookId);
        if (book == null) {
            throw new BookNotFoundException("Cannot find " + bookId);
        }

        Optional<User> optionalUser = userRepository.findById(userId);
        if (!optionalUser.isPresent()) {
            throw new UserNotFoundException("User not found with id " + userId);
        }

        Loans existedLoan = loanRepository.findByIdSelfLinkAndUserId(bookId, userId);

        if (existedLoan == null) {
            if (book.getCopy() <= 0) {
                throw new BookNotAvailableException("The book " + bookId + " is not available for borrowing.");
            }

            book.setCopy(book.getCopy() - 1);
            bookRepository.save(book);

            Loans loan = new Loans();
            loan.setBook(book);
            loan.setUser(optionalUser.get());
            loan.setIdSelfLink(bookId);
            loan.setBookTitle(book.getTitle());
            loan.setBorrowDate(LocalDateTime.now());
            loan.setLoanStatus(Loans.LoanStatus.ACTIVE);
//            loan.setDueDate(LocalDateTime.now().plusDays(BORROW_PERIOD_DATE));
            loan.setDueDate(LocalDateTime.now().plusMinutes(BORROW_PERIOD_MINUTES)); //test
            loanRepository.save(loan);

            emailService.makingLoanSuccessfully(optionalUser.get().getEmail(),
                    optionalUser.get().getUsername(), book.getTitle(), loan.getDueDate());

            return convertToDTO(loan);
        } else {

            if (existedLoan.getLoanStatus() == Loans.LoanStatus.ACTIVE || existedLoan.getReturnDate() == null) {
                throw new IllegalStateException("User must return the book before borrowing it again.");
            }

            existedLoan.setBorrowDate(LocalDateTime.now());
            existedLoan.setLoanStatus(Loans.LoanStatus.ACTIVE);
//            existedLoan.setDueDate(LocalDateTime.now().plusDays(BORROW_PERIOD_DATE));
            existedLoan.setDueDate(LocalDateTime.now().plusMinutes(BORROW_PERIOD_MINUTES)); //Test
            existedLoan.setReturnDate(null);
            loanRepository.save(existedLoan);

            emailService.makingLoanSuccessfully(optionalUser.get().getEmail(),
                    optionalUser.get().getUsername(), book.getTitle(), existedLoan.getDueDate());

            return convertToDTO(existedLoan);
        }
    }

    //Process return a book
    public LoanDTO returnBook (String bookId, long userId) {
        Loans loan = loanRepository.findByIdSelfLinkAndUserId(bookId, userId);

        if (loan == null) {
            throw new LoanNotFoundException("Can not found your loans");
        }
        User user = userRepository.findById(userId).get();
        Book book = loan.getBook();
        loan.setReturnDate(LocalDateTime.now());
        loan.setLoanStatus(Loans.LoanStatus.RETURNED);
        loan.setBookTitle(book.getTitle());
        loanRepository.save(loan);
        book.setCopy(book.getCopy() + 1);
        bookRepository.save(book);
        emailService.returnSuccessfully(user.getEmail(), user.getUsername(), book.getTitle());

        return convertToDTO(loan);
    }

    /**
     * Method check the overdue loans if overdue happens.
     * I check
     */
//    public void checkOverDueDateLoans() {
//        List<Loans> overDueDateLoans = loanRepository.findAllByDueDateAndReturnDateIsNull(LocalDate.now());
//
//        if (overDueDateLoans.isEmpty()) {
//            System.out.println("No overdue loans found for processing.");
//            return;
//        }
//
//        for (Loans loan : overDueDateLoans) {
//            try {
//                if (loan.getRenewalCount() >= MAX_RENEWALS) {
//                    autoReturn(loan);
//                } else if (loan.getNotificationSentDate() == null) {
//                    loan.setNotificationSentDate(LocalDate.now());
//                    loan.setLoanStatus(Loans.LoanStatus.OVERDUE);
//                    loanRepository.save(loan);
//                    sendNotifications(loan.getUser(), loan);
//                } else {
//                    long daysOverdue = ChronoUnit.DAYS.between(loan.getNotificationSentDate(), LocalDate.now());
//                    if (daysOverdue >= MAX_DATE_AFTER_NOTIFY) {
//                        markAsLost(loan);
//                    }
//                }
//            } catch (Exception e) {
//                System.out.println("Error processing loan ID " + loan.getLoansId() + ": " + e.getMessage());
//            }
//        }
//    }

    //Test with minute
    public void checkOverDueDateLoans() {
        List<Loans> overDueDateLoans = loanRepository.findAllByDueDateAndReturnDateIsNull(LocalDateTime.now());

        for (Loans loan : overDueDateLoans) {
            try {
                if (loan.getRenewalCount() >= MAX_RENEWALS) {
                    autoReturn(loan);
                } else if (loan.getNotificationSentDate() == null) {
                    loan.setNotificationSentDate(LocalDateTime.now()); // Switch to LocalDateTime
                    loan.setLoanStatus(Loans.LoanStatus.OVERDUE);
                    loanRepository.save(loan);
                    sendNotifications(loan.getUser(), loan);
                } else {
                    // Switch to using MINUTES for testable precision
                    long minutesOverdue = ChronoUnit.MINUTES.between(loan.getNotificationSentDate(), LocalDateTime.now());
                    if (minutesOverdue >= MAX_MINUTES_AFTER_NOTIFY) { // Replace with your test constant
                        markAsLost(loan);
                    }
                }
            } catch (Exception e) {
                System.out.println("Error processing loan ID " + loan.getLoansId() + ": " + e.getMessage());
            }
        }
    }


    private void sendNotifications(User user, Loans loan) {
        emailService.sendOverdueNotification(user.getEmail(), user.getUsername(), loan.getBook().getTitle());
        System.out.println("Notification sent to: " + user.getEmail());
    }

    //re-borrow
    public LoanDTO borrowAgain (Long loanId) {
        Optional<Loans> optionalLoan = loanRepository.findById(loanId);
        if (optionalLoan.isEmpty()) {
            throw new LoanNotFoundException("Can not found your loans");
        }

        Loans loan = optionalLoan.get();
        if (loan.getReturnDate() != null) {
            throw new IllegalStateException("Book has already been returned");
        }

        if (loan.getRenewalCount() >= MAX_RENEWALS) {
            throw new IllegalStateException("Maximum renewals reached for this loan");
        }

//        loan.setDueDate(LocalDateTime.now().plusDays(BORROW_PERIOD_DATE_NEW));
        loan.setDueDate(LocalDateTime.now().plusMinutes(BORROW_PERIOD_MINUTES)); //test
        loan.setRenewalCount(loan.getRenewalCount() + 1);
        loanRepository.save(loan);

        return convertToDTO(loan);
    }

    private void autoReturn(Loans loan) {
        loan.setReturnDate(LocalDateTime.now());
        loanRepository.save(loan);

        Book book = loan.getBook();
        book.setCopy(book.getCopy() - 1); //check
        bookRepository.save(book);

        emailService.sendAutoReturn(loan.getUser().getEmail(), loan.getUser().getUsername(), loan.getBookTitle());
        System.out.println("Notification sent to " + loan.getUser().getEmail());
    }

    //so there will be place notify for user with role_admin
    //whether they should update copy in real?
    private void markAsLost(Loans loan) {
        loan.setLoanStatus(Loans.LoanStatus.LOST);
        loan.setReturnDate(LocalDateTime.now());
        loanRepository.save(loan);

        Book book = loan.getBook();
        book.setCopy(book.getCopy());
        bookRepository.save(book);

        //Send to all administrators
        List<User> adminUsers = userRepository.findAllByRole(User.UserRole.ROLE_ADMIN);
        for (User admin : adminUsers) {
            emailService.notifyAdminOfLostBook(admin.getEmail(), admin.getUsername(), loan.getBookTitle());
        }

        System.out.println("Marked loan as lost and notified admin.");
    }

    private LoanDTO convertToDTO(Loans loan) {
        return new LoanDTO(
                loan.getLoansId(),
                loan.getUser().getId(),
                loan.getBook().getId(),
                loan.getIdSelfLink(),
                loan.getBookTitle(),
                loan.getBorrowDate(),
                loan.getLoanStatus().name(),
                loan.getReturnDate(),
                loan.getDueDate(),
                loan.getRenewalCount());
    }
}
