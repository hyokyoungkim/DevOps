package com.ithows.util;

public class HTMLUtils {



    public static String getHtmlEncode(String str) {
        str = str.replace("&", "&amp;");
        str = str.replace("\"", "&quot;");
        str = str.replace(">", "&gt;");
        str = str.replace("<", "&lt;");
        str = str.replace(" ", "&nbsp;");
        str = str.replace("\r\n", "<br>");
        str = str.replace("\n", "<br>");
        str = str.replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;");
        return str;
    }

    public static String getHtmlEncode2(String str) {
        str = str.replace("&", "&amp;");
        str = str.replace("\"", "&quot;");
        str = str.replace(">", "&gt;");
        str = str.replace("<", "&lt;");
        str = str.replace("\r\n", "<br>");
        str = str.replace("\n", "<br>");
        return str;
    }

    public static String getBrEncode(String str) {
        str = str.replace("\r\n", "<br>");
        str = str.replace("\n", "<br>");
        return str;
    }


}
