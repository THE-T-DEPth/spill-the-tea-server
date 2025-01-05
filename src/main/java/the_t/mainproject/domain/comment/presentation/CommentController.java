package the_t.mainproject.domain.comment.presentation;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import the_t.mainproject.domain.comment.application.CreateCommentService;
import the_t.mainproject.domain.comment.application.CreateCommentServiceFactory;
import the_t.mainproject.domain.comment.dto.request.CreateCommentReq;
import the_t.mainproject.global.common.Message;
import the_t.mainproject.global.common.SuccessResponse;
import the_t.mainproject.global.security.UserDetailsImpl;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CreateCommentServiceFactory createCommentServiceFactory;

    @Operation(summary = "새로운 댓글 작성")
    @PostMapping
    public ResponseEntity<SuccessResponse<Message>> createComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                  @Valid @RequestBody CreateCommentReq createCommentReq) {
        CreateCommentService createCommentService = createCommentServiceFactory.find(createCommentReq.getParentCommentId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createCommentService.createComment(userDetails, createCommentReq));
    }
}
