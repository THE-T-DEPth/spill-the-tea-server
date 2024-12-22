package the_t.mainproject.domain.post.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum VoiceType {

    MALE("MALE", "성인 남성"),
    FEMALE("FEMALE", "성인 여성");

    String key;
    String value;
}
