package com.codeenator.dao;

import com.ithows.JdbcDao;
import com.ithows.ResultMap;
import com.ithows.util.DateTimeUtils;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class FileDAO {
    
    /**
     * 첨부파일 조회
     * @param uuid          첨부파일 UUID
     * @return              첨부파일
     * @throws SQLException 
     */
    public static ResultMap selectFileByUUID(String uuid) throws SQLException {
        String query = "SELECT  *"
                + "     FROM    file"
                + "     WHERE   uuid = ?"
                + "     AND     is_deleted = FALSE";
        
        return JdbcDao.queryForMapObject(query, new Object[]{uuid});
    }
    
    
    /**
     * 첨부파일 목록 조회
     * @param contextPath   context path
     * @param uuidList      첨부파일 UUID
     * @return              첨부파일 목록
     * @throws SQLException 
     */
    public static List<ResultMap> selectFileList(String contextPath, List<String> uuidList) throws SQLException {
        String query = "SELECT  CONCAT(?, path) AS path,"
                + "             original_name AS name,"
                + "             stored_name"
                + "     FROM    file"
                + "     WHERE   uuid IN (";
        
        if (!uuidList.isEmpty()) {
            query += uuidList.stream()
                    .map(m -> "'" + m + "'")
                    .collect(Collectors.joining(","));
        } else {
            query += "              ''";
        }
        
        query += "      )";
        
        return JdbcDao.queryForMapList(query, new Object[]{contextPath});
    }
    
    
    /**
     * 첨부파일 저장
     * @param uuid          UUID 
     * @param path          경로
     * @param originalName  원래 이름
     * @param storedName    저장된 이름
     * @param size          크기
     * @return              저장 여부
     * @throws SQLException 
     */
    public static int insertFile(   String uuid,
                                    String path,
                                    String originalName,
                                    String storedName,
                                    long size) throws SQLException {
        String query = "INSERT INTO file(   uuid,"
                + "                         path,"
                + "                         original_name,"
                + "                         stored_name,"
                + "                         size,"
                + "                         upload_datetime"
                + "     )"
                + "     VALUES( ?, ?, ?, ?, ?,"
                + "             ?"
                + "     )";
        
        String datetime = DateTimeUtils.getTimeDateNow();                       // yyyy-MM-dd HH:mm:ss
        
        return JdbcDao.update(query, new Object[]{  uuid,
                                                    path,
                                                    originalName,
                                                    storedName,
                                                    size,
                                                    datetime
        });
    }
    
    
    /**
     * 첨부파일 저장
     * @param fileList      첨부파일 목록
     * @return              저장 여부
     * @throws SQLException 
     */
    public static int insertFileList(List<ResultMap> fileList) throws SQLException {
        String query = "INSERT INTO file(   uuid,"
                + "                         original_name,"
                + "                         stored_name,"
                + "                         path,"
                + "                         size,"
                + "                         upload_datetime"
                + "     )"
                + "     VALUES";
        
        String datetime = DateTimeUtils.getTimeDateNow();                       // yyyy-MM-dd HH:mm:ss
        
        // 파일 정보를 Query 형식으로 변경
        query += fileList.stream()
                    .map(m ->   
                            "(" +
                                "'" + m.getString("uuid") + "'," +
                                "'" + m.getString("original_name") + "'," +
                                "'" + m.getString("stored_name") + "'," +
                                "'" + m.getString("path") + "'," +
                                "'" + m.getInt("size") + "'," +
                                "'" + datetime + "'" +
                            ")"
                    )
                    .collect(Collectors.joining(","));
        
        return JdbcDao.update(query, new Object[]{});
    }
    
    
    /**
     * 파일 업로드 활성화
     * @param file          파일
     * @return              업로드 활성화 여부
     * @throws SQLException 
     */
    public static int updateEnableUpload(String file) throws SQLException {
        String query = "UPDATE  file"
                + "     SET     enable_upload = TRUE"
                + "     WHERE   stored_name = ?";
        
        return JdbcDao.update(query, new Object[]{file});
    }
    
    
    /**
     * 파일 업로드 활성화
     * @param fileList      파일 목록
     * @return              업로드 활성화 여부
     * @throws SQLException 
     */
    public static int updateEnableUpload(List<String> fileList) throws SQLException {
        String query = "UPDATE  file"
                + "     SET     enable_upload = TRUE"
                + "     WHERE   FIND_IN_SET(stored_name, ?)";
        
        String file = fileList.stream()
                                .map(item -> item.substring(item.lastIndexOf("/") + 1))
                                .collect(Collectors.joining(","));
        
        return JdbcDao.update(query, new Object[]{file});
    }
    
    
    /**
     * 파일 삭제
     * @param file          파일
     * @return              삭제 여부
     * @throws SQLException 
     */
    public static int updateDeleteFile(String file) throws SQLException {
        String query = "UPDATE  file"
                + "     SET     is_deleted = TRUE,"
                + "             delete_datetime = ?"
                + "     WHERE   stored_name = ?"
                + "     AND     is_deleted = FALSE";
        
        String datetime = DateTimeUtils.getTimeDateNow();                       // yyyy-MM-dd HH:mm:ss
        
        return JdbcDao.update(query, new Object[]{  datetime,
                                                    file
        });
    }
    
    
    /**
     * 파일 삭제
     * @param fileList      파일
     * @return              삭제 여부
     * @throws SQLException 
     */
    public static int deleteFileList(List<String> fileList) throws SQLException {
        String query = "DELETE FROM     file"
                + "     WHERE           FIND_IN_SET(stored_name, ?)";
        
        String files = fileList.stream()
                                .map(item -> item.substring(item.lastIndexOf("/") + 1))
                                .collect(Collectors.joining(","));

        return JdbcDao.update(query, new Object[]{files});
    }
}
