package the_t.mainproject.domain.post.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import the_t.mainproject.domain.common.BaseEntity;
import the_t.mainproject.domain.member.domain.Member;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "post")
public class Post extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Image> images = new ArrayList<>();

    @Column(name = "title")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "liked_count")
    @Min(value = 0)
    private Integer likedCount;

    @Column(name = "comment_count")
    @Min(value = 0)
    private Integer commentCount;

    @Column(name = "reported_count")
    @Min(value = 0)
    private Integer reportedCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "voice_type")
    private VoiceType voiceType;

    @Builder
    public Post(Member member, String title, String content, VoiceType voiceType) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.likedCount = 0;
        this.commentCount = 0;
        this.reportedCount = 0;
        this.voiceType = voiceType;
    }

    public void updatePost(String title, String content, VoiceType voiceType) {
        this.title = title;
        this.content = content;
        this.voiceType = voiceType;
    }

    public void addLiked() {
        this.likedCount++;
    }

    public void subtractLiked() {
        this.likedCount--;
    }

    public void addCommentCount() {
        this.commentCount++;
    }

    public void subtractCommentCount() {
        this.commentCount--;
    }

    public void addReportedCount() {
        this.reportedCount++;
    }

    // 썸네일 이미지를 가져오는 메소드
    public static String getThumbImageUrl(Post post) {
        // post에 연결된 이미지 목록에서 thumb가 true인 이미지를 찾기
        List<Image> images = post.getImages(); // Post 엔티티에서 이미지 목록 가져오기
        for (Image image : images) {
            if (image.isThumb()) { // thumb가 true인 이미지 찾기
                return image.getUrl(); // 썸네일 이미지의 URL 반환
            }
        }
        return images.get(0).getUrl(); // 썸네일 이미지가 없으면 첫번째 이미지 반환
    }
}