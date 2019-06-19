package com.ibm.ksw;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class Utils {
    public static String encode(String string){
        try {
            return URLEncoder.encode(string, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return string;
        }
    }


}
