package com.autumn.mybatis.split.enums;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author autumn
 * @desc 线程池枚举类
 * @date 2025/5/25 16:02
 **/
public enum ThreadPoolEnum {

    /**
     * 默认线程池
     */
    DEFAULT,

    /**
     * 高并发场景线程池
     */
    HIGH_CONCURRENCY,

    /**
     * IO 密集型任务线程池
     */
    IO_BOUND;

    private final ExecutorService executor;

    ThreadPoolEnum() {
        this.executor = createExecutor();
    }

    private ExecutorService createExecutor() {
        return switch (this) {
            case DEFAULT -> defaultPool();
            case HIGH_CONCURRENCY -> highConcurrencyPool();
            case IO_BOUND -> ioBoundPool();
        };
    }

    public ExecutorService getThreadPoolExecutor() {
        return executor;
    }

    private ExecutorService defaultPool() {
        int corePoolSize = Runtime.getRuntime().availableProcessors();
        return new ThreadPoolExecutor(
                corePoolSize,
                corePoolSize * 2,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(1000),
                new CustomThreadFactory("default-pool"),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    private ExecutorService highConcurrencyPool() {
        int corePoolSize = Runtime.getRuntime().availableProcessors() * 4;
        return new ThreadPoolExecutor(
                corePoolSize,
                corePoolSize * 4,
                30L, TimeUnit.SECONDS,
                new SynchronousQueue<>(),
                new CustomThreadFactory("high-concurrency-pool"),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    private ExecutorService ioBoundPool() {
        return new ThreadPoolExecutor(
                50,
                100,
                60L, TimeUnit.SECONDS,
                new LinkedBlockingQueue<>(2000),
                new CustomThreadFactory("io-bound-pool"),
                new ThreadPoolExecutor.CallerRunsPolicy());
    }

    public static void shutdownAll() {
        for (ThreadPoolEnum pool : values()) {
            pool.executor.shutdownNow();
        }
    }

    static class CustomThreadFactory implements ThreadFactory {
        private final String poolName;
        private final AtomicInteger threadNumber = new AtomicInteger(1);

        public CustomThreadFactory(String poolName) {
            this.poolName = poolName;
        }

        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r, poolName + "-thread-" + threadNumber.getAndIncrement());
            t.setDaemon(false);
            return t;
        }
    }

}
