package software.bluelib.api.utils.schedular;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import javax.annotation.Nonnull;

public class DelayedScheduler {

    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    @Nonnull
    public static CompletableFuture<Void> schedule(Runnable task, long delay, TimeUnit unit) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        EXECUTOR_SERVICE.schedule(() -> {
            try {
                task.run();
                future.complete(null);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        }, delay, unit);
        return future;
    }

    @Nonnull
    public static <A> CompletableFuture<A> schedule(Supplier<A> supplier, long delay, TimeUnit unit) {
        CompletableFuture<A> future = new CompletableFuture<>();
        EXECUTOR_SERVICE.schedule(() -> {
            try {
                A a = supplier.get();
                future.complete(a);
            } catch (Exception e) {
                future.completeExceptionally(e);
            }
        }, delay, unit);
        return future;
    }
}
