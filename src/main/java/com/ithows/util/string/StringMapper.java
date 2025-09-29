/*
 *  Copyright 2020. S.O.X. All rights reserved
 */
package com.ithows.util.string;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class StringMapper
 *
 * @author Roi Kim <S.O.X Co. Ltd.>
 */

public class StringMapper {

    public static final String MAPPING_PREFIX = "${";
    public static final String MAPPING_SUFFIX = "}";
    public static final String EMPTY_STRING = "";
    public static final String MAPPING_PATTERN = "(\\$\\{)([a-zA-Z0-9:;,.-]*)(})";

    static class Cursor {

        private int s;
        private int e;
        public String text;
        
        void setS(int s) {
            this.s = s;
        }

        void setE(int e) {
            this.e = e;
        }

        int getS() {
            return this.s;
        }

        int getE() {
            return this.e;
        }
    }

    private static Cursor find(String source, int offset) {
        int sid = source.indexOf(MAPPING_PREFIX, offset);
        int eid = source.indexOf(MAPPING_SUFFIX, offset);
        Cursor csr = null;
        csr = new Cursor();
        csr.setS(sid);
        csr.setE(eid);
        csr.text = "";
        if (sid >= 0 && eid >= 0) {
            csr.text = source.substring(sid+2, eid);
            System.out.println(csr.text);
        }
        return csr;
    }


    // 문자열중에서 매핑 키워드와 일반 텍스트를 구분하여 리턴 
    public static ArrayList<String> separateString(final String str) {
        ArrayList<String> partList = new ArrayList<String>();
        
        Pattern pattern = Pattern.compile(MAPPING_PATTERN);
        Matcher matcher = pattern.matcher(str);
        HashMap<Integer, String> partMap = null;
        String replaced = str;
        String part = "";
        int s = 0;
        int e = 0;    
        int offset = 0;
        
        while (matcher.find()) {
            String prefix = matcher.group(1);
            String key = matcher.group(2);
            String suffix = matcher.group(3);
            
            String value = "";
            String meta = (prefix + key + suffix).replace("$", "[$]").replace("{", "\\{").replace("}", "\\}" );
            replaced = replaced.replaceFirst(meta, value);
            
            int oLen = prefix.length() + key.length() + suffix.length();
            int oIndex = str.indexOf((prefix + key + suffix), offset);
            part = str.substring(offset, oIndex);
            
            if(offset < oIndex){
//                System.out.println("part = " + part + "   s = " + offset + "  e = " + ( offset < oIndex ? oIndex-1 : oIndex));
                partList.add(part);
            }
//            System.out.println("key = " + (prefix + key + suffix) + "  s = " + oIndex + " e = " + (oIndex + oLen) );
            partList.add(prefix + key + suffix);
            
            offset = oIndex + oLen;
        }
        
        if(offset < str.length()){
//            System.out.println("end = " +  str.substring(offset, str.length()));
            partList.add(str.substring(offset, str.length()));
        }
        return partList;
    }
    
    public static String doReplace(final String str, final Map<String, String> map) {
        int offset = 0;
        String replaced = str;
        while (true) {
            Cursor csr = find(replaced, offset);
            int s = csr.getS();
            int e = csr.getE();
            if (s < 0 || e < 0) {
                break;
            } else {
                String key = replaced.substring(s + MAPPING_PREFIX.length(), e);
                String value = (map.get(key) == null ? EMPTY_STRING : map.get(key));
                int vLen = value.length();
                int oLen = MAPPING_PREFIX.length() + key.length() + MAPPING_SUFFIX.length();
                int diff = vLen - oLen;
                String meta = (MAPPING_PREFIX + key + MAPPING_SUFFIX).replace("$", "[$]").replace("{", "\\{").replace("}", "\\}" );
                replaced = replaced.replaceFirst(meta, value);
//                replaced = replaced.replaceFirst("[$]\\{map1\\}", value);
                offset = e + diff;
            }
        }
        return replaced;
    }
    
    

    public static String doReplaceWithRegEx(final String str, final Map<String, String> map) {
        Pattern pattern = Pattern.compile(MAPPING_PATTERN);
        Matcher matcher = pattern.matcher(str);
        String replaced = str;
        while (matcher.find()) {
            String prefix = matcher.group(1);
            String key = matcher.group(2);
            String suffix = matcher.group(3);
            String value = (map.get(key) == null ? EMPTY_STRING : map.get(key));
            String meta = (prefix + key + suffix).replace("$", "[$]").replace("{", "\\{").replace("}", "\\}" );
            replaced = replaced.replaceFirst(meta, value);
            // replaced = replaced.replace(prefix + key + suffix, value);
        }
        return replaced;
    }

    
    public static void main(String[] args) {
        
        Map<String, String> map = new HashMap<>();
//        map.put("animal", "quick brown fox");
//        map.put("target", "lazy dog");
//        String str = "The ${animal} jumped over the ${target}.";

//        map.put("map1", "123123");
//        map.put("int;0;111", "234234");
//        map.put("123qweasdzxc", "345345");
//        String doReplacedRegEx = StringMapper.doReplaceWithRegEx(str, map);
//        System.out.println(doReplacedRegEx);

        String str = "${date;2020-04-19;2020-04-20}  ${time;10:00;11:00}";
//        String str = "0123456789${map1}가가가가 " +
//            "${map1}22222" +
//            "${int;0;111}나나나나" +
//            "_${int;0;111}" +
//            "${nomapping} << nomapping 없는 부분${int;0;111}" ;

        System.out.println(str);
        
        ArrayList<String> partList = StringMapper.separateString(str);
        for(String part : partList){
            System.out.println(part);
        }
        
    }
}