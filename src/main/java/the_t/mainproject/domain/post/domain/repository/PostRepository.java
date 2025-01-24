package the_t.mainproject.domain.post.domain.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import the_t.mainproject.domain.member.domain.Member;
import the_t.mainproject.domain.post.domain.Post;

import java.time.LocalDateTime;
import java.util.List;

public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByMemberId(Long memberId, Pageable pageRequest);

    List<Post> findByMember(Member member);

    @Query(value = "SELECT * FROM post WHERE MATCH(title) AGAINST(:word IN BOOLEAN MODE)", nativeQuery = true)
    Page<Post> findByWord(String word, Pageable pageRequest);

    @Query(value = """
    SELECT p.*
    FROM post p
    JOIN post_keyword pk ON p.id = pk.post_id
    JOIN keyword k ON pk.keyword_id = k.id
    WHERE k.name IN (:keywords)
    GROUP BY p.id
    ORDER BY COUNT(k.id) DESC
    """, nativeQuery = true)
    Page<Post> findByKeywords(List<String> keywords, Pageable pageable);

    List<Post> findTop12ByOrderByCreatedDateDesc(); // 최신순으로 12개 조회

    // 최근 30일 안에 올라온 게시글 중 공감순으로 12개 조회
    List<Post> findTop12ByCreatedDateAfterOrderByLikedCountDesc(LocalDateTime criteriaDate);
}
