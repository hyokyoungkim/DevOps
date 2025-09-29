/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ithows.dao;

import com.ithows.JdbcDao;
import com.ithows.ResultMap;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author alchemist
 */
public class UserLogDAO {
    
    public final static int USETYPE_LOGIN = 1;
    public final static int USETYPE_LOGOUT = 7;
    public final static int USETYPE_REGISTER = 8;
    public final static int USETYPE_ACCESSDENIED = 2;
    public final static int USETYPE_UPLOAD = 3;
    public final static int USETYPE_DOWNLOAD = 4;
    public final static int USETYPE_VIEW = 9;
    public final static int USETYPE_DELETE = 5;
    public final static int USETYPE_CONVERT = 6;


    public final static int USERLOGSORT_DATE_DESC = 1;
    public final static int USERLOGSORT_DATE_ASC = 2;
    public final static int USERLOGSORT_USER_DESC = 3;
    public final static int USERLOGSORT_USER_ASC = 4;
    public final static int USERLOGSORT_USETYPE_DESC = 5;
    public final static int USERLOGSORT_USETYPE_ASC = 6;
    public final static int USERLOGSORT_USEFILE_DESC = 7;
    public final static int USERLOGSORT_USEFILE_ASC = 8;
    
    /**
     * 전체 로그
     * @param orderOption
     * @param pageIdx : -1이면 전체
     * @param count
     * @return 
     */
    public static List getAllLog(int orderOption, int pageIdx, int count) {
        String query = "select * , "
                + "IF (logUseType=1,  'Login',  IF (logUseType=2,  'Denied',  IF (logUseType=3,  'Upload',  IF (logUseType=4,  'Download', IF (logUseType=5,  'Delete',  IF (logUseType=6,  'Convert',  IF (logUseType=7,  'Logout',  IF (logUseType=8,  'register',  'view')))))))) logTypeText, "
                + "(select userId from user where userNo = logUser) logUserName from uselog ";

         if(pageIdx > -1 && count > -1){
            String limitStr = "limit " + (pageIdx - 1) * count + ", " + count + " ";
            query = addOrderBy(query, orderOption)  + "  " + limitStr + ";" ;
            
        }else{
            
            query = addOrderBy(query, orderOption) + " ;";
        }
        
        try {
            return JdbcDao.queryForMapList(query, new Object[]{}); 
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }    
    
    public static int countAllLog() {
        String query = "select count(*) from uselog ";
        
        try {
            return JdbcDao.queryForInt(query, new Object[]{}); 
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }    
    
    
    /**
     * 유저 로그
     * @param orderOption
     * @param pageIdx : -1이면 전체
     * @param count
     * @return 
     */
    public static List getUserLog(String userId, int orderOption, int pageIdx, int count) {
        
        String query = "select * , "
                + "IF (logUseType=1,  'Login',  IF (logUseType=2,  'Denied',  IF (logUseType=3,  'Upload',  IF (logUseType=4,  'Download', IF (logUseType=5,  'Delete',  IF (logUseType=6,  'Convert',  IF (logUseType=7,  'Logout',  IF (logUseType=8,  'register',  'view')))))))) logTypeText, "
                + "(select userId from user where userNo = logUser) logUserName  "
                   + "from uselog, (select userNo from user where userId =? ) t "
                   + "where logUser=t.userNo ";
        
         if(pageIdx > -1 && count > -1){
            String limitStr = "limit " + (pageIdx - 1) * count + ", " + count + " ";
            query = addOrderBy(query, orderOption)  + "  " + limitStr + ";" ;
            
        }else{
            
            query = addOrderBy(query, orderOption) + " ;";
        }
        
        try {
            return JdbcDao.queryForMapList(query, new Object[]{userId}); 
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }    

    /**
     * 유저의 로그의 수 리턴
     * @param userId
     * @param useType : 0이면 유즈타입을 신경쓰지 않음
     * @return 
     */
    public static int countUserLog(String userId, int useType) {

        try {
               if(useType>0){
                    return JdbcDao.queryForInt("select count(*) from uselog, (select userNo from user where userId =? ) t where logUser=t.userNo and logUseType =?", new Object[]{userId, useType}); 
               }else{
                   return JdbcDao.queryForInt("select count(*) from uselog, (select userNo from user where userId =? ) t where logUser=t.userNo ;" , new Object[]{userId});  
               }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    
    
    // 3시간 이내만 확인
    public static int countAccessUser() {
            try {
            return JdbcDao.queryForInt("select count(distinct logUser) from uselog where logUseType =1 and logDate >= date_add(now(), interval -3 hour) ", new Object[]{}); 
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    // 3시간 이내만 확인
    public static List getAccessUser() {
            try {
            return JdbcDao.queryForMapList("select * from user where userNo in ( "
                    + " select distinct logUser from uselog where logUseType =1 and logDate >= date_add(now(), interval -3 hour) ) ; ", new Object[]{}); 
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public static int countAccessDenied() {
            return countLog(USETYPE_ACCESSDENIED);
    }

    public static int countDownload() {
            return countLog(USETYPE_DOWNLOAD) + countLog(USETYPE_VIEW);
    }

    public static int countLog(int useType) {

        try {
               if(useType>0){
                    return JdbcDao.queryForInt("select count(*) from uselog where logUseType =?", new Object[]{useType}); 
               }else{
                   return JdbcDao.queryForInt("select count(*) from uselog ;"); 
               }
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }

    public static void uploadLog(int userNo, String userName, String fileName, int useType, String useText) throws SQLException {

        int fileNo = JdbcDao.queryForInt("select fileId from file where fileName=? ;", new Object[]{fileName});
        insertLog (userNo, userName, fileNo, useType, useText);
        return ;
    }

    public static void insertLog(int userNo, String userName, int fileNo, int useType, String useText) throws SQLException {

        JdbcDao.updateStored("{call sp_insert_uselog(?,?,?,?,?)} ", new Object[]{fileNo, userNo, userName, useType, useText});
        return ;
    }
     

    public static void deleteUserLog(String userName) throws SQLException {
        JdbcDao.update("DELETE FROM uselog where logUser = (select userNo from user where userName= '" + userName + "') ; " );
    }
    
    
      
    private static String addOrderBy(String query, int orderOption){
        String queryStr = "";
        
        if(orderOption == UserLogDAO.USERLOGSORT_DATE_DESC ){
            queryStr = query + " order by logDate desc " ;
            
        } else if(orderOption == UserLogDAO.USERLOGSORT_DATE_ASC ){
            queryStr = query + " order by logDate asc  " ;
            
        } else if(orderOption == UserLogDAO.USERLOGSORT_USER_DESC ){
            queryStr = query + " order by logUser desc, logDate desc  " ;
            
        } else if(orderOption == UserLogDAO.USERLOGSORT_USER_ASC ){
            queryStr = query + " order by logUser asc, logDate desc  " ;
            
        } else if(orderOption == UserLogDAO.USERLOGSORT_USETYPE_DESC ){
            queryStr = query + " order by logUseType desc, logDate desc    " ;
            
        } else if(orderOption == UserLogDAO.USERLOGSORT_USETYPE_ASC ){
            queryStr = query + " order by logUseType asc, logDate desc    " ;
            
        } else if(orderOption == UserLogDAO.USERLOGSORT_USEFILE_DESC ){
            queryStr = query + " order by logFile desc, logDate desc    " ;
            
        } else if(orderOption == UserLogDAO.USERLOGSORT_USEFILE_ASC ){
            queryStr = query + " order by logFile asc, logDate desc    " ;
           

        }else{
            queryStr = query;
        }
        
        return queryStr;
    }
    
}
