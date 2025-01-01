package the_t.mainproject.domain.post.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import the_t.mainproject.domain.common.BaseEntity;
import the_t.mainproject.domain.member.domain.Member;

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

    @Column(name = "title")
    private String title;

    @Column(name = "content", columnDefinition = "TEXT")
    private String content;

    @Column(name = "thumb", columnDefinition = "TEXT")
    private String thumb;   // 썸네일

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
    public Post(Member member, String title, String content, String thumb, VoiceType voiceType) {
        this.member = member;
        this.title = title;
        this.content = content;
        this.thumb = thumb;
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

    public void updateThumb(String thumb) {
        this.thumb = thumb;
    }
}