package the_t.mainproject.domain.postreport.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import the_t.mainproject.domain.member.domain.Member;
import the_t.mainproject.domain.postreport.domain.PostReport;

import java.util.List;

public interface PostReportRepository extends JpaRepository<PostReport, Long> {

    void deleteAllByPostId(Long postId);

    boolean existsByMemberAndPostId(Member member, Long postId);

    List<PostReport> findAllByMember(Member member);
}
