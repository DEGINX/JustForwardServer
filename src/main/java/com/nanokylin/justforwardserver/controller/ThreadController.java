package com.nanokylin.justforwardserver.controller;

import com.nanokylin.justforwardserver.common.Config;
import com.nanokylin.justforwardserver.common.Resources;
import com.nanokylin.justforwardserver.service.ThreadPoolService;
import com.nanokylin.justforwardserver.service.impl.ThreadPoolServiceImpl;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.TimeUnit;

public class ThreadController {
    public void initThreadPool() {
        // 设置线程
        Resources.threadPool = new ThreadPoolServiceImpl((int) Config.getConfig("corePoolSize"), (int) Config.getConfig("maximumPoolSize"),
                Config.getLong("keepAliveTime"), TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>((int) Config.getConfig("queue")));
    }

    public ThreadPoolService getThreadPool() {
        return Resources.threadPool;
    }
}
