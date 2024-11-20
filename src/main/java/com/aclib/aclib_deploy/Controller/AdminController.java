package com.aclib.aclib_deploy.Controller;

import com.aclib.aclib_deploy.DTO.UserDTO;
import com.aclib.aclib_deploy.Service.AdminService;
import com.aclib.aclib_deploy.Service.LoanService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @Autowired
    private LoanService loanService;


    @DeleteMapping("/book/id")
    public ResponseEntity<Void> deleteBookFromStock(@RequestParam String BookId, HttpSession session) {
        System.out.println(session.getAttribute("authUsername"));

        adminService.deleteBookInStock(BookId);
        return ResponseEntity.ok().build();
    }

    // need requestBody for new copies
    // adding HttpSession for all
    @PutMapping("/book/update")
    public ResponseEntity<String> updateBookIntoStock(@RequestBody updateBook book, HttpSession session) {
        System.out.println(session.getAttribute("authUsername"));

        adminService.updateBookCopy(book.Id_self_link, book.newCopies);
        return ResponseEntity.ok().build();
    }

    // when user admin triggers the endpoint, the mail will be automatically sent.
    @PostMapping("/check-overdue-loans")
    public ResponseEntity<String> checkOverDueDateLoans(HttpSession session) {
        System.out.println(session.getAttribute("authUsername"));

        loanService.checkOverDueDateLoans();
        return ResponseEntity.ok("Overdue loans are checked and processed successfully.");
    }

    @PutMapping("/newAdmin")
    public ResponseEntity<UserDTO> updatingNewAdmin(@RequestBody updateNewAdmin newAdmin, HttpSession session) {
        System.out.println(session.getAttribute("authUsername"));

        UserDTO newAddedAdmin = adminService.updateAdminRole(newAdmin.userId);
        return ResponseEntity.ok(newAddedAdmin);
    }

    public record updateBook(String Id_self_link, int newCopies) {}
    public record updateNewAdmin(long userId) {}
}
