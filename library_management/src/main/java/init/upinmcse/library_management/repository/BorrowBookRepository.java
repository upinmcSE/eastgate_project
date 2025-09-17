package init.upinmcse.library_management.repository;

import init.upinmcse.library_management.constant.BorrowBookStatus;
import init.upinmcse.library_management.model.BorrowBook;
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

    List<BorrowBook> findAllByUserIdAndStatus(Integer userId, BorrowBookStatus status);

    List<BorrowBook> findAllByBookIdAndStatus(Integer bookId, BorrowBookStatus status);

    List<BorrowBook> findByStatusAndDueDateBefore(BorrowBookStatus status, LocalDateTime now);
}
