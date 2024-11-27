package the_t.mainproject.domain.comment.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;
import the_t.mainproject.domain.member.domain.Member;
import the_t.mainproject.domain.post.domain.Post;

import java.time.LocalDateTime;

@Entity
@Table(name = "Comment")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Comment {

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

    @Builder(builderMethodName = "memberCommentBuilder", builderClassName = "memberCommentBuilder")
    public Comment(Post post, Member member, String content, Integer likedCount, Integer reportedCount) {
        this.post = post;
        this.member = member;
        this.content = content;
        this.likedCount = likedCount;
        this.reportedCount = reportedCount;
    }

    @Builder(builderMethodName = "memberReplyBuilder", builderClassName = "memberRelpyBuilder")
    public Comment(Comment parentComment, Member member, String content, Integer likedCount, Integer reportedCount) {
        this.parentComment = parentComment;
        this.member = member;
        this.content = content;
        this.likedCount = likedCount;
        this.reportedCount = reportedCount;
    }
}