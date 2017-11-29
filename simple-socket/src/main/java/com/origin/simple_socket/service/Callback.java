package com.origin.simple_socket.service;

/**
 * Created by fantao on 2017/8/23.
 */
public interface Callback {
    /**
     * @return true表示在UI线程回调，false表示在新的、非UI线程回调
     */
    boolean callbackOnUIThread();

    void onRst(String reqId, int action, Rst rst);
}