package the_t.mainproject.domain.comment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import the_t.mainproject.domain.comment.domain.Comment;
import the_t.mainproject.domain.comment.domain.repository.CommentRepository;
import the_t.mainproject.domain.comment.dto.response.CommentListRes;
import the_t.mainproject.domain.comment.dto.response.ReplyListRes;
import the_t.mainproject.domain.comment.exception.UnauthorizedException;
import the_t.mainproject.domain.commentliked.domain.CommentLiked;
import the_t.mainproject.domain.commentliked.domain.repository.CommentLikedRepository;
import the_t.mainproject.domain.member.domain.Member;
import the_t.mainproject.domain.member.domain.repository.MemberRepository;
import the_t.mainproject.domain.post.domain.Post;
import the_t.mainproject.domain.post.domain.repository.PostRepository;
import the_t.mainproject.global.common.Message;
import the_t.mainproject.global.common.SuccessResponse;
import the_t.mainproject.global.exception.BusinessException;
import the_t.mainproject.global.exception.ErrorCode;
import the_t.mainproject.global.security.UserDetailsImpl;

import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentService {

    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentLikedRepository commentLikedRepository;

    public SuccessResponse<List<CommentListRes>> getCommentList(Long postId, Long memberId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다. " + postId));

        List<Comment> commentList = commentRepository.findByPostAndParentCommentIsNull(post);
        List<Comment> replyList = commentRepository.findByPostAndParentCommentIsNotNull(post);

        // 1. 공감순 상위 3개 댓글 추출
        List<Comment> topLikedCommentList = commentList.stream()
                .sorted((c1, c2) -> Integer.compare(c2.getLikedCount(), c1.getLikedCount()))
                .limit(3)
                .toList();
        // 2. 최신 생성순 댓글 정렬
        List<Comment> sortedCommentList = commentList.stream()
                .filter(comment -> !topLikedCommentList.contains(comment))
                .sorted((c1, c2) -> c2.getCreatedDate().compareTo(c1.getCreatedDate()))
                .toList();
        List<CommentListRes> commentDetails = new ArrayList<>();

        for(Comment comment : topLikedCommentList) {
            CommentListRes commentListRes = mappingCommentListRes(comment, memberId);
            commentDetails.add(commentListRes);
        }

        for(Comment comment : sortedCommentList) {
            CommentListRes commentListRes = mappingCommentListRes(comment, memberId);
            commentDetails.add(commentListRes);
        }

        for(Comment reply : replyList) {
            ReplyListRes replyListRes = ReplyListRes.builder()
                    .commentId(reply.getId())
                    .mine(memberId != null && memberId.equals(reply.getMember().getId()))
                    .liked(memberId != null && commentLikedRepository.existsByMemberIdAndCommentId(memberId, reply.getId()))
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

    private CommentListRes mappingCommentListRes(Comment comment, Long memberId) {
        return CommentListRes.builder()
                .commentId(comment.getId())
                .mine(memberId != null && memberId.equals(comment.getMember().getId()))
                .liked(memberId != null && commentLikedRepository.existsByMemberIdAndCommentId(memberId, comment.getId()))
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
    public SuccessResponse<Message> likeComment(Long memberId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 댓글 혹은 대댓글이 없습니다. " + commentId));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 멤버를 찾을 수 없습니다. " + memberId));

        if(commentLikedRepository.existsByMemberIdAndCommentId(member.getId(), comment.getId())) {
            throw new BusinessException(ErrorCode.ALREADY_EXISTS);
        }

        CommentLiked commentLiked = CommentLiked.builder()
                .member(member)
                .comment(comment)
                .build();
        commentLikedRepository.save(commentLiked);

        comment.addLikedCount();
        commentRepository.save(comment);

        Message message = Message.builder()
                .message("댓글 공감이 완료되었습니다.")
                .build();

        return SuccessResponse.of(message);
    }

    @Transactional
    public SuccessResponse<Message> dislikeComment(Long memberId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 댓글 혹은 대댓글이 없습니다. " + commentId));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 멤버를 찾을 수 없습니다. " + memberId));

        if(!commentLikedRepository.existsByMemberIdAndCommentId(memberId, commentId)) {
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        }

        commentLikedRepository.deleteByMemberIdAndCommentId(member.getId(), comment.getId());
        comment.subtractLikedCount();
        commentRepository.save(comment);

        Message message = Message.builder()
                .message("댓글 공감 취소가 완료되었습니다.")
                .build();

        return SuccessResponse.of(message);
    }

    @Transactional
    public SuccessResponse<Message> deleteComment(Long memberId, Long commentId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 댓글 혹은 대댓글이 없습니다. " + commentId));
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 멤버를 찾을 수 없습니다. " + memberId));

        if(!comment.getMember().equals(member)) {
            throw new UnauthorizedException("본인이 작성한 댓글이 아니므로 삭제할 수 없습니다.");
        }

        int deleteCount = 1;
        if(comment.getParentComment() == null) {
            List<Comment> replyList = commentRepository.findByParentComment(comment);
            deleteCount += replyList.size();
            commentRepository.deleteAll(replyList);
        }
        commentRepository.delete(comment);

        Post post = comment.getPost();
        for(int i=0; i < deleteCount; i++) {
            post.subtractCommentCount();
        }
        postRepository.save(post);

        Message message = Message.builder()
                .message("댓글 삭제가 완료되었습니다.")
                .build();

        return SuccessResponse.of(message);
    }
}
