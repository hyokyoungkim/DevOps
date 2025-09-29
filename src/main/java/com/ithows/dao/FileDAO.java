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

import com.ithows.service.FileManager;
import org.apache.commons.io.FilenameUtils;

/**
 *
 * @author ksyuser
 */
public class FileDAO {
    
    public final static int FILESORT_DATE_DESC = 1;
    public final static int FILESORT_DATE_ASC = 2;
    public final static int FILESORT_NAME_DESC = 3;
    public final static int FILESORT_NAME_ASC = 4;
    public final static int FILESORT_USER_DESC = 5;
    public final static int FILESORT_USER_ASC = 6;

    public final static int FILETYPE_ALL = 0;
    public final static int FILETYPE_IMAGE = 1;
    public final static int FILETYPE_DOC = 2;
    public final static int FILETYPE_MAP = 3;
    public final static int FILETYPE_ARCHIVE = 4;
    public final static int FILETYPE_EXCEL = 5;
    
    public final static int FILEPERMISSION_ALL = 7;
    public final static int FILEPERMISSION_DOWNLOAD = 1;
    public final static int FILEPERMISSION_VIEW = 2;
    public final static int FILEPERMISSION_DELETE = 4;    
    
    
    
    
    public static void main(String[] args) {
     
        
//         List list = getFiles("map", false, FileManager.FILETYPE_ARCHIVE ,FILESORT_DATE_DESC , 1, 10);
         List list = getTenFiles(FileDAO.FILETYPE_ARCHIVE, FileDAO.FILESORT_DATE_DESC, 1 );
         System.out.println("count " + list.size());

//         System.out.println("count " + countFiles(4));
         return ;
        
    }


    public static int countAdminFiles(int typeNum) {
        
        String sql = "select count(*) from file ";
        
        if(typeNum > 0){
            sql += "where fileType=" + typeNum + " ;";
        }

        try {
            return JdbcDao.queryForInt(sql, new Object[]{}); // 검색에 필요한 데이터는 Object 배열로 묶어 호출한다.
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    
    public static int countFiles(String userId, int typeNum) {
        
        String sql = "select count(*) "
                + "from file, (select userNo from user where userId =? ) t "
                + "where fileUser=t.userNo ";
        
        if(typeNum > 0){
            sql += "and fileType=" + typeNum + " ;";
        }

        try {
            return JdbcDao.queryForInt(sql, new Object[]{userId}); 
        } catch (SQLException e) {
            e.printStackTrace();
            return 0;
        }
    }
    
    
    public static int getFileId(String fileName) {
        String query = "select fileId from file where fileName=?";
        int result = -1;
        try {
            result = JdbcDao.queryForInt(query, new Object[]{fileName});
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    
    public static String getFilePath(int fid) {
        String query = "select * from file where fileId=?";
        String result = "";
        try {
            ResultMap map = JdbcDao.queryForMapObject(query, new Object[]{fid});
            result = map.getString("filePath") + map.getString("fileName");

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }    
    
    public static String getFilePath(String fileName) {
        String query = "select filePath from file where fileName=?";
        String result = "";
        try {
            result = JdbcDao.queryForString(query, new Object[]{fileName});
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
   
    public static ResultMap getFile(long fileNo) {
        String query = "select *,  "
                + "IF (fileType=1,  'Image',  IF (fileType=2,  'Document',  IF (fileType=3,  'Map',  IF (fileType=4,  'Archive', IF (fileType=5,  'Excel', 'other' ))))) fileTypeText, "
                + "(select userId from user where userNo = fileUser) fileUserName  from file where fileId=?";
        ResultMap result = null;
        try {
            result = JdbcDao.queryForMapObject(query, new Object[]{fileNo});
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
    
    
    public static ResultMap getFile(String fileName) {
        String query = "select *, "
                + "IF (fileType=1,  'Image',  IF (fileType=2,  'Document',  IF (fileType=3,  'Map',  IF (fileType=4,  'Archive', IF (fileType=5,  'Excel', 'other' ))))) fileTypeText,  "
                + "(select userId from user where userNo = fileUser) fileUserName  from file where fileName=?";
        ResultMap result = null;
        try {
            result = JdbcDao.queryForMapObject(query, new Object[]{fileName});
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }
   
    /**
     *  유저와 타입 별 파일 리스트 가져오기
     * @param userId
     * @param typeNum : 0이면 전체 타입
     * @param orderOption
     * @param pageIdx : -1이면 다 받아 옴
     * @param count : -1이면 다 받아 옴
     * @return 
     */
    public static List getUserFiles(String userId,  int typeNum, int orderOption, int pageIdx, int count) {

        String sql = "select *, "
                + "IF (fileType=1,  'Image',  IF (fileType=2,  'Document',  IF (fileType=3,  'Map',  IF (fileType=4,  'Archive', IF (fileType=5,  'Excel', 'other' ))))) fileTypeText,  "
                + "IF(fileShare=1, 1, IF(fileShare=3, 1, IF(fileShare=5, 1, IF(fileShare=7, 1, 0)))) fileShareDown ," 
                + "IF(fileShare=2, 1,  IF(fileShare=3, 1, IF(fileShare=6, 1, IF(fileShare=7, 1, 0)))) fileShareView ," 
                + "IF(fileShare=4, 1, IF(fileShare=5, 1, IF(fileShare=6, 1, IF(fileShare=7, 1, 0)))) fileShareDelete, "
                + "(select userId from user where userNo = fileUser) fileUserName  "
                   + "from file, (select userNo from user where userId =? ) t "
                   + "where fileUser=t.userNo ";
        
        if(typeNum > 0){
            sql = sql + "and fileType=? ";
        }
        
        if(pageIdx > -1 && count > -1){
            String limitStr = "limit " + (pageIdx - 1) * count + ", " + count + " ";
            sql = addOrderBy(sql, orderOption)  + "  " + limitStr + ";" ;
            
        }else{
            
            sql = addOrderBy(sql, orderOption) + " ;";
        }

        try {
            if(typeNum > 0){
                return JdbcDao.queryForMapList(sql, new Object[]{userId, typeNum}); // 검색에 필요한 데이터는 Object 배열로 묶어 호출한다.
            }else{
                return JdbcDao.queryForMapList(sql, new Object[]{userId}); // 검색에 필요한 데이터는 Object 배열로 묶어 호출한다.
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    } 
    
    /**
     * 어드민용 타입 별 파일 리스트 가져오기
     * @param typeNum : 0이면 전체 타입
     * @param orderOption
     * @param pageIdx : -1이면 다 받아 옴
     * @param count : -1이면 다 받아 옴
     * @return 
     */
    public static List getAdminFiles(int typeNum, int orderOption, int pageIdx, int count) {

        String sql = "select *, "
                    + "IF (fileType=1,  'Image',  IF (fileType=2,  'Document',  IF (fileType=3,  'Map',  IF (fileType=4,  'Archive', IF (fileType=5,  'Excel', 'other' ))))) fileTypeText,  "
                    + "IF(fileShare=1, 1, IF(fileShare=3, 1, IF(fileShare=5, 1, IF(fileShare=7, 1, 0)))) fileShareDown ," 
                    + "IF(fileShare=2, 1,  IF(fileShare=3, 1, IF(fileShare=6, 1, IF(fileShare=7, 1, 0)))) fileShareView ," 
                    + "IF(fileShare=4, 1, IF(fileShare=5, 1, IF(fileShare=6, 1, IF(fileShare=7, 1, 0)))) fileShareDelete, "
                    + "(select userId from user where userNo = fileUser) fileUserName  "
                       + "from file ";
        
        
        if(typeNum > 0){
             sql = sql + "where fileType=? ";
        }

        if(pageIdx > -1 && count > -1){
            String limitStr = "limit " + (pageIdx - 1) * count + ", " + count + " ";
            sql = addOrderBy(sql, orderOption)  + "  " + limitStr + ";" ;
            
        }else{
            
            sql = addOrderBy(sql, orderOption) + " ;";
        }
                
        try {
            if(typeNum > 0){
                return JdbcDao.queryForMapList(sql, new Object[]{typeNum}); // 검색에 필요한 데이터는 Object 배열로 묶어 호출한다.
            }else{
                return JdbcDao.queryForMapList(sql, new Object[]{}); // 검색에 필요한 데이터는 Object 배열로 묶어 호출한다.
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    } 
    
    
    
    public static List getTenFiles(int typeNum, int orderOption, int pageIdx) {


//        
        return getFiles(typeNum, orderOption, pageIdx, 10);
    } 

    
    // 타입에 따라 파일 리스트 가져오기
    // 페이징을 위한 옵션이 있음
    public static List getFiles(int typeNum, int orderOption, int pageIdx, int count) {

        String limitStr = "limit " + (pageIdx - 1) * count + ", " + count + " ";
        
        String sql = "select *, "
                + "IF (fileType=1,  'Image',  IF (fileType=2,  'Document',  IF (fileType=3,  'Map',  IF (fileType=4,  'Archive', IF (fileType=5,  'Excel', 'Dashboard' ))))) fileTypeText,  "
                + "IF(fileShare=1, 1, IF(fileShare=3, 1, IF(fileShare=5, 1, IF(fileShare=7, 1, 0)))) fileShareDown ," 
                    + "IF(fileShare=2, 1,  IF(fileShare=3, 1, IF(fileShare=6, 1, IF(fileShare=7, 1, 0)))) fileShareView ," 
                    + "IF(fileShare=4, 1, IF(fileShare=5, 1, IF(fileShare=6, 1, IF(fileShare=7, 1, 0)))) fileShareDelete, "
                + "(select userId from user where userNo = fileUser) fileUserName "
                   + "from file "
                   + "where fileType=? ";
        
                sql = addOrderBy(sql, orderOption) + "  " + limitStr + ";" ;

        try {
            return JdbcDao.queryForMapList(sql, new Object[]{typeNum}); // 검색에 필요한 데이터는 Object 배열로 묶어 호출한다.
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    } 
    
    
    // 파일명에 따라 파일 리스트 가져오기
    // 페이징을 위한 옵션이 있음
    // 검색박스를 위한 용도 
    public static List getFiles(String fileName, boolean exact, int typeNum, int orderOption, int pageIdx, int count) {

        String limitStr = "limit " + (pageIdx - 1) * count + ", " + count + " ";
        String sql = "";
        
        if(exact){
            
            sql = "select *, "
                    + "IF (fileType=1,  'Image',  IF (fileType=2,  'Document',  IF (fileType=3,  'Map',  IF (fileType=4,  'Archive', IF (fileType=5,  'Excel', 'other' ))))) fileTypeText, "
                    + "IF(fileShare=1, 1, IF(fileShare=3, 1, IF(fileShare=5, 1, IF(fileShare=7, 1, 0)))) fileShareDown ," 
                    + "IF(fileShare=2, 1,  IF(fileShare=3, 1, IF(fileShare=6, 1, IF(fileShare=7, 1, 0)))) fileShareView ," 
                    + "IF(fileShare=4, 1, IF(fileShare=5, 1, IF(fileShare=6, 1, IF(fileShare=7, 1, 0)))) fileShareDelete, "
                    + "(select userId from user where userNo = fileUser) fileUserName "
                       + "from file "
                       + "where fileName=? ";
        }else{
            fileName = "%" + fileName + "%";
            sql = "select *, "
                    + "IF (fileType=1,  'Image',  IF (fileType=2,  'Document',  IF (fileType=3,  'Map',  IF (fileType=4,  'Archive', IF (fileType=5,  'Excel', 'other' ))))) fileTypeText, "
                    + "IF(fileShare=1, 1, IF(fileShare=3, 1, IF(fileShare=5, 1, IF(fileShare=7, 1, 0)))) fileShareDown ," 
                    + "IF(fileShare=2, 1,  IF(fileShare=3, 1, IF(fileShare=6, 1, IF(fileShare=7, 1, 0)))) fileShareView ," 
                    + "IF(fileShare=4, 1, IF(fileShare=5, 1, IF(fileShare=6, 1, IF(fileShare=7, 1, 0)))) fileShareDelete, "
                    + "(select userId from user where userNo = fileUser) fileUserName "
                       + "from file "
                       + "where fileName like ? ";
        }

        if(typeNum > 0){
            sql += "and fileType=" + typeNum + " ";
        }
        
        sql = addOrderBy(sql, orderOption) + "  " + limitStr + ";" ;

        try {
            return JdbcDao.queryForMapList(sql, new Object[]{fileName}); // 검색에 필요한 데이터는 Object 배열로 묶어 호출한다.
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    } 
    

    // 추가는 1 리턴, 수정은 0 리턴
    public static int insertDashboard(int userNo, String fileName, int contentType, String path, String url, String servUrl) throws SQLException {
        
//        @@ 마리아 DB에서는 에러 발생         
//        String[] outParmas = new String[]{new String()};  // @@ 기존 버그 해결. 무조건 String으로 받은 후 변환
//        JdbcDao.updateStored("{call sp_insert_file(?,?,?,?,?,?,?)} ", new Object[]{fileName, fileSize, fileType, userId, path, url}, outParmas);
//        return Integer.parseInt(outParmas[0].toString());

        
        String sql = "SELECT count(*) FROM file WHERE fileUser=? and fileName=? and fileType=? ;";
        int l_count = JdbcDao.queryForInt(sql, new Object[]{userNo, fileName, contentType });
        
        String extStr = "map";
        
	if(l_count == 0){
            JdbcDao.update("INSERT INTO file (  " +
"                                       fileName," +
"					fileSize," +
"					fileType," +
"					fileUser," +
"					fileShare," +
"					fileLog," +
"					fileRegisterDate," +
"					filePath," +
"                                       fileExt," +
"					fileURI," +
"					fileServURI)" +
"				  VALUES (" +
"					?, " +
"					?," +
"					?," +
"					?," +
"					1," +
"					\"\"," +
"					now()," +
"					?," +
"                                       ?," +
"                                       ?," +
"					?" +
"             ); ", new Object[]{fileName, contentType, userNo, path, extStr ,url, servUrl});
            
            return 1;
            
        }else{
            sql = "SELECT fileId FROM file WHERE fileUser=? and fileName=? and fileType=? ; ";
            int l_fileId = JdbcDao.queryForInt(sql, new Object[]{userNo, fileName, contentType });
            sql = "UPDATE file SET fileRegisterDate = now(), filePath = ?, fileURI= ?, fileConvert=0, fileServURI=?, fileExt= ?  WHERE fileId=? ;";
            JdbcDao.update(sql, new Object[]{path, url, servUrl, extStr, l_fileId});
        }

        return 0;    
    }


    // 추가는 1 리턴, 수정은 0 리턴
    public static int insertFile(String fileName, long fileSize, int fileType, String userId, String path, String url) throws SQLException {
        
//        @@ 마리아 DB에서는 에러 발생         
//        String[] outParmas = new String[]{new String()};  // @@ 기존 버그 해결. 무조건 String으로 받은 후 변환
//        JdbcDao.updateStored("{call sp_insert_file(?,?,?,?,?,?,?)} ", new Object[]{fileName, fileSize, fileType, userId, path, url}, outParmas);
//        return Integer.parseInt(outParmas[0].toString());

        String sql = "SELECT userNo FROM user WHERE userName=? ;";
        int userNo = JdbcDao.queryForInt(sql, new Object[]{userId});
        
        sql = "SELECT count(*) FROM file WHERE fileUser=? and fileName=? and fileType=? ;";
        int l_count = JdbcDao.queryForInt(sql, new Object[]{userNo, fileName, fileType });
        
        String extStr = FilenameUtils.getExtension(path);
        
	if(l_count == 0){
            JdbcDao.update("INSERT INTO file (  " +
"                                       fileName," +
"					fileSize," +
"					fileType," +
"					fileUser," +
"					fileShare," +
"					fileLog," +
"					fileRegisterDate," +
"					filePath," +
"                                        fileExt," +
"					fileURI)" +
"				  VALUES (" +
"					?, " +
"					?," +
"					?," +
"					?," +
"					1," +
"					\"\"," +
"					now()," +
"					?," +
"                                       ?," +
"					?" +
"             ); ", new Object[]{fileName, fileSize, fileType, userNo, path, extStr ,url});
            
            return 1;
            
        }else{
            sql = "SELECT fileId FROM file WHERE fileUser=? and fileName=? and fileType=? ; ";
            int l_fileId = JdbcDao.queryForInt(sql, new Object[]{userNo, fileName, fileType });
            sql = "UPDATE file SET fileRegisterDate = now(), filePath = ?, fileURI= ?, fileConvert=0, fileServURI='', fileExt= ?  WHERE fileId=? ;";
            JdbcDao.update(sql, new Object[]{path, url, extStr, l_fileId});
        }

        return 0;    
    }
   
       
    public static void convertFile(long fileId, String serviceURL) throws SQLException {
        String query = "UPDATE file SET fileConvert = 1, fileServURI='" + serviceURL + "' WHERE fileId = " + fileId;
        JdbcDao.update(query);
        
    }
    
   
    public static void deleteFile(String fileName) throws SQLException {
        
       if(!fileName.equals("")) {
            JdbcDao.update("DELETE FROM file where fileName = ? ; " ,  new Object[]{fileName});
        }
    }
   
    public static void deleteFile(long fileId) throws SQLException {
        
        JdbcDao.update("DELETE FROM file where fileId = " + fileId + " ;" );
        
    }
    
    public static void deleteUserFiles(String userId) throws SQLException {
        
       if(!userId.equals("")) {
            JdbcDao.update("DELETE FROM file "
                    + "where fileUser=(select userNo from user where userId = '" + userId +  "' ) ");
        }
    }
    
    public static void updatePermission(long fileId, int newPermissionValue) throws SQLException {
        JdbcDao.update("UPDATE file SET fileShare=? WHERE fileId = ? ", new Object[]{newPermissionValue, fileId});
    }
    
    /**
     * 파일이 유저의 소유인지 확인
     * @param userId
     * @param fileId
     * @return  : 어드민이면 2 리턴
     */
    public static int checkUserFile(String userId, int fileId){
        
        if(UserDAO.checkAdmin(userId)){
            return 2;
        }
        
        String sql = "select count(*) "
                + "from file, (select userNo from user where userId =? ) t "
                + "where fileUser=t.userNo and fileId=? ";
        int count = 0;
          
        try {
            count = JdbcDao.queryForInt(sql, new Object[]{userId, fileId}); 
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        if(count>0){
            return 1;
        }
        return 0;
    }
    
       
    public static int updateShare(int fileId, int newValue) {
        
        try{
              JdbcDao.update("UPDATE file SET fileShare = fileShare + (" + newValue + " ) WHERE fileId = ? ", new Object[]{fileId});
            
        }catch(SQLException e){
            System.out.println("updateShare Error");
            return 0;
        }
        return 1;
    }
    
    
    
    private static String addOrderBy(String query, int orderOption){
        String queryStr = "";
        
        if(orderOption == FileDAO.FILESORT_DATE_DESC ){
            queryStr = query + " order by fileRegisterDate desc " ;
            
        } else if(orderOption == FileDAO.FILESORT_DATE_ASC ){
            queryStr = query + " order by fileRegisterDate asc " ;
            
        } else if(orderOption == FileDAO.FILESORT_NAME_DESC ){
            queryStr = query + " order by fileName desc " ;
            
        } else if(orderOption == FileDAO.FILESORT_NAME_ASC ){
            queryStr = query + " order by fileName asc " ;
            
        } else if(orderOption == FileDAO.FILESORT_USER_DESC ){
            queryStr = query + " order by fileUserName desc " ;
            
        } else if(orderOption == FileDAO.FILESORT_USER_ASC ){
            queryStr = query + " order by fileUserName asc " ;
            
        }else{
            queryStr = query;
        }
        
        return queryStr;
    }
}
