// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.exception;

/**
 * Custom exception thrown when a JSON resource could not be loaded. <br>
 * This exception provides additional context by including the resource ID of the JSON file that failed to load.
 */
public class CouldNotLoadJSON extends RuntimeException {

    /**
     * The ID of the resource that could not be loaded.
     */
    private final String resourceId;

    /**
     * Constructs a new {@code CouldNotLoadJSON} exception with the specified detail message and resource ID.
     *
     * @param pMessage    The detail message explaining the reason for the exception.
     * @param pResourceId The ID of the resource that could not be loaded.
     */
    public CouldNotLoadJSON(String pMessage, String pResourceId) {
        super(pMessage);
        this.resourceId = pResourceId;
    }

    /**
     * Retrieves the resource ID of the JSON file that could not be loaded.
     *
     * @return The resource ID as a string.
     */
    public String getResourceId() {
        return resourceId;
    }
}
