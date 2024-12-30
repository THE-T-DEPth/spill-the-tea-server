package the_t.mainproject.domain.post.presentation;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import the_t.mainproject.domain.post.application.PostService;
import the_t.mainproject.domain.post.dto.req.CreatePostReq;
import the_t.mainproject.global.security.UserDetailsImpl;


@RestController
@RequestMapping("/post")
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @Operation(summary = "게시글 등록")
    @PostMapping("")
    public ResponseEntity createPost(@RequestPart CreatePostReq createPostReq,
                                     @RequestPart MultipartFile image,
                                     @AuthenticationPrincipal UserDetailsImpl userDetails) {
        postService.createPost(createPostReq, image, userDetails);
        return ResponseEntity.ok().build();
    }
}
