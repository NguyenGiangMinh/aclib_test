package com.aclib.aclib_deploy.Repository;

import com.aclib.aclib_deploy.Entity.Book;
//import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
//import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Long> {

    Optional<Book> findById(Long id);

//    @Transactional
//    @Modifying
//    @Query("DELETE FROM Book b WHERE b.idSelfLink = :bookId")
//    void deleteBookByIdSelfLink(@Param("bookId") String idSelfLink);

    Book findByIdSelfLink(String idSelfLink);

    @Query("SELECT b FROM Book b ORDER BY b.addedDate desc")
    List<Book> findRecentlyAddedBooks();

}
