package the_t.mainproject.domain.auth.exception;

public class RefreshTokenNotFoundException extends RuntimeException {

    public RefreshTokenNotFoundException(String msg) { super(msg); }
}
