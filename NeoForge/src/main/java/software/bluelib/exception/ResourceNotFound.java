package software.bluelib.exception;

public class ResourceNotFound extends RuntimeException {
    public ResourceNotFound(String pMessage) {
        super(pMessage);
    }
}
