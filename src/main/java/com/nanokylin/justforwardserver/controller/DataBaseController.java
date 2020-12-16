package com.nanokylin.justforwardserver.controller;

import com.nanokylin.justforwardserver.common.Resources;
import com.nanokylin.justforwardserver.service.DataBaseService;
import com.nanokylin.justforwardserver.service.impl.database.SQLiteDataBaseImpl;
import com.nanokylin.justforwardserver.utils.LogUtil;

public class DataBaseController {
    /**
     * 在这里为数据库连接开线程
     */
    public void initDatabase(ThreadController threadController) {
        Thread dataBaseThread = new DataBaseThread();
        threadController.getThreadPool().execute(dataBaseThread);
    }
}

class DataBaseThread extends Thread {
    private static final LogUtil log = new LogUtil();

    @Override
    public void run() {
        super.setName("DataBaseThread");
        DataBaseService dataBaseService = new SQLiteDataBaseImpl();
        Resources.SQLLiteConnection = dataBaseService.loadDataBase("data/catnote.db");
    }
}
