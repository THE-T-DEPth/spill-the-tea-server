package the_t.mainproject.domain.post.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import the_t.mainproject.domain.keyword.domain.Keyword;
import the_t.mainproject.domain.keyword.domain.repository.KeywordRepository;
import the_t.mainproject.domain.member.domain.repository.MemberRepository;
import the_t.mainproject.domain.post.domain.Post;
import the_t.mainproject.domain.post.domain.VoiceType;
import the_t.mainproject.domain.post.domain.repository.PostRepository;
import the_t.mainproject.domain.post.dto.req.PostReq;
import the_t.mainproject.domain.postkeyword.PostKeyword;
import the_t.mainproject.domain.postkeyword.repository.PostKeywordRepository;
import the_t.mainproject.global.common.Message;
import the_t.mainproject.global.common.SuccessResponse;
import the_t.mainproject.global.security.UserDetailsImpl;
import the_t.mainproject.global.service.S3Service;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final S3Service s3Service;
    private final MemberRepository memberRepository;
    private final KeywordRepository keywordRepository;
    private final PostKeywordRepository postKeywordRepository;

    @Override
    @Transactional
    public SuccessResponse<Message> createPost(PostReq postReq, MultipartFile image,
                                               UserDetailsImpl userDetails) {
        // post 생성 및 저장
        Post post = Post.builder()
                        .title(postReq.getTitle())
                        .content(postReq.getContent())
                        .thumb(s3Service.uploadImage(image))
                        .voiceType(VoiceType.valueOf(postReq.getVoice_type()))
                        .member(memberRepository.findByEmail(userDetails.getUsername()).get())
                        .build();
        postRepository.save(post);

        // 키워드 저장
        postReq.getKeyword().forEach(keywordName -> {
            // 기존 키워드 가져오기 또는 생성
            Keyword keyword = keywordRepository.findByName(keywordName)
                    .orElseGet(() -> keywordRepository.save(new Keyword(keywordName)));

            // PostKeyword 엔티티 생성 및 저장
            PostKeyword postKeyword = PostKeyword.builder()
                    .post(post)
                    .keyword(keyword)
                    .build();

            postKeywordRepository.save(postKeyword);
        });

        return SuccessResponse.of(Message.builder()
                .message("게시글 등록이 완료됨")
                .build());
    }
}
