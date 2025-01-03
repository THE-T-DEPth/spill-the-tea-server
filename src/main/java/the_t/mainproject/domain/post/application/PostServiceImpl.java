package the_t.mainproject.domain.post.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import the_t.mainproject.domain.keyword.domain.Keyword;
import the_t.mainproject.domain.keyword.domain.repository.KeywordRepository;
import the_t.mainproject.domain.liked.domain.Liked;
import the_t.mainproject.domain.liked.domain.repository.LikedRepository;
import the_t.mainproject.domain.member.domain.Member;
import the_t.mainproject.domain.member.domain.repository.MemberRepository;
import the_t.mainproject.domain.post.domain.Post;
import the_t.mainproject.domain.post.domain.VoiceType;
import the_t.mainproject.domain.post.domain.repository.PostRepository;
import the_t.mainproject.domain.post.dto.req.PostReq;
import the_t.mainproject.domain.post.dto.res.LikedCountRes;
import the_t.mainproject.domain.post.dto.res.PostDetailRes;
import the_t.mainproject.domain.postkeyword.PostKeyword;
import the_t.mainproject.domain.postkeyword.repository.PostKeywordRepository;
import the_t.mainproject.global.common.Message;
import the_t.mainproject.global.common.SuccessResponse;
import the_t.mainproject.global.exception.BusinessException;
import the_t.mainproject.global.exception.ErrorCode;
import the_t.mainproject.global.security.UserDetailsImpl;
import the_t.mainproject.global.service.S3Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final S3Service s3Service;
    private final MemberRepository memberRepository;
    private final KeywordRepository keywordRepository;
    private final PostKeywordRepository postKeywordRepository;
    private final LikedRepository likedRepository;

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

    @Override
    @Transactional
    public SuccessResponse<Message> updatePost(Long postId, PostReq postReq, MultipartFile image,
                                               UserDetailsImpl userDetails) {
        // post 찾기
        Post post = postRepository.findById(postId)
                .orElseThrow((() -> new BusinessException(ErrorCode.NOT_FOUND_ERROR)));
        if (!post.getMember().equals(memberRepository.findByEmail(userDetails.getUsername()).get())){
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR);
        }
        // post 제목, 내용, 음성 유형 수정
        post.updatePost(postReq.getTitle(), postReq.getContent(), VoiceType.valueOf(postReq.getVoice_type()));

        // 썸네일 수정
        if (!image.isEmpty()) {
            // 기존 썸네일 삭제
            if (post.getThumb() != null && !post.getThumb().isEmpty()) {
                s3Service.deleteImage(post.getThumb());
            }
            // 새 이미지 업로드 및 업데이트
            post.updateThumb(s3Service.uploadImage(image));
        }

        // PostKeyword 삭제
        postKeywordRepository.deleteAllByPostId(postId);

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
                .message("게시글 수정이 완료됨")
                .build());
    }

    @Override
    @Transactional
    public SuccessResponse<Message> deletePost(Long postId, UserDetailsImpl userDetails) {
        // post 찾기
        Post post = postRepository.findById(postId)
                .orElseThrow((() -> new BusinessException(ErrorCode.NOT_FOUND_ERROR)));
        if (!post.getMember().equals(memberRepository.findByEmail(userDetails.getUsername()).get())){
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR);
        }
        if (post.getThumb() != null && !post.getThumb().isEmpty()) {
            s3Service.deleteImage(post.getThumb());
        }
        postKeywordRepository.deleteAllByPostId(postId);
        postRepository.deleteById(postId);
        return SuccessResponse.of(Message.builder()
                .message("게시글 삭제가 완료됨")
                .build());
    }

    @Override
    @Transactional(readOnly = true)
    public SuccessResponse<PostDetailRes> getPost(Long postId) {
        // post 찾기
        Post post = postRepository.findById(postId)
                .orElseThrow((() -> new BusinessException(ErrorCode.NOT_FOUND_ERROR)));
        //keyword 찾기
        List<PostKeyword> postKeywordList = postKeywordRepository.findAllByPostId(postId);
        List<String> keywordList = new ArrayList<>(3);
        for (PostKeyword postKeyword : postKeywordList) {
            String keyword = postKeyword.getKeyword().getName();
            keywordList.add(keyword);
        }
        PostDetailRes postDetailRes = PostDetailRes.builder()
                .postId(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .thumb(post.getThumb())
                .likedCount(post.getLikedCount())
                .commentCount(post.getCommentCount())
                .voiceType(post.getVoiceType().toString())
                .keywordList(keywordList.toString())
                .memberId(post.getMember().getId())
                .createdDateTime(post.getCreatedDate().toString())
                .build();
        return SuccessResponse.of(postDetailRes);
    }

    @Override
    @Transactional
    public SuccessResponse<LikedCountRes> likePost(Long postId, UserDetailsImpl userDetails) {
        // post 찾기
        Post post = postRepository.findById(postId)
                .orElseThrow((() -> new BusinessException(ErrorCode.NOT_FOUND_ERROR)));
        // 이미 공감을 눌렀는지 확인
        Member member = memberRepository.findByEmail(userDetails.getUsername()).get();
        if (likedRepository.existsByPostIdAndMemberId(post.getId(),member.getId())){
            throw new BusinessException(ErrorCode.ALREADY_EXISTS);
        } else {
            Liked liked = Liked.builder()
                    .member(member)
                    .post(post)
                    .build();
            likedRepository.save(liked);
            post.addLiked();
            postRepository.save(post);
        }
        LikedCountRes likedCountRes = LikedCountRes.builder()
                .postId(postId)
                .likedCount(post.getLikedCount())
                .build();
        return SuccessResponse.of(likedCountRes);
    }

    @Override
    @Transactional
    public SuccessResponse<LikedCountRes> dislikePost(Long postId, UserDetailsImpl userDetails) {
        // post 찾기
        Post post = postRepository.findById(postId)
                .orElseThrow((() -> new BusinessException(ErrorCode.NOT_FOUND_ERROR)));
        // 공감을 누른 적이 없다면 예외 반환
        Member member = memberRepository.findByEmail(userDetails.getUsername()).get();
        if (!likedRepository.existsByPostIdAndMemberId(post.getId(), member.getId())){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR);
        } else {
            likedRepository.deleteByPostIdAndMemberId(postId, member.getId());
            post.subtractLiked();
            postRepository.save(post);
        }
        LikedCountRes likedCountRes = LikedCountRes.builder()
                .postId(postId)
                .likedCount(post.getLikedCount())
                .build();
        return SuccessResponse.of(likedCountRes);
    }
}
