package com.origin.originproject;

import com.origin.simple_socket.service.ProtocolAdapter;
import com.origin.simple_socket.service.Rst;
import com.origin.simple_socket.utils.LogUtil;
import com.origin.simple_socket.utils.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.util.Random;

/**
 * Created by fantao on 2017/11/29.
 */
public class TestProtocol extends ProtocolAdapter {
    private static final String TAG = "TestProtocol";
    public final static int ACTION_QUERY = 1;
    public final static long TIMEOUT = 3000;
    private long readStartAt;

    @Override
    public byte[] getCommandByAction(int action, String params) {
        switch (action) {
            case ACTION_QUERY:
                byte[] bytes = new byte[6];
                new Random().nextBytes(bytes);
                return bytes;
        }
        return new byte[0];
    }

    /**
     * 等待并判断是否超时
     *
     * @return true 表示超时
     */
    private boolean waitMs() {
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (System.currentTimeMillis() - readStartAt > TIMEOUT) {
            return true;
        }
        return false;
    }

    @Override
    public Rst readRst(int action, String params, InputStream inputStream) {
        LogUtil.d(TAG, "start readRst");
        readStartAt = System.currentTimeMillis();
        //根据具体通信协议解析数据包
        switch (action) {
            case ACTION_QUERY:
                byte[] buffer = new byte[1024];
                /**
                 * 通常做法是：
                 * 1.读取协议头，直到读取到指定字节的协议头或者超时
                 * 2.校验协议头，校验成功则继续，失败则舍弃一个字节，再读取一个字节组成新的头，继续校验，依次递归直到头校验成功或者超时
                 * 3.读取数据内容，根据协议规定的格式读取指定长度的数据内容或者超时
                 * 4.校验数据合法性，数据尾校验，合法则解释3.步骤读取到的内容封装成Rst，然后返回。不合法则报错
                 *
                 * 下面只是简单的demo了一下，并未按照上述做法来写代码，请使用者自行实现
                 */
                //读取头
                while (true) {
                    try {
                        int rst = inputStream.read();
                        if (rst != -1) {
                            buffer[0] = (byte) rst;
                            break;
                        }
                        if (waitMs()) {
                            return Rst.timeoutRst();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                //读取内容
                int len;
                try {
                    len = inputStream.read(buffer, 1, buffer.length - 1);
                    if (len > 0) {
                        System.out.println("####response command:" + Utils.bytesToHexString(buffer, 0, len + 1));
                        byte[] realRstBytes = new byte[1 + len];
                        System.arraycopy(buffer, 0, realRstBytes, 0, realRstBytes.length);
                        Rst rst = new Rst(Rst.CODE_SUCCESS, "success");
                        rst.setOtherRst(realRstBytes);
                        return rst;
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return null;

        }
        return null;

    }

    @Override
    public InetSocketAddress getSocketAddress() {
        return new InetSocketAddress("172.16.0.126", 8333);
    }
}
