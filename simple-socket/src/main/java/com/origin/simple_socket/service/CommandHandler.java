package com.origin.simple_socket.service;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Looper;

import com.origin.simple_socket.trans.WriteUtil;
import com.origin.simple_socket.utils.LogUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by fantao on 2017/8/22.
 * 请求响应式串口操作，例如485串口
 */
public final class CommandHandler {
    private static final String TAG = "CommandHandler";
    /**
     * 读取超时时间
     */
    private final static int READ_TIMEOUT_SECONDS = 20;
    /**
     * 连接超时时间
     */
    private final static int CONNECT_TIMEOUT_SECONDS = 30;
    private ProtocolAdapter protocolAdapter;

    private InputStream inputStream;
    private OutputStream outputStream;
    private Socket socket;
    private static ExecutorService executor = Executors.newFixedThreadPool(3);
    /**
     * 用于在UI线程处理回调
     */
    private Handler uiHandler = new Handler(Looper.getMainLooper());

    public CommandHandler() {

    }


    public synchronized void proceed(SocketOp socketOp) {
        try {
            if (!isOK()) {
                Rst rst = new Rst(Rst.CODE_CONNECTION_ERROR, "socket not available");
                callback(rst, socketOp);
                return;
            }
            //开始写命令，如果写失败，则回调
            if (WriteUtil.write(outputStream, protocolAdapter.getCommandByAction(socketOp.getAction()
                    , socketOp.getParams()), inputStream, protocolAdapter) == -1) {
                Rst rst = new Rst(Rst.CODE_WRITE_FAILED, "command write fail");
                callback(rst, socketOp);
                return;
            }
            //如果写成功，则开始读取
            Rst rst = protocolAdapter.readRst(socketOp.getAction(), socketOp.getParams(), inputStream);
            callback(rst, socketOp);
        } catch (Exception e) {
            e.printStackTrace();
            Rst rst = new Rst(Rst.CODE_UNKNOWN, e.getMessage());
            callback(rst, socketOp);
            if (e instanceof SocketException) {
                //断开，等待重连
                disConnect();
            }
        }

    }

    private void callback(final Rst rst, final SocketOp socketOp) {
        LogUtil.d(TAG, "serial port rst:" + rst);
        // 在不同线程回调
        if (socketOp.getCallback() != null) {
            if (socketOp.getCallback().callbackOnUIThread()) {
                uiHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        socketOp.getCallback().onRst(socketOp.getReqId(), socketOp.getAction(), rst);
                    }
                });
            } else {
                executor.execute(new Runnable() {
                    @Override
                    public void run() {
                        socketOp.getCallback().onRst(socketOp.getReqId(), socketOp.getAction(), rst);
                    }
                });
            }
        }
    }

    public void init(Context context, final ProtocolAdapter adapter) {
        if (adapter == null) {
            throw new RuntimeException("the protocol adapter can not be null");
        }
        final Context finalContext = context.getApplicationContext();

        this.protocolAdapter = adapter;
        executor.execute(new Runnable() {
            @Override
            public void run() {
                connect();
                finalContext.startService(new Intent(finalContext, CheckService.class));
            }
        });

    }


    public boolean isOK() {
        return socket != null &&
                !socket.isInputShutdown() &&
                !socket.isOutputShutdown() &&
                socket.isConnected() &&
                !socket.isClosed() &&
                outputStream != null
                && inputStream != null;
    }

    public void disConnect() {
        if (socket != null && !socket.isClosed()) {
            LogUtil.d(TAG, "closing socket");
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            socket = null;
        }
    }

    public synchronized void connect() {
        if (!isOK()) {
            LogUtil.d(TAG, "start connect" + protocolAdapter.getSocketAddress().toString());
            try {
                socket = new Socket();
                socket.connect(protocolAdapter.getSocketAddress(), CONNECT_TIMEOUT_SECONDS * 1000);
                try {
                    socket.setSoTimeout(READ_TIMEOUT_SECONDS * 1000);
                } catch (SocketException e) {
                    e.printStackTrace();
                }
                inputStream = socket.getInputStream();
                outputStream = socket.getOutputStream();
                LogUtil.d(TAG, "connect success");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            LogUtil.d(TAG, "already connected");
        }

    }
}
