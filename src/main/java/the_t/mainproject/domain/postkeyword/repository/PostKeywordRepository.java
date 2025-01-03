package the_t.mainproject.domain.postkeyword.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import the_t.mainproject.domain.keyword.domain.Keyword;
import the_t.mainproject.domain.postkeyword.PostKeyword;

import java.util.List;

public interface PostKeywordRepository extends JpaRepository<PostKeyword, Long> {

    List<PostKeyword> findAllByPostId(Long postId);
    void deleteAllByPostId(Long postId);
}
