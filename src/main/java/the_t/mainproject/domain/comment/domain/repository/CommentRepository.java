package the_t.mainproject.domain.comment.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import the_t.mainproject.domain.comment.domain.Comment;
import the_t.mainproject.domain.post.domain.Post;

import java.util.List;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    // 댓글 찾기
    List<Comment> findByPostAndParentCommentIsNull(Post post);
    // 대댓글 찾기
    List<Comment> findByPostAndParentCommentIsNotNull(Post post);
}
