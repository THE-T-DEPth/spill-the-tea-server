package the_t.mainproject.domain.comment.application;

import the_t.mainproject.domain.comment.dto.request.CreateCommentReq;
import the_t.mainproject.global.common.Message;
import the_t.mainproject.global.common.SuccessResponse;
import the_t.mainproject.global.security.UserDetailsImpl;

public interface CreateCommentService {

    // 댓글, 대댓글 선택 도우미
    boolean commentSelector(Long parentCommentId);

    SuccessResponse<Message> createComment(UserDetailsImpl userDetails, CreateCommentReq createCommentReq);
}
