package the_t.mainproject.domain.post.application;

import org.springframework.web.multipart.MultipartFile;
import the_t.mainproject.domain.post.dto.req.PostReq;
import the_t.mainproject.domain.post.dto.res.ImageRes;
import the_t.mainproject.domain.post.dto.res.LikedCountRes;
import the_t.mainproject.domain.post.dto.res.PostDetailRes;
import the_t.mainproject.domain.post.dto.res.PostListRes;
import the_t.mainproject.global.common.Message;
import the_t.mainproject.global.common.PageResponse;
import the_t.mainproject.global.common.SuccessResponse;
import the_t.mainproject.global.security.UserDetailsImpl;

import java.util.List;

public interface PostService {
    SuccessResponse<ImageRes> uploadImage(MultipartFile image, UserDetailsImpl userDetails);
    SuccessResponse<Message> createPost(PostReq request, UserDetailsImpl userDetails);
    SuccessResponse<Message> updatePost(Long postId, PostReq postReq, UserDetailsImpl userDetails);
    SuccessResponse<Message> deletePost(Long postId, UserDetailsImpl userDetails);
    SuccessResponse<List<PostListRes>> getSortedPost(String sortBy);
    SuccessResponse<PostDetailRes> getPost(Long postId);
    SuccessResponse<LikedCountRes> likePost(Long postId, UserDetailsImpl userDetails);
    SuccessResponse<LikedCountRes> dislikePost(Long postId, UserDetailsImpl userDetails);
    SuccessResponse<PageResponse<PostListRes>> getMyPost(int page, int size, String sortBy,
                                                         UserDetailsImpl userDetails);
    SuccessResponse<PageResponse<PostListRes>> getMyLikedPost(int page, int size, String sortBy,
                                                         UserDetailsImpl userDetails);
    SuccessResponse<PageResponse<PostListRes>> getWordSearchedPost(int page, int size, String word);
    SuccessResponse<PageResponse<PostListRes>> getKeywordSearchedPost(int page, int size, List<String> keywords);
}
