package the_t.mainproject.domain.comment.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@RequiredArgsConstructor
@Component
public class CreateCommentServiceFactory {

    private final List<CreateCommentService> createCommentServiceList;

    public CreateCommentService find(Long parentCommentId) {
        return createCommentServiceList.stream()
                .filter(v -> v.commentSelector(parentCommentId))
                .findFirst()
                .orElseThrow();
    }
}
