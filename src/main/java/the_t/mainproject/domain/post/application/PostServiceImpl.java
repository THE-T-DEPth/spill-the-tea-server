package the_t.mainproject.domain.post.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import the_t.mainproject.domain.comment.domain.repository.CommentRepository;
import the_t.mainproject.domain.keyword.domain.Keyword;
import the_t.mainproject.domain.keyword.domain.repository.KeywordRepository;
import the_t.mainproject.domain.liked.domain.Liked;
import the_t.mainproject.domain.liked.domain.repository.LikedRepository;
import the_t.mainproject.domain.member.domain.Member;
import the_t.mainproject.domain.member.domain.repository.MemberRepository;
import the_t.mainproject.domain.image.domain.Image;
import the_t.mainproject.domain.post.domain.Post;
import the_t.mainproject.domain.post.domain.VoiceType;
import the_t.mainproject.domain.image.domain.repository.ImageRepository;
import the_t.mainproject.domain.post.domain.repository.PostRepository;
import the_t.mainproject.domain.post.dto.req.PostReq;
import the_t.mainproject.domain.image.dto.ImageRes;
import the_t.mainproject.domain.post.dto.res.LikedCountRes;
import the_t.mainproject.domain.post.dto.res.PostDetailRes;
import the_t.mainproject.domain.post.dto.res.PostListRes;
import the_t.mainproject.domain.postkeyword.PostKeyword;
import the_t.mainproject.domain.postkeyword.repository.PostKeywordRepository;
import the_t.mainproject.global.common.Message;
import the_t.mainproject.global.common.PageResponse;
import the_t.mainproject.global.common.SuccessResponse;
import the_t.mainproject.global.exception.BusinessException;
import the_t.mainproject.global.exception.ErrorCode;
import the_t.mainproject.global.security.UserDetailsImpl;
import the_t.mainproject.global.service.S3Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static the_t.mainproject.domain.post.domain.Post.getThumbImageUrl;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    private final PostRepository postRepository;
    private final S3Service s3Service;
    private final MemberRepository memberRepository;
    private final KeywordRepository keywordRepository;
    private final PostKeywordRepository postKeywordRepository;
    private final LikedRepository likedRepository;
    private final CommentRepository commentRepository;
    private final ImageRepository imageRepository;

    @Override
    @Transactional
    public SuccessResponse<ImageRes> uploadImage(MultipartFile image, UserDetailsImpl userDetails) {
        if (!memberRepository.existsById(userDetails.getMember().getId())){
            throw new BusinessException(ErrorCode.UNAUTHORIZED_ERROR);
        }

        String imgUrl = s3Service.uploadImage(image); // 파일 업로드 후 url 반환
        Image savedImage = imageRepository.save(Image.builder()
                .post(null)
                .url(imgUrl)
                .thumb(false)
                .build());
        return SuccessResponse.of(ImageRes.builder()
                        .id(savedImage.getId())
                        .url(imgUrl)
                        .build());
    }

    @Override
    @Transactional
    public SuccessResponse<Message> createPost(PostReq postReq,
                                               UserDetailsImpl userDetails) {
        // post 생성 및 저장
        Post post = Post.builder()
                        .title(postReq.getTitle())
                        .content(postReq.getContent())
                        .voiceType(VoiceType.valueOf(postReq.getVoice_type()))
                        .member(memberRepository.findByEmail(userDetails.getUsername())
                                .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ERROR)))
                        .build();
        postRepository.save(post);

        // image - post 연결
        List<Image> imageList = new ArrayList<>();
        for(Long imageId : postReq.getImageIdList()){
            Image image = imageRepository.findById(imageId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ERROR));
            // image가 이미 다른 post에 등록된 경우 예외 반환
            if (image.getPost() != null){
                throw new BusinessException("이미 다른 게시글에 포함된 이미지입니다. ImageId = " + imageId, ErrorCode.ALREADY_EXISTS);
            }
            image.updatePost(post);
            // 썸네일 설정
            if (image.getId().equals(postReq.getThumbImageId())){
                image.updateThumb(true);
            }
            imageList.add(image);
        }
        imageRepository.saveAll(imageList);

        // 키워드 저장
        postReq.getKeyword().forEach(keywordName -> {
            // 기존 키워드 가져오기
            Keyword keyword = keywordRepository.findByName(keywordName)
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ERROR));

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
    public SuccessResponse<Message> updatePost(Long postId, PostReq postReq,
                                               UserDetailsImpl userDetails) {
        // post 찾기
        Post post = postRepository.findById(postId)
                .orElseThrow((() -> new BusinessException(ErrorCode.NOT_FOUND_ERROR)));
        if (!post.getMember().equals(memberRepository.findByEmail(userDetails.getUsername()).get())){
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR);
        }
        //** post 제목, 내용, 음성 유형 수정 **//
        post.updatePost(postReq.getTitle(), postReq.getContent(), VoiceType.valueOf(postReq.getVoice_type()));

        //** 이미지 수정 **//
        // 기존 이미지 목록 가져오기
        List<Image> existingImages = imageRepository.findAllByPostId(postId);

        // 요청된 이미지 ID 목록과 기존 이미지 목록 비교하여 더 이상 쓰이지 않는 이미지 삭제
        List<Long> newImageIdList = postReq.getImageIdList();
        List<Image> imagesToDelete = new ArrayList<>();
        for (Image existingImage : existingImages) {
            if (!newImageIdList.contains(existingImage.getId())) {
                imagesToDelete.add(existingImage); // 삭제할 이미지 추가
            }
        }
        // 이미지 삭제
        for (Image imageToDelete : imagesToDelete) {
            s3Service.deleteImage(imageToDelete.getUrl());
        }
        imageRepository.deleteAll(imagesToDelete);
        imageRepository.flush();
        // 삭제 이후 남은 이미지 중에서 썸네일 이미지가 있다면 `thumb=false`로 설정
        Image existingThumb = imageRepository.findByPostIdAndThumb(postId, true);
        if (existingThumb != null) {
            existingThumb.updateThumb(false);
        }
        // 새롭게 추가된 이미지 처리
        List<Image> imagesToUpdate = new ArrayList<>();
        for (Long imageId : newImageIdList) {
            Image image = imageRepository.findById(imageId)
                    .orElseThrow(() -> new BusinessException(ErrorCode.NOT_FOUND_ERROR));
            // image가 이미 다른 post에 등록된 경우 예외 반환
            if (image.getPost() != null){
                throw new BusinessException("이미 다른 게시글에 포함된 이미지입니다. 이미지id = " + imageId, ErrorCode.ALREADY_EXISTS);
            }
            // image - post 연결
            image.updatePost(post);
            // 썸네일 이미지 설정
            if (image.getId().equals(postReq.getThumbImageId())) {
                image.updateThumb(true);
            }
            imagesToUpdate.add(image); // 업데이트할 이미지 목록에 추가
        }
        // 이미지를 저장
        imageRepository.saveAll(imagesToUpdate);

        //** 키워드 수정 **//
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
        // 본인이 쓴 글이 아닐 경우 예외 반환
        if (!post.getMember().equals(memberRepository.findByEmail(userDetails.getUsername()).get())){
            throw new BusinessException(ErrorCode.FORBIDDEN_ERROR);
        }
        // 이미지 삭제
        List<Image> imagesToDelete = imageRepository.findAllByPostId(postId);
        if (!imagesToDelete.isEmpty()) {
            for (Image imageToDelete : imagesToDelete){
                s3Service.deleteImage(imageToDelete.getUrl());
            }
            imageRepository.deleteAll(imagesToDelete);
        }
        // liked 삭제
        likedRepository.deleteAllByPostId(postId);
        // PostKeyword 삭제
        postKeywordRepository.deleteAllByPostId(postId);
        // 댓글, 대댓글 삭제
        commentRepository.deleteAllByPostId(postId);
        // post 삭제
        postRepository.deleteById(postId);

        return SuccessResponse.of(Message.builder()
                .message("게시글 삭제가 완료됨")
                .build());
    }

    @Override
    public SuccessResponse<List<PostListRes>> getSortedPost(String sortBy) {
        List<Post> postList;
        if (sortBy.equals("latest")) {
            postList = postRepository.findTop12ByOrderByCreatedDateDesc();
        } else if (sortBy.equals("liked")) {
            LocalDateTime criteriaDate = LocalDateTime.now().minusDays(30);
            postList = postRepository.findTop12ByCreatedDateAfterOrderByLikedCountDesc(criteriaDate);
        } else {
            throw new BusinessException("유효하지 않은 정렬 방식입니다. sortBy 값은 latest 또는 liked만 가능합니다.",
                    ErrorCode.ILLEGAL_ARGUMENT_EXCEPTION_ERROR);
        }
        // DTO로 변환
        List<PostListRes> postListRes = postList.stream()
                .map(post -> {
                    // 키워드 리스트 생성
                    List<PostKeyword> postKeywordList = postKeywordRepository.findAllByPostId(post.getId());
                    List<String> keywordList = postKeywordList.stream()
                            .map(postKeyword -> postKeyword.getKeyword().getName())
                            .toList();

                    return PostListRes.builder()
                            .postId(post.getId())
                            .title(post.getTitle())
                            .thumbUrl(getThumbImageUrl(post))
                            .likedCount(post.getLikedCount())
                            .commentCount(post.getCommentCount())
                            .keywordList(keywordList.toString())
                            .createDate(post.getCreatedDate().toLocalDate())
                            .createTime(post.getCreatedDate().toLocalTime())
                            .build();
                })
                .toList();

        return SuccessResponse.of(postListRes);
    }

    @Override
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
                .thumbUrl(getThumbImageUrl(post))
                .likedCount(post.getLikedCount())
                .commentCount(post.getCommentCount())
                .voiceType(post.getVoiceType().toString())
                .keywordList(keywordList.toString())
                .memberId(post.getMember().getId())
                .nickname(post.getMember().getNickname())
                .profileImage(post.getMember().getProfileImage())
                .createDate(post.getCreatedDate().toLocalDate())
                .createTime(post.getCreatedDate().toLocalTime())
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

    @Override
    public SuccessResponse<PageResponse<PostListRes>> getMyPost(int page, int size, String sortBy,
                                                                UserDetailsImpl userDetails) {
        //dafault 설정(DATE_DESC)으로 초기화
        Pageable pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdDate")));
        switch (sortBy) {
            // default : DATE_DESC 최근 ~> 과거순
            case "DATE_DESC" -> pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdDate")));
            case "DATE_ASC" -> pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.asc("createdDate")));
            case "TITLE_DESC" -> pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.desc("title")));
            case "TITLE_ASC" -> pageRequest = PageRequest.of (page, size, Sort.by(Sort.Order.asc("title")));
            default -> {}
        }
        Member member = memberRepository.findByEmail(userDetails.getUsername()).get();
        Page<Post> postPage = postRepository.findByMemberId(member.getId(), pageRequest);

        // DTO로 변환
        List<PostListRes> postListRes = postPage.stream()
                .map(post -> {
                    // 키워드 리스트 생성
                    List<PostKeyword> postKeywordList = postKeywordRepository.findAllByPostId(post.getId());
                    List<String> keywordList = postKeywordList.stream()
                            .map(postKeyword -> postKeyword.getKeyword().getName())
                            .toList();

                    return PostListRes.builder()
                            .postId(post.getId())
                            .title(post.getTitle())
                            .thumbUrl(getThumbImageUrl(post))
                            .likedCount(post.getLikedCount())
                            .commentCount(post.getCommentCount())
                            .keywordList(keywordList.toString())
                            .createDate(post.getCreatedDate().toLocalDate())
                            .createTime(post.getCreatedDate().toLocalTime())
                            .build();
                })
                .toList();

        // PageResponse 생성
        PageResponse<PostListRes> pageResponse = PageResponse.<PostListRes>builder()
                .totalPage(postPage.getTotalPages())
                .pageSize(postPage.getSize())
                .totalElements(postPage.getTotalElements())
                .contents(postListRes)
                .build();

        return SuccessResponse.of(pageResponse);
    }

    @Override
    public SuccessResponse<PageResponse<PostListRes>> getMyLikedPost(int page, int size, String sortBy, UserDetailsImpl userDetails) {
        // 기본 정렬 설정 (DATE_DESC)
        Pageable pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdDate")));
        switch (sortBy) {
            case "DATE_DESC" -> pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.desc("createdDate"))); // 공감일시 내림차순
            case "DATE_ASC" -> pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.asc("createdDate")));   // 공감일시 오름차순
            case "TITLE_DESC" -> pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.desc("post.title"))); // 게시글 제목 내림차순
            case "TITLE_ASC" -> pageRequest = PageRequest.of(page, size, Sort.by(Sort.Order.asc("post.title")));   // 게시글 제목 오름차순
            default -> {}
        }

        // 현재 사용자 조회
        Member member = memberRepository.findByEmail(userDetails.getUsername()).get();

        // 사용자가 공감한 게시글을 페이징 처리하여 조회
        Page<Liked> likedPage = likedRepository.findByMemberId(member.getId(), pageRequest);

        // DTO로 변환
        List<PostListRes> postListRes = likedPage.stream()
                .map(liked -> {
                    // 키워드 리스트 생성
                    List<PostKeyword> postKeywordList = postKeywordRepository.findAllByPostId(liked.getPost().getId());
                    List<String> keywordList = postKeywordList.stream()
                            .map(postKeyword -> postKeyword.getKeyword().getName())
                            .toList();

                    return PostListRes.builder()
                            .postId(liked.getPost().getId())
                            .title(liked.getPost().getTitle())
                            .thumbUrl(getThumbImageUrl(liked.getPost()))
                            .likedCount(liked.getPost().getLikedCount())
                            .commentCount(liked.getPost().getCommentCount())
                            .keywordList(keywordList.toString())
                            .createDate(liked.getPost().getCreatedDate().toLocalDate())
                            .createTime(liked.getPost().getCreatedDate().toLocalTime())
                            .build();
                })
                .toList();


        // PageResponse 생성
        PageResponse<PostListRes> pageResponse = PageResponse.<PostListRes>builder()
                .totalPage(likedPage.getTotalPages())
                .pageSize(likedPage.getSize())
                .totalElements(likedPage.getTotalElements())
                .contents(postListRes)
                .build();

        return SuccessResponse.of(pageResponse);
    }

    @Override
    public SuccessResponse<PageResponse<PostListRes>> getWordSearchedPost(int page, int size, String word) {
        Pageable pageRequest = PageRequest.of(page, size);
        Page<Post> postPage = postRepository.findByWord(word, pageRequest);

        // DTO로 변환
        List<PostListRes> postListRes = postPage.stream()
                .map(post -> {
                    // 키워드 리스트 생성
                    List<PostKeyword> postKeywordList = postKeywordRepository.findAllByPostId(post.getId());
                    List<String> keywordList = postKeywordList.stream()
                            .map(postKeyword -> postKeyword.getKeyword().getName())
                            .toList();

                    return PostListRes.builder()
                            .postId(post.getId())
                            .title(post.getTitle())
                            .thumbUrl(getThumbImageUrl(post))
                            .likedCount(post.getLikedCount())
                            .commentCount(post.getCommentCount())
                            .keywordList(keywordList.toString())
                            .createDate(post.getCreatedDate().toLocalDate())
                            .createTime(post.getCreatedDate().toLocalTime())
                            .build();
                })
                .toList();

        // PageResponse 생성
        PageResponse<PostListRes> pageResponse = PageResponse.<PostListRes>builder()
                .totalPage(postPage.getTotalPages())
                .pageSize(postPage.getSize())
                .totalElements(postPage.getTotalElements())
                .contents(postListRes)
                .build();

        return SuccessResponse.of(pageResponse);
    }

    @Override
    public SuccessResponse<PageResponse<PostListRes>> getKeywordSearchedPost(int page, int size, List<String> keywords) {
        Pageable pageRequest = PageRequest.of(page, size);

        // 검색 쿼리 실행
        Page<Post> postPage = postRepository.findByKeywords(keywords, pageRequest);

        // DTO로 변환
        List<PostListRes> postListRes = postPage.stream()
                .map(post -> {
                    // 키워드 리스트 생성
                    List<PostKeyword> postKeywordList = postKeywordRepository.findAllByPostId(post.getId());
                    List<String> keywordList = postKeywordList.stream()
                            .map(postKeyword -> postKeyword.getKeyword().getName())
                            .toList();

                    return PostListRes.builder()
                            .postId(post.getId())
                            .title(post.getTitle())
                            .thumbUrl(getThumbImageUrl(post))
                            .likedCount(post.getLikedCount())
                            .commentCount(post.getCommentCount())
                            .keywordList(keywordList.toString())
                            .createDate(post.getCreatedDate().toLocalDate())
                            .createTime(post.getCreatedDate().toLocalTime())
                            .build();
                })
                .toList();

        // PageResponse 생성
        PageResponse<PostListRes> pageResponse = PageResponse.<PostListRes>builder()
                .totalPage(postPage.getTotalPages())
                .pageSize(postPage.getSize())
                .totalElements(postPage.getTotalElements())
                .contents(postListRes)
                .build();

        return SuccessResponse.of(pageResponse);
    }


}
