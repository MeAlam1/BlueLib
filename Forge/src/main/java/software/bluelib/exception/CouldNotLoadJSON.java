// Copyright (c) BlueLib. Licensed under the MIT License.

package software.bluelib.exception;

/**
 * A {@code RuntimeException} that represents an error when a JSON file could not be loaded.
 * <p>
 * This exception provides additional context by including the {@link #getResourceId()} of the JSON file that failed to load.
 * </p>
 * <p>
 * Key Features:
 * <ul>
 *   <li>{@link #getResourceId()} - Retrieves the ID of the resource that could not be loaded.</li>
 * </ul>
 * </p>
 * @author MeAlam
 * @Co-author Dan
 * @since 1.0.0
 */
public class CouldNotLoadJSON extends RuntimeException {

    /**
     * A {@link String} that represents the ID of the resource that could not be loaded.
     * <p>
     * This ID is used to provide additional context for the error.
     * </p>
     * @Co-author MeAlam, Dan
     * @since 1.0.0
     */
    private final String resourceId;

    /**
     * Constructs a new {@code CouldNotLoadJSON} exception with the specified detail message and resource ID.
     * <p>
     * The detail message provides information about the nature of the error, while the resource ID indicates
     * which specific resource could not be loaded.
     * </p>
     *
     * @param pMessage {@link String} - The detail message explaining the reason for the exception.
     * @param pResourceId {@link String} - The ID of the resource that could not be loaded.
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
    public CouldNotLoadJSON(String pMessage, String pResourceId) {
        super(pMessage);
        this.resourceId = pResourceId;
    }

    /**
     * A {@link String} that retrieves the resource ID of the JSON file that could not be loaded.
     * <p>
     * This method provides access to the ID of the resource that caused the exception.
     * </p>
     *
     * @return The resource ID as a {@link String}.
     * @author MeAlam
     * @Co-author Dan
     * @since 1.0.0
     */
    public String getResourceId() {
        return resourceId;
    }
}
