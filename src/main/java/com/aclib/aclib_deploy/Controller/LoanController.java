package com.aclib.aclib_deploy.Controller;

import com.aclib.aclib_deploy.DTO.LoanDTO;
import com.aclib.aclib_deploy.Entity.Loans;
import com.aclib.aclib_deploy.Entity.User;
import com.aclib.aclib_deploy.Service.LoanService;
import com.aclib.aclib_deploy.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.List;

@RestController
@RequestMapping("/api/loan")
public class LoanController {

    @Autowired
    private LoanService loanService;

    @Autowired
    private UserService userService;

    //borrow
    //check parameters all methods
    @PostMapping("/borrowing")
    public ResponseEntity<Loans> borrowBook(@RequestBody BorrowRequest borrowRequest, HttpSession session) {
        String authUsername = (String) session.getAttribute("authUsername");
        User user1 = userService.findUser(authUsername);

        Loans loan = loanService.borrowBook(borrowRequest.bookId, user1.getId());
        if (loan == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(loan);
    }

    //return
    @PostMapping("/returning")
    public ResponseEntity<Loans> returnBook(@RequestBody ReturnRequest returnRequest, HttpSession session) {
        String authUsername = (String) session.getAttribute("authUsername");
        User user1 = userService.findUser(authUsername);

        Loans loan = loanService.returnBook(returnRequest.bookId, user1.getId());
        if (loan != null) {
            return ResponseEntity.ok(loan);
        }

        return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
    }

    // borrow again (if allowed)
    // PutMapping needed here like Update purpose?
    @PutMapping("/renewing")
    public ResponseEntity<Loans> renewLoan(@RequestBody LoanRenewalRequest renewalRequest, HttpSession session) {
        System.out.println(session.getAttribute("authUsername"));

        Loans re_loans = loanService.borrowAgain(renewalRequest.loanId());
        if (re_loans == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }

        return ResponseEntity.ok(re_loans);
    }

    // see the user's loans -> return list of loans deal
    @GetMapping("/loanDeals")
    public ResponseEntity<List<LoanDTO>> getMyLoans(HttpSession session) {
        String authUsername = (String) session.getAttribute("authUsername");
        User user1 = userService.findUser(authUsername);

        if (user1 == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
        }

        List<LoanDTO> loans = userService.getLoans(user1.getId());
        return new ResponseEntity<>(loans, HttpStatus.OK);
    }


    public record BorrowRequest(String bookId) {}

    public record ReturnRequest(String bookId) {}

    public record LoanRenewalRequest(Long loanId) {}

}