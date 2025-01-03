package the_t.mainproject.domain.post.application;

import org.springframework.web.multipart.MultipartFile;
import the_t.mainproject.domain.post.dto.req.PostReq;
import the_t.mainproject.domain.post.dto.res.LikedCountRes;
import the_t.mainproject.domain.post.dto.res.PostDetailRes;
import the_t.mainproject.global.common.Message;
import the_t.mainproject.global.common.SuccessResponse;
import the_t.mainproject.global.security.UserDetailsImpl;

public interface PostService {
    SuccessResponse<Message> createPost(PostReq request, MultipartFile image, UserDetailsImpl userDetails);
    SuccessResponse<Message> updatePost(Long postId, PostReq postReq, MultipartFile image, UserDetailsImpl userDetails);
    SuccessResponse<Message> deletePost(Long postId, UserDetailsImpl userDetails);
    SuccessResponse<PostDetailRes> getPost(Long postId);
    SuccessResponse<LikedCountRes> likePost(Long postId, UserDetailsImpl userDetails);
    SuccessResponse<LikedCountRes> dislikePost(Long postId, UserDetailsImpl userDetails);
}
