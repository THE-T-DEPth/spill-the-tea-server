package the_t.mainproject.domain.post.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import the_t.mainproject.domain.post.domain.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByMemberId(Long memberId, Pageable pageRequest);
}
