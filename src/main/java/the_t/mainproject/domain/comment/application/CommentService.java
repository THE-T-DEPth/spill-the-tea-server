package the_t.mainproject.domain.comment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import the_t.mainproject.domain.comment.domain.Comment;
import the_t.mainproject.domain.comment.domain.repository.CommentRepository;
import the_t.mainproject.domain.comment.dto.response.CommentListRes;
import the_t.mainproject.domain.comment.dto.response.ReplyListRes;
import the_t.mainproject.domain.post.domain.Post;
import the_t.mainproject.domain.post.domain.repository.PostRepository;
import the_t.mainproject.global.common.Message;
import the_t.mainproject.global.common.SuccessResponse;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public SuccessResponse<List<CommentListRes>> getCommentList(Long postId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. " + postId));

        List<Comment> commentList = commentRepository.findByPostAndParentCommentIsNull(post);
        List<Comment> replyList = commentRepository.findByPostAndParentCommentIsNotNull(post);

        List<CommentListRes> commentDetails = new ArrayList<>();

        for(Comment comment : commentList) {
            CommentListRes commentListRes = CommentListRes.builder()
                    .commentId(comment.getId())
                    .profileImage(comment.getMember().getProfileImage())
                    .nickname(comment.getMember().getNickname())
                    .createTime(comment.getCreatedDate().toLocalTime())
                    .createDate(comment.getCreatedDate().toLocalDate())
                    .content(comment.getContent())
                    .replyList(new ArrayList<>())
                    .build();

            commentDetails.add(commentListRes);
        }

        for(Comment reply : replyList) {
            ReplyListRes replyListRes = ReplyListRes.builder()
                    .commentId(reply.getId())
                    .parentCommentId(reply.getParentComment().getId())
                    .profileImage(reply.getMember().getProfileImage())
                    .nickname(reply.getMember().getNickname())
                    .createTime(reply.getCreatedDate().toLocalTime())
                    .createDate(reply.getCreatedDate().toLocalDate())
                    .content(reply.getContent())
                    .build();

            commentDetails.stream()
                    .filter(parentComment -> parentComment.getCommentId().equals(reply.getParentComment().getId()))
                    .findFirst()
                    .ifPresent(parentComment -> parentComment.getReplyList().add(replyListRes));
        }

        return SuccessResponse.of(commentDetails);
    }

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
