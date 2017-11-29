package com.origin.simple_socket.trans;

import android.util.Log;


import com.origin.simple_socket.service.ProtocolAdapter;
import com.origin.simple_socket.utils.Utils;

import java.io.InputStream;
import java.io.OutputStream;

public class WriteUtil {
    private static final String TAG = "WriteUtil";

    public static int write(OutputStream outputStream, byte[] mBuffer, InputStream inputStream, ProtocolAdapter adapter) throws Exception {
        int rst = -1;
        // FIXME 根据实际需要看是否需要在每次写数据之前清空读缓存
        byte[] buffer = new byte[64];
        int readLen = 0;
        while (inputStream.available() > 0) {
            readLen = inputStream.read(buffer);
            if (readLen == 0 || readLen == -1) {
                break;
            }
        }

        Thread.sleep(20);

        outputStream.write(mBuffer);
        outputStream.flush();
        Log.i(TAG, "Write:" + Utils.bytesToHexString(mBuffer));

        Thread.sleep(20);
        rst = 1;
        return rst;
    }
}
