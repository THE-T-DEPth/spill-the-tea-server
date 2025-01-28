package the_t.mainproject.domain.commentliked.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import the_t.mainproject.domain.comment.domain.Comment;
import the_t.mainproject.domain.common.BaseEntity;
import the_t.mainproject.domain.member.domain.Member;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "comment_liked", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"member_id", "comment_id"})
})
public class CommentLiked extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "comment_id", nullable = false)
    private Comment comment;

    @Builder
    public CommentLiked(Member member, Comment comment) {
        this.member = member;
        this.comment = comment;
    }
}