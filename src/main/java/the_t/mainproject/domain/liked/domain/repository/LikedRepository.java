package the_t.mainproject.domain.liked.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import the_t.mainproject.domain.liked.domain.Liked;

public interface LikedRepository extends JpaRepository<Liked, Long> {
    boolean existsByPostIdAndMemberId(Long id, Long id1);
    void deleteByPostIdAndMemberId(Long postId, Long memberId);
}
