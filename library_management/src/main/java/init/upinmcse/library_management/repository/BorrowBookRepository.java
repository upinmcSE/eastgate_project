package init.upinmcse.library_management.repository;

import init.upinmcse.library_management.constant.BorrowBookStatus;
import init.upinmcse.library_management.model.BorrowBook;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowBookRepository extends JpaRepository<BorrowBook, Integer> {

    @Query("""
            SELECT bb
            FROM BorrowBook bb
            WHERE bb.book.id = :bookId
            AND bb.user.id = :userId
            AND bb.status = :status
    """)
    Optional<BorrowBook> findBorrowedBookByBookIdAndUserId(@Param("bookId") Integer bookId,
                                                           @Param("userId") Integer userId,
                                                           @Param("status")BorrowBookStatus status);

    Page<BorrowBook> findAllByUserId(
            Integer userId,
            Pageable pageable);

    Page<BorrowBook> findAllByBookId(
            Integer bookId,
            Pageable pageable);

    Page<BorrowBook> findByStatusAndDueDateBefore(
            BorrowBookStatus status,
            LocalDateTime now,
            Pageable pageable);

    @Query("""
            SELECT bb
            FROM BorrowBook bb
            WHERE bb.dueDate < CURRENT TIMESTAMP
            AND bb.status = :status
            AND NOT EXISTS (
                      SELECT 1
                      FROM LateFee lf
                      WHERE lf.user = bb.user
                        AND lf.book = bb.book
                  )
            """)
    List<BorrowBook> findOverdueBorrowedWithoutLateFee(@Param("status") BorrowBookStatus status);

    @Query("""
        SELECT bb
        FROM BorrowBook bb
        WHERE bb.status = :status
          AND bb.dueDate BETWEEN CURRENT_TIMESTAMP AND :limitDate
        """)
    List<BorrowBook> findBorrowedBooksDueInTwoDays(
            @Param("status") BorrowBookStatus status,
            @Param("limitDate") LocalDateTime limitDate);
}
