package the_t.mainproject.domain.commentreport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import the_t.mainproject.domain.commentreport.CommentReport;

public interface CommentReportRepository extends JpaRepository<CommentReport, Long> {

}
