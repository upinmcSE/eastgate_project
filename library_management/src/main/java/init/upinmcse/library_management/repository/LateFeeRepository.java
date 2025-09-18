package init.upinmcse.library_management.repository;

import init.upinmcse.library_management.constant.LateFeeStatus;
import init.upinmcse.library_management.model.LateFee;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LateFeeRepository extends JpaRepository<LateFee, Integer> {

    Optional<LateFee> findLateFeeByUserIdAndBookIdAndStatus(int userId, int bookId, LateFeeStatus status);

    Page<LateFee> findLateFeeByUserId(int userId, Pageable pageable);
}
