package the_t.mainproject.domain.gpt.presentation;

import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import the_t.mainproject.domain.gpt.application.GPTService;
import the_t.mainproject.domain.gpt.dto.req.NovelizeReq;
import the_t.mainproject.domain.gpt.dto.res.NovelizeRes;
import the_t.mainproject.global.common.SuccessResponse;

@RestController
@RequestMapping("/gpt")
@RequiredArgsConstructor
public class GPTController {
    private final GPTService gptService;

    @Operation(summary = "게시글 소설화")
    @PostMapping("/novelization")
    public ResponseEntity<SuccessResponse<NovelizeRes>> novelize(@Valid @RequestBody NovelizeReq novelizeReq) {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(gptService.novelize(novelizeReq));
    }
}
