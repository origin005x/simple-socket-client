package com.origin.simple_socket.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.origin.simple_socket.utils.LogUtil;

/**
 * Created by fantao on 2017/6/19.
 */
public class CheckService extends Service {
    private static final String TAG = "CheckService";
    private ConsumerCheckTread consumerCheckTread;
    private boolean stop;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        stop = false;
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        stop = false;
        LogUtil.d(TAG, "service start");
        if (consumerCheckTread == null) {
            LogUtil.d(TAG, "init ConsumerCheckTread");
            consumerCheckTread = new ConsumerCheckTread();
            consumerCheckTread.start();
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 确保新的socketApi及其消费线程已经启动
     */
    public class ConsumerCheckTread extends Thread {
        @Override
        public void run() {
            while (!stop) {
                if (SocketApi.socketApis != null && SocketApi.socketApis.size() > 0) {
                    for (SocketApi socketApi : SocketApi.socketApis) {
                        socketApi.checkConsumerThread();
                        socketApi.checkConnection();
                    }
                }
                try {
                    Thread.sleep(5000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void onDestroy() {
        stop = true;

        if (SocketApi.socketApis != null && SocketApi.socketApis.size() > 0) {
            for (SocketApi socketApi : SocketApi.socketApis) {
                socketApi.stopSelf();
            }
        }
        super.onDestroy();
    }
}