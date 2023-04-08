package com.example.xiaosage.utils;

import java.io.IOException;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public final class BASE64Util {
    public BASE64Util() {
    }

    public static final String encode(String base) {
        return encode(base.getBytes());
    }

    public static final String encode(byte[] baseBuff) {
        return (new BASE64Encoder()).encode(baseBuff);
    }

    public static final String decode(String encoder) {
        try {
            BASE64Decoder decoder = new BASE64Decoder();
            byte[] buf = decoder.decodeBuffer(encoder);
            return new String(buf);
        } catch (IOException var3) {
            var3.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        String decode = decode("MWEyYjNjNGQ=");
        System.out.println(decode);
    }
}