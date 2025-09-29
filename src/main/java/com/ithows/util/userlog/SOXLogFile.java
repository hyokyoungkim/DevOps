/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ithows.util.userlog;

import com.ithows.AppConfig;
import com.ithows.util.DateTimeUtils;
import com.ithows.util.UtilJSON;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * log 지정 경로에 로그파일이 생성됨
 * @author mailt
 */
public class SOXLogFile {
    private PrintWriter LogWriter = null;
    private String LogFileName = "";
    
    
    public  SOXLogFile (String fileName){
        makelogFile(fileName);
    }
    
    
    public void close(){
        if (LogWriter != null) {
            LogWriter.flush();
            LogWriter.close();
            LogWriter = null;
        }
    }
    
    private void makelogFile(String fileName){
            
        String logPath = AppConfig.getContextPath() + AppConfig.getConf("testlog_dir") ;
            
        // 디렉토리가 없으면 생성
        File dir = new File(logPath);
        if(!dir.exists()){
            dir.mkdir();
        }

        if(fileName.equals("")){
            logPath += "test_log_" + DateTimeUtils.getTimeDateNow2() + ".txt"  ;
        }else{
            logPath = fileName  ;
        }
        
        File file = new File(logPath);

        try {
            FileWriter fileWriter = new FileWriter(file);
            LogWriter = new PrintWriter(fileWriter);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    
    public void writeLog(String str) { // Console Out with Line Feed
    
       // System.out.println(str);

        if (LogWriter == null) {
            makelogFile("");
        }
        
        if (LogWriter != null) {
            LogWriter.println(str);
            LogWriter.flush();
        }
        
    }

    public void writeLog(JSONArray arr) { 
        String str = UtilJSON.JSonBeautify(arr.toString());
        writeLog(str);
    }
    
    public void writeLog(JSONObject obj) { 
        String str = UtilJSON.JSonBeautify(obj.toString());
        writeLog(str);
    }
}
