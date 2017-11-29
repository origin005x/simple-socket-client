package com.origin.simple_socket.service;

/**
 * Created by fantao on 2017/8/22.
 */
public class OpFuture {
    private SocketOp socketOp;

    public OpFuture(SocketOp socketOp) {
        this.socketOp = socketOp;
    }

    protected void setSocketOp(SocketOp socketOp) {
        this.socketOp = socketOp;
    }

    public void releaseCallback() {
        if (socketOp != null && socketOp.getCallback() != null) {
            socketOp.clearCallback();
        }

        socketOp = null;
    }
}
