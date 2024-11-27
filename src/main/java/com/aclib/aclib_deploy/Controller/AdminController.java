package com.aclib.aclib_deploy.Controller;

import com.aclib.aclib_deploy.DTO.UserDTO;
import com.aclib.aclib_deploy.Service.AdminService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private AdminService adminService;

    @DeleteMapping("/book/id")
    public ResponseEntity<Void> deleteBookFromStock(@RequestParam long BookId, HttpSession session) {
        System.out.println(session.getAttribute("authUsername"));

        adminService.deleteBookInStock(BookId);
        return ResponseEntity.ok().build();
    }

    // need requestBody for new copies
    @PutMapping("/book/update")
    public ResponseEntity<String> updateBookIntoStock(@RequestBody updateBook book, HttpSession session) {
        System.out.println(session.getAttribute("authUsername"));

        adminService.updateBookCopy(book.Id_self_link, book.newCopies);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/add-copies")
    public ResponseEntity<Void> addBookCopies(@RequestParam String category) {
        adminService.addBookCopyPart2(category);
        return ResponseEntity.ok().build();
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
