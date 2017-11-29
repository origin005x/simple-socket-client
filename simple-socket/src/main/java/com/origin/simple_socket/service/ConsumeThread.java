package com.origin.simple_socket.service;

import com.origin.simple_socket.utils.LogUtil;

import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by fantao on 2017/11/10.
 */
public class ConsumeThread extends Thread {
    private static final String TAG = "ConsumeThread";
    private boolean stop;
    PriorityBlockingQueue<SocketOp> socketOpQueue;
    CommandHandler commandHandler;

    public ConsumeThread(PriorityBlockingQueue<SocketOp> socketOpQueue, CommandHandler commandHandler) {
        super();
        this.socketOpQueue = socketOpQueue;
        this.commandHandler = commandHandler;
        if (commandHandler == null) {
            throw new RuntimeException("commandHandler can not be null");
        }
    }

    @Override
    public void run() {
        while (!stop) {
            if (commandHandler != null && socketOpQueue != null) {
                try {
                    //poll若队列为空，返回null，不阻塞
                    SocketOp socketOp = socketOpQueue.poll();
                    if (socketOp != null) {
                        commandHandler.proceed(socketOp);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }
    }

    public void initAndStart() {
        LogUtil.d(TAG, "initAndStart");
        commandHandler.connect();
        this.stop = false;
        start();
    }

    public void stopSelf() {
        this.stop = true;
    }
}
