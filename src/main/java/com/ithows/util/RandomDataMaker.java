package com.ithows.util;


import com.ithows.util.random.KoreaNameMaker;
import com.ithows.util.random.NameDBManager;
import com.ithows.util.string.StringMapper;
import com.ithows.util.string.Tools;
import com.ithows.util.string.UtilString;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.math3.random.MersenneTwister;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class RandomDataMaker
 *
 * @author Roi Kim <S.O.X Co. Ltd.>
 */
public class RandomDataMaker {

    public static int DATATYPE_NULL = 0;
    public static int DATATYPE_INT = 1;
    public static int DATATYPE_DOUBLE = 2;
    public static int DATATYPE_BOOLEAN = 3;
    public static int DATATYPE_SEQ = 4;
    public static int DATATYPE_NORMALTEXT = 10;
    public static int DATATYPE_NAMETEXT = 11;
    public static int DATATYPE_UUID = 12;
    public static int DATATYPE_KEY = 13;
    public static int DATATYPE_USERNAME = 14;   // 유저가 지정한 텍스트 중 고르기
    public static int DATATYPE_FORMTEXT = 15;
    public static int DATATYPE_KNAMETEXT = 16;   // 한글 이름
    public static int DATATYPE_KADDRESS = 17;   // 한글 주소
    public static int DATATYPE_ENAMETEXT = 18;   // 영어 이름
    public static int DATATYPE_EADDRESS = 19;   // 영어 주소
    public static int DATATYPE_DATETIME = 20;
    public static int DATATYPE_KCOMPANY = 110;
    public static int DATATYPE_ECOMPANY = 111;
    public static int DATATYPE_TIME = 21;
    public static int DATATYPE_DATE = 22;
    public static int DATATYPE_COLOR = 30;
    public static int DATATYPE_COORD = 40;
    public static int DATATYPE_JSON = 4;
    public static int DATATYPE_IP = 51;
    public static int DATATYPE_EMAIL = 52;
    public static int DATATYPE_URL = 53;
    public static int DATATYPE_MACADDRESS = 54;
    public static int DATATYPE_DOMAIN = 55;

    public static String DATATYPESTRING_NULL = "$null";
    public static String DATATYPESTRING_INT = "$int";
    public static String DATATYPESTRING_DOUBLE = "$double";
    public static String DATATYPESTRING_BOOLEAN = "$boolean";
    public static String DATATYPESTRING_SEQ = "$seq";
    public static String DATATYPESTRING_NORMALTEXT = "$text";
    public static String DATATYPESTRING_NAMETEXT = "$name";
    public static String DATATYPESTRING_KNAMETEXT = "$kname";
    public static String DATATYPESTRING_ENAMETEXT = "$ename";
    public static String DATATYPESTRING_USERNAME = "$username";
    public static String DATATYPESTRING_UUID = "$uuid";
    public static String DATATYPESTRING_KEY = "$key";
    public static String DATATYPESTRING_FORMTEXT = "$formtext";
    public static String DATATYPESTRING_KADDRESS = "$kaddress";
    public static String DATATYPESTRING_EADDRESS = "$eaddress";
    public static String DATATYPESTRING_KCOMPANY = "$kcompany";
    public static String DATATYPESTRING_ECOMPANY = "$ecompany";
    public static String DATATYPESTRING_DATETIME = "$datetime";
    public static String DATATYPESTRING_DATE = "$date";
    public static String DATATYPESTRING_TIME = "$time";
    public static String DATATYPESTRING_COLOR = "$color";
    public static String DATATYPESTRING_COORD = "$coords";
    public static String DATATYPESTRING_JSON = "$json";
    public static String DATATYPESTRING_IP = "$ip";
    public static String DATATYPESTRING_EMAIL = "$email";
    public static String DATATYPESTRING_DOMAIN = "$domain";
    public static String DATATYPESTRING_URL = "$url";
    public static String DATATYPESTRING_MACADDRESS = "$mac";

    
    // - 주소 만들기 
    
    // - 일렬번호
    public static int seqNum = 0;
    public static int intervalNum = 1;

    private static int TOTAL = 62;
    private static char[] mapper = new char[]{
            48, 49, 50, 51, 52,
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

    public static int NAME_ENAME = 0;
    public static int NAME_HNAME = 1;
    public static int NAME_CNAME = 2;
    public static int NAME_PNAME = 3;
    public static int NAME_DOMAIN = 4;
    public static int NAME_COL = 10;
    private static String[][] nameMapper = new String[][]{
            {"Smith", "Tom", "Sam", "Alex", "Paul", "Peter", "John", "Jackey" , "Owen", "Sarah"},
            {"김철수", "홍길동", "이명기", "감우선", "박말우", "이소영", "허난설", "이설", "하윤기" , "백상구"},
            {"Hyundai", "Samsung", "Google", "Facebook", "Yahoo", "Microsoft", "SOX", "Logitec", "CJ", "Amazon"},
            {"Office", "Windows", "Linux", "VisualStudio", "Oracle", "Mysql", "Keynote", "Chrome", "Photoshop", "AutoCAD"},
            {"officehelf.com", "widowsokk.com", "limuxalleh.com", "visulsttud.com", "oraklle.com", "misqldot.com", "ckeynothole.com", "khrometic.com", "phootuship.com", "aotucado.com"}
    };



    
    static{
      
        
    }
    
    private static MersenneTwister mtRandom = new MersenneTwister();  

    // Mersenne Twister Random
    private static double mersenneRandom(){
        return mtRandom.nextDouble();
    }
    
    // Gaussian Random
    private static double gaussianRandom(double avg, double div) {
        Random rnd = new Random();
        double value = (div * rnd.nextGaussian()) + avg ;
        return value;
    }
    
    // 스트링 배열을 넣어주면 거기서 하나를 골라 리턴
    private static String randomSelect(String[] names) {
        Random rand = new Random();
        int cnt = rand.nextInt(names.length);
        return names[cnt];
    }
    
    
    public static int createRandom4Number() {
        Random rand = new Random();
        int authNum = rand.nextInt(8999);  // 0~8999
        authNum += 1000; // 1000~ 9999 사이의 숫자 생성
        return authNum;
    }

    public static int createRandomInt(int startNum, int endNum) {
        Random rand = new Random();
        int authNum = rand.nextInt((endNum - startNum));
        authNum += startNum; // startNum ~ endNum 사이의 숫자 생성
        return authNum;
    }
    

    public static String createRandomHexInt(int startNum, int endNum, int hexdigit) {
        Random rand = new Random();
        int authNum = rand.nextInt((endNum - startNum));
        authNum += startNum; // startNum ~ endNum 사이의 숫자 생성
        return Tools.int2hex(authNum, hexdigit) ;
    }

    public static boolean createRandomBoolean() {
        Random rand = new Random();
        int authNum = rand.nextInt(2);
        return authNum == 0 ? false : true;
    }
    

    public static void setSequence(int seedNum, int interval) {
        seqNum = seedNum;
        intervalNum = interval;
    }
    
    public static int createSequenceInt() {
        seqNum += intervalNum;
        return seqNum;
    }


    public static String createRandomColor() {
        String color = "";
        Random rand = new Random();
        int r = rand.nextInt(254);
        int g = rand.nextInt(254);
        int b = rand.nextInt(254);
        color = "#" + Tools.int2hex(r, 2) + Tools.int2hex(g, 2) + Tools.int2hex(b, 2) ;
        return color;
    }


    // 임의의 긴 정수 생성
    public static long createRandomLong(long startNum, long endNum) {
        Random rand = new Random();
        long authNum = ThreadLocalRandom.current().nextLong(startNum, endNum);
        return authNum;
    }

    // 임의의 실수 생성
    public static double createRandomDouble(double startNum, double endNum) {
        Random rand = new Random();
        double authNum = startNum + (endNum - startNum) * rand.nextDouble();
        return authNum;
    }

    // 임의 이름 문자열 생성
    public static String createRandomName(int type) {

        if(type < 0 || type > 6){
            type = 0;
        }

        String result = "";
        int r = createRandomInt(0, 9);
        result += nameMapper[type][r]  ;
        return result;
    }

    public static String createRandomString(int charCount) {
        Random rand = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < charCount; i++) {
            int pos = rand.nextInt(TOTAL - 1);
            sb.append(mapper[pos]);
        }
        return sb.toString();
    }

    // 사용자가 입력한 텍스트 중에 임의로 골라서 리턴
    // 각 요소는 ','로 구분
    public static String createRandomUserName(String userText) {
        
        try{
            String[] arr = userText.split(",");
            Random rand = new Random();
            int authNum = createRandomInt(0, arr.length);
            return arr[authNum];
        }catch(Exception ex){
            
        }
        
        return "";
    }
    
    
    public static String createKoreaPeopleName() {
         return KoreaNameMaker.randomHangulName();
    }
    
    public static String createKoreaAddress(String regcode) {
        String result = "";
        result += NameDBManager.getKoreaAddress(regcode);
        
        return result;
    }
    
    public static String createKoreaCompany() {
        
        String result = "";
        result += NameDBManager.getKName("company");
        
        return result;
    }

    
    public static String createEnglishPeopleName() {
        
        String result = "";
        result += NameDBManager.getEName("firstname");
        result += NameDBManager.getEName("surname");
        
        return result;
    }

    public static String createEnglishAddress() {
        
        String result = "";
        result += NameDBManager.getEName("countryUS");
        
        return result;
    }
    
    public static String createEnglishCompany() {
        
        String result = "";
        result += NameDBManager.getEName("company");
        
        return result;
    }


    
    public static String createDomainName() {
        String result = "";
        String[] domainPostfix= {"com", "co.kr", "kr", "net", "org", "or.kr","cloud", "online", "site"
                    , "icu","cn", "jp", "io", "vn", "ai", "me", "re.kr", "go.kr", "tech", "email", "info", "tv", "club"
                    , "coffee", "work", "blog", "show", "service", "sale"};
        
        int idx = createRandomInt(1, 168);
        
        result += NameDBManager.getEName("company");
        result += "." + randomSelect(domainPostfix);

        return result;
    }
    
    
    // 좌표 생성
    public static double[] createRandomCoords(double minx, double maxx, double miny, double maxy) {
        double[] coord = new double[2];
        coord[0] = createRandomDouble(minx, maxx);
        coord[1] = createRandomDouble(miny, maxy);

        return coord;
    }

    // 기간을 주면 임의의 날짜 및 시간 생성
    // dFormat을 변경하면 임의의 날짜만 생성할 수 있음
    public static String createRandomDate(String fromDate, String endDate, String dFormat) {

        if(dFormat.equals("")){
            dFormat = "yyyy-MM-dd HH:mm:ss";
        }
        Date sDate = DateTimeUtils.getStringToDate(fromDate , dFormat);
        Date eDate = DateTimeUtils.getStringToDate(endDate , dFormat);
        long sTime = sDate.getTime();
        long eTime = eDate.getTime();
        long rTime = sTime;
        if(sTime <= eTime){
            rTime = createRandomLong(sTime, eTime);
        }else{
            rTime = createRandomLong(eTime, sTime);

        }
        return DateTimeUtils.getDateTimeToString(new Date(rTime), dFormat);
    }
    
    public static String createRandomTime(String fromTime, String toTime) {

        LocalTime startTime = LocalTime.parse(fromTime);
        LocalTime endTime = LocalTime.parse(toTime);
        
        int startSeconds = startTime.toSecondOfDay();
        int endSeconds = endTime.toSecondOfDay();
        int randomTime = ThreadLocalRandom
                .current()
                .nextInt(startSeconds, endSeconds);

        return LocalTime.ofSecondOfDay(randomTime).toString();
       
    }
    
    // 형식 텍스트 
    public static String createRandomFormText(String formStr) {
        String formText = "";
        
        ArrayList<String> partList = StringMapper.separateString(formStr);
        for(String part : partList){
            String partStr = genRandomDataFormText(part);    
            formText += partStr;
        }

        return formText;

    }

    // formtext가 쓰는 메소드
    private static String genRandomDataFormText(String elementStr){
        
        String dataTypeStr =  elementStr;
        dataTypeStr = elementStr.replace("{", "");
        dataTypeStr = dataTypeStr.replace("}", "");
        
        String[] dParam = dataTypeStr.split(";");
        String result = "";
        
        int dtypeNum = RandomDataMaker.getTypeNum(dParam[0]);
        if(dtypeNum == -1){
            return elementStr;
        }else if(dtypeNum == RandomDataMaker.DATATYPE_FORMTEXT ){
            dParam = dataTypeStr.split(";",2);
        }

        // 구조에 따른 변경 
        if(dParam.length == 1){
            result = RandomDataMaker.genRandomData(dtypeNum, "", "", "", "");
        }else if(dParam.length == 3){
            result = RandomDataMaker.genRandomData(dtypeNum, dParam[1], dParam[2], "", "");
        }else if(dParam.length == 2){
            result = RandomDataMaker.genRandomData(dtypeNum, dParam[1], "", "", "");
        }else if(dParam.length == 5){
            result = RandomDataMaker.genRandomData(dtypeNum, dParam[1], dParam[2], dParam[3], dParam[4]);
        }else if(dParam.length == 4){
            result = RandomDataMaker.genRandomData(dtypeNum, dParam[1], dParam[2], dParam[3], "");
        }else{
            dParam = dataTypeStr.split(";",5);
            result = RandomDataMaker.genRandomData(dtypeNum, dParam[1], dParam[2], dParam[3], dParam[4]);
        }
        
        return result;
    }


    // 문자열을 그냥 변경
    public static String createKey(String keys){
        return KeyGenerator.getKey(keys.toCharArray(), 1189);
    }

    // 숫자를 랜덤으로 만들고 문자열로 변환
    public static String createKey(int type){

        int r = createRandomInt(100000, 999999);
        String str = String.valueOf(r);
        return KeyGenerator.getClientKey(str.toCharArray(), type);
    }

    public static String createUUID(){
        return KeyGenerator.makeGuid();
    }


    public static String createRandomEmail() {
        String result = "";

        Random rand = new Random();
        int num = rand.nextInt(4) + 4;

        result += createRandomString(num);
        result += "@" + createDomainName();

        return result;
    }


    public static String createRandomUrl() {
        String result = "http://";

        Random rand = new Random();
        int num = rand.nextInt(9);

        result += createRandomString(6) ;
        result += "." + createRandomName(NAME_DOMAIN);

        return result;
    }

    public static String createRandomIP(String subNet) {
        String result = "";
        
        String[] part = subNet.split("\\.");
        Random rand = new Random();
        int authNum = rand.nextInt(254);

        if(subNet.equals("") || part == null || part.length == 0){
            authNum += 1;
            result += authNum + ".";
            authNum = rand.nextInt(254);
            authNum += 1;
            result += authNum + ".";
            authNum = rand.nextInt(254);
            authNum += 1;
            result += authNum + ".";
            authNum = rand.nextInt(254);
            authNum += 1;
            result += authNum ;
        }else if(part.length == 1){
            if(UtilString.isValidNumeric(part[0]) == 1){
                result += part[0] + ".";
                authNum = rand.nextInt(254);
                authNum += 1;
                result += authNum + ".";
                authNum = rand.nextInt(254);
                authNum += 1;
                result += authNum + ".";
                authNum = rand.nextInt(254);
                authNum += 1;
                result += authNum ;
            }else{
                authNum += 1;
                result += authNum + ".";
                authNum = rand.nextInt(254);
                authNum += 1;
                result += authNum + ".";
                authNum = rand.nextInt(254);
                authNum += 1;
                result += authNum + ".";
                authNum = rand.nextInt(254);
                authNum += 1;
                result += authNum ;
            }
        }else{
            int i=0;
            for(i=0; i<part.length; i++){
                result += part[i] + ".";
            }
            for(;i<4; i++){
                authNum = rand.nextInt(254);
                authNum += 1;
                result += authNum + ".";
            }
            result = UtilString.trimString(result, 1, false);
        }


        return result;
    }

    public static String createRandomMacAddress() {
        String result = "";

        Random rand = new Random();
        int t1 = rand.nextInt(254);
        int t2 = rand.nextInt(254);
        int t3 = rand.nextInt(254);
        int t4 = rand.nextInt(254);
        int t5 = rand.nextInt(254);
        int t6 = rand.nextInt(254);
        result = "" + Tools.int2hex(t1, 2) + ":" + Tools.int2hex(t2, 2) + ":" + Tools.int2hex(t3, 2) + ":" + Tools.int2hex(t4, 2) + 
                ":" + Tools.int2hex(t5, 2) + ":" + Tools.int2hex(t6, 2) ;
        
        return result.toLowerCase();
    }

    ///////////////////////////////////////////////////////////////////////////////////

    public static int getTypeNum(String typeStr){

        if(typeStr.equals("null") || typeStr.equals(DATATYPESTRING_NULL) ){
            return DATATYPE_NULL;
        }else if(typeStr.equals("int") || typeStr.equals(DATATYPESTRING_INT) ){
            return DATATYPE_INT;
        }else if(typeStr.equals("double") || typeStr.equals(DATATYPESTRING_DOUBLE) ){
            return DATATYPE_DOUBLE;
        }else if(typeStr.equals("boolean") || typeStr.equals(DATATYPESTRING_BOOLEAN) ){
            return DATATYPE_BOOLEAN;
        }else if(typeStr.equals("seq") || typeStr.equals(DATATYPESTRING_SEQ) ){
            return DATATYPE_SEQ;
        }else if(typeStr.equals("text") || typeStr.equals(DATATYPESTRING_NORMALTEXT) ){
            return DATATYPE_NORMALTEXT;
        }else if(typeStr.equals("name") || typeStr.equals(DATATYPESTRING_NAMETEXT) ){
            return DATATYPE_NAMETEXT;
        }else if(typeStr.equals("kname") || typeStr.equals(DATATYPESTRING_KNAMETEXT) ){
            return DATATYPE_KNAMETEXT;
        }else if(typeStr.equals("ename") || typeStr.equals(DATATYPESTRING_ENAMETEXT) ){
            return DATATYPE_ENAMETEXT;
        }else if(typeStr.equals("uuid") || typeStr.equals(DATATYPESTRING_UUID) ){
            return DATATYPE_UUID;
        }else if(typeStr.equals("key") || typeStr.equals(DATATYPESTRING_KEY) ){
            return DATATYPE_KEY;
        }else if(typeStr.equals("username") || typeStr.equals(DATATYPESTRING_USERNAME) ){
            return DATATYPE_USERNAME;
        }else if(typeStr.equals("formtext") || typeStr.equals(DATATYPESTRING_FORMTEXT) ){
            return DATATYPE_FORMTEXT;
        }else if(typeStr.equals("kaddress") || typeStr.equals(DATATYPESTRING_KADDRESS) ){
            return DATATYPE_KADDRESS;
        }else if(typeStr.equals("eaddress") || typeStr.equals(DATATYPESTRING_EADDRESS) ){
            return DATATYPE_EADDRESS;
        }else if(typeStr.equals("kcompany") || typeStr.equals(DATATYPESTRING_KCOMPANY) ){
            return DATATYPE_KCOMPANY;
        }else if(typeStr.equals("ecompany") || typeStr.equals(DATATYPESTRING_ECOMPANY) ){
            return DATATYPE_ECOMPANY;
        }else if(typeStr.equals("datetime") || typeStr.equals(DATATYPESTRING_DATETIME) ){
            return DATATYPE_DATETIME;
        }else if(typeStr.equals("date") || typeStr.equals(DATATYPESTRING_DATE) ){
            return DATATYPE_DATE;
        }else if(typeStr.equals("time") || typeStr.equals(DATATYPESTRING_TIME) ){
            return DATATYPE_TIME;
        }else if(typeStr.equals("color") || typeStr.equals(DATATYPESTRING_COLOR) ){
            return DATATYPE_COLOR;
        }else if(typeStr.equals("ip") || typeStr.equals(DATATYPESTRING_IP) ){
            return DATATYPE_IP;
        }else if(typeStr.equals("email") || typeStr.equals(DATATYPESTRING_EMAIL) ){
            return DATATYPE_EMAIL;
        }else if(typeStr.equals("url") || typeStr.equals(DATATYPESTRING_URL) ){
            return DATATYPE_URL;
        }else if(typeStr.equals("coords") || typeStr.equals(DATATYPESTRING_COORD) ){
            return DATATYPE_COORD;
        }else if(typeStr.equals("json") || typeStr.equals(DATATYPESTRING_JSON) ){
            return DATATYPE_JSON;
        }else if(typeStr.equals("mac") || typeStr.equals(DATATYPESTRING_MACADDRESS) ){
            return DATATYPE_MACADDRESS;
        }else if(typeStr.equals("domain") || typeStr.equals(DATATYPESTRING_DOMAIN) ){
            return DATATYPE_DOMAIN;
        }

        return -1;
    }
    


    // @@ 타입 별로 임의 값 만들어 리턴
    public static String genRandomData(int type, String val1, String val2, String val3, String val4){
        String dataStr = "";

        if(type == DATATYPE_NULL){
            dataStr = "null";
        }else if(type == DATATYPE_INT){
            int sValue = -10000;
            int eValue = 10000;
            if(!val1.equals("") && !val2.equals("") ){
                sValue = Integer.parseInt(val1);
                eValue = Integer.parseInt(val2);
                if(sValue > eValue){
                    int tmp = sValue;
                    sValue = eValue;
                    eValue = tmp;
                }
            }
            
            if(!val3.equals("") && val3.equals("text") ){
                dataStr += "\"" + createRandomInt(sValue,eValue) + "\"";
            }else{
                dataStr += createRandomInt(sValue,eValue);
            }

        }else if(type == DATATYPE_DOUBLE){
            double sValue = 0;
            double eValue = 1;
            if(!val1.equals("") && !val2.equals("") ){
                sValue = Double.parseDouble(val1);
                eValue = Double.parseDouble(val2);
                if(sValue > eValue){
                    double tmp = sValue;
                    sValue = eValue;
                    eValue = tmp;
                }                
            }
            if(!val3.equals("") && val3.equals("text") ){
                dataStr += "\"" + createRandomDouble(sValue,eValue) + "\"";
            }else{
                dataStr += createRandomDouble(sValue,eValue);
            }


        }else if(type == DATATYPE_BOOLEAN){
            
            if(!val1.equals("") && val1.equals("text") ){
                dataStr += "\"" + createRandomBoolean() + "\"";
            }else{
                dataStr += createRandomBoolean();
            }            
            
        }else if(type == DATATYPE_SEQ){
            if(!val1.equals("") && !val2.equals("") ){
                seqNum = Integer.parseInt(val1);
                intervalNum = Integer.parseInt(val2);
            }else if(!val1.equals("") && val2.equals("") ){
                seqNum = Integer.parseInt(val1);
                intervalNum = 1;
            }
            dataStr += createSequenceInt();
            
        }else if(type == DATATYPE_NORMALTEXT){
            int countValue = 10;
            if(!val1.equals("")){
                countValue = Integer.parseInt(val1);
            }
            dataStr += createRandomString(countValue);

        }else if(type == DATATYPE_NAMETEXT){
            int r = createRandomInt(0, 9);

            int index = 0;
            if(!val1.equals("")){
                index = Integer.parseInt(val1);
                index = index > 5 ? 0 : index;
            }

            dataStr += nameMapper[index][r];

        }else if(type == DATATYPE_UUID){
            dataStr +=  createUUID();

        }else if(type == DATATYPE_KEY){
            dataStr +=  createKey(2);
            
        }else if(type == DATATYPE_KNAMETEXT){
            dataStr +=  createKoreaPeopleName();

        }else if(type == DATATYPE_ENAMETEXT){
            dataStr +=  createEnglishPeopleName();

        }else if(type == DATATYPE_USERNAME){
            if(!val1.equals("")){
               dataStr +=  createRandomUserName(val1);            
            }
        }else if(type == DATATYPE_KADDRESS){
            if(!val1.equals("")){
               dataStr +=  createKoreaAddress(val1);            
            }else{
               dataStr +=  createKoreaAddress(""); 
            }
        }else if(type == DATATYPE_EADDRESS){
             dataStr +=  createEnglishAddress();            
             
        }else if(type == DATATYPE_KCOMPANY){
             dataStr +=  createKoreaCompany();            
             
        }else if(type == DATATYPE_ECOMPANY){
             dataStr +=  createEnglishCompany();            
             
        }else if(type == DATATYPE_FORMTEXT){
            if(!val1.equals("")){
               dataStr +=  createRandomFormText(val1);            
            }
        }else if(type == DATATYPE_DATETIME){
            String sTime = "2000-01-01 00:00:00";
            String eTime = DateTimeUtils.getTimeDateNow();
            if(!val1.equals("") && !val2.equals("") ){
                sTime = val1;
                eTime = val2;
            }
            dataStr +=  createRandomDate(sTime, eTime, "");

        }else if(type == DATATYPE_TIME){
            String sTime = "00:00:00";
            String eTime = "23:59:59";
            if(!val1.equals("") && !val2.equals("") ){
                sTime = val1;
                eTime = val2;
            }
            dataStr +=  createRandomTime(sTime, eTime);

        }else if(type == DATATYPE_DATE){
            String sTime = "2000-01-01 00:00:00";
            String eTime = DateTimeUtils.getTimeDateNow();
            if(!val1.equals("") && !val2.equals("") ){
                sTime = val1;
                eTime = val2;
            }
            dataStr +=  createRandomDate(sTime, eTime, "yyyy-MM-dd");

        }else if(type == DATATYPE_COLOR){
            dataStr +=  createRandomColor();

        }else if(type == DATATYPE_IP){
            
            if(!val1.equals("")){
               dataStr +=  createRandomIP(val1);
            }else{
               dataStr +=  createRandomIP("");  
            }

        }else if(type == DATATYPE_EMAIL){
            dataStr +=  createRandomEmail();

        }else if(type == DATATYPE_URL){
            dataStr +=  createRandomUrl();
            
        }else if(type == DATATYPE_MACADDRESS){
            dataStr +=  createRandomMacAddress();

        }else if(type == DATATYPE_DOMAIN){
            dataStr +=  createDomainName();

        }else if(type == DATATYPE_COORD){
            double minx = 123;
            double maxx = 129;
            double miny = 33;
            double maxy = 39;
            if(!val1.equals("") && !val2.equals("") && !val3.equals("") && !val4.equals("") ){
                minx = Double.parseDouble(val1);
                maxx = Double.parseDouble(val2);
                miny = Double.parseDouble(val3);
                maxy = Double.parseDouble(val4);
            }

            double[] pt = createRandomCoords(minx, maxx, miny, maxy);
            dataStr +=  "[" + pt[0] + "," + pt[1] + "]";

        }

        return dataStr;
    }
    
    
    
    // @@ 단일 랜덤 데이터 
    // 타입만 주면 랜덤 데이터 생성해서 리턴    
    // System.out.println( genRandomData("$formtext;hello-${int;10;110}")  );
    public static String genRandomData(String elementStr){
        
        String dataTypeStr =  elementStr;
        
        String[] dParam = dataTypeStr.split(";");
        String result = "";
        
       
        int dtypeNum = RandomDataMaker.getTypeNum(dParam[0]);
        if(dtypeNum == -1){
            return elementStr;
        }else if(dtypeNum == RandomDataMaker.DATATYPE_FORMTEXT ){
            dParam = dataTypeStr.split(";",2);
        }

        // 구조에 따른 변경 
        if(dParam.length == 1){
            result = RandomDataMaker.genRandomData(dtypeNum, "", "", "", "");
        }else if(dParam.length == 3){
            result = RandomDataMaker.genRandomData(dtypeNum, dParam[1], dParam[2], "", "");
        }else if(dParam.length == 2){
            result = RandomDataMaker.genRandomData(dtypeNum, dParam[1], "", "", "");
        }else if(dParam.length == 5){
            result = RandomDataMaker.genRandomData(dtypeNum, dParam[1], dParam[2], dParam[3], dParam[4]);
        }else if(dParam.length == 4){
            result = RandomDataMaker.genRandomData(dtypeNum, dParam[1], dParam[2], dParam[3], "");
        }else{
            dParam = dataTypeStr.split(";",5);
            result = RandomDataMaker.genRandomData(dtypeNum, dParam[1], dParam[2], dParam[3], dParam[4]);
        }
        
        return result;
    }


    // 임의 값으로 배열 스트링을 만들어 리턴
    public static String genRandomArray(String[] dParam, int length){
        String arrStr = "[\r\n";

        int dtypeNum = RandomDataMaker.getTypeNum(dParam[0]);
        if(dtypeNum == -1){
            return "";
        }

         
        String itemResult = "";

        for(int i=0; i < length ; i++){

            if(dtypeNum == RandomDataMaker.DATATYPE_FORMTEXT ){
                dParam = UtilString.extractString(dParam, ";", 0).split(";",2);
            }
            
            // 구조에 따른 변경 
            if(dParam.length == 1){
                itemResult = RandomDataMaker.genRandomData(dtypeNum, "", "", "", "");
            }else if(dParam.length == 3){
                itemResult = RandomDataMaker.genRandomData(dtypeNum, dParam[1], dParam[2], "", "");
            }else if(dParam.length == 2){
                itemResult = RandomDataMaker.genRandomData(dtypeNum, dParam[1], "", "", "");
            }else if(dParam.length == 5){
                itemResult = RandomDataMaker.genRandomData(dtypeNum, dParam[1], dParam[2], dParam[3], dParam[4]);
            }else if(dParam.length == 4){
                itemResult = RandomDataMaker.genRandomData(dtypeNum, dParam[1], dParam[2], dParam[3], "");
            }else{
                dParam = UtilString.extractString(dParam, ";", 0).split(";",5);
                itemResult = RandomDataMaker.genRandomData(dtypeNum, dParam[1], dParam[2], dParam[3], dParam[4]);
            }

            // @@ 문자열의 경우 쿼테이션 포매팅
            if(dtypeNum == DATATYPE_NULL){
                arrStr += "null";
            }else if(dtypeNum == DATATYPE_INT || dtypeNum == DATATYPE_DOUBLE || dtypeNum == DATATYPE_BOOLEAN || dtypeNum == DATATYPE_SEQ || dtypeNum == DATATYPE_COORD){
                arrStr += itemResult;

            }else if(dtypeNum == DATATYPE_NORMALTEXT || dtypeNum == DATATYPE_NAMETEXT || dtypeNum == DATATYPE_UUID || dtypeNum == DATATYPE_KEY
                    || dtypeNum == DATATYPE_DATETIME || dtypeNum == DATATYPE_TIME || dtypeNum == DATATYPE_DATE || dtypeNum == DATATYPE_COLOR
                    || dtypeNum == DATATYPE_IP || dtypeNum == DATATYPE_EMAIL || dtypeNum == DATATYPE_URL || dtypeNum == DATATYPE_MACADDRESS || dtypeNum == DATATYPE_KADDRESS 
                    || dtypeNum ==  DATATYPE_USERNAME || dtypeNum ==  DATATYPE_FORMTEXT || dtypeNum ==  DATATYPE_KNAMETEXT ||  dtypeNum ==  DATATYPE_DOMAIN 
                    || dtypeNum ==  DATATYPE_EADDRESS || dtypeNum ==  DATATYPE_ENAMETEXT || dtypeNum ==  DATATYPE_ECOMPANY || dtypeNum ==  DATATYPE_KCOMPANY  ){
                arrStr +=  "\""  + itemResult + "\"" ;

            }else{
                arrStr += itemResult;

            }

            if(i < length-1){
                arrStr += ",\r\n";
            }
        }

        arrStr += "\r\n]";

        return arrStr;
    }




    // csv 만들어 리턴
    public static String genRandomCSV(int length, String format){
        String arrStr = "";

        String itemResult = "";
        String[] dParam;
        

        for(int i=0; i < length ; i++){
            itemResult = createRandomFormText(format);           
            arrStr += itemResult;

            if(i < length-1){
                arrStr += "\r\n";
            }
        }

        return arrStr;
    }


    /////////////////////////////////////////////////////////////////////////
    // JSON
    
    // @@ JSON 형식을 주면 거기에 랜덤값을 담아서 리턴
    public static JSONObject genRandomJson(JSONObject jsonTemplate){

        JSONObject jResult = new JSONObject();
        
        Iterator i = jsonTemplate.keys();
        while(i.hasNext())
        {
            try {
                String key = i.next().toString();

                // 하위 구조에 JSON 객체가 있는 경우를 관리 
                if(jsonTemplate.get(key) instanceof JSONObject){
                    JSONObject subObj = genRandomJson((JSONObject)jsonTemplate.get(key));
                    jResult.put(key, subObj);
                }else if(jsonTemplate.get(key) instanceof JSONArray){
                    JSONArray arr = genRandomJson((JSONArray)jsonTemplate.get(key));
                    jResult.put(key, arr);
                }else{
                    
                    String value = jsonTemplate.getString(key);
                    genJsonElement(jResult, key, value);
                }


            } catch (JSONException ex) {
                Logger.getLogger(RandomDataMaker.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return jResult;

    }

    
    public static JSONArray genRandomJson(JSONArray jArr) {

        JSONArray arraylist = new JSONArray();
        
        if(jArr.length() < 1){
            return arraylist;
        }
        
        try {
            int count = jArr.length();
            
            for(int i=0; i< count; i++){
                
                if(jArr.get(i) instanceof  JSONObject){
                    JSONObject jObj = jArr.getJSONObject(i);
                    JSONObject subObj = genRandomJson(jObj);
                    arraylist.put(subObj);
                }else if(jArr.get(i) instanceof  JSONArray){
                    JSONArray jObj = jArr.getJSONArray(i);
                    JSONArray subObj = genRandomJson(jObj);
                    arraylist.put(subObj);
                }else{
                    String subData = genRandomData(jArr.get(i).toString() );
                    arraylist.put(subData);
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


        return arraylist;
    }  
    
    public static void genJsonElement(JSONObject jResult, String key, String value) {
        
            try {

                if(value.equals("")){
                    jResult.put(key, value);
                }

                String[] part = value.split(";");

                if(part[0].equals(DATATYPESTRING_NULL)){
                    jResult.put(key, "null");
                    
                }else if(part[0].equals(DATATYPESTRING_INT)){
                    if(part.length == 3){
                        int s = Integer.parseInt(part[1]);
                        int e = Integer.parseInt(part[2]);
                        
                        if(s > e){
                            int tmp = s;
                            s = e;
                            e = tmp;
                        }
                        
                        jResult.put(key, createRandomInt(s,e));
                    }else if(part.length == 4 && part[3].equals("text")){
                        int s = Integer.parseInt(part[1]);
                        int e = Integer.parseInt(part[2]);
                        if(s > e){
                            int tmp = s;
                            s = e;
                            e = tmp;
                        }
                        jResult.put(key, Integer.toString(createRandomInt(s,e)));
                        
                    }else if(part.length == 2){
                        int s = Integer.parseInt(part[1]);
                        jResult.put(key, createRandomInt(-10000,s));
                    }else{
                        jResult.put(key, createRandomInt(-10000,10000));

                    }
                }else if(part[0].equals(DATATYPESTRING_DOUBLE)){
                    if(part.length == 3){
                        double s = Double.parseDouble(part[1]);
                        double e = Double.parseDouble(part[2]);
                        if(s > e){
                            double tmp = s;
                            s = e;
                            e = tmp;
                        }
                        jResult.put(key, createRandomDouble(s,e));
                    }else if(part.length == 4 && part[3].equals("text")){
                        double s = Integer.parseInt(part[1]);
                        double e = Integer.parseInt(part[2]);
                        if(s > e){
                            double tmp = s;
                            s = e;
                            e = tmp;
                        }                        
                        jResult.put(key, Double.toString(createRandomDouble(s,e)));
                        
                    }else if(part.length == 2){
                        double s = Double.parseDouble(part[1]);
                        jResult.put(key, createRandomDouble(-100,s));
                    }else{
                        jResult.put(key, createRandomDouble(0,1));

                    }
                }else if(part[0].equals(DATATYPESTRING_BOOLEAN)){
                    if(part.length == 2 && part[1].equals("text")){
                        double s = Double.parseDouble(part[1]);
                        jResult.put(key, Boolean.toString(createRandomBoolean()));
                    }else{
                        jResult.put(key, createRandomBoolean());
                    }
                }else if(part[0].equals(DATATYPESTRING_SEQ)){
                    if(part.length == 2 && part[1].equals("text")){
                        jResult.put(key, Integer.toString(createSequenceInt()));
                    }else{
                        jResult.put(key, createSequenceInt());
                    }

                }else if(part[0].equals(DATATYPESTRING_NORMALTEXT)){

                    if(part.length == 2){
                        int s = Integer.parseInt(part[1]);
                        s = s < 1 ? 10 : s ;
                        jResult.put(key, createRandomString(s));
                    }else{
                        jResult.put(key, createRandomString(10));

                    }

                }else if(part[0].equals(DATATYPESTRING_NAMETEXT)){

                    if(part.length == 2){
                        int s = Integer.parseInt(part[1]);
                        int r = createRandomInt(0, 9);
                        jResult.put(key,  nameMapper[s][r]);
                    }else{
                        int r = createRandomInt(0, 9);
                        jResult.put(key,  nameMapper[0][r]);

                    }

                }else if(part[0].equals(DATATYPESTRING_UUID)){
                    jResult.put(key, createUUID());

                }else if(part[0].equals(DATATYPESTRING_KEY)){
                    jResult.put(key, createKey(2));

                }else if(part[0].equals(DATATYPESTRING_USERNAME)){
                    
                   jResult.put(key, createRandomUserName(part[1]));            
                                        
                }else if(part[0].equals(DATATYPESTRING_KADDRESS)){
                    
                    if(part.length > 1 && !part[1].equals("")){
                        jResult.put(key, createKoreaAddress(part[1]));            
                    }else{
                        jResult.put(key, createKoreaAddress("")); 
                    }
                   
                }else if(part[0].equals(DATATYPESTRING_EADDRESS)){
                    
                    jResult.put(key, createEnglishAddress());            
                   
                }else if(part[0].equals(DATATYPESTRING_ECOMPANY)){
                    
                    jResult.put(key, createEnglishCompany());            
                   
                }else if(part[0].equals(DATATYPESTRING_KCOMPANY)){
                    
                    jResult.put(key, createKoreaCompany());            
                   
                                        
                }else if(part[0].equals(DATATYPESTRING_KNAMETEXT)){
                    
                   jResult.put(key,createKoreaPeopleName());            
                   
                }else if(part[0].equals(DATATYPESTRING_ENAMETEXT)){
                    
                   jResult.put(key,createEnglishPeopleName());            
                                        
                }else if(part[0].equals(DATATYPESTRING_FORMTEXT)){
                    
                    // form인 경우는 재조합
                    part = value.split(";",2);
                    jResult.put(key, createRandomFormText(part[1]));    
                    
                }else if(part[0].equals(DATATYPESTRING_DATETIME)){
                    if(part.length == 3){
                        jResult.put(key, createRandomDate(part[1], part[2], ""));
                    }else if(part.length == 2){
                        String eTime = DateTimeUtils.getTimeDateNow();
                        jResult.put(key, createRandomDate(part[1], eTime, ""));
                    }else{
                        String sTime = "2000-01-01 00:00:00";
                        String eTime = DateTimeUtils.getTimeDateNow();
                        jResult.put(key, createRandomDate(sTime, eTime, ""));
                    }

                }else if(part[0].equals(DATATYPESTRING_DATE)){
                    if(part.length == 3){
                        jResult.put(key, createRandomDate(part[1], part[2], "yyyy-MM-dd"));
                    }else if(part.length == 2){
                        String eTime = DateTimeUtils.getTimeDateNow();
                        jResult.put(key, createRandomDate(part[1], eTime, "yyyy-MM-dd"));
                    }else{
                        String sTime = "2000-01-01 00:00:00";
                        String eTime = DateTimeUtils.getTimeDateNow();
                        jResult.put(key, createRandomDate(sTime, eTime, "yyyy-MM-dd"));
                    }

                }else if(part[0].equals(DATATYPESTRING_TIME)){
                    if(part.length == 3){
                        jResult.put(key, createRandomTime(part[1], part[2]));
                    }else if(part.length == 2){
                        String eTime = "23:59:59";
                        jResult.put(key, createRandomTime(part[1], eTime));
                    }else{
                        String sTime = "00:00:00";
                        String eTime = "23:59:59";
                        jResult.put(key, createRandomTime(sTime, eTime));
                    }

                }else if(part[0].equals(DATATYPESTRING_COLOR)){
                    jResult.put(key, createRandomColor());

                }else if(part[0].equals(DATATYPESTRING_IP)){
                    
                     if(part.length == 2){
                        jResult.put(key, createRandomIP(part[1]));

                    }else{
                        jResult.put(key, createRandomIP(""));
                    }

                }else if(part[0].equals(DATATYPESTRING_EMAIL)){
                    jResult.put(key, createRandomEmail());

                }else if(part[0].equals(DATATYPESTRING_URL)){
                    jResult.put(key, createRandomUrl());
                    
                }else if(part[0].equals(DATATYPESTRING_MACADDRESS)){
                    jResult.put(key, createRandomMacAddress());

                }else if(part[0].equals(DATATYPESTRING_COORD)){
                    if(part.length == 5){
                        double minx = Double.parseDouble(part[1]);
                        double miny = Double.parseDouble(part[2]);
                        double maxx = Double.parseDouble(part[3]);
                        double maxy = Double.parseDouble(part[4]);
                        jResult.put(key, createRandomCoords(minx, maxx, miny, maxy));

                    }else{
                        jResult.put(key, createRandomCoords(123, 129, 33, 39));

                    }
                }else if(part[0].equals(DATATYPESTRING_DOMAIN)){
                   jResult.put(key, createDomainName()); 
                   
                }else{
                    jResult.put(key, value);
                }


            } catch (JSONException ex) {
                Logger.getLogger(RandomDataMaker.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
    
    public static void main(String[] args) {
        System.out.println("" + createRandomInt(-99, -20));
//        System.out.println("" + createRandomDouble(0.5d, 0.7d));
//        System.out.println("" + createKey("595342", 4));
//        System.out.println("" + createRandomString(14));
//        System.out.println("" + createRandomHexInt(0,3000, 4));
//        System.out.println("" + createRandomColor());

//        String inputFormat = "yyyy-MM-dd HH:mm:ss";
//        System.out.println("" + createRandomDate("2021-03-01", "2021-03-14", "yyyy-MM-dd"));
//        String input = "{\"sigMac\":\"$mac\", \"sigMacvendor\":\"$username;samsung,iptime,intel\", \"sigAverage\":\"$int;-99;-20\", \"sigPackType\":\"0x40\", \"sigType\":\"station\", \"sigChannel\":\"$int;2;9\"}";
//        String input = "{\"Counter\":\"$int;0;200\",\"SrcPort\":\"$int;1;45000;text\",\"DpiProto\":\"$username;ModbusTCP,TCP\",\"DstIP\":\"$ip\","
//                + "\"EtherType\":\"$username;TCP,UDP\",\"DstPort\":\"$int;1;45000;text\",\"SrcIP\":\"$ip\",\"Created\":\"$datetime\"}";
//        String input = "{\"HMI_count\":\"$int;0;20\", \"Use_HMI_count\":\"$int;0;10\", \"PLC_count\":\"$int;0;50\", \"Use_PLC_count\":\"$int;0;30\", \"Device_count\":\"$int;0;100\", \"Use_Device_count\":\"$int;0;70\", "
//                + "\"Traffic_count\":\"$int;0;20\", \"Session_count\":\"$int;0;20\", \"DpiProto_Session\":\"$int;0;20\", \"Unknown_Session\":\"$int;0;20\", \"Event_count\":\"$int;0;20\"}";
//        String input = "{\"Type\":\"$username;PLC,HMI,Sensor\", \"Vendor\":\"$username;UWCP,CIMON,Unknown,LS ELECTRIC,MITSUBISHI ELECTRIC,SIEMENS,FATEK AUTOMATION,Keyence,Yaskawa,BACNet,FUJI ELECTRIC,Emerson,Yaskawa,IEC\","
//                + " \"Model\":\"$username;UWCP,CM-NT15-Am Simulator,MWS,CM3-SP16MDR,XBM-DR16S,K3P-07AS,GM6-CPUA,CM1-SPC,XGK-CPUE\", \"IP\":\"$ip;172.123\", \"EtherType\":\"$username;TCP,UDP\","
//                + " \"PLC_Port\":\"$int;1;45000;text\", \"DpiProto\":\"$username;ModbusTCP,TCP\", \"HMI_IP\":\"$formtext;${int;172;200}.${int;172;200}.${int;172;200}.${int;172;200}\", \"Status\":\"$username;Running,Error\", \"UpTime\":\"$datetime\"}";

        String input = "{\"sigMac\":\"$mac\", \"sigMacvendor\":\"$username;samsung,iptime,intel\", " + 
                  "\"sigLatestTime\":\"$formtext;${date;2021-01-01;2021-09-30} ${time;00:00;23:59}\", \"sigDiffNow\":\"$int;0;200\", " +
                  "\"sigDiffNowSecond\":\"$int;0;10000\", \"sigScanAP\":\"$int;1;5\", " +
                  "\"sigAverage\":\"$int;-95;-20\", \"sigPackType\":\"0x40\", " +
                  "\"sigESSID\":\"$username;asus,iptime,next,zio\", \"sigInterval\":\"$int;0;1000\", " +
                  "\"sigType\":\"station\", \"sigChannel\":\"$int;2;9\"}";
        
        try {
            JSONObject jObj = new JSONObject(input);
            JSONObject jObj2 = genRandomJson(jObj);
            System.out.println(jObj2);
        } catch (JSONException ex) {
            Logger.getLogger(RandomDataMaker.class.getName()).log(Level.SEVERE, null, ex);
        }

//        System.out.println(genRandomData("${username;UWCP,CIMON,Unknown}"));
//        System.out.println(genRandomArray(DATATYPE_COORD, 10) );
//        System.out.println(createRandomEmail() );
//        System.out.println(createRandomMacAddress());

//        String str = "0123456789${double;0.0;1.0;text}가가가가" +
//            "${int;0;111}나나나나" +
//            "_${int;0;111}" +
//            "${nomapping} << nomapping 없는 부분${int;0;111}" ;

//        String str = "IP : ${int;0;255}.${int;0;255}.${int;0;255}.${int;0;255}\r\n"
//                + "CODE : Hello-${int;100;255}";
//        
//        System.out.println( createRandomFormText(str)  );

       System.out.println( genRandomData("$formtext;hello-${int;10;110;text}")  );

    }
}

