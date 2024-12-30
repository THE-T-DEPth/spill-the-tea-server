package the_t.mainproject.domain.keyword.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import the_t.mainproject.domain.keyword.domain.Keyword;

import java.util.Optional;


public interface KeywordRepository extends JpaRepository<Keyword, Long> {
    boolean existsByName(String keywordName);
    Optional<Keyword> findByName(String keywordName);
}
