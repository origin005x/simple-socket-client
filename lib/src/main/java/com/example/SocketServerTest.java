package com.example;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

public class SocketServerTest {
    public static void main(String[] args) {
        try {
            ServerSocket ss = new ServerSocket(8333);
            Socket so = ss.accept();
            System.out.println("get one connection:" + so.getInetAddress().toString());
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        InputStream in = so.getInputStream();
                        OutputStream outputStream = so.getOutputStream();
                        byte[] buffer = new byte[1024];
                        while (true) {
                            int len = in.read(buffer, 0, buffer.length);
                            if (len > 0) {
                                System.out.println("####get command:" + Utils.bytesToHexString(buffer, 0, len));
                                byte[] bytes = new byte[5];
                                new Random().nextBytes(bytes);
                                outputStream.write(bytes);
                                outputStream.flush();
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }).start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
