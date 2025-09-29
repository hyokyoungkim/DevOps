package com.ithows;

import com.ithows.util.DateTimeUtils;
import com.ithows.util.UtilJSON;
import java.io.*;
import org.json.JSONArray;
import org.json.JSONObject;

public class CommonUtils {

    
    //////////////////////////////////////////////////////////////////////
    // Log member
    private static PrintWriter LogWriter = null;
    
    public static void makeNewLogFile(){
        if (LogWriter != null) {
            LogWriter.flush();
            LogWriter.close();
            LogWriter = null;
        }
        
        makelogFile();
    }
    
    private static void makelogFile(){
            
        String logPath = AppConfig.getContextPath() + AppConfig.getConf("testlog_dir") ;
            
        // 디렉토리가 없으면 생성
        File dir = new File(logPath);
        if(!dir.exists()){
            dir.mkdir();
        }

        logPath += "test_log_" + DateTimeUtils.getTimeDateNow2() + ".log"  ;
        
        
        File file = new File(logPath);

        try {
            FileWriter fileWriter = new FileWriter(file);
            LogWriter = new PrintWriter(fileWriter);
            
        } catch (Exception e) {
        }
        
    }

    
    public static void writeLog(String str) { // Console Out with Line Feed
    
        System.out.println(str);

        if (LogWriter == null) {
            makelogFile();
        }
        
        if (LogWriter != null) {
            LogWriter.println(str);
            LogWriter.flush();
        }
        
    }

    public static void writeLog(JSONArray arr) { 
        String str = UtilJSON.JSonBeautify(arr.toString());
        writeLog(str);
    }
    public static void writeLog(JSONObject obj) { 
        String str = UtilJSON.JSonBeautify(obj.toString());
        writeLog(str);
    }


    public static void Sleep(double sec) {
        try {
            Thread.sleep((int)(sec * 1000));
        } catch (Exception e) {
            
        }
    }
}
