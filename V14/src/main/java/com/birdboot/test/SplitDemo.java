package com.birdboot.test;

import java.util.Arrays;

public class SplitDemo {
    public static void main(String[] args) {
        String line = "1,2,3,4,5,6,7,,,,,,,,,";
        String[] arr = line.split(",");
        String[] arr1 = line.split(",", 100);
        String[] arr2 = line.split(",", 0);
        String[] arr3 = line.split(",", -1);
        System.out.println(Arrays.toString(arr));
        System.out.println(Arrays.toString(arr1));
        System.out.println(Arrays.toString(arr2));
        System.out.println(Arrays.toString(arr3));
    }
}
