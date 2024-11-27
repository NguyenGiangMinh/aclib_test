package com.aclib.aclib_deploy.Entity;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "aclib_loans")
public class Loans {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long loansId;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "book_id", referencedColumnName = "id_selfLink", columnDefinition = "VARCHAR(255) NOT NULL")
    private Book book;

    @Column(nullable = false)
    private String idSelfLink;

    @Column(name = "Book_title", nullable = false)
    private String bookTitle;

    @Column(nullable = false)
    private LocalDateTime borrowDate;

    public Loans(long l, LocalDateTime localDateTime, Object o, int i, Object o1, LoanStatus loanStatus) {}

    public Loans() {}

    public enum LoanStatus {
        ACTIVE,
        RETURNED,
        OVERDUE,
        LOST
    }

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private LoanStatus loanStatus = LoanStatus.ACTIVE;

    @Column()
    private LocalDateTime returnDate;

    @Column(nullable = false)
    private LocalDateTime dueDate;

    private int renewalCount = 0;

    private LocalDateTime notificationSentDate;

    //getter and setter
    public long getLoansId() {
        return loansId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Book getBook() {
        return book;
    }

    public void setBook(Book book) {
        this.book = book;
    }

    public String getIdSelfLink() { return idSelfLink; }

    public void setIdSelfLink(String idSelfLink) { this.idSelfLink = idSelfLink; }

    public String getBookTitle() {
        return bookTitle;
    }

    public void setBookTitle(String bookTitle) {
        this.bookTitle = bookTitle;
    }

    public LocalDateTime getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDateTime borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDateTime getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDateTime dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDateTime getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDateTime returnDate) {
        this.returnDate = returnDate;
    }

    public int getRenewalCount() {
        return renewalCount;
    }

    public void setRenewalCount(int renewalCount) {
        this.renewalCount = renewalCount;
    }

    public LocalDateTime getNotificationSentDate() {
        return notificationSentDate;
    }

    public void setNotificationSentDate(LocalDateTime notificationSentDate) {
        this.notificationSentDate = notificationSentDate;
    }

    public LoanStatus getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(LoanStatus loanStatus) {
        this.loanStatus = loanStatus;
    }
}
