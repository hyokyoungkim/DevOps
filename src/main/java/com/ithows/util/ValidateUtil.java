/*
 *  Copyright 2020. S.O.X. All rights reserved
 */

package com.ithows.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Class ValidateUtil
 * 
 * @author Roi Kim <S.O.X Co. Ltd.>
 */
public class ValidateUtil {
    
    final static private String pattern_alpha = "^[A-Za-z]*$";
    final static private String pattern_numeric = "^[0-9]*$";
    final static private String pattern_korean = "^[ㄱ-ㅎ|가-힣]*$";
    final static private String pattern_special = "^[~!@#\\$\\%\\^\\&\\*`\\(\\)\\+\\-_=\\\\:;<>,\\.\\?/\\|\\[\\]\\{\\}'\"]*$";
    final static private String pattern_email = "^[-A-Za-z0-9_]+[-A-Za-z0-9_.]*[@]{1}[-A-Za-z0-9_]+[-A-Za-z0-9_.]*[.]{1}[A-Za-z]{2,5}$";
    final static private String pattern_url = "^(https?)://[-a-zA-Z0-9+&@#/%?=~_|!:,.;]*[-a-zA-Z0-9+&@#/%=~_|]";
    final static private String pattern_gis = "^[0-9]+\\.[0-9]+$";
    final static private String pattern_valid_seo = "^[0-9|A-Z|a-z|\\/|\\-]*$";
    final static private String pattern_invalid_seo = ".*?[\\s~!@#\\$\\%\\^\\&\\*`\\(\\)\\+_=\\\\:;<>,\\.\\?\\|\\[\\]\\{\\}'\"].*?";
    final static private String pattern_invalid_id = "[a-z0-9]{5,25}$";
    final static private String pattern_invalid_password = "^(?=.*?[a-zA-Z])(?=.*?[0-9])(?=.*?[#?!@$%^&*-]).{8,25}$";


    /**
     * Check Regular Expression
     * @param patternIndex        pattern index
     * @param value                        value
     * @return
     */
    private static boolean patternChecker(int patternIndex, String value) {
            Integer pi = new Integer(patternIndex);
            if ((pi == null) || (pi < 1)) {
                    return false;
            }
            //set pattern
            String pattern = "";
            switch (pi) {
                    case 1:
                            pattern = pattern_alpha;
                            break;
                    case 2:
                            pattern = pattern_numeric;
                            break;
                    case 3:
                            pattern = pattern_korean;
                            break;
                    case 4:
                            pattern = pattern_special;
                            break;
                    case 5:
                            pattern = pattern_email;
                            break;
                    case 6:
                            pattern = pattern_url;
                            break;
                    case 7:
                            pattern = pattern_gis;
                            break;
                    case 8:
                            pattern = pattern_valid_seo;
                            break;
                    case 9:
                            pattern = pattern_invalid_id;
                            break;
                    case 10:
                            pattern = pattern_invalid_password;
                            break;
            }
            Pattern p = Pattern.compile(pattern);
            Matcher m = p.matcher(value);
            return m.matches();
    }

    /**
     * check NULL
     * @param value
     * @return
     */
    private static boolean nullChecker(String value) {
            return CollectionUtils.isEmpty(value);
    }


    /**
     * check char Type
     * @param patternIndex
     * @param value
     * @return
     */
    private static boolean charTypeChecker(int patternIndex, String value) {
            if (!nullChecker(value)) {
                    return patternChecker(patternIndex, value);
            } else {
                    return false;
            }
    }




    /**
     * check alphabet
     * @param value
     * @return
     */
    public static boolean isAlpha(String value) {
            return charTypeChecker(1, value);
    }
    /**
     * check numeric
     * @param value
     * @return
     */
    public static boolean isNumeric(String value) {
            return charTypeChecker(2, value);
    }
    /**
     * check korean character
     * @param value
     * @return
     */
    public static boolean isKorean(String value) {
            return charTypeChecker(3, value);
    }
    /**
     * check special character
     * @param value
     * @return
     */
    public static boolean isSpecial(String value) {
            return charTypeChecker(4, value);
    }


    /**
     * check alphabet & numeric
     * @param value
     * @return
     */
    public static boolean isAlphaNumeric(String value) {
            if (!nullChecker(value)) {
                    boolean bReturn = true;
                    char[] cArr = value.toCharArray();
                    int alphaLen = 0;
                    int numericLen = 0;
                    for (char c : cArr) {
                            if (isAlpha(String.valueOf(c))) {
                                    alphaLen++;
                            }
                            if (isNumeric(String.valueOf(c))) {
                                    numericLen++;
                            }
                    }
                    if ((alphaLen == 0) || (numericLen == 0) || (cArr.length != (alphaLen + numericLen))){
                            bReturn = false;
                    }
                    return bReturn;
            } else {
                    return false;
            }
    }


    /**
     * check alphabet & numeric & special character
     * @param value
     * @return
     */
    public static boolean isAlphaNumericSpecial(String value) {
            if (!nullChecker(value)) {
                    boolean bReturn = true;
                    char[] cArr = value.toCharArray();
                    int alphaLen = 0;
                    int numericLen = 0;
                    int specialLen = 0;
                    for (char c : cArr) {
                            if (isAlpha(String.valueOf(c))) {
                                    alphaLen++;
                            }
                            if (isNumeric(String.valueOf(c))) {
                                    numericLen++;
                            }
                            if (isSpecial(String.valueOf(c))) {
                                    specialLen++;
                            }
                    }
                    if ((alphaLen == 0) || (numericLen == 0) || (specialLen == 0) || (cArr.length != (alphaLen + numericLen + specialLen))){
                            bReturn = false;
                    }
                    return bReturn;
            } else {
                    return false;
            }
    }


    /**
     * check Email
     * @param value
     * @return
     */
    public static boolean isEmail(String value) {
            return charTypeChecker(5, value);
    }

    /**
     * check URL
     * @param value
     * @return
     */
    public static boolean isUrl(String value) {
            return charTypeChecker(6, value);
    }

    /**
     * check GIS
     * @param value
     * @return
     */
    public static boolean isGis(String value) {
            return charTypeChecker(7, value);
    }

    /**
     * check Seo
     * @param value
     * @return
     */
    public static boolean isValidSeo(String value) {
            return charTypeChecker(8, value);
    }

    /**
     * check invalid Seo
     * @param value
     * @return
     */
    public static boolean isInValidSeo(String value) {
            if (!nullChecker(value)) {
                    Pattern p = Pattern.compile(pattern_invalid_seo);
                    Matcher m = p.matcher(value);
                    return m.lookingAt();
            } else {
                    return false;
            }
    }


    /**
     * check id check
     * 5~25자리, 영문소문자, 숫자, 특수문자만 가능
     * @param value
     * @return
     */
    public static boolean isValidId(String value){
            return charTypeChecker(9, value);
    }


    /**
     * check password check
     * 8~25자리, 알파벳, 숫자, 특수문자로 구성된 문자열
     * @param value
     * @return
     */
    public static boolean isValidPassword(String value){
            return charTypeChecker(10, value);
    }


    // IP 어드레스 형식 확인 
    public static boolean isValidIP(final String s) {
        final String IPV4_REGEX = "(([0-1]?[0-9]{1,2}\\.)|(2[0-4][0-9]\\.)|(25[0-5]\\.)){3}(([0-1]?[0-9]{1,2})|(2[0-4][0-9])|(25[0-5]))";
        Pattern IPV4_PATTERN = Pattern.compile(IPV4_REGEX);
        return IPV4_PATTERN.matcher(s).matches();
    }        
}
