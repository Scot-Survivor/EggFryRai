package com.comp5590.utils;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ScreenUtils {

    // this method is used to stall the tester for a given amount of time
    private static final ExecutorService executor = Executors.newSingleThreadExecutor();

    // this method is used to stall the tester for a given amount of time
    public static CompletableFuture<Void> stall(int secs) {
        CompletableFuture<Void> future = new CompletableFuture<>();
        executor.submit(() -> {
            try {
                TimeUnit.SECONDS.sleep(secs);
                future.complete(null); // Complete the future after the delay
            } catch (InterruptedException e) {
                future.completeExceptionally(e);
            }
        });
        return future;
    }
}
