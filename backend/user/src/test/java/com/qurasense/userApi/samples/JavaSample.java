package com.qurasense.userApi.samples;

import java.time.LocalTime;

public class JavaSample {

    public static void main(String[] args) {
        System.out.println(LocalTime.now().toString());
        System.out.println(LocalTime.parse("19:10:34").toString());
    }

}
