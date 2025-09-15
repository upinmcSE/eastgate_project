package init.upinmcse.library_management.repository;

import init.upinmcse.library_management.model.BorrowBook;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BorrowBookRepository extends JpaRepository<BorrowBook, Integer> {
}
