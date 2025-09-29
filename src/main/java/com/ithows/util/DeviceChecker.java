/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ithows.util;


public class DeviceChecker {
    
    
    public static final int PC = 11;
    public static final int MOBILE = 12;
    public static final int TABLET = 13;
    

    
    public static int getDeviceType(String userAgent) {
        int type = 0;
        String uAgent = userAgent.toLowerCase();
        if (uAgent.indexOf("android") != -1) {
            if (uAgent.indexOf("mobile") != -1) {
                type = DeviceChecker.MOBILE; //모바일 4
            } else {
                type = DeviceChecker.TABLET; // 테블릿 5
            }
        } else if (uAgent.indexOf("ipad") != -1) {
            type = DeviceChecker.TABLET; // 테블릿 5
        } else if (uAgent.indexOf("ipod") != -1 || uAgent.indexOf("iphone") != -1) {
            type = DeviceChecker.MOBILE; //모바일 4
        } else if (uAgent.indexOf("phone") != -1) {
            type = DeviceChecker.MOBILE; //모바일 4
        } else {
            type = DeviceChecker.PC; //PC 3
        }
        return type;
    }
}