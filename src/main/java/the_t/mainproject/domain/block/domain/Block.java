package the_t.mainproject.domain.block.domain;

import jakarta.persistence.*;
import lombok.*;
import the_t.mainproject.domain.common.BaseEntity;
import the_t.mainproject.domain.member.domain.Member;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(name = "block", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"blocker_id", "blocked_id"})
})
public class Block extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocker_id", nullable = false)
    private Member blocker;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "blocked_id", nullable = false)
    private Member blocked;

    @Builder
    public Block(Member blocker, Member blocked) {
        this.blocker = blocker;
        this.blocked = blocked;
    }
}