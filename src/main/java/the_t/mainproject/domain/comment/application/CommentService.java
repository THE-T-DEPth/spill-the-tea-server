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

        // 1. 공감순 상위 3개 댓글 추출
        List<Comment> allComments = new ArrayList<>();
        allComments.addAll(commentList);
        allComments.addAll(replyList);

        List<Comment> topLikedCommentList = allComments.stream()
                .sorted((c1, c2) -> Integer.compare(c2.getLikedCount(), c1.getLikedCount()))
                .limit(3)
                .toList();
        // 2. 최신 생성순 댓글 정렬
        List<Comment> sortedCommentList = commentList.stream()
                .sorted((c1, c2) -> c2.getCreatedDate().compareTo(c1.getCreatedDate()))
                .toList();
        List<CommentListRes> commentDetails = new ArrayList<>();

        for(Comment comment : topLikedCommentList) {
            CommentListRes commentListRes = mappingCommentListRes(comment);
            commentDetails.add(commentListRes);
        }

        for(Comment comment : sortedCommentList) {
            CommentListRes commentListRes = mappingCommentListRes(comment);
            commentDetails.add(commentListRes);
        }

        for(Comment reply : replyList) {
            ReplyListRes replyListRes = ReplyListRes.builder()
                    .commentId(reply.getId())
                    .parentCommentId(reply.getParentComment().getId())
                    .profileImage(reply.getMember().getProfileImage())
                    .nickname(reply.getMember().getNickname())
                    .content(reply.getContent())
                    .createTime(reply.getCreatedDate().toLocalTime())
                    .createDate(reply.getCreatedDate().toLocalDate())
                    .likedCount(reply.getLikedCount())
                    .build();

            commentDetails.stream()
                    .filter(parentComment -> parentComment.getCommentId().equals(reply.getParentComment().getId()))
                    .forEach(parentComment -> parentComment.getReplyList().add(replyListRes));
        }

        return SuccessResponse.of(commentDetails);
    }

    private CommentListRes mappingCommentListRes(Comment comment) {
        return CommentListRes.builder()
                .commentId(comment.getId())
                .profileImage(comment.getMember().getProfileImage())
                .nickname(comment.getMember().getNickname())
                .content(comment.getContent())
                .createTime(comment.getCreatedDate().toLocalTime())
                .createDate(comment.getCreatedDate().toLocalDate())
                .likedCount(comment.getLikedCount())
                .replyList(new ArrayList<>())
                .build();
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
