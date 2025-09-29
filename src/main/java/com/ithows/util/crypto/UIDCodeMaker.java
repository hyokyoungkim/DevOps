/*
 *  Copyright 2020. S.O.X. All rights reserved
 */

package com.ithows.util.crypto;

/**
 * Class UIDCodeMaker
 * 
 * @author Roi Kim <S.O.X Co. Ltd.>
 */

import java.math.BigInteger;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Hex;


public class UIDCodeMaker {

    private static String encodeString(String str) {
        char[] sbBytes = str.toString().toCharArray(); // .getBytes();
        StringBuffer outputBuf = new StringBuffer();
        StringBuffer strBuf = new StringBuffer();

        // 1. 구분자는 그냥 16진수로 변환
        strBuf.append(sbBytes[0]);
        strBuf.append(sbBytes[1]);
        int a = Integer.parseInt(new String(strBuf));
        outputBuf.append(String.format("%02x", a));

        // 2. 날짜는 255 나눈값과 나머지를 구분하여 16진수로 입력 (나머지 부터 입력)
        strBuf.setLength(0);
        strBuf.append(sbBytes[6]);
        strBuf.append(sbBytes[7]);
        strBuf.append(sbBytes[8]);
        strBuf.append(sbBytes[9]);
        a = Integer.parseInt(new String(strBuf));
        outputBuf.append(String.format("%02x", a % 255));                
        outputBuf.append(String.format("%02x", a / 255));

        
        // 3. 년도는 255 나눈값과 나머지를 구분하여 16진수로 입력 (나머지 부터 입력)
        strBuf.setLength(0);
        strBuf.append(sbBytes[2]);
        strBuf.append(sbBytes[3]);
        strBuf.append(sbBytes[4]);
        strBuf.append(sbBytes[5]);
        a = Integer.parseInt(new String(strBuf));
        outputBuf.append(String.format("%02x", a % 255));        
        outputBuf.append(String.format("%02x", a / 255));



        // 4. 일련번호는 먼저 자리수를 바꾼 후 그 값을 255 나눈값과 나머지를 구분하여 16진수로 입력 (나머지 부터 입력)
        strBuf.setLength(0);
        strBuf.append(sbBytes[13]);
        strBuf.append(sbBytes[12]);
        strBuf.append(sbBytes[11]);
        strBuf.append(sbBytes[10]);
        a = Integer.parseInt(new String(strBuf));
        outputBuf.append(String.format("%02x", a % 255));        
        outputBuf.append(String.format("%02x", a / 255));


        // 5. 최종 결과는 순서를 바꾸어 전달
        outputBuf.reverse();
        outputBuf.insert(3, '-');
        outputBuf.insert(8, '-');
        outputBuf.insert(13, '-');
        
        return outputBuf.toString().toUpperCase();
    }

    private static String decodeString(String str) {
        
        StringBuffer orgBuf = new StringBuffer(str.replace("-", ""));
        
        // 1. 순서를 바꾸어 원래대로
        char[] sbBytes = orgBuf.reverse().toString().toCharArray(); 


        StringBuffer outputBuf = new StringBuffer();

        // 2. 구분자는 16진수를 10진 값으로
        StringBuffer strBuf = new StringBuffer();
        strBuf.append(sbBytes[0]);
        strBuf.append(sbBytes[1]);
        int a = Integer.parseInt(new String(strBuf), 16);
        int b = Integer.parseInt(new String(strBuf), 16);
        outputBuf.append(String.format("%02d", a));

        // 3. 날짜는 255 나눈값을 곱하고 나머지를 더하여 4자리수로 복원
        strBuf.setLength(0);
        strBuf.append(sbBytes[6]);
        strBuf.append(sbBytes[7]);
        b = Integer.parseInt(new String(strBuf), 16);
        strBuf.setLength(0);
        strBuf.append(sbBytes[8]);
        strBuf.append(sbBytes[9]);
        a = Integer.parseInt(new String(strBuf), 16);
        outputBuf.append(String.format("%04d", a * 255 + b));
        
        // 4. 년도는 255 나눈값을 곱하고 나머지를 더하여 4자리수로 복원
        strBuf.setLength(0);
        strBuf.append(sbBytes[2]);
        strBuf.append(sbBytes[3]);
        b = Integer.parseInt(new String(strBuf), 16);
        strBuf.setLength(0);
        strBuf.append(sbBytes[4]);
        strBuf.append(sbBytes[5]);
        a = Integer.parseInt(new String(strBuf), 16);
        outputBuf.append(String.format("%04d", a * 255 + b));


        // 5. 일련번호는 255 나눈값을 곱하고 나머지를 더하여 4자리수로 복원후 자리수를 바꿈
        strBuf.setLength(0);
        strBuf.append(sbBytes[10]);
        strBuf.append(sbBytes[11]);
        b = Integer.parseInt(new String(strBuf), 16);
        strBuf.setLength(0);
        strBuf.append(sbBytes[12]);
        strBuf.append(sbBytes[13]);
        a = Integer.parseInt(new String(strBuf), 16);
        StringBuffer tmp = new StringBuffer(String.format("%04d", a * 255 + b));
        outputBuf.append(tmp.reverse());

        return outputBuf.toString();

    }

    public static void main(String[] args) throws Exception {

//        String sb = "01201312039999";  // 구분자(2)+년도(4)+날짜(4)+일련번호(4)
//        String encodeStr = encodeString(sb);
//        String result = decodeString(encodeStr);
//        
//        System.out.println(">>>>>> orginal Code  = " + sb);
//        System.out.println(">>>>>> encodeStr  = " + encodeStr);
//        System.out.println(">>>>>> decodeStr  = " + result);
        System.out.println(">>>>>>   = " +new SimpleDateFormat("yyyyMMdd").format(new Date()).toString());
        return;
    }

}

