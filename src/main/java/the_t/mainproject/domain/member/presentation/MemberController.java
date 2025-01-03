package the_t.mainproject.domain.member.presentation;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import the_t.mainproject.domain.member.application.MemberServiceImpl;
import the_t.mainproject.global.common.Message;
import the_t.mainproject.global.common.SuccessResponse;
import the_t.mainproject.global.security.UserDetailsImpl;

@RequiredArgsConstructor
@RestController
@RequestMapping("/members")
public class MemberController {

    private final MemberServiceImpl memberService;

    @Operation(summary = "프로필 이미지 수정")
    @PutMapping("/image")
    public ResponseEntity<SuccessResponse<Message>> modifyProfileImage(@AuthenticationPrincipal UserDetailsImpl member, String profile_image) {
        return ResponseEntity.ok(memberService.modifyProfileImage(member, profile_image));
    }

    @Operation(summary = "프로필 이미지 삭제")
    @DeleteMapping("/image")
    public ResponseEntity<SuccessResponse<Message>> deleteProfileImage(@AuthenticationPrincipal UserDetailsImpl member) {
        return ResponseEntity.ok(memberService.deleteProfileImage(member));
    }
}
