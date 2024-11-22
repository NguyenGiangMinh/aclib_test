package com.aclib.aclib_deploy.Entity;

import jakarta.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "aclib_loans")
public class Loans {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long loansId;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "book_id", referencedColumnName = "id_selfLink", columnDefinition = "VARCHAR(255) NOT NULL")
    private Book book;

    @Column(nullable = false)
    private String idSelfLink;

    @Column(name = "Book_title", nullable = false)
    private String bookTitle;

    @Column(nullable = false)
    private LocalDate borrowDate;

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
    private LocalDate returnDate;

    @Column(nullable = false)
    private LocalDate dueDate;

    private int renewalCount = 0;

    private LocalDate notificationSentDate;

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

    public LocalDate getBorrowDate() {
        return borrowDate;
    }

    public void setBorrowDate(LocalDate borrowDate) {
        this.borrowDate = borrowDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public int getRenewalCount() {
        return renewalCount;
    }

    public void setRenewalCount(int renewalCount) {
        this.renewalCount = renewalCount;
    }

    public LocalDate getNotificationSentDate() {
        return notificationSentDate;
    }

    public void setNotificationSentDate(LocalDate notificationSentDate) {
        this.notificationSentDate = notificationSentDate;
    }

    public LoanStatus getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(LoanStatus loanStatus) {
        this.loanStatus = loanStatus;
    }
}
