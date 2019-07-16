package gateway.exception;

public class ResourceNotFoundException extends NettyHandlerException{
    public ResourceNotFoundException() {
        this.code = 404;
        this.state = "Not Found";
    }
}
