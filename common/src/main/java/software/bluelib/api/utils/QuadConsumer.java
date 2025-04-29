package software.bluelib.api.utils;

@FunctionalInterface
public interface QuadConsumer<T, U, V, W> {

    void accept(T t, U u, V v, W w);
}
