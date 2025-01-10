package the_t.mainproject.domain.post.presentation;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import the_t.mainproject.domain.post.application.PostService;
import the_t.mainproject.domain.post.dto.req.PostReq;
import the_t.mainproject.domain.post.dto.res.LikedCountRes;
import the_t.mainproject.domain.post.dto.res.PostDetailRes;
import the_t.mainproject.domain.post.dto.res.PostListRes;
import the_t.mainproject.global.common.Message;
import the_t.mainproject.global.common.PageResponse;
import the_t.mainproject.global.common.SuccessResponse;
import the_t.mainproject.global.security.UserDetailsImpl;


@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @Operation(summary = "게시글 등록")
    @PostMapping("")
    public ResponseEntity<SuccessResponse<Message>> createPost(@Valid @RequestPart PostReq postReq,
                                                               @RequestPart(value = "image") MultipartFile image,
                                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(postService.createPost(postReq, image, userDetails));
    }

    @Operation(summary = "게시글 수정")
    @PutMapping("/{postId}")
    public ResponseEntity<SuccessResponse<Message>> updatePost(@PathVariable Long postId,
                                                               @Valid @RequestPart PostReq postReq,
                                                               @RequestPart(value = "image") MultipartFile image,
                                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(postService.updatePost(postId, postReq, image, userDetails));
    }

    @Operation(summary = "게시글 삭제")
    @DeleteMapping("/{postId}")
    public ResponseEntity<SuccessResponse<Message>> deletePost(@PathVariable Long postId,
                                                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(postService.deletePost(postId, userDetails));
    }

    @Operation(summary = "게시글 상세 조회")
    @GetMapping("/{postId}")
    public ResponseEntity<SuccessResponse<PostDetailRes>> getPost(@PathVariable Long postId) {
        return ResponseEntity.ok(postService.getPost(postId));
    }

    @Operation(summary = "게시글 공감")
    @PostMapping("/liked/{postId}")
    public ResponseEntity<SuccessResponse<LikedCountRes>> likePost(@PathVariable Long postId,
                                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(postService.likePost(postId, userDetails));
    }

    @Operation(summary = "게시글 공감 취소")
    @DeleteMapping("/liked/{postId}")
    public ResponseEntity<SuccessResponse<LikedCountRes>> dislikePost(@PathVariable Long postId,
                                                                      @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(postService.dislikePost(postId, userDetails));
    }

    // default : DATE_DESC (날짜 내림차순, 최근 ~> 과거)
    @Operation(summary = "내가 쓴 게시글 조회 (페이지네이션)")
    @GetMapping("/my-post")
    public ResponseEntity<SuccessResponse<PageResponse<PostListRes>>> getMyPost(
            @Parameter(description = "현재 페이지의 번호 (0부터 시작)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "한 페이지의 개수") @RequestParam(defaultValue = "15") int size,
            @Parameter(description = "정렬 방법" +
                    "TITLE_ASC - 가나다 오름차순 (가~하)" +
                    "TITLE_DESC - 가나다 내림차순 (하~가)" +
                    "DATE_ASC - 작성일시 오름차순 (과거 ~> 최근)" +
                    "DATE_DESC - 작성일시 내림차순 (최근 ~> 과거), default값)")
                @RequestParam(defaultValue = "DATE_DESC") String sortBy,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(postService.getMyPost(page, size, sortBy, userDetails));
    }

    // default : TITLE_ASC (가나다 오름차순, 가~하)
    @Operation(summary = "내가 공감한 게시글 조회 (페이지네이션)")
    @GetMapping("/my-liked-post")
    public ResponseEntity<SuccessResponse<PageResponse<PostListRes>>> getMyLikedPost(
            @Parameter(description = "현재 페이지의 번호 (0부터 시작)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "한 페이지의 개수") @RequestParam(defaultValue = "15") int size,
            @Parameter(description = "정렬 방법" +
                    "TITLE_ASC - 가나다 오름차순 (가~하)" +
                    "TITLE_DESC - 가나다 내림차순 (하~가)" +
                    "DATE_ASC - 공감일시 오름차순 (과거 ~> 최근)" +
                    "DATE_DESC - 공감일시 내림차순 (최근 ~> 과거), default값)")
            @RequestParam(defaultValue = "DATE_DESC") String sortBy,
            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(postService.getMyLikedPost(page, size, sortBy, userDetails));
    }

    @Operation(summary = "단어로 게시글 검색 (페이지네이션)")
    @GetMapping("/search/word")
    public ResponseEntity<SuccessResponse<PageResponse<PostListRes>>> getWordSearchedPost(
//    public ResponseEntity<SuccessResponse<PageResponse<PostListScoreRes>>> getWordSearchedPost(
            @Parameter(description = "현재 페이지의 번호 (0부터 시작)") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "한 페이지의 개수") @RequestParam(defaultValue = "15") int size,
            @Parameter(description = "검색 키워드") String word) {
        return ResponseEntity.ok(postService.getWordSearchedPost(page, size, word));
    }
}
