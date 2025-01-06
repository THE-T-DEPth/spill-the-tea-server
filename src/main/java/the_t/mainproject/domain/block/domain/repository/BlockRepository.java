package the_t.mainproject.domain.block.domain.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import the_t.mainproject.domain.block.domain.Block;
import the_t.mainproject.domain.member.domain.Member;

import java.util.List;

public interface BlockRepository extends JpaRepository<Block, Long> {

    boolean existsByBlockerAndBlocked(Member blocker, Member blocked);

    List<Block> findAllByBlocker(Member blocker);

    List<Block> findAllByBlocked(Member blocked);
}
