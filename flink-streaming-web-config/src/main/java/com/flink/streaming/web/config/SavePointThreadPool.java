package com.flink.streaming.web.config;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 *
 * 核心线程10个 最大线程100 队列200 公平策略
 *
 * @author zhuhuipei
 * @Description:
 * @date 2019-08-18
 * @time 00:16
 */
@Slf4j
public class SavePointThreadPool {

    private static int corePoolSize = 10;

    private static int maximumPoolSize = 100;

    private static long keepAliveTime = 10;

    private static ThreadPoolExecutor threadPoolExecutor;

    private static SavePointThreadPool asyncThreadPool;

    private SavePointThreadPool() {
        BlockingQueue<Runnable> workQueue = new ArrayBlockingQueue<>(400, true);
        threadPoolExecutor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MINUTES, workQueue);
    }

    public static synchronized SavePointThreadPool getInstance() {
        if (null == asyncThreadPool) {
            synchronized (SavePointThreadPool.class) {
                if (null == asyncThreadPool) {
                    asyncThreadPool = new SavePointThreadPool();
                }
            }
        }
        log.info("JobThreadPool threadPoolExecutor={}", threadPoolExecutor);
        return asyncThreadPool;
    }

    public synchronized ThreadPoolExecutor getThreadPoolExecutor() {
        return threadPoolExecutor;
    }
}
