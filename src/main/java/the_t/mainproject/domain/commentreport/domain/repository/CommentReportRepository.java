package the_t.mainproject.domain.commentreport.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import the_t.mainproject.domain.commentreport.domain.CommentReport;
import the_t.mainproject.domain.member.domain.Member;

import java.util.List;

public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {

    void deleteAllByCommentId(Long commentId);

    boolean existsByMemberAndCommentId(Member member, Long commentId);

    List<CommentReport> findAllByMember(Member member);
}
