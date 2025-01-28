package the_t.mainproject.domain.commentliked.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import the_t.mainproject.domain.commentliked.domain.CommentLiked;
import the_t.mainproject.domain.member.domain.Member;

import java.util.List;

public interface CommentLikedRepository extends JpaRepository<CommentLiked, Long> {

    boolean existsByMemberIdAndCommentId(Long memberId, Long commentId);

    void deleteByMemberIdAndCommentId(Long memberId, Long commentId);

    List<CommentLiked> findAllByMember(Member member);
}
