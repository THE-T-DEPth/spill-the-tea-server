package the_t.mainproject.domain.commentreport.presentation;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import the_t.mainproject.domain.commentreport.application.CommentReportService;
import the_t.mainproject.global.common.Message;
import the_t.mainproject.global.common.SuccessResponse;
import the_t.mainproject.global.security.UserDetailsImpl;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reports/comment")
public class CommentReportController {

    private final CommentReportService commentReportService;

    @Operation(summary = "댓글 신고")
    @PostMapping(value = "/{commentId}")
    public ResponseEntity<SuccessResponse<Message>> reportComment(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                  @PathVariable Long commentId) {
        return ResponseEntity.ok(commentReportService.reportComment(userDetails, commentId));
    }
}
