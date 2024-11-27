package the_t.mainproject.domain.postkeyword;

import jakarta.persistence.*;
import lombok.*;
import the_t.mainproject.domain.common.BaseEntity;
import the_t.mainproject.domain.keyword.domain.Keyword;
import the_t.mainproject.domain.post.domain.Post;

@Entity
@Table(name = "PostKeyword")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PostKeyword extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "keword_id", nullable = false)
    private Keyword keyword;

    @Builder
    public PostKeyword(Post post, Keyword keyword) {
        this.post = post;
        this.keyword = keyword;
    }
}