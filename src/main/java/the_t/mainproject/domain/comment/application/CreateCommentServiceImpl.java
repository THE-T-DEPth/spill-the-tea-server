package the_t.mainproject.domain.comment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import the_t.mainproject.domain.comment.domain.Comment;
import the_t.mainproject.domain.comment.domain.repository.CommentRepository;
import the_t.mainproject.domain.comment.dto.request.CreateCommentReq;
import the_t.mainproject.domain.member.domain.Member;
import the_t.mainproject.domain.member.domain.repository.MemberRepository;
import the_t.mainproject.domain.post.domain.Post;
import the_t.mainproject.domain.post.domain.repository.PostRepository;
import the_t.mainproject.global.common.Message;
import the_t.mainproject.global.common.SuccessResponse;
import the_t.mainproject.global.security.UserDetailsImpl;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CreateCommentServiceImpl implements CreateCommentService {

    private final PostRepository postRepository;
    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;

    @Override
    public boolean commentSelector(Long parentCommentId) {
        return parentCommentId == null || parentCommentId == 0;
    }

    @Override
    @Transactional
    public SuccessResponse<Message> createComment(UserDetailsImpl userDetails, CreateCommentReq createCommentReq) {
        Long postId = createCommentReq.getPostId();
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글을 찾을 수 없습니다." + postId));
        Member member = memberRepository.findById(userDetails.getMember().getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));

        Comment comment = Comment.CommentBuilder()
                .post(post)
                .member(member)
                .content(createCommentReq.getContent())
                .build();

        commentRepository.save(comment);
        post.addCommentCount();

        Message message = Message.builder()
                .message("댓글 생성이 완료되었습니다.")
                .build();

        return SuccessResponse.of(message);
    }
}
