package the_t.mainproject.domain.commentreport.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import the_t.mainproject.domain.commentreport.domain.CommentReport;

public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {

}
