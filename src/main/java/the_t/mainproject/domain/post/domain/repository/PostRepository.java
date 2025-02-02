package the_t.mainproject.domain.post.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import the_t.mainproject.domain.member.domain.Member;
import the_t.mainproject.domain.post.domain.Post;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByMemberId(Long memberId, Pageable pageRequest);

    List<Post> findByMember(Member member);

    @Query(value = """
    SELECT *
    FROM post
    WHERE MATCH(title) AGAINST(:word IN BOOLEAN MODE)
    AND member_id NOT IN (
        SELECT block.blocked_id
        FROM block
        WHERE blocker_id = :memberId
        AND blocked_id IS NOT NULL
        )
    """, nativeQuery = true)
    Page<Post> findByWord(String word, Pageable pageRequest, Long memberId);

    @Query(value = """
    SELECT p.*
    FROM post p
    JOIN post_keyword pk ON p.id = pk.post_id
    JOIN keyword k ON pk.keyword_id = k.id
    WHERE k.name IN (:keywords)
    AND p.member_id NOT IN (
        SELECT block.blocked_id
        FROM block
        WHERE blocker_id = :memberId
        AND blocked_id IS NOT NULL
        )
    GROUP BY p.id
    ORDER BY COUNT(k.id) DESC
    """, nativeQuery = true)
    Page<Post> findByKeywords(List<String> keywords, Pageable pageable, Long memberId);

    // 최신순으로 12개 조회
    @Query(value = """
    SELECT *
    FROM post
    WHERE member_id NOT IN (
        SELECT block.blocked_id
        FROM block
        WHERE blocker_id = :memberId
        AND blocked_id IS NOT NULL
        )
    ORDER BY created_date DESC
    """, nativeQuery = true)
    List<Post> findTop12ByOrderByCreatedDateDesc(Long memberId);

    // 최근 30일 안에 올라온 게시글 중 공감순으로 12개 조회
    @Query(value = """
    SELECT *
    FROM post
    WHERE created_date > :criteriaDate
    AND member_id NOT IN (
        SELECT block.blocked_id
        FROM block
        WHERE blocker_id = :memberId
        AND blocked_id IS NOT NULL
        )
    ORDER BY liked_count DESC
    """, nativeQuery = true)
    List<Post> findTop12ByCreatedDateAfterOrderByLikedCountDesc(LocalDateTime criteriaDate, Long memberId);
}
