package the_t.mainproject.domain.gpt.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import the_t.mainproject.domain.gpt.dto.req.GPTRequest;
import the_t.mainproject.domain.gpt.dto.req.NovelizeReq;
import the_t.mainproject.domain.gpt.dto.res.GPTResponse;
import the_t.mainproject.domain.gpt.dto.res.NovelizeRes;
import the_t.mainproject.global.common.SuccessResponse;
import the_t.mainproject.global.config.GPTConfig;
import the_t.mainproject.global.exception.BusinessException;
import the_t.mainproject.global.exception.ErrorCode;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class GPTService {
	private final GPTConfig gptConfig;
	private final RestTemplate restTemplate;

	@Transactional
	public SuccessResponse<NovelizeRes> novelize(NovelizeReq novelizeReq) {
		String prompt = "다음 글을 소설 문체로 바꿔줘.\n" + novelizeReq.getContent();
		GPTRequest gptRequest = GPTRequest.builder()
				.model(gptConfig.getModel())
				.prompt(prompt)
				.build();

		GPTResponse gptResponse = null;
		try {
			gptResponse = restTemplate.postForObject(
					gptConfig.getApiUrl(),
					gptRequest,
					GPTResponse.class
			);
		} catch (Exception e) {
			throw new BusinessException(e.getMessage(), ErrorCode.GPT_REQUEST_FAILED);
		}

		if (gptResponse == null || gptResponse.getChoices().isEmpty()) {
			throw new BusinessException("GPT API 응답이 비어 있습니다.", ErrorCode.GPT_REQUEST_FAILED);
		}

		return SuccessResponse.of(NovelizeRes.builder()
				.novel(gptResponse.getChoices().get(0).getMessage().getContent())
				.build());
	}
}