package init.upinmcse.library_management.repository;

import init.upinmcse.library_management.model.Genre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Integer>, JpaSpecificationExecutor<Genre> {
    Optional<Genre> findByGenreName(String name);
}
