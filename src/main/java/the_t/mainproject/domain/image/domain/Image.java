package the_t.mainproject.domain.image.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import the_t.mainproject.domain.common.BaseEntity;
import the_t.mainproject.domain.post.domain.Post;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "image")
public class Image extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = true)
    private Post post;

    @Column(name = "url", columnDefinition = "TEXT")
    private String url;   // 파일url

    @Column(name = "thumb")
    private boolean thumb;   // 썸네일 여부

    @Builder
    public Image(Post post, String url, boolean thumb) {
        this.post = post;
        this.url = url;
        this.thumb = thumb;
    }

    public void updatePost(Post post) {
        this.post = post;
    }

    public void updateThumb(boolean thumb) {
        this.thumb = thumb;
    }
}
