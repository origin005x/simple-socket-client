package com.origin.simple_socket.service;

/**
 * Created by fantao on 2017/8/22.
 */
public class Rst {
    public final static int CODE_UNKNOWN = -1;
    public final static int CODE_SUCCESS = 1;
    /**
     * 写数据的过程中失败
     */
    public final static int CODE_WRITE_FAILED = 2;
    /**
     * 还未准备好
     */
    public final static int CODE_CONNECTION_ERROR = 3;
    /**
     * 读或写超时
     */
    public final static int CODE_TIMEOUT = 4;
    /**
     * 回复的是失败结果
     */
    public final static int CODE_RESPONSE_ERROR = 5;
    private int code;
    private String msg;
    private Object otherRst;

    public Rst(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Rst timeoutRst() {
        return new Rst(CODE_TIMEOUT, "timeout");
    }

    public Object getOtherRst() {
        return otherRst;
    }

    public void setOtherRst(Object otherRst) {
        this.otherRst = otherRst;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public boolean isSuccess() {
        return code == CODE_SUCCESS;
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return "Rst{" +
                "code=" + code +
                ", msg='" + msg + '\'' +
                ", otherRst=" + otherRst +
                '}';
    }
}
