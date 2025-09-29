/*
 *  Copyright 2020. S.O.X. All rights reserved
 */

package com.ithows.util.random;

import com.ithows.JdbcDao;
import static com.ithows.util.RandomDataMaker.createRandomInt;
import static com.ithows.util.RandomDataMaker.createRandomString;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.Normalizer;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.binary.Hex;
import org.apache.poi.xddf.usermodel.chart.RadarStyle;


/**
 * Class KoreaNameMaker
 * 
 * @author Roi Kim <S.O.X Co. Ltd.>
 */
public class KoreaNameMaker {

    public static void main(String[] args) {
        System.out.println(randomHangulString());
        System.out.println(randomHangulName());
        String han = "한";
        System.out.println("" + devideJaSo(han));

    }
    
    public static String randomHangulString() {
        Random rand = new Random();
        int textLength = rand.nextInt(10) + 1;
        char[] textChars = new char[textLength];
        for (int i = 0; i < textLength; i++) {
            char ch = (char) ((Math.random() * 11172) + 0xAC00);
            if(isKSC5601Char(ch)){
                textChars[i] = ch;
            }else{
                i--;
            }
        }
        return String.valueOf(textChars);
    }
    
    public static String randomCity() {
        String[] cityName = {"김", "이", "박", "최", "정", "강", "조", "윤", "장", "임", "한", "오", "서", "신", "권", "황", "안",
            "송", "류", "전", "홍", "고", "문", "양", "손", "배", "조", "백", "허", "유", "남", "심", "노", "정", "하", "곽", "성", "차", "주",
            "우", "구", "신", "임", "나", "전", "민", "육", "진", "지", "엄", "채", "원", "천", "방", "공", "강", "현", "함", "변", "염", "양",
            "변", "여", "추", "노", "도", "소", "신", "석", "선", "설", "마", "길", "주", "연", "방", "위", "표", "명", "기", "반", "왕", "금",
            "옥", "육", "인", "맹", "제", "모", "장", "남", "탁", "국", "여", "진", "어", "은", "편", "구", "용"};
        
        return randomSelect(cityName);
    }

    // 스트링 배열을 넣어주면 거기서 하나를 골라 리턴
    private static String randomSelect(String[] names) {
        Random rand = new Random();
        int cnt = rand.nextInt(names.length);
        return names[cnt];
    }
    
    public static String randomHangulName() {
        String result = "";        
        int sungIdx = createRandomInt(1, 118);
        int nameIdx = createRandomInt(1, 2800);
        
        String sql = "Select sung from kname " +
                "where id=? ;" ;
        String sql2 = "Select name from kname " +
                "where id=? ;" ;
                
        try {
            result += JdbcDao.queryForString(sql, new Object[]{sungIdx});
            result += JdbcDao.queryForString(sql2, new Object[]{nameIdx});
            
        } catch (Exception e) {
            result = randomHangulName2();
        }     
        
        return result;
    }
    
    public static String randomHangulName2() {
        List<String> secondName = Arrays.asList("김", "이", "박", "최", "정", "강", "조", "윤", "장", "임", "한", "오", "서", "신", "권", "황", "안",
            "송", "류", "전", "홍", "고", "문", "양", "손", "배", "조", "백", "허", "유", "남", "심", "노", "정", "하", "곽", "성", "차", "주",
            "우", "구", "신", "임", "나", "전", "민", "육", "진", "지", "엄", "채", "원", "천", "방", "공", "강", "현", "함", "변", "염", "양",
            "변", "여", "추", "노", "도", "소", "신", "석", "선", "설", "마", "길", "주", "연", "방", "위", "표", "명", "기", "반", "왕", "금",
            "옥", "육", "인", "맹", "제", "모", "장", "남", "탁", "국", "여", "진", "어", "은", "편", "구", "용", "제갈", "남궁", "선우");
        
        List<String> firstName1 = Arrays.asList("가", "강", "건", "경", "고", "관", "광", "구", "규", "근", "기", "길", "나", "남", "노", "난", 
            "단", "달", "담", "대", "덕", "도", "동", "두", "라", "마", "만", "명", "무", "문", "미", "민", 
            "백", "범", "병", "보", "사", "산", "상", "새", "서", "석", "선", "설", "성", "세", "소", "솔", "수", "숙", "순",
            "슬", "승", "시", "신", "아", "안", "애", "엄", "여", "연", "영", "예", "오", "옥", "완", "용", "우", "원", "월", "위",
            "유", "윤", "율", "은", "의", "이", "익", "인", "일", "잎", "자", "잔", "장", "재", "전", "정", "제", "조", "종", "주", "준",
            "중", "지", "진", "찬", "창", "천", "철", "초", "춘", "치", "태", "택", "판", "하", "한", "해", "혁", "현", "형",
            "혜", "호", "홍", "화", "환", "효", "훈", "희", "운", "배", "부", "봉", "황", "공", "온", "항", "견", "삼",
            "열", "분", "양", "손", "술", "반", "빈", "실", "직", "란",
            "복", "심", "헌", "향", " ");

        List<String> firstName2 = Arrays.asList("가", "강", "건", "경", "고", "관", "광", "구", "규", "근", "길", "나", "남",  
            "단", "달", "담", "대", "덕", "도", "동", "두", "라", "래", "리", "마", "만", "명", "무", "문", "미", "민", 
            "백", "범", "별", "병", "보", "빛", "사", "산", "상", "새", "서", "석", "선", "설", "섭", "성", "세", "소", "솔", "수", "숙", "순",
            "숭", "슬", "승", "신", "아", "안", "애", "연", "영", "예",  "옥", "완", "요", "용", "우", "원", "월", "위",
            "유", "윤", "율", "은", "의", "이", "익", "인", "일", "잎", "자", "잔", "장", "재", "전", "정", "제", "조", "종", "주", "준",
            "중", "지", "진", "찬", "창", "채", "천", "철", "초", "춘", "치", "태", "택", "판", "하", "한", "해", "혁", "현", "형",
            "혜", "호", "홍", "화", "환", "회", "효", "훈", "휘", "희", "운", "모", "배", "부", "림", "봉", "황", "린", "을", "비",
            "솜", "공", "면", "탁", "온", "디", "항", "후", "려", "균", "묵", "송", "욱", "언", "령", "들", "견", "추", "걸", "삼",
            "열", "웅", "분", "변", "양", "타", "겸", "곤", "번", "식", "란", "더", "손", "술", "반", "빈", "실", "직", "흠",
            "악", "람", "권", "복", "심", "헌", "엽", "학", "평", "랑", "향", "련");
        
        Collections.shuffle(secondName);
        Collections.shuffle(firstName1);
        Collections.shuffle(firstName2);
        
        String result = "";
        Random rand = new Random();
        int cnt = rand.nextInt(7);
        if(cnt == 0){
            result = secondName.get(0) + firstName2.get(0);
        }else{
            result = secondName.get(0) + firstName1.get(0) + firstName2.get(0);
        }
        
        return result;
    }

    
    private static String getUnicodeNormalizeCode(String string) {
        String result ="";
        for (int i = 0; i < string.length(); i++) {
            result += String.format("U+%04X ", string.codePointAt(i));
        }
        return result;
    }
    
    private static String devideJaSo(String string) {
        
        String result = "";
        try {
            result = HangulParser.disassembleString(string);
        } catch (HangulParserException ex) {
            Logger.getLogger(KoreaNameMaker.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return result;
    }

    public static void test() {
        String han = "한";
        
        String nfd = Normalizer.normalize(han, Normalizer.Form.NFD);
        System.out.println(nfd);
        
        String nfc = Normalizer.normalize(nfd, Normalizer.Form.NFC);
        System.out.println(nfc);
    }

    public static final Charset CHARSET_EUC_KR = Charset.forName("EUC-KR");
    public static final String KSC5601_START_HEX = "A1A1"; //EUC-KR 코드페이지 내에서 KSC5601의 시작코드
    public static final String KSC5601_END_HEX = "FEFE"; //EUC-KR 코드페이지 내에서 KSC5601의 종료코드
    public static final int KSC5601_START_INT = Integer.parseInt(KSC5601_START_HEX, 16);
    public static final int KSC5601_END_INT = Integer.parseInt(KSC5601_END_HEX, 16);
    
    // 완성형 한글만 구분
    public static boolean isKSC5601Char(char c){
	String eachCharString = String.valueOf(c);
	String hex = null;
	try{
		hex = Hex.encodeHexString(eachCharString.getBytes(CHARSET_EUC_KR));
	}catch(Exception e){
                System.out.println("isKSC5601Char() > "+c+" 를 16진수로 변환 중 오류 발생");
	}
	int decimal = Integer.parseInt(hex, 16);
	boolean isValid = decimal >= KSC5601_START_INT && decimal <= KSC5601_END_INT;
	return isValid;
    }
}
