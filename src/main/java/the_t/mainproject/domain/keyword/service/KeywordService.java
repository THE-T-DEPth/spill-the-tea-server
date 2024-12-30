package the_t.mainproject.domain.keyword.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import the_t.mainproject.domain.keyword.domain.Keyword;
import the_t.mainproject.domain.keyword.domain.repository.KeywordRepository;

@Service
@RequiredArgsConstructor
public class KeywordService {
    private final KeywordRepository keywordRepository;

    @PostConstruct
    public void init() {
        String[] defaultKeywords = {
                "첫사랑", "우정", "가족", "이별", "고백", "서운함", "위로", "배신", "신뢰", "재회",
                "실수", "행운", "버스", "출근", "일탈", "방학", "하루", "고민", "변화", "추억",
                "영화", "게임", "여행", "음악", "독서", "요리", "드라마", "덕질", "운동", "컬렉션",
                "초능력", "타임머신", "로또", "외계인", "평행세계", "유령", "동물", "대통령", "꿈", "미래",
                "연애", "진로", "돈", "스트레스", "시험", "인간관계", "취업", "실패", "도전", "성장"
        };

        for (String keywordName : defaultKeywords) {
            // 이미 존재하지 않는 키워드만 추가
            if (!keywordRepository.existsByName(keywordName)) {
                Keyword keyword = new Keyword(keywordName);
                keywordRepository.save(keyword);
            }
        }
    }
}
