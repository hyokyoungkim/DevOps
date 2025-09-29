package com.codeenator.utils;

import java.util.UUID;

/**
 * 랜덤 생성 Utils
 */
public class RandomUtils {
    
    private static final char[] UPPER_CHAR_SET = new char[] { 
            '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
            'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
            'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z'
    };
    
    private static final char[] CHAR_SET = new char[] {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 
        'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm',
        'n', 'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'n', 'x', 'y', 'z',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J', 'K', 'L', 'M',
        'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U', 'V', 'W', 'X', 'Y', 'Z',
    };
    
    
    /**
     * 랜덤 문자 생성 (대문자)
     * @param length        문자 수
     * @return              랜덤 문자
     */
    public static String createUpper(int length) {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0, position = 0; i < length; i++) {
            position = (int) (UPPER_CHAR_SET.length * Math.random());
            
            sb.append(UPPER_CHAR_SET[position]);
        }
        
        return sb.toString();
    }
    
   
    /**
     * 랜덤 문자 생성
     * @param length        문자 수
     * @return              랜덤 문자
     */
    public static String create(int length) {
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0, position = 0; i < length; i++) {
            position = (int) (CHAR_SET.length * Math.random());
            
            sb.append(CHAR_SET[position]);
        }
        
        return sb.toString();
    }
    
    
    /**
     * UUID 생성
     * @return              UUID
     */
    public static String createUUID() {
        return UUID.randomUUID().toString();
    }
}
