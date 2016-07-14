package com.tinymood;

import java.text.ParseException;
import java.text.SimpleDateFormat;

/**
 * Created by hztaoran on 2016/7/13.
 */
public class Test {
    public static void main(String[] args) {
        String str1 = "20160706160000";
        String str2 = "20160706161000";
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddhhmmss");
        Long millisecond1 = 0L;
        Long millisecond2 = 0L;
        try {
            millisecond1 = sdf.parse(str1).getTime();
            millisecond2 = sdf.parse(str2).getTime();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        System.out.println(millisecond1);
        System.out.println(millisecond2);
    }
}
