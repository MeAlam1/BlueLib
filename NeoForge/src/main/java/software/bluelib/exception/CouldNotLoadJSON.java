// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.exception;

import software.bluelib.entity.variant.VariantParameter;

/**
 * A {@code Class} representing an exception that is thrown when a JSON file could not be loaded.
 * This exception provides additional context by including the {@code getResourceId} of the JSON file that failed to load.
 */
public class CouldNotLoadJSON extends RuntimeException {

    /**
     * A {@link String} representing the ID of the resource that could not be loaded.
     */
    private final String resourceId;

    /**
     * Constructs a new {@code CouldNotLoadJSON} exception with the specified detail message and resource ID.
     *
     * @param pMessage {@link String} - The detail message explaining the reason for the exception.
     * @param pResourceId {@link String} - The ID of the resource that could not be loaded.
     */
    public CouldNotLoadJSON(String pMessage, String pResourceId) {
        super(pMessage);
        this.resourceId = pResourceId;
    }

    /**
     * A {@link String} that retrieves the resource ID of the JSON file that could not be loaded.
     *
     * @return The resource ID as a string.
     */
    public String getResourceId() {
        return resourceId;
    }
}
