package the_t.mainproject.domain.post.application;

import org.springframework.web.multipart.MultipartFile;
import the_t.mainproject.domain.post.dto.req.CreatePostReq;
import the_t.mainproject.global.common.Message;
import the_t.mainproject.global.common.SuccessResponse;
import the_t.mainproject.global.security.UserDetailsImpl;

public interface PostService {
    SuccessResponse<Message> createPost(CreatePostReq request, MultipartFile image, UserDetailsImpl userDetails);
}
