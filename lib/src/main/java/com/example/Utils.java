package com.example;

/**
 * Created by fantao on 2017/8/22.
 */
public class Utils {

    public static String bytesToHexString(byte[] src, int offset, int len) {
        StringBuilder stringBuilder = new StringBuilder("");
        if (src == null || src.length <= offset) {
            return null;
        }
        for (int i = offset; (i < src.length && i < offset + len); i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v);
            if (hv.length() < 2) {
                hv = "0" + hv;
            }
            stringBuilder.append(" 0x" + hv);
        }
        return stringBuilder.toString();
    }

}
