package the_t.mainproject.domain.auth.application;

import org.springframework.http.ResponseEntity;
import the_t.mainproject.domain.auth.dto.request.JoinReq;
import the_t.mainproject.domain.auth.dto.request.LoginReq;

public interface AuthService {

    ResponseEntity<?> join(JoinReq joinReq);

    ResponseEntity<?> login(LoginReq loginReq);
}
