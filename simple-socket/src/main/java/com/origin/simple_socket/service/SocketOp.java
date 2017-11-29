package com.origin.simple_socket.service;

/**
 * Created by fantao on 2017/6/19.
 */
public final class SocketOp implements Comparable {
    /**
     * 请求ID
     */
    private String reqId;
    /**
     * 请求类型
     */
    private int action;
    /**
     * 请求参数
     */
    private String params;
    /**
     * 回调
     */
    private Callback callback;
    /**
     * 优先级
     */
    private int priority;

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getReqId() {
        return reqId;
    }

    public void setReqId(String reqId) {
        this.reqId = reqId;
    }

    public void registerCallback(Callback callback) {
        this.callback = callback;
    }

    public Callback getCallback() {
        return callback;
    }

    public void clearCallback() {
        this.callback = null;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(Object o) {
        if (o instanceof SocketOp) {
            SocketOp other = (SocketOp) o;
            if (priority > other.getPriority()) {
                return -1;
            } else if (priority < other.getPriority()) {
                return 1;
            }
        }
        return 0;
    }

    @Override
    public String toString() {
        return "SocketOp{" +
                "action=" + action +
                ", reqId='" + reqId + '\'' +
                ", params='" + params + '\'' +
                ", callback=" + callback +
                ", priority=" + priority +
                '}';
    }
}