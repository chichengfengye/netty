package gateway.exception;

public class AccessedDeniedException extends NettyHandlerException{
    public AccessedDeniedException() {
        this.code = 403;
        this.state = "Forbidden";
    }
}
