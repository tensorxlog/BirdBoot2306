package com.birdboot.test;

import javax.activation.MimetypesFileTypeMap;

public class ContentTypeDemo {
    public static void main(String[] args) {
        MimetypesFileTypeMap mftm = new MimetypesFileTypeMap();
        String type = mftm.getContentType("test.jpeg");
        System.out.println(type);
    }
}
