package com.aclib.aclib_deploy.Repository;

import com.aclib.aclib_deploy.Entity.Loans;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LoanRepository extends JpaRepository<Loans, Long> {
    Loans findByBookId(Long bookId);

    @Query("SELECT l FROM Loans l WHERE l.book.idSelfLink = :bookId AND l.user.id = :userId")
    Loans findByIdSelfLinkAndUserId(@Param("bookId") String bookId, @Param("userId") Long userId);

    @Query("select l from Loans l where l.user.id = :userId")
    List<Loans> findByUserId(@Param("userId") Long userId);

    Loans save(Loans loans);

    @Query("SELECT l FROM Loans l WHERE l.dueDate < :curDate AND l.returnDate IS NULL")
    List<Loans> findAllByDueDateAndReturnDateIsNull(LocalDateTime curDate);
}
