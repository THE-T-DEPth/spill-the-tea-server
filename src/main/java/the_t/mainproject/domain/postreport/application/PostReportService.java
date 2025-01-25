package the_t.mainproject.domain.postreport.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import the_t.mainproject.domain.member.domain.Member;
import the_t.mainproject.domain.member.domain.repository.MemberRepository;
import the_t.mainproject.domain.post.domain.Post;
import the_t.mainproject.domain.post.domain.repository.PostRepository;
import the_t.mainproject.domain.postreport.domain.PostReport;
import the_t.mainproject.domain.postreport.domain.repository.PostReportRepository;
import the_t.mainproject.global.common.Message;
import the_t.mainproject.global.common.SuccessResponse;
import the_t.mainproject.global.security.UserDetailsImpl;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class PostReportService {

    private final MemberRepository memberRepository;
    private final PostRepository postRepository;
    private final PostReportRepository postReportRepository;

    @Transactional
    public SuccessResponse<Message> reportPost(UserDetailsImpl userDetails, Long postId) {
        Member member = memberRepository.findById(userDetails.getMember().getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 게시글을 찾을 수 없습니다."));

        PostReport postReport = PostReport.builder()
                .post(post)
                .member(member)
                .build();

        post.addReportedCount();
        postReportRepository.save(postReport);

        // 일정 수 쌓였을 경우 댓글 삭제(10회 초과로 설정)
        if(post.getReportedCount() > 10) {
            postReportRepository.deleteAllByPostId(postId);
            postRepository.deleteById(postId);
        }

        Message message = Message.builder()
                .message("게시글 신고가 완료되었습니다.")
                .build();

        return SuccessResponse.of(message);
    }
}
