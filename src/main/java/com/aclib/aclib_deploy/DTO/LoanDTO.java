package com.aclib.aclib_deploy.DTO;

import java.time.LocalDate;

public class LoanDTO {
    private Long loansId;
    private Long userId; // Only the ID of the user
    private Long bookId; // Only the ID of the book
    private String idSelfLink;
    private String bookTitle;
    private LocalDate borrowDate;
    private String loanStatus;
    private LocalDate returnDate;
    private LocalDate dueDate;
    private int renewalCount;

    // Constructor
    public LoanDTO(Long loansId, Long userId, Long bookId, String idSelfLink, String bookTitle,
                   LocalDate borrowDate, String loanStatus, LocalDate returnDate,
                   LocalDate dueDate, int renewalCount) {
        this.loansId = loansId;
        this.userId = userId;
        this.bookId = bookId;
        this.idSelfLink = idSelfLink;
        this.bookTitle = bookTitle;
        this.borrowDate = borrowDate;
        this.loanStatus = loanStatus;
        this.returnDate = returnDate;
        this.dueDate = dueDate;
        this.renewalCount = renewalCount;
    }

    public Long getLoansId() {
        return loansId;
    }

    public void setLoansId(Long loansId) {
        this.loansId = loansId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Long getBookId() {
        return bookId;
    }

    public void setBookId(Long bookId) {
        this.bookId = bookId;
    }

    public String getIdSelfLink() {
        return idSelfLink;
    }

    public void setIdSelfLink(String idSelfLink) {
        this.idSelfLink = idSelfLink;
    }

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

    public String getLoanStatus() {
        return loanStatus;
    }

    public void setLoanStatus(String loanStatus) {
        this.loanStatus = loanStatus;
    }

    public LocalDate getReturnDate() {
        return returnDate;
    }

    public void setReturnDate(LocalDate returnDate) {
        this.returnDate = returnDate;
    }

    public LocalDate getDueDate() {
        return dueDate;
    }

    public void setDueDate(LocalDate dueDate) {
        this.dueDate = dueDate;
    }

    public int getRenewalCount() {
        return renewalCount;
    }

    public void setRenewalCount(int renewalCount) {
        this.renewalCount = renewalCount;
    }


}
