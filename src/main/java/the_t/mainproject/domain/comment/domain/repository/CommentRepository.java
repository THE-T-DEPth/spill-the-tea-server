package the_t.mainproject.domain.comment.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import the_t.mainproject.domain.comment.domain.Comment;

public interface CommentRepository extends JpaRepository<Comment, Long> {

}
