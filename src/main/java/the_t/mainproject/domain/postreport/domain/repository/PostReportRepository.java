package the_t.mainproject.domain.postreport.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import the_t.mainproject.domain.postreport.domain.PostReport;

public interface PostReportRepository extends JpaRepository<PostReport, Long> {

    void deleteAllByPostId(Long postId);
}
