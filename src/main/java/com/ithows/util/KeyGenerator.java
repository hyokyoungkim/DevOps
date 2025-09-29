/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ithows.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;


public class KeyGenerator {

    private static int TOTAL = 62;
    private static char[] mapper = new char[]{48, 49, 50, 51, 52,
            53, 54, 55, 56, 57,
            65, 66, 67, 68, 69,
            70, 71, 72, 73, 74,
            75, 76, 77, 78, 79,
            80, 81, 82, 83, 84,
            85, 86, 87, 88, 89,
            90, 97, 98, 99, 100,
            101, 102, 103, 104, 105,
            106, 107, 108, 109, 110,
            111, 112, 113, 114, 115,
            116, 117, 118, 119, 120,
            121, 122};
    private static char[] specialKey = new char[]{'S', 'M', 'T', 'H', 'O', 'W', 'S', 'n', 'o', 'm', 's', 'y', 's', 't', 'e', 'm', 'k', 'w', 'e', 'o', 'n'};
    private static char[] specialKey2 = new char[]{'d', 'l', 't', 'k', 'd', 'g', 'k', 's', 'S', 'K', 'F', 'K', 'D', 'M', 'L', 'd', 'p', 'f', 'f', 'l', 't', 'm'};
    private static char[][] specialKey3 = {
            {'J', 'A', 'B', 'O', 'K', 'S', 'M', 'I', 'L', 'K'},
            {'D', 'R', 'E', 'A', 'M', 'C', 'T', 'O', 'K', 'S'},
            {'J', '3', 'A', '1', 'B', '0', 'K', '4', 'P', '9'},
            {'Q', '0', 'W', '3', 'E', '6', 'R', '9', 'T', '8'},
            {'d', '3', 'r', '1', 'e', '-', 'a', '0', 'm', '4'},
            {'w', '3', 'k', '1', 'q', '0', 'n', '4', 'r', '7'}
    };

    public static String getKey(char[] keys, int no) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.length; i++) {
            int pos = (keys[i] * no) % TOTAL;
            sb.append(mapper[pos]);
        }
        return sb.toString();
    }

    public static String getClientKey(char[] keys, int clientNo) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.length; i++) {
            int pos = Integer.parseInt(keys[i] + "");
            sb.append(specialKey3[clientNo % 4][pos]);
        }
        return sb.toString();
    }

    public static String getMovKey(char[] keys) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.length; i++) {
            int pos = Integer.parseInt(keys[i] + "");
            sb.append(specialKey3[4][pos]);
        }
        return sb.toString();
    }

    public static String getAutoLoginKey(int ref){
        return getKey(specialKey2, ref);
    }

    public static String getAutoLoginKey(String keys){
        return getKey(keys.toCharArray(), 1189);
    }

    public static String makeGuid(){
        UUID uuid = UUID.randomUUID();
        String uuidStr = uuid.toString();
        return uuidStr;
    }

    public static String getOrderDir(int no) {
        StringBuilder sb = new StringBuilder();
        sb.append(no);
        sb.append("_");
        sb.append(getKey(specialKey, no));
        sb.append(getKey(specialKey2, no));
        return sb.toString();
    }
//    public static String getExcelFile(int no, int gubun) {
//        StringBuilder sb = new StringBuilder();
//        sb.append(no);
//        sb.append("_");
//        sb.append(getKey(specialKey2, gubun));
//        sb.append(".xls");
//        return sb.toString();
//    }

    public static void main(String[] args) throws Exception{
        int no = 100033;

//        String str_date ;
//        String add ;
//
//        for(int i=0; i<40; i++){
//            str_date = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date()).toString();
//            add =  String.format("%08d" + str_date, no);
//           System.out.println(no + " : " + KeyGenerator.getMovKey(add.toCharArray()));
//           no++;
//           Thread.sleep(2500);
//        }
        System.out.println(" guid : " + makeGuid());

    }
}