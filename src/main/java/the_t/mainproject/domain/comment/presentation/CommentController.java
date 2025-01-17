package the_t.mainproject.domain.comment.presentation;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import the_t.mainproject.domain.comment.application.CommentService;
import the_t.mainproject.domain.comment.application.CreateCommentService;
import the_t.mainproject.domain.comment.application.CreateCommentServiceFactory;
import the_t.mainproject.domain.comment.dto.request.CreateCommentReq;
import the_t.mainproject.domain.comment.dto.response.CommentListRes;
import the_t.mainproject.domain.member.domain.Member;
import the_t.mainproject.global.common.Message;
import the_t.mainproject.global.common.SuccessResponse;
import the_t.mainproject.global.security.UserDetailsImpl;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/comments")
public class CommentController {

    private final CreateCommentServiceFactory createCommentServiceFactory;
    private final CommentService commentService;

    @Operation(summary = "새로운 댓글 작성")
    @PostMapping
    public ResponseEntity<SuccessResponse<Message>> createComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                  @Valid @RequestBody CreateCommentReq createCommentReq) {
        CreateCommentService createCommentService = createCommentServiceFactory.find(createCommentReq.getParentCommentId());
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(createCommentService.createComment(userDetails, createCommentReq));
    }

    @Operation(summary = "댓글 목록")
    @GetMapping("/posts/{postId}")
    public ResponseEntity<SuccessResponse<List<CommentListRes>>> getComments(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                             @PathVariable(value = "postId") Long postId) {
        Long memberId = (userDetails != null) ? userDetails.getMember().getId() : null;
        return ResponseEntity.ok(commentService.getCommentList(postId, memberId));
    }

    @Operation(summary = "댓글 공감")
    @PostMapping("/liked/{commentId}")
    public ResponseEntity<SuccessResponse<Message>> likedComment(@PathVariable(value = "commentId") Long commentId) {
        return ResponseEntity.ok(commentService.likeComment(commentId));
    }
}
