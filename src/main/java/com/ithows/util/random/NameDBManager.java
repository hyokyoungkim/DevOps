/*
 *  Copyright 2020. S.O.X. All rights reserved
 */

package com.ithows.util.random;

import com.ithows.JdbcDao;
import com.ithows.ResultMap;
import static com.ithows.util.RandomDataMaker.createRandomInt;
import java.util.HashMap;
import java.util.Map;

/**
 * Class NameDBManager
 * 
 * @author Roi Kim <S.O.X Co. Ltd.>
 */
public class NameDBManager {
    
        // 주소를 위한 멤버
    private static int[][] fromTo = { {2,493}, {495,702}, {704,915}, {917,1082}, {1084,1290}, 
        {1292,1473}, {1475,1562}, {1564,1588}, {1590,2350}, {2352,2666}, 
        {2668,2915}, {2917,3215}, {3217,3639}, {3641,4083}, {4085,4636}, {4638,5199}, {5200,5275}};
    private static String[] cityName = { "서울특별시","부산광역시","대구광역시","인천광역시","광주광역시",
        "대전광역시","울산광역시","세종시","경기도","강원도",
        "충청북도","충청남도","전라북도","전라남도","경상북도",  "경상남도","제주도"};
    private static String[] cityNum = { "11","26","27","28","29",
        "30","31","36", "41","42",
        "43","44","45","46","47", "48","50"};
    
        
    private static Map<String, int[]> addressIndex = null;
    static{
        addressIndex = new HashMap<String, int[]>();
        for(int i=0; i < fromTo.length ; i++){
            addressIndex.put(cityNum[i], fromTo[i]);  
        }
        
    }
    
    public static String getEName(String field){
        String result = "";
        int endIndex = 100;
        
        if(field.equals("firstname")){
            endIndex = 472;
        }else if(field.equals("surname")){
            endIndex = 1000;
        }else if(field.equals("countryUS")){
            endIndex = 63;
        }else if(field.equals("company")){
            endIndex = 168;
        }
         
         
        int countryIdx = createRandomInt(1, endIndex);
        
        String sql = "Select " + field + " from ename " +
                "where id=? ;" ;
                
        try {
            String name = JdbcDao.queryForString(sql, new Object[]{countryIdx});
            result = name;
            
        } catch (Exception e) {
        }
        
        return result;
    }
    
    public static String getKName(String field){
        String result = "";
        int endIndex = 100;
        
        if(field.equals("name")){
            endIndex = 2800;
        }else if(field.equals("sung")){
            endIndex = 118;
        }else if(field.equals("company")){
            endIndex = 554;
        }
         
         
        int countryIdx = createRandomInt(1, endIndex);
        
        String sql = "Select " + field + " from kname " +
                "where id=? ;" ;
                
        try {
            String name = JdbcDao.queryForString(sql, new Object[]{countryIdx});
            result = name;
            
        } catch (Exception e) {
        }
        
        return result;
    }
    
     public static String getKoreaAddress(String regcode) {
        String cityName = regcode;
         
        if(regcode.equals("")){
            cityName = cityNum[createRandomInt(0, 16)];
        } 
         
        int[] fromTo = (int[])addressIndex.get(cityName);
        String randList = "";
        randList += createRandomInt(fromTo[0], fromTo[1]) + ",";
        randList += createRandomInt(fromTo[0], fromTo[1]) + ",";
        randList += createRandomInt(fromTo[0], fromTo[1]) + ",";
        randList += createRandomInt(fromTo[0], fromTo[1]) + ",";
        randList += createRandomInt(fromTo[0], fromTo[1]) ;
        
        String result = "";
        
        String sql = "Select * from regcode " +
                "where id in (" + randList + ") and dong <> \"\" " +
                "limit 1 ; " ;
                
        try {
            ResultMap map = JdbcDao.queryForMapObject(sql, new Object[]{});
            result = map.getString("sido") + " " + map.getString("gun") + " " + map.getString("dong") + " " + createRandomInt(1, 100) ;
            
        } catch (Exception e) {
        }
        
        return result;
    }
}
