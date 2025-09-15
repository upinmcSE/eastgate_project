package init.upinmcse.library_management.repository;

import init.upinmcse.library_management.model.BorrowQueue;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowQueueRepository extends JpaRepository<BorrowQueue, Integer> {
}
