package the_t.mainproject.global.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class DateTimeFormatterUtil {

    /**
     * LocalDateTime을 주어진 형식의 날짜와 시간 문자열로 변환합니다.
     *
     * @param createdDateTime LocalDateTime 객체
     * @return 배열 [0]: "24.12.31" 형식의 날짜 문자열, [1]: "02:21" 형식의 시간 문자열
     */
    public static String[] formatDateTime(LocalDateTime createdDateTime) {
        if (createdDateTime == null) {
            throw new IllegalArgumentException("createdDateTime cannot be null");
        }

        String createdDate = createdDateTime.format(DateTimeFormatter.ofPattern("yy.MM.dd"));
        String createdTime = createdDateTime.format(DateTimeFormatter.ofPattern("HH:mm"));

        return new String[]{createdDate, createdTime};
    }
}
