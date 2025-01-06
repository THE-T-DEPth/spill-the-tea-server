package the_t.mainproject.domain.comment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import the_t.mainproject.domain.comment.domain.Comment;
import the_t.mainproject.domain.comment.domain.repository.CommentRepository;
import the_t.mainproject.global.common.Message;
import the_t.mainproject.global.common.SuccessResponse;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {

    private final CommentRepository commentRepository;

    @Transactional
    public SuccessResponse<Message> likeComment(Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 댓글 혹은 대댓글이 없습니다."));

        comment.addLikedCount();

        Message message = Message.builder()
                .message("댓글 공감이 완료되었습니다.")
                .build();

        return SuccessResponse.of(message);
    }
}
