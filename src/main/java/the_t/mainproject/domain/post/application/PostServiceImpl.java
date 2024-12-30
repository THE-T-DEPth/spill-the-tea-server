package the_t.mainproject.domain.post.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import the_t.mainproject.domain.member.domain.repository.MemberRepository;
import the_t.mainproject.domain.post.domain.Post;
import the_t.mainproject.domain.post.domain.VoiceType;
import the_t.mainproject.domain.post.domain.repository.PostRepository;
import the_t.mainproject.domain.post.dto.req.CreatePostReq;
import the_t.mainproject.global.security.UserDetailsImpl;
import the_t.mainproject.global.service.S3Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final S3Service s3Service;
    private final MemberRepository memberRepository;
    @Override
    public void createPost(CreatePostReq createPostReq, MultipartFile image, UserDetailsImpl userDetails) {
        Post post = Post.builder()
                        .title(createPostReq.getTitle())
                        .content(createPostReq.getContent())
                        .thumb(s3Service.uploadImage(image))
                        .voiceType(VoiceType.valueOf(createPostReq.getVoice_type()))
                        .member(memberRepository.findByEmail(userDetails.getUsername()).get())
                        .build();
        postRepository.save(post);
    }
}
