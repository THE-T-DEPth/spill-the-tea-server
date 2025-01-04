package the_t.mainproject.domain.member.presentation;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import the_t.mainproject.domain.member.application.MemberServiceImpl;
import the_t.mainproject.domain.member.dto.MemberUpdateReq;
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

    @Operation(summary = "회원정보 수정 (닉네임, 비밀번호)")
    @PutMapping("/update")
    public ResponseEntity<SuccessResponse<Message>> updateNicknamePassword(@AuthenticationPrincipal UserDetailsImpl member,
                                                                           @RequestBody MemberUpdateReq memberUpdateReq) {
        return ResponseEntity.ok(memberService.updateNicknamePassword(member, memberUpdateReq));
    }
}
