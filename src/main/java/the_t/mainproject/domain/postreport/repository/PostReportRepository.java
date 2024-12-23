package the_t.mainproject.domain.postreport.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import the_t.mainproject.domain.postreport.PostReport;

public interface PostReportRepository extends JpaRepository<PostReport, Long> {

}
