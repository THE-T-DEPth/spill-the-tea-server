package the_t.mainproject.domain.liked.domain;

import jakarta.persistence.*;
import lombok.*;
import the_t.mainproject.domain.common.BaseEntity;
import the_t.mainproject.domain.member.domain.Member;
import the_t.mainproject.domain.post.domain.Post;

@Entity
@Table(name = "Liked")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Liked extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Builder
    public Liked(Member member, Post post) {
        this.member = member;
        this.post = post;
    }
}