package the_t.mainproject.domain.block.presentation;

import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import the_t.mainproject.domain.block.application.BlockService;
import the_t.mainproject.global.common.Message;
import the_t.mainproject.global.common.SuccessResponse;
import the_t.mainproject.global.security.UserDetailsImpl;

@RequiredArgsConstructor
@RestController
@RequestMapping("/blocks")
public class BlockController {

    private final BlockService blockService;

    @Operation(summary = "사용자 차단")
    @PostMapping("/blocked")
    public ResponseEntity<SuccessResponse<Message>> blockMember(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                                                @RequestParam(value = "email") String blockedEmail) {
        return ResponseEntity.ok(blockService.blockMember(userDetails, blockedEmail));
    }
}
