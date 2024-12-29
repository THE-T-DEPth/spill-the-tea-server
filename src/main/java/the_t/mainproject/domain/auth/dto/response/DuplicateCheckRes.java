package the_t.mainproject.domain.auth.dto.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DuplicateCheckRes {

    // true : 사용 가능(중복 X), false : 사용 불가(중복 O)
    private boolean availability;
}
