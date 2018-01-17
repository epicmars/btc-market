package com.fafabtc.app.utils;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Created by jastrelax on 2018/1/12.
 */

public class ExecutorManager {

    /**
     * Used for slow network io or file io.
     */
    private static final ExecutorService IO = Executors.newFixedThreadPool(3, new DefaultThreadFactory("io"));

    private static final ExecutorService STATISTICS = Executors.newSingleThreadExecutor(new DefaultThreadFactory("statistics"));

    /**
     * Used for business like database assets.
     */
    private static final ExecutorService NOW = Executors.newFixedThreadPool(3, new DefaultThreadFactory("now"));

    public static ExecutorService getIO() {
        return IO;
    }

    public static ExecutorService getSTATISTICS() {
        return STATISTICS;
    }

    public static ExecutorService getNOW() {
        return NOW;
    }

    private static class DefaultThreadFactory implements ThreadFactory {

        private static final AtomicInteger poolNumber = new AtomicInteger(1);
        private final ThreadGroup group;
        private final AtomicInteger threadNumber = new AtomicInteger(1);
        private final String namePrefix;
        private final String nameSuffix;

        DefaultThreadFactory(String suffix) {
            SecurityManager s = System.getSecurityManager();
            group = (s != null) ? s.getThreadGroup() :
                    Thread.currentThread().getThreadGroup();
            namePrefix = "pool-" +
                    poolNumber.getAndIncrement() +
                    "-thread-";
            nameSuffix = suffix;
        }

        public Thread newThread(Runnable r) {
            Thread t = new Thread(group, r,
                    namePrefix + threadNumber.getAndIncrement() + "-" + nameSuffix,
                    0);
            if (t.isDaemon())
                t.setDaemon(false);
            if (t.getPriority() != Thread.NORM_PRIORITY)
                t.setPriority(Thread.NORM_PRIORITY);
            return t;
        }
    }
}
