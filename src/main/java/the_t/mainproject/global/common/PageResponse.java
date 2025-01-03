package the_t.mainproject.global.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class PageResponse<T> {

    @Schema(type = "Integer", example = "5", description = "전체 페이지 개수")
    private Integer totalPage;

    @Schema(type = "Integer", example = "13", description = "한 페이지의 사이즈")
    private Integer pageSize;

    @Schema(type = "Long", example = "33", description = "전체 요소 개수")
    private Long totalElements;

    @Schema(type = "List<T>", description = "페이징 처리된 데이터의 리스트")
    private List<T> contents = new ArrayList<>();

}
