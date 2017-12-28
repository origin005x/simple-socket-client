package com.origin.originproject;

import android.content.Context;

import com.origin.simple_socket.service.DefaultCallback;
import com.origin.simple_socket.service.SocketApi;

/**
 * Created by fantao on 2017/11/29.
 * 业务请求实现类，在这里可以定义业务请求，然后调用通信框架发送
 */
public class APICtrl {
    static SocketApi socketApi;

    public static void init(Context context) {
        socketApi = new SocketApi(context, new TestProtocol(), true);
    }

    public static void testQuery(DefaultCallback callback) {
        socketApi.sendOpCmd("1", TestProtocol.ACTION_QUERY, "", callback);
    }
}
