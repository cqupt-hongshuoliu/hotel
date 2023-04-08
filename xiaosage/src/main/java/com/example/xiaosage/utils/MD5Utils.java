package com.example.xiaosage.utils;

import cn.hutool.core.util.IdUtil;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.stereotype.Component;

import java.util.Scanner;


@Component
public class MD5Utils {

    public static String md5(String src){
        return DigestUtils.md5Hex(src);
    }

    private static final String salt = "1a2b3c4d";
 
    //第一次加密
    public static String inputPassToFormPass(String inputPass){
        //md5加密密码前，先对密码进行处理，按以下salt的规则处理密码
        String str = "" + salt.charAt(0) + salt.charAt(2) + inputPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }
    //第二次加密
    public static String formPassToDBPass(String formPass, String salt){
        String str = "" + salt.charAt(0) + salt.charAt(2) + formPass + salt.charAt(5) + salt.charAt(4);
        return md5(str);
    }
    //实际调用的方法，将第一次加密和第二次加密合并，结果应该一致
    public static String inputPassToDBPass(String inputPass, String salt){
        String formPass = inputPassToFormPass(inputPass);
        String dbPass = formPassToDBPass(formPass, salt);
        return dbPass;
    }

    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        int n = scanner.nextInt();
        int[] a = new int[n];
        for (int i=0;i<n;i++){
            a[i]=scanner.nextInt();
        }
        System.out.println(fun(a));

    }

    public static int fun(int[] a){
        if (a.length==0){
            return 0;
        }
        int max = 0;
        int box;
        for (int i=0;i<a.length-1;i++){
            for (int j=i+1;j<a.length;j++){
                box = a[j]-a[i];
                if (box>max){
                    max=box;
                }
            }
        }
        return max;
    }



}