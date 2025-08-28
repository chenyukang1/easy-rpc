package com.cyk.easy.rpc.common.extension;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.fail;

public class ConcurrentLazyCacheTest {

    @Test
    public void test_get_Null() {
        ConcurrentLazyCache<String, Object> cache = new ConcurrentLazyCache<>();
        try {
            cache.get(null, key -> null);
            fail();
        } catch (ExtensionException e) {
            // pass
            assertThat(e.getMessage(), containsString("Key can not be null"));
        }
    }

    @Test
    public void test_get_concurrent() {
        ConcurrentLazyCache<String, Object> cache = new ConcurrentLazyCache<>();
        int nThreads = 10;
        CountDownLatch ready = new CountDownLatch(nThreads);
        CountDownLatch start = new CountDownLatch(1);
        ExecutorService pool = Executors.newFixedThreadPool(nThreads);

        List<Future<Object>> futures = new ArrayList<>();
        for (int i = 0; i < nThreads; i++) {
            futures.add(pool.submit(() -> {
                ready.countDown();
                start.await();
                return cache.get("key", key -> new Object());
            }));
        }

        try {
            ready.await();
        } catch (InterruptedException ignored) {
        }
        start.countDown(); // start all threads

        List<Object> results = new ArrayList<>();
        for (Future<Object> future : futures) {
            try {
                results.add(future.get());
            } catch (Exception e) {
                fail();
            }
        }
        Object object = results.get(0);
        for (int i = 1; i < results.size(); i++) {
            assertThat("Object not same", results.get(i) == object);
        }

        pool.shutdown();
    }
}
