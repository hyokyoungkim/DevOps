/*
 *  Copyright 2020. S.O.X. All rights reserved
 */

package com.ithows.util.random;

import java.util.ArrayList;
import java.util.List;

/**
 * Class HangulParser
 * 
 * @author Roi Kim <S.O.X Co. Ltd.>
 */
public class HangulParser {
 private static final String TAG = HangulParser.class.getSimpleName();

  // First '가' : 0xAC00(44032), 끝 '힟' : 0xD79F(55199)
  private static final int FIRST_HANGUL = 44032;

  // 19 initial consonants
  private static final char[] CHOSUNG_LIST = {
      'ㄱ', 'ㄲ', 'ㄴ', 'ㄷ', 'ㄸ', 'ㄹ', 'ㅁ', 'ㅂ', 'ㅃ', 'ㅅ',
      'ㅆ', 'ㅇ', 'ㅈ', 'ㅉ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
  };

  private static int JUNGSUNG_COUNT = 21;

  // 21 vowels
  private static final char[] JUNGSUNG_LIST = {
      'ㅏ', 'ㅐ', 'ㅑ', 'ㅒ', 'ㅓ', 'ㅔ', 'ㅕ', 'ㅖ', 'ㅗ', 'ㅘ',
      'ㅙ', 'ㅚ', 'ㅛ', 'ㅜ', 'ㅝ', 'ㅞ', 'ㅟ', 'ㅠ', 'ㅡ', 'ㅢ',
      'ㅣ'
  };

  private static int JONGSUNG_COUNT = 28;

  // 28 consonants placed under a vowel(plus one empty character)
  private static final char[] JONGSUNG_LIST = {
      ' ', 'ㄱ', 'ㄲ', 'ㄳ', 'ㄴ', 'ㄵ', 'ㄶ', 'ㄷ', 'ㄹ', 'ㄺ',
      'ㄻ', 'ㄼ', 'ㄽ', 'ㄾ', 'ㄿ', 'ㅀ', 'ㅁ', 'ㅂ', 'ㅄ', 'ㅅ',
      'ㅆ', 'ㅇ', 'ㅈ', 'ㅊ', 'ㅋ', 'ㅌ', 'ㅍ', 'ㅎ'
  };

  public static List<String> disassemble(char hangul) throws HangulParserException {
    List<String> jasoList = new ArrayList<>();

    String hangulStr = String.valueOf(hangul);

    if (hangulStr.matches(".*[가-힣]+.*")) {
      int baseCode = hangulStr.charAt(0) - FIRST_HANGUL;

      final int chosungIndex = baseCode / (JONGSUNG_COUNT * JUNGSUNG_COUNT);
      jasoList.add(Character.toString(CHOSUNG_LIST[chosungIndex]));

      final int jungsungIndex = (baseCode - ((JONGSUNG_COUNT * JUNGSUNG_COUNT) * chosungIndex)) / JONGSUNG_COUNT;
      jasoList.add(Character.toString(JUNGSUNG_LIST[jungsungIndex]));

      final int jongsungIndex = (baseCode - ((JONGSUNG_COUNT * JUNGSUNG_COUNT) * chosungIndex) - (JONGSUNG_COUNT * jungsungIndex));
      if (jongsungIndex > 0) {
        jasoList.add(Character.toString(JONGSUNG_LIST[jongsungIndex]));
      }
    } else if (hangulStr.matches(".*[ㄱ-ㅎ]+.*")) {
      throw new HangulParserException("음절이 아닌 자음입니다");
    } else if (hangulStr.matches(".*[ㅏ-ㅣ]+.*")) {
      throw new HangulParserException("음절이 아닌 모음입니다");
    } else {
      throw new HangulParserException("한글이 아닙니다");
    }

    return jasoList;
  }

  public static List<String> disassemble(String hangul) throws HangulParserException {
    List<String> jasoList = new ArrayList<String>();

    for (int i = 0, li = hangul.length(); i < li; i++) {
      try {
        jasoList.addAll(disassemble(hangul.charAt(i)));
      } catch (HangulParserException e) {
        throw new HangulParserException((i+1) + "번째 글자 분리 오류 : " + e.getMessage());
      }
    }

    return jasoList;
  }
  
  public static String disassembleString(String hangul) throws HangulParserException {
    List<String> jasoList = disassemble(hangul);
    String result ="";
    for(String str : jasoList){
        result += str;
    }
    
    return result;
  }

  public static String assemble(List<String> jasoList) throws HangulParserException {
    if (jasoList.size() > 0) {
      String result = "";
      int startIdx = 0;

      while (true) {
        if(startIdx < jasoList.size()) {
          final int assembleSize = getNextAssembleSize(jasoList, startIdx);
          result += assemble(jasoList, startIdx, assembleSize);
          startIdx += assembleSize;
        } else {
          break;
        }
      }

      return result;
    } else {
      throw new HangulParserException("자소가 없습니다");
    }
  }

  private static String assemble(List<String> jasoList, final int startIdx, final int assembleSize) throws HangulParserException {
    int unicode = FIRST_HANGUL;

    final int chosungIndex = new String(CHOSUNG_LIST).indexOf(jasoList.get(startIdx));

    if (chosungIndex >= 0) {
      unicode += JONGSUNG_COUNT * JUNGSUNG_COUNT * chosungIndex;
    } else {
      throw new HangulParserException((startIdx + 1) + "번째 자소가 한글 초성이 아닙니다");
    }

    final int jungsungIndex = new String(JUNGSUNG_LIST).indexOf(jasoList.get(startIdx + 1));

    if(jungsungIndex >= 0) {
      unicode += JONGSUNG_COUNT * jungsungIndex;
    } else {
      throw new HangulParserException((startIdx + 2) + "번째 자소가 한글 중성이 아닙니다");
    }

    if (assembleSize > 2) {
      final int jongsungIndex = new String(JONGSUNG_LIST).indexOf(jasoList.get(startIdx + 2));

      if (jongsungIndex >= 0) {
        unicode += jongsungIndex;
      } else {
        throw new HangulParserException((startIdx + 3) + "번째 자소가 한글 종성이 아닙니다");
      }
    }

    return Character.toString((char) unicode);
  }

  private static int getNextAssembleSize(List<String> jasoList, final int startIdx) throws HangulParserException {
    final int remainJasoLength = jasoList.size() - startIdx;
    final int assembleSize;

    if (remainJasoLength > 3) {
      if (new String(JUNGSUNG_LIST).contains(jasoList.get(startIdx + 3))) {
        assembleSize = 2;
      } else {
        assembleSize = 3;
      }
    } else if(remainJasoLength == 3 || remainJasoLength == 2) {
      assembleSize = remainJasoLength;
    } else {
      throw new HangulParserException("한글을 구성할 자소가 부족하거나 한글이 아닌 문자가 있습니다");
    }

    return assembleSize;
  }
}
