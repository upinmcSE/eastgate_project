package init.upinmcse.library_management.repository;

import init.upinmcse.library_management.constant.BookStatus;
import init.upinmcse.library_management.model.Book;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {
    Optional<Book> findByBookCode(String bookCode);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Book b WHERE b.id = :id AND b.status = :bookStatus")
    Optional<Book> findByBookIdWithPessimisticLock(
            @Param("id") Integer bookId, @Param("bookStatus") BookStatus bookStatus);
}
