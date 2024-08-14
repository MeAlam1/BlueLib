// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.exception;

public class CouldNotLoadJSON extends RuntimeException {
    private final String resourceId;

    public CouldNotLoadJSON(String pMessage, String pResourceId) {
        super(pMessage);
        this.resourceId = pResourceId;
    }

    public String getResourceId() {
        return resourceId;
    }
}
