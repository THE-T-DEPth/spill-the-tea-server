package the_t.mainproject.domain.post.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum VoiceType {

    // 임시 지정. 기획팀과 상의 후 변경
    TYPE1("TYPE1", "첫번째 타입");

    String key;
    String value;
}
