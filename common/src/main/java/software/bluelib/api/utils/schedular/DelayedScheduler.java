/*
 * Copyright (C) 2024 BlueLib Contributors
 *
 * This Source Code Form is subject to the terms of the MIT License.
 * If a copy of the MIT License was not distributed with this file,
 * You can obtain one at https://opensource.org/licenses/MIT.
 */
package software.bluelib.api.utils.schedular;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import org.jetbrains.annotations.NotNull;

public class DelayedScheduler {

    @NotNull
    private static final ScheduledExecutorService EXECUTOR_SERVICE = Executors.newSingleThreadScheduledExecutor();

    @NotNull
    public static CompletableFuture<Void> schedule(@NotNull Runnable task, @NotNull Long delay, @NotNull TimeUnit unit) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        EXECUTOR_SERVICE.schedule(() -> {
            try {
                task.run();
                future.complete(null);
            } catch (Exception pException) {
                future.completeExceptionally(pException);
            }
        }, delay, unit);
        return future;
    }

    @NotNull
    public static <A> CompletableFuture<A> schedule(@NotNull Supplier<A> supplier, @NotNull Long delay, @NotNull TimeUnit unit) {
        CompletableFuture<A> future = new CompletableFuture<>();
        EXECUTOR_SERVICE.schedule(() -> {
            try {
                A a = supplier.get();
                future.complete(a);
            } catch (Exception pException) {
                future.completeExceptionally(pException);
            }
        }, delay, unit);
        return future;
    }
}
