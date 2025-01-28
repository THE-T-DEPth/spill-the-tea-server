package the_t.mainproject.domain.commentreport.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import the_t.mainproject.domain.comment.domain.Comment;
import the_t.mainproject.domain.comment.domain.repository.CommentRepository;
import the_t.mainproject.domain.commentreport.domain.CommentReport;
import the_t.mainproject.domain.commentreport.domain.repository.CommentReportRepository;
import the_t.mainproject.domain.member.domain.Member;
import the_t.mainproject.domain.member.domain.repository.MemberRepository;
import the_t.mainproject.global.common.Message;
import the_t.mainproject.global.common.SuccessResponse;
import the_t.mainproject.global.security.UserDetailsImpl;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class CommentReportService {

    private final MemberRepository memberRepository;
    private final CommentRepository commentRepository;
    private final CommentReportRepository commentReportRepository;

    @Transactional
    public SuccessResponse<Message> reportComment(UserDetailsImpl userDetails, Long commentId) {
        Member member = memberRepository.findById(userDetails.getMember().getId())
                .orElseThrow(() -> new IllegalArgumentException("해당 회원을 찾을 수 없습니다."));
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당하는 댓글을 찾을 수 없습니다."));

        if(commentReportRepository.existsByMemberAndCommentId(userDetails.getMember(), commentId)) {
            throw new IllegalArgumentException("이미 신고한 댓글입니다.");
        }

        CommentReport commentReport = CommentReport.builder()
                .comment(comment)
                .member(member)
                .build();

        comment.addReportedCount();
        commentReportRepository.save(commentReport);

        // 일정 수 쌓였을 경우 댓글 삭제(10회 초과로 설정)
        if(comment.getReportedCount() > 10) {
            commentReportRepository.deleteAllByCommentId(commentId);
            commentRepository.deleteById(commentId);
        }

        Message message = Message.builder()
                .message("댓글 신고가 완료되었습니다.")
                .build();

        return SuccessResponse.of(message);
    }
}
