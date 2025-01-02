package the_t.mainproject.domain.post.presentation;

import io.swagger.v3.oas.annotations.Operation;
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
import the_t.mainproject.global.common.Message;
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
    @PostMapping("/{postId}")
    public ResponseEntity<SuccessResponse<LikedCountRes>> likePost(@PathVariable Long postId,
                                                                   @AuthenticationPrincipal UserDetailsImpl userDetails) {
        return ResponseEntity.ok(postService.likePost(postId, userDetails));
    }
}
