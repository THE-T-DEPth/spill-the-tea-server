package the_t.mainproject.domain.comment.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import the_t.mainproject.domain.common.BaseEntity;
import the_t.mainproject.domain.member.domain.Member;
import the_t.mainproject.domain.post.domain.Post;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "comment")
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_comment_id")
    private Comment parentComment;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(name = "liked_count")
    @Min(value = 0)
    private Integer likedCount;

    @Column(name = "reported_count")
    @Min(value = 0)
    private Integer reportedCount;

    @Builder(builderMethodName = "CommentBuilder", builderClassName = "CommentBuilder")
    public Comment(Post post, Member member, String content) {
        this.post = post;
        this.member = member;
        this.content = content;
        this.likedCount = 0;
        this.reportedCount = 0;
    }

    @Builder(builderMethodName = "ReplyBuilder", builderClassName = "ReplyBuilder")
    public Comment(Comment parentComment, Member member, String content) {
        this.parentComment = parentComment;
        this.member = member;
        this.content = content;
        this.likedCount = 0;
        this.reportedCount = 0;
    }
}