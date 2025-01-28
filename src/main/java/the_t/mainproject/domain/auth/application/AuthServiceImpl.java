package the_t.mainproject.domain.auth.application;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import the_t.mainproject.domain.auth.dto.mapper.MemberAuthMapper;
import the_t.mainproject.domain.auth.dto.request.JoinReq;
import the_t.mainproject.domain.auth.dto.request.LoginReq;
import the_t.mainproject.domain.auth.dto.request.LogoutReq;
import the_t.mainproject.domain.auth.dto.request.ModifyPasswordReq;
import the_t.mainproject.domain.auth.dto.response.DuplicateCheckRes;
import the_t.mainproject.domain.auth.dto.response.LoginRes;
import the_t.mainproject.domain.auth.dto.response.ReissueRes;
import the_t.mainproject.domain.block.domain.Block;
import the_t.mainproject.domain.block.domain.repository.BlockRepository;
import the_t.mainproject.domain.comment.application.CommentService;
import the_t.mainproject.domain.comment.domain.Comment;
import the_t.mainproject.domain.comment.domain.repository.CommentRepository;
import the_t.mainproject.domain.commentreport.domain.CommentReport;
import the_t.mainproject.domain.commentreport.domain.repository.CommentReportRepository;
import the_t.mainproject.domain.liked.domain.Liked;
import the_t.mainproject.domain.liked.domain.repository.LikedRepository;
import the_t.mainproject.domain.member.domain.Member;
import the_t.mainproject.domain.member.domain.repository.MemberRepository;
import the_t.mainproject.domain.post.application.PostService;
import the_t.mainproject.domain.post.domain.Post;
import the_t.mainproject.domain.post.domain.repository.PostRepository;
import the_t.mainproject.domain.postreport.domain.PostReport;
import the_t.mainproject.domain.postreport.domain.repository.PostReportRepository;
import the_t.mainproject.global.common.Message;
import the_t.mainproject.global.common.SuccessResponse;
import the_t.mainproject.global.security.UserDetailsImpl;
import the_t.mainproject.global.security.jwt.JwtTokenProvider;
import the_t.mainproject.infrastructure.redis.RedisUtil;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthServiceImpl implements AuthService {

    @Value("${jwt.refresh.expiration}")
    private long refreshTokenValidityInSecond;
    // refresh token to Redis
    private static String RT_PREFIX = "RT_";
    private static String BL_AT_PREFIX = "BL_AT_";

    private final RedisUtil redisUtil;
    private final AuthenticationManager authenticationManager;
    private final MemberAuthMapper memberAuthMapper;

    private final MemberRepository memberRepository;
    private final BlockRepository blockRepository;
    private final LikedRepository likedRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final CommentReportRepository commentReportRepository;
    private final PostReportRepository postReportRepository;

    private final PostService postService;
    private final CommentService commentService;

    private final JwtTokenProvider jwtTokenProvider;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public SuccessResponse<Message> join(JoinReq joinReq) {
        String email = joinReq.getEmail();
        checkVerify(email);
        String nickname = joinReq.getNickname();

        if(memberRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("이미 가입된 이메일입니다.");
        }
        if(memberRepository.existsByNickname(nickname)) {
            throw new IllegalArgumentException("이미 사용중인 닉네임입니다.");
        }

        Member member = memberAuthMapper.joinToMember(joinReq);
        memberRepository.save(member);

        Message message = Message.builder()
                .message("회원가입이 완료됨")
                .build();

        return SuccessResponse.of(message);
    }

    @Override
    @Transactional
    public SuccessResponse<LoginRes> login(LoginReq loginReq) {
        String email = loginReq.getEmail();
        String password = loginReq.getPassword();

        // 로그인 시 회원 체크
        memberRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("해당 이메일의 유저를 찾을 수 없습니다: " + email));

        // 인증
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }catch (BadCredentialsException e) {
            throw new BadCredentialsException("비밀번호가 일치하지 않습니다.");
        }

        // Token 생성
        String accessToken = jwtTokenProvider.createAccessToken(email);
        String refreshToken = jwtTokenProvider.createRefreshToken();

        // refreshToken 발급
        redisUtil.setDataExpire(RT_PREFIX + refreshToken, email, refreshTokenValidityInSecond);

        LoginRes loginRes = LoginRes.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        return SuccessResponse.of(loginRes);
    }

    @Override
    public SuccessResponse<Message> logout(UserDetailsImpl userDetails, LogoutReq logoutReq) {
        String email = userDetails.getMember().getEmail();
        String findEmail = redisUtil.getData(RT_PREFIX + logoutReq.getRefreshToken());
        if(!email.equals(findEmail))
            throw new IllegalArgumentException("본인의 리프레시 토큰만 삭제할 수 있습니다.");
        redisUtil.deleteData(RT_PREFIX + logoutReq.getRefreshToken());

        // 남은 시간 초단위 계산
        DecodedJWT decodedJWT = JWT.decode(logoutReq.getAccessToken());
        Instant expiresAt = decodedJWT.getExpiresAt().toInstant();
        Instant now = Instant.now();
        long between = ChronoUnit.SECONDS.between(now, expiresAt);
        System.out.println("남은 시간: " + between);

        // 남은 만료시간만큼 access token blacklist
        redisUtil.setDataExpire(BL_AT_PREFIX + logoutReq.getAccessToken(), "black list token", between);

        Message message = Message.builder()
                .message("로그아웃이 완료되었습니다.")
                .build();

        return SuccessResponse.of(message);
    }

    @Override
    @Transactional
    public SuccessResponse<Message> exit(UserDetailsImpl userDetails) {
        Member member = memberRepository.findById(userDetails.getMember().getId())
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 회원입니다."));

        // 사용자 차단 내역 삭제
        List<Block> blockerList = blockRepository.findAllByBlocker(member);
        List<Block> blockedList = blockRepository.findAllByBlocked(member);
        blockRepository.deleteAll(blockerList);
        blockRepository.deleteAll(blockedList);

        // 댓글 및 게시글 신고 내역 삭제
        List<CommentReport> commentReportList = commentReportRepository.findAllByMember(member);
        for(CommentReport commentReport : commentReportList) {
            Comment comment = commentReport.getComment();
            comment.subtractReportedCount();
        }
        commentReportRepository.deleteAll(commentReportList);

        List<PostReport> postReportList = postReportRepository.findAllByMember(member);
        for(PostReport postReport : postReportList) {
            Post post = postReport.getPost();
            post.subtractReportedCount();
        }
        postReportRepository.deleteAll(postReportList);

        // 공감한 글 삭제
        List<Liked> likedList = likedRepository.findAllByMember(member);
        for(Liked liked: likedList) {
            Post post = liked.getPost();
            postService.dislikePost(post.getId(), userDetails);
        }

        // 작성한 댓글 삭제
        List<Comment> replyList = commentRepository.findAllByMemberAndParentCommentIsNotNull(member);
        for(Comment reply : replyList) {
            commentService.deleteComment(userDetails.getMember().getId(), reply.getId());
        }
        List<Comment> commentList = commentRepository.findAllByMemberAndParentCommentIsNull(member);
        for(Comment comment: commentList) {
            commentService.deleteComment(userDetails.getMember().getId(), comment.getId());
        }

        // 작성한 게시글 삭제
        List<Post> postList = postRepository.findByMember(member);
        for(Post post: postList) {
            postService.deletePost(post.getId(), userDetails);
        }

        memberRepository.delete(member);

        Message message = Message.builder()
                .message("회원 탈퇴가 완료되었습니다.")
                .build();

        return SuccessResponse.of(message);
    }

    @Override
    public SuccessResponse<ReissueRes> reissue(String refreshToken) {
        if(!jwtTokenProvider.isTokenValid(refreshToken))
            throw new TokenExpiredException("유효하지 않은 리프레시 토큰입니다.");

        String email = redisUtil.getData(RT_PREFIX + refreshToken);
        String accessToken = jwtTokenProvider.createAccessToken(email);

        ReissueRes reissueRes = ReissueRes.builder()
                .accessToken(accessToken)
                .build();

        return SuccessResponse.of(reissueRes);
    }

    @Override
    public SuccessResponse<DuplicateCheckRes> checkEmailDuplicate(String email) {
        DuplicateCheckRes emailDuplicateCheckRes = DuplicateCheckRes.builder()
                .availability(!memberRepository.existsByEmail(email))
                .build();

        return SuccessResponse.of(emailDuplicateCheckRes);
    }

    @Override
    public SuccessResponse<DuplicateCheckRes> checkNicknameDuplicate(String nickname) {
        DuplicateCheckRes nicknameDuplicateCheckRes = DuplicateCheckRes.builder()
                .availability(!memberRepository.existsByNickname(nickname))
                .build();

        return SuccessResponse.of(nicknameDuplicateCheckRes);
    }

    @Override
    @Transactional
    public SuccessResponse<Message> modifyPassword(ModifyPasswordReq modifyPasswordReq) {
        String email = modifyPasswordReq.getEmail();

        memberRepository.findByEmail(email)
                .ifPresentOrElse(
                        member -> member.updatePassword(passwordEncoder.encode(modifyPasswordReq.getPassword())),
                        () -> {
                            throw new IllegalArgumentException("해당 이메일로 가입한 계정이 없습니다.");
                        }
                );

        Message message = Message.builder()
                .message("비밀번호 변경 완료")
                .build();

        return SuccessResponse.of(message);
    }

    private void checkVerify(String email) {
        String data = redisUtil.getData(email + "_verify");
        if(data == null) {
            throw new IllegalArgumentException("인증이 필요한 이메일입니다.");
        }
        redisUtil.deleteData(email + "_verify");
    }
}
