package init.upinmcse.library_management.repository;

import init.upinmcse.library_management.constant.BorrowQueueStatus;
import init.upinmcse.library_management.model.BorrowQueue;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BorrowQueueRepository extends JpaRepository<BorrowQueue, Integer> {

    @Query("""
            SELECT bq
            FROM BorrowQueue bq
            WHERE bq.book.id = :bookId
            AND bq.user.id = :userId
            AND bq.status = :status
            """)
    Optional<BorrowQueue> findBorrowQueueByBookIdAndUserId(
                        @Param("bookId") Integer bookId,
                        @Param("userId") Integer userId,
                        @Param("status") BorrowQueueStatus status);

    Optional<BorrowQueue> findBorrowQueueByIdAndStatus(Integer borrowBookId, BorrowQueueStatus status);

    Page<BorrowQueue> findAllByUserIdAndStatus(
            Integer userId,
            BorrowQueueStatus status,
            Pageable pageable);

    Page<BorrowQueue> findAllByBookIdAndStatus(
            Integer bookId,
            BorrowQueueStatus status,
            Pageable pageable);

    Optional<BorrowQueue> findFirstByBookIdAndStatusOrderByCreatedAtAsc(Integer bookId, BorrowQueueStatus status);
}
