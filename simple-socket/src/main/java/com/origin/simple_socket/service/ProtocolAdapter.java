package com.origin.simple_socket.service;

import java.io.InputStream;
import java.net.InetSocketAddress;

/**
 * Created by fantao on 2017/8/22.
 */
public abstract class ProtocolAdapter {
    /**
     * 根据操作action类型获取要发送的byte数据
     *
     * @param action 操作类型
     * @param params 操作参数
     * @return action类型的操作，要往串口发送的命令数据
     */
    public abstract byte[] getCommandByAction(int action, String params);

    /**
     * 命令发送后，从串口中读取响应数据
     *
     * @param action      操作类型
     * @param params      操作参数
     * @param inputStream 串口的输入流
     * @return 读取结果，将在回调中返回
     */
    public abstract Rst readRst(int action, String params, InputStream inputStream);

    /**
     * 串口设备名
     *
     * @return
     */
    public abstract InetSocketAddress getSocketAddress();


}
