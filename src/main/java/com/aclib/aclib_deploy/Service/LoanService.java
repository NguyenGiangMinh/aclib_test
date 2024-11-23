package com.aclib.aclib_deploy.Service;

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

import java.time.LocalDate;
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

    public Optional<Loans> searchByLoansId(long loansId) {
        return loanRepository.findById(loansId);
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
    public Loans borrowBook(String bookId, Long userId) {
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
            loan.setBorrowDate(LocalDate.now());
            loan.setLoanStatus(Loans.LoanStatus.ACTIVE);
            loan.setDueDate(LocalDate.now().plusDays(BORROW_PERIOD_DATE));
            loanRepository.save(loan);

            emailService.makingLoanSuccessfully(optionalUser.get().getEmail(),
                    optionalUser.get().getUsername(), bookId, loan.getDueDate());

            return loan;
        } else {
            existedLoan.setBorrowDate(LocalDate.now());
            existedLoan.setLoanStatus(Loans.LoanStatus.ACTIVE);
            existedLoan.setDueDate(LocalDate.now().plusDays(BORROW_PERIOD_DATE));
            loanRepository.save(existedLoan);

            return existedLoan;
        }
    }

    //Process return a book
    public Loans returnBook (String bookId, long userId) {
        Loans loan = loanRepository.findByIdSelfLinkAndUserId(bookId, userId);

        if (loan == null) {
            throw new LoanNotFoundException("Can not found your loans");
        }

        Book book = loan.getBook();
        loan.setReturnDate(LocalDate.now());
        loan.setLoanStatus(Loans.LoanStatus.RETURNED);
        loan.setBookTitle(book.getTitle());
        loanRepository.save(loan);
        book.setCopy(book.getCopy() + 1);
        bookRepository.save(book);

        return loan;
    }

    /**
     * Method check the overdue loans if overdue happens.
     */
    public void checkOverDueDateLoans() {
        List<Loans> overDueDateLoans = loanRepository.findAllByDueDateAndReturnDateIsNull(LocalDate.now());

        for (Loans loan : overDueDateLoans) {
            try {
                if (loan.getRenewalCount() >= MAX_RENEWALS) {
                    autoReturn(loan);
                } else if (loan.getNotificationSentDate() == null) {
                    loan.setNotificationSentDate(LocalDate.now());
                    loan.setLoanStatus(Loans.LoanStatus.OVERDUE);
                    loanRepository.save(loan);
                    sendNotifications(loan.getUser(), loan);
                } else {
                    long daysOverdue = ChronoUnit.DAYS.between(loan.getNotificationSentDate(), LocalDate.now());
                    if (daysOverdue >= MAX_DATE_AFTER_NOTIFY) {
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
    public Loans borrowAgain (Long loanId) {
        Optional<Loans> optionnalLoan = loanRepository.findById(loanId);
        if (optionnalLoan.isEmpty()) {
            throw new LoanNotFoundException("Can not found your loans");
        }

        Loans loan = optionnalLoan.get();
        if (loan.getReturnDate() != null) {
            throw new IllegalStateException("Book has already been returned");
        }

        if (loan.getRenewalCount() >= MAX_RENEWALS) {
            throw new IllegalStateException("Maximum renewals reached for this loan");
        }

        loan.setDueDate(LocalDate.now().plusDays(BORROW_PERIOD_DATE_NEW));
        loan.setRenewalCount(loan.getRenewalCount() + 1);
        loanRepository.save(loan);

        return loan;
    }

    private void autoReturn(Loans loan) {
        loan.setReturnDate(LocalDate.now());
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
        loan.setReturnDate(LocalDate.now());
        loanRepository.save(loan);

        Book book = loan.getBook();
        book.setCopy(book.getCopy() - 1);
        bookRepository.save(book);

        //Send to all administrators
        List<User> adminUsers = userRepository.findAllByRole(User.UserRole.ROLE_ADMIN);
        for (User admin : adminUsers) {
            emailService.notifyAdminOfLostBook(admin.getEmail(), admin.getUsername(), loan.getBookTitle());
        }

        System.out.println("Marked loan as lost and notified admin.");
    }

}
