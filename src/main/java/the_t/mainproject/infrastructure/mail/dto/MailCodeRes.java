package the_t.mainproject.infrastructure.mail.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class MailCodeRes {

    private String code;
}
