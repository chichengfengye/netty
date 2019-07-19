package gateway.helper;

import java.util.concurrent.*;

public class AsyncService {
    private static final int nThreads;
    private static ExecutorService executorService ;

    static {
        nThreads = 10;
        executorService = Executors.newFixedThreadPool(nThreads);
    }

    public static ExecutorService getFixedThreadPool() {
        return executorService;
    }

    public static ExecutorService newFixedThreadPool(int nThreads, ThreadFactory threadFactory) {
        return Executors.newFixedThreadPool(nThreads, threadFactory);
    }
}
