package init.upinmcse.library_management.repository;

import init.upinmcse.library_management.constant.BookStatus;
import init.upinmcse.library_management.model.Book;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer>, JpaSpecificationExecutor<Book> {
    Optional<Book> findByBookCode(String bookCode);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM Book b WHERE b.id = :id AND b.status = :bookStatus")
    Optional<Book> findByBookIdWithPessimisticLock(
            @Param("id") Integer bookId,
            @Param("bookStatus") BookStatus bookStatus);

    @Query("""
            SELECT b
            FROM Book b
            JOIN b.genres g
            WHERE b.status = :status
            AND g.genreName = :name
            """)
    Page<Book> findBooksByGenre(
            @Param("status") BookStatus bookStatus,
            @Param("name") String genreName,
            Pageable pageable);

    @Query("""
            SELECT b
            FROM Book b
            JOIN b.authors a
            WHERE b.status = :status
            AND a.fullName = :name
            """)
    Page<Book> findBooksByAuthor(
            @Param("status") BookStatus bookStatus,
            @Param("name") String authorName,
            Pageable pageable);

    Page<Book> findByTitleContainingIgnoreCase(String title, Pageable pageable);
}
