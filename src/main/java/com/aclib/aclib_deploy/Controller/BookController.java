package com.aclib.aclib_deploy.Controller;

import com.aclib.aclib_deploy.DTO.BookDTO;
import com.aclib.aclib_deploy.Service.BookService;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/book")
public class BookController {

    @Autowired
    private BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    @GetMapping("/searching")
    public ResponseEntity<List<BookDTO>> getBookByTitle(@RequestParam String title) {

        List<BookDTO> bookDTOList = bookService.searchByTitle(title);
        if (bookDTOList.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(bookDTOList);
    }


    //homepage
    @GetMapping("/homepage")
    public ResponseEntity<Map<String, Object>> getHomepageData() {
        Map<String, Object> homepageData = new HashMap<>();
        homepageData.put("featuredBooks", bookService.getHomepageBooks());
        return ResponseEntity.ok(homepageData);
    }


    //searchById
    @GetMapping("/searchingWithId")
    public ResponseEntity<BookDTO> getSearchedBookByTitle(@RequestParam String id) {
        BookDTO bookDTOFounded = bookService.searchById(id);
        if (bookDTOFounded == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }

        return ResponseEntity.ok(bookDTOFounded);
    }
}
