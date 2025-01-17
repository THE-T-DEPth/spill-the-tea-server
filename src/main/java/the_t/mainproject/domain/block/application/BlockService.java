package the_t.mainproject.domain.block.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import the_t.mainproject.domain.block.domain.Block;
import the_t.mainproject.domain.block.domain.repository.BlockRepository;
import the_t.mainproject.domain.member.domain.Member;
import the_t.mainproject.domain.member.domain.repository.MemberRepository;
import the_t.mainproject.global.common.Message;
import the_t.mainproject.global.common.SuccessResponse;
import the_t.mainproject.global.security.UserDetailsImpl;

@RequiredArgsConstructor
@Service
public class BlockService {

    private final BlockRepository blockRepository;
    private final MemberRepository memberRepository;

    @Transactional
    public SuccessResponse<Message> blockMember(UserDetailsImpl userDetails, Long blockedId) {
        Member blocker = memberRepository.findById(userDetails.getMember().getId())
                .orElseThrow(() -> new IllegalArgumentException("차단자 정보가 없습니다."));

        Member blocked = memberRepository.findById(blockedId)
                .orElseThrow(() -> new IllegalArgumentException("차단하고자 하는 멤버가 없습니다."));

        if(blockRepository.existsByBlockerAndBlocked(blocker, blocked)) {
            throw new IllegalArgumentException("이미 차단한 사용자입니다.");
        }

        Block block = Block.builder()
                .blocker(blocker)
                .blocked(blocked)
                .build();

        blockRepository.save(block);

        Message message = Message.builder()
                .message("차단이 완료되었습니다.")
                .build();

        return SuccessResponse.of(message);
    }
}
