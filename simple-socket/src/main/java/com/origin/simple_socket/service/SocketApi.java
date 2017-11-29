package com.origin.simple_socket.service;

import android.content.Context;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

/**
 * Created by fantao on 2017/8/22.
 * 通信工具，适用于请求响应式的场景。内部封装了，初始化、发送接受数据、以及请求队列和回调的处理。
 * <br/>但是通信协议的封装和解析需要使用者自行实现
 * <br/>需要使用者调用{@link #init(Context, ProtocolAdapter, boolean)}方法来初始化、设置协议适配器
 */
public class SocketApi {

    private PriorityBlockingQueue<SocketOp> socketOpQueue = new PriorityBlockingQueue<>();
    private ExecutorService queueExecutor = Executors.newSingleThreadExecutor();
    private CommandHandler commandHandler;
    private ConsumeThread consumeThread;
    public static ArrayList<SocketApi> socketApis = new ArrayList<>();

    public SocketApi(Context context, ProtocolAdapter protocolAdapter, boolean logOpen) {
        commandHandler = new CommandHandler();
        init(context, protocolAdapter, logOpen);
        consumeThread = new ConsumeThread(socketOpQueue, commandHandler);
        socketApis.add(this);
    }


    /**
     * 初始化，此过程异步执行，是否初始化成功需要通过{@link #isOk()}判断
     *
     * @param protocolAdapter
     * @param logOpen
     */
    private void init(Context context, ProtocolAdapter protocolAdapter, boolean logOpen) {
        commandHandler.init(context, protocolAdapter);
    }

    public boolean isOk() {
//        todo service ok
        return commandHandler != null && commandHandler.isOK();
    }

    /**
     * 发送指令，并等待回调
     *
     * @param reqId    请求ID，会放在回调参数中
     * @param action   请求类型，使用者自己定义，使用者自己在{@link ProtocolAdapter}中使用
     * @param params   请求参数，action类型命令所需要的参数，使用者自己在{@link ProtocolAdapter}中使用
     * @param callback 结果回调
     * @return OpFuture，用于在界面销毁时释放回调对象，以免造成内存泄漏
     */
    public
    @Nullable
    OpFuture sendOpCmd(String reqId, int action, String params, DefaultCallback callback) {
        if (!isOk()) {
            if (callback != null) {
                callback.onRst(reqId, action, new Rst(Rst.CODE_CONNECTION_ERROR, "api hasn't been initialized"));
            }
            return null;
        }
        SocketOp socketOp = new SocketOp();
        socketOp.setAction(action);
        socketOp.setParams(params);
        socketOp.setReqId(reqId);
        socketOp.registerCallback(callback);
        addOpToQueue(socketOp);
        return new OpFuture(socketOp);
    }


    public void addOpToQueue(final SocketOp socketOp) {
        try {
            queueExecutor.execute(new Runnable() {
                @Override
                public void run() {
                    //put 若队列满了，阻塞，直到加入成功，以保证最终能加入进去
                    socketOpQueue.put(socketOp);
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void checkConsumerThread() {
        if (consumeThread != null && consumeThread.getState() == Thread.State.NEW) {
            consumeThread.initAndStart();
        }
    }

    public void checkConnection() {
        if (commandHandler != null && !commandHandler.isOK()) {
            commandHandler.connect();
        }
    }

    public void stopSelf() {
        if (consumeThread != null) {
            consumeThread.stopSelf();
        }
        if (commandHandler != null) {
            commandHandler.disConnect();
        }
    }
}
