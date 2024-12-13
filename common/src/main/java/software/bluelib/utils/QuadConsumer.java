package software.bluelib.utils;

/**
 * Represents an operation that accepts four input arguments and returns no result.
 * <p>
 * Purpose: This functional interface defines a single method that takes four parameters and performs an operation without returning a result.<br>
 * When: Used when an operation needs to be performed on four inputs without producing a return value.<br>
 * Where: Can be used in lambda expressions or method references that require four input parameters.<br>
 * Additional Info: This is a functional interface and can be used as the assignment target for a lambda expression or method reference.
 * </p>
 *
 * @param <T> the type of the first argument to the operation
 * @param <U> the type of the second argument to the operation
 * @param <V> the type of the third argument to the operation
 * @param <W> the type of the fourth argument to the operation
 * @author MeAlam
 * @version 1.6.0
 * @see java.util.function.Consumer
 * @see java.util.function.BiConsumer
 * @see java.util.function.Function
 * @see java.util.function.BiFunction
 * @see FunctionalInterface
 * @since 1.6.0
 */
@FunctionalInterface
public interface QuadConsumer<T, U, V, W> {

    /**
     * Performs this operation on the given arguments.
     * <p>
     * Purpose: This method performs an operation using four input arguments.<br>
     * When: Called to execute the operation with the provided arguments.<br>
     * Where: Invoked in contexts where an operation with four inputs is needed.<br>
     * Additional Info: The method does not return any result.
     * </p>
     *
     * @param t the first input argument
     * @param u the second input argument
     * @param v the third input argument
     * @param w the fourth input argument
     * @author MeAlam
     * @see java.util.function.Consumer
     * @see java.util.function.BiConsumer
     * @see java.util.function.Function
     * @see java.util.function.BiFunction
     * @since 1.6.0
     */
    void accept(T t, U u, V v, W w);
}
