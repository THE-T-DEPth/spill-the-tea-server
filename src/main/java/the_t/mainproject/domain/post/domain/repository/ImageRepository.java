package the_t.mainproject.domain.post.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import the_t.mainproject.domain.post.domain.Image;

import java.util.List;

public interface ImageRepository extends JpaRepository<Image, Long> {
    List<Image> findAllByPostId(Long postId);
    Image findByPostIdAndThumb(Long postId, boolean thumb);
}
