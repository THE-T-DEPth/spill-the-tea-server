package the_t.mainproject.domain.postreport.presentation;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import the_t.mainproject.domain.postreport.application.PostReportService;
import the_t.mainproject.global.common.Message;
import the_t.mainproject.global.common.SuccessResponse;
import the_t.mainproject.global.security.UserDetailsImpl;

@RequiredArgsConstructor
@RestController
@RequestMapping("/reports/post")
public class PostReportController {

    private final PostReportService postReportService;

    @Operation(summary = "게시글 신고")
    @PostMapping(value = "/{postId}")
    public ResponseEntity<SuccessResponse<Message>> reportPost(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                               @PathVariable Long postId) {
        return ResponseEntity.ok(postReportService.reportPost(userDetails, postId));
    }
}
