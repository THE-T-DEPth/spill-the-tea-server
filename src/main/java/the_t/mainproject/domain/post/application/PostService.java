package the_t.mainproject.domain.post.application;

import org.springframework.web.multipart.MultipartFile;
import the_t.mainproject.domain.post.dto.req.CreatePostReq;
import the_t.mainproject.global.security.UserDetailsImpl;

public interface PostService {
    void createPost(CreatePostReq request, MultipartFile image, UserDetailsImpl userDetails);
}
