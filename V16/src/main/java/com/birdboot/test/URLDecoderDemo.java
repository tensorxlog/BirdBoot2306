package com.birdboot.test;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public class URLDecoderDemo {
    public static void main(String[] args) throws UnsupportedEncodingException {
        String original = "/loginUser?username=test%E6%B5%8B%E8%AF%95&password=35353454";
        System.out.println(URLDecoder.decode(original, "utf-8"));
    }
}
