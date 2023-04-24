package com.henry.shijiserver.util;

public class Tools {

    /**
     * 如果字符串为“”，则返回null，便于数据库处理
     */
    public static String strProcess(String str){
        if("".equals(str)){
            return null;
        }
        else{
            return str;
        }
    }
}
