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
}