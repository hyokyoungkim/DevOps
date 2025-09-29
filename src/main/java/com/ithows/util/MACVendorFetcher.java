/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ithows.util;

import com.ithows.JdbcDao;
import com.ithows.ResultMap;
import com.ithows.util.string.UtilString;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author mailt
 */
public class MACVendorFetcher {
    private static Map<String, String> vendors = new HashMap<>();

    public static void readFromfile(String fileName) {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.isEmpty()) continue;
                String prefix = line.substring(0, 2) + ':' + line.substring(2, 4) + ':' + line.substring(4, 6);
                vendors.put(prefix, line.substring(6));
            }
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    public static void reInsertDB(){
       
        int bundleCount = 0;
        int bundle = 1; // 쿼리가 많은 경우 500개씩 쪼개서 인서트 함
        int i=0;
        
       String SQL = "insert into macvendor (mac, vendor) values ";        
        
        try{
            JdbcDao.execute("truncate table macvendor ;");

            
            bundleCount = vendors.size();
            
            for( String key : vendors.keySet() ){
                
                if(i>0 && (i%bundle == 0 || i == (bundleCount-1))){
                    // 쿼리 조합
                    SQL += "(\"" + key + "\", \"" +  vendors.get(key) +  "\" ); " ;

                    try {
                        JdbcDao.update(SQL);
                    } catch (Exception e) {
                    }

                    SQL = "insert into macvendor (mac, vendor) values ";

                }else{
                    // 쿼리 조합
                     SQL += "(\"" + key + "\", \"" +  vendors.get(key) +  "\" ), " ;
                }
                
                i++;
                
            }
            
        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getLocalizedMessage());
        }
        
        return ;
    }

    public static String findMACVendorFromMap(String mac) {
        if(vendors.size() == 0){
            return "";
        }
        String vendorStr = vendors.get(mac.substring(0, 8).toUpperCase());
        if(vendorStr == null){
            vendorStr = "unknown vendor";
        }
        return vendorStr;
    }
    
    public static String findMACVendor(String mac) {
        
        if(mac.equals("")){
            return "";
        }
        
        String vendorStr = "" ;
 
        mac = mac.substring(0, 8).toUpperCase();
        
        String SQL = "select vendor " +
                   " from macvendor " +
                   " where mac=? ; ";        
        
        try{
            vendorStr = JdbcDao.queryForString(SQL, new Object[]{mac});

            if(vendorStr == null || vendorStr.equals("")){
                vendorStr = "unknown vendor";
            }

            
        }catch(Exception ex){
            ex.printStackTrace();
            System.out.println(ex.getLocalizedMessage());
        }        
        
        
        return vendorStr;
    }

    public static void main(String[] args) {
        String filename = "C:\\Users\\mailt\\Desktop\\mac-vendors.txt";
        readFromfile(filename);
//        reInsertDB();
        
        System.out.println("1: " + MACVendorFetcher.findMACVendor("64:7b:ce:01:7f:56"));
        System.out.println("2: " +  MACVendorFetcher.findMACVendor("d0:2b:20:be:a9:f2"));
        System.out.println("3: " +  MACVendorFetcher.findMACVendor("f8:e6:1a:52:e8:51"));
        System.out.println("4: " +  MACVendorFetcher.findMACVendor("c4:9a:02:3d:59:a2"));
        System.out.println("5: " +  MACVendorFetcher.findMACVendor("90:9f:33:f7:53:30"));
        System.out.println("6: " +  MACVendorFetcher.findMACVendor("84:F3:EB:AF:12:F8"));
        System.out.println("7: " +  MACVendorFetcher.findMACVendor("4c:56:9d:6f:c4:dd"));
        System.out.println("8: " +  MACVendorFetcher.findMACVendor("c8:09:a8:06:36:fa"));
    }
}
