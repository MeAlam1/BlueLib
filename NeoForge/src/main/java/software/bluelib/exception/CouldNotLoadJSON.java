package software.bluelib.exception;

public class CouldNotLoadJSON extends RuntimeException {
    private final String resourceId;

    public CouldNotLoadJSON(String pMessage, String resourceId) {
        super(pMessage);
        this.resourceId = resourceId;
    }

    public String getResourceId() {
        return resourceId;
    }
}
