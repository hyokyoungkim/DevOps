/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ithows.service;

import com.ithows.AppConfig;
import java.io.File;
import javax.servlet.http.HttpSession;

/**
 *
 * @author ksyuser
 */
public class UploadConst {
    public final static int CONTENTTYPE_DASHBOARD = 1;
    public final static int CONTENTTYPE_CHART = 2;
    public final static int CONTENTTYPE_TABLE = 3;
    public final static int CONTENTTYPE_IMAGE = 4;
    public final static int CONTENTTYPE_VIDEO = 5;
    public final static int CONTENTTYPE_HTML = 6;
    public final static int CONTENTTYPE_MAP = 7;
    public final static int CONTENTTYPE_FILE = 99;


      
    public static String contextPath(){
        String tempPath = "";
        String OS = System.getProperty("os.name").toLowerCase();
        if(OS.indexOf("win") >= 0){ 
           tempPath = AppConfig.getConf("context_win_dir") ;

        }else{
           tempPath = AppConfig.getConf("context_dir") ;
        } 
        return tempPath;
    }
    
    public static String resourcePath(String key){
        String path = "";
        path = contextPath() + AppConfig.getConf(key);
        return path;
    }
    
    
    public static String getSessionDir(HttpSession session) {
        
        String sessionDir = session.getServletContext().getRealPath("/");
        return sessionDir;
    }
    
    // 프리젠테이션(맵) 업로드 경로
    // web 폴더가 root 경로임
    public static String getDashboardUploadDir(String mapGuid) {
        return AppConfig.getConf("config_dashboard_upload_url") ;
    }

    public static String getDashboardUploadDir(HttpSession session) {
        //여기서의 session은 현재의 디렉터리만 얻어낸다. getServletContext()
        String mapPath = getDir(session, AppConfig.getConf("config_dashboard_upload_url"),  "");
        return mapPath;
    }

    public static String getDashboardDir(HttpSession session, String guidStr) {
        //여기서의 session은 현재의 디렉터리만 얻어낸다. getServletContext()
        String mapPath = getDir(session, AppConfig.getConf("config_dashboard_upload_url"),  guidStr);
        return mapPath;
    }

    public static String getMapDir(HttpSession session, String guidStr) {
        //여기서의 session은 현재의 디렉터리만 얻어낸다. getServletContext()
        String mapPath = getDir(session, AppConfig.getConf("config_gismap_upload_url"),  guidStr);
        return mapPath;
    }

    
    public static String getHtmlUploadDir(HttpSession session) {
        String htmlPath = getDir(session, AppConfig.getConf("config_html_upload_url"),  "");
        return htmlPath;
    }

    public static String getHtmlDir(HttpSession session, String guidStr) {
        String htmlPath = getDir(session, AppConfig.getConf("config_html_upload_url"),  guidStr);
        return htmlPath;
    }    
    
    
    // 컨텐츠 저장 경로
    public static String getCotentsUploadDir(HttpSession session, String guidStr, int contentsType ) {

        String filePath = "";

        if(contentsType == CONTENTTYPE_CHART) {
            filePath = getDir(session, AppConfig.getConf("config_chart_upload_url"), guidStr );

        }else if(contentsType == CONTENTTYPE_TABLE){
            filePath = getDir(session, AppConfig.getConf("config_table_upload_url"), guidStr );

        }else if(contentsType == CONTENTTYPE_IMAGE){
            filePath = getDir(session, AppConfig.getConf("config_image_upload_url"), guidStr);

        }else if(contentsType == CONTENTTYPE_VIDEO){
            filePath = getDir(session, AppConfig.getConf("config_video_upload_url"), guidStr);

        }else if(contentsType == CONTENTTYPE_HTML){
            filePath = getDir(session, AppConfig.getConf("config_table_upload_url"), guidStr );

        }else if(contentsType == CONTENTTYPE_MAP){
            filePath = getDir(session, AppConfig.getConf("config_gismap_upload_url"), guidStr );

        }else if(contentsType == CONTENTTYPE_FILE){
            filePath = getDir(session, AppConfig.getConf("config_file_upload_url"), guidStr);

        }

        return filePath;
    }

    // 컨텐츠 저장 경로
    public static String getCotentsUploadDir(String guidStr, int contentsType) {

        String filePath = "";

        if(contentsType == CONTENTTYPE_DASHBOARD) {
            filePath = AppConfig.getConf("context_dir") + AppConfig.getConf("config_dashboard_upload_url") + guidStr + File.separator;

        }else if(contentsType == CONTENTTYPE_CHART) {
            filePath = AppConfig.getConf("context_dir") + AppConfig.getConf("config_chart_upload_url") + guidStr + File.separator;

        }else if(contentsType == CONTENTTYPE_TABLE){
            filePath = AppConfig.getConf("context_dir") + AppConfig.getConf("config_table_upload_url") + guidStr + File.separator;


        }else if(contentsType == CONTENTTYPE_IMAGE){
            filePath = AppConfig.getConf("context_dir") + AppConfig.getConf("config_image_upload_url") + guidStr + File.separator;


        }else if(contentsType == CONTENTTYPE_VIDEO){
            filePath = AppConfig.getConf("context_dir") + AppConfig.getConf("config_video_upload_url") + guidStr + File.separator;


        }else if(contentsType == CONTENTTYPE_HTML){
            filePath = AppConfig.getConf("context_dir") + AppConfig.getConf("config_table_upload_url") + guidStr + File.separator;


        }else if(contentsType == CONTENTTYPE_MAP){
            filePath = AppConfig.getConf("context_dir") + AppConfig.getConf("config_gismap_upload_url") + guidStr + File.separator;


        }else if(contentsType == CONTENTTYPE_FILE){
            filePath = AppConfig.getConf("context_dir") + AppConfig.getConf("config_file_upload_url") + guidStr + File.separator;


        }

        return filePath;
    }


    
    
    // 업로드 템프 경로
    // ** web 폴더가 root 경로임
    public static String getTempUploadDir() {
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.indexOf("win") >= 0) {
            return AppConfig.getConf("context_win_dir") + AppConfig.getConf("temp_dir")  ;
        } else {
            return AppConfig.getConf("context_dir") + AppConfig.getConf("temp_dir")  ;
        }
    }
    
    public static String getTempUploadDir(String userId) {
        return AppConfig.getConf("temp_dir") + userId  ;
    }

    // 업로드 된 임시 파일이 저장되어 있는 모든 경로
    public static String getTempUploadDir(HttpSession session, String userId) {
        //여기서의 session은 현재의 디렉터리만 얻어낸다. getServletContext()
        String imagePath = getDir(session, AppConfig.getConf("temp_dir"),  userId);
        return imagePath;
    }
       
    // 업로드 이미지 경로
    // ** web 폴더가 root 경로임
    public static String getImageUploadDir(String userId) {
        return AppConfig.getConf("config_image_upload_url") + userId  ;
    }

    // 업로드 이미지가 저장되어 있는 모든 경로
    public static String getImageUploadDir(HttpSession session, String userId) {
        //여기서의 session은 현재의 디렉터리만 얻어낸다. getServletContext()
        String imagePath = getDir(session, AppConfig.getConf("config_image_upload_url"),  userId);
        return imagePath;
    }
    
    // 업로드 파일 경로
    // ** web 폴더가 root 경로임
    public static String getFileUploadDir(String userId) {
        return AppConfig.getConf("config_file_upload_url") + userId  ;
    }

    // 업로드 파일이 저장되어 있는 모든 경로
    public static String getFileUploadDir(HttpSession session, String userId) {
        //여기서의 session은 현재의 디렉터리만 얻어낸다. getServletContext()
        String imagePath = getDir(session, AppConfig.getConf("config_file_upload_url"),  userId);
        return imagePath;
    }
    
    
    // 업로드 Map 경로
    // ** web 폴더가 root 경로임
    public static String getGISMapUploadDir(String userId) {
        return AppConfig.getConf("config_gismap_upload_url") + userId  ;
    }

    // 업로드 Map이 저장되어 있는 모든 경로
    public static String getGISMapUploadDir(HttpSession session, String userId) {
        //여기서의 session은 현재의 디렉터리만 얻어낸다. getServletContext()
        String imagePath = getDir(session, AppConfig.getConf("config_gismap_upload_url"),  userId);
        return imagePath;
    }
    
    
    // 업로드 Document 경로
    // ** web 폴더가 root 경로임  config_document_upload_url
    public static String getDocumentUploadDir(String userId) {
        return AppConfig.getConf("config_document_upload_url") + userId  ;
    }

    // 업로드 Map이 저장되어 있는 모든 경로
    public static String getDocumentUploadDir(HttpSession session, String userId) {
        //여기서의 session은 현재의 디렉터리만 얻어낸다. getServletContext()
        String imagePath = getDir(session,  AppConfig.getConf("config_document_upload_url"),  userId);
        return imagePath;
    }
    
    
    
    
    public static String getDir(HttpSession session, String subDir, String childDir) {

        String servletPath = session.getServletContext().getRealPath("/");

        //Tomcat 8.0에서는 마지막 경로에 '/'를 붙여주지 않기 때문에 이런 처리를 해야 함.        
//        if(!servletPath.endsWith("\\")){
//            servletPath += "\\";
//        }
//        if(!servletPath.endsWith("/")){    // @@ 리눅스 경로 문제
//            servletPath += "/";
//        }
        
        StringBuilder sb = new StringBuilder();
        sb.append(servletPath);
        sb.append(subDir);
        if (childDir != null && childDir.equals("") == false ) {
            sb.append(childDir);
//            sb.append("\\");
            sb.append(File.separator);
        }
        
        File file = new File(sb.toString());
        if (!file.exists()) {
            file.mkdirs();
        }
        
        return sb.toString();
    }
    
    
    // Usermap 이미지 업로드 경로
    // web 폴더가 root 경로임
    public static String getUsermapUploadDir() {
        
         String OS = System.getProperty("os.name").toLowerCase();
        if (OS.indexOf("win") >= 0) {
            return AppConfig.getConf("context_win_dir") + AppConfig.getConf("image_dir") + "map/" ;
        } else {
            return AppConfig.getConf("context_dir") + AppConfig.getConf("image_dir") + "map/" ;
        }
    }
    
    public static String getFPCsvUploadDir() {
        
         String OS = System.getProperty("os.name").toLowerCase();
        if (OS.indexOf("win") >= 0) {
            return AppConfig.getConf("context_win_dir") + AppConfig.getConf("config_data_upload_url")  ;
        } else {
            return AppConfig.getConf("context_dir") + AppConfig.getConf("config_data_upload_url")  ;
        }
    }
    
    
}
