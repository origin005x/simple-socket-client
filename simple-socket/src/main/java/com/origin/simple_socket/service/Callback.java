package com.origin.simple_socket.service;

/**
 * Created by fantao on 2017/8/23.
 */
public interface Callback {
    /**
     * @return true表示在UI线程回调，false表示在新的、非UI线程回调
     */
    boolean callbackOnUIThread();

    /**
     * 返回写完数据之后开始读取数据之前需要等待的时间间隔
     *
     * @return 单位 ms
     */
    long waitBeforeReadData();

    void onRst(String reqId, int action, Rst rst);
}