package the_t.mainproject.domain.liked.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import the_t.mainproject.domain.liked.domain.Liked;
import the_t.mainproject.domain.member.domain.Member;

import java.util.List;

public interface LikedRepository extends JpaRepository<Liked, Long> {

    boolean existsByPostIdAndMemberId(Long id, Long id1);

    void deleteByPostIdAndMemberId(Long postId, Long memberId);

    Page<Liked> findByMemberId(Long id, Pageable pageRequest);

    List<Liked> findAllByMember(Member member);

    void deleteAllByPostId(Long postId);
}
