package software.bluelib.exception;

public class ResourceNotFound extends RuntimeException {
    private final String resourceId;


    public ResourceNotFound(String pMessage, String resourceId) {
        super(pMessage);
        this.resourceId = resourceId;
    }

    public String getResourceId() {
        return resourceId;
    }
}
