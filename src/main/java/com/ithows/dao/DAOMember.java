/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ithows.dao;

import com.ithows.JdbcDao;
import com.ithows.ResultMap;
import java.sql.SQLException;


public class DAOMember {

    /**
     * 매개변수로 입력된 User ID가 DB에 존재하는지 확인하는 함수
     *
     * @param userId
     * @return
     */
    public static int getCountFromUserId(String userId) {
        int result = 0;
        try {
            result = JdbcDao.queryForInt("select count(*) from member where userId=?", new Object[]{userId});
        } catch (SQLException e) {
            result = -1;
            e.printStackTrace();
        }
        return result;
    }

    public static int insertNewMember(String userId, String userPswd, String userName, String userCellPhone, int userSecurityLevel, String userMemo, int userType) {
        
        Object[] outParam = new Object[]{new Object()};//1
        try {
            String query = "{call sp_insert_member(?,password(?),?,?,?,?,?,?)}";
            /*회사명을 여기서 넣어준다.*/
            com.ithows.JdbcDao.updateStored(query, new Object[]{userId, userPswd, userName, userCellPhone, userSecurityLevel, userMemo, userType}, outParam);//16
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Integer.parseInt(outParam[0].toString());
    }
    
    

    /* @@ member : ResultMap 전환 부분 >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> */
    public static ResultMap selectMember(int userNo) throws SQLException {
        return JdbcDao.queryForMapObject("SELECT * FROM member WHERE userNo = ?", new Object[]{userNo});
    }
    
    public static ResultMap selectMember(String userId) throws SQLException {
        return JdbcDao.queryForMapObject("SELECT * FROM member WHERE userId = ?", new Object[]{userId});
    }

    public static ResultMap selectMember(String userId, String userPswd) throws SQLException {
        return JdbcDao.queryForMapObject("SELECT * FROM member WHERE userId = ? AND userPswd = password(?)", new Object[]{userId, userPswd});
    }
     /* >>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>  @@ member : ResultMap 전환 부분  */
    
    
    
    
    public static int updatePswd(int userNo, String userPswd) throws SQLException {
        return JdbcDao.update("UPDATE member SET userPswd = password(?) WHERE userNo = ?", new Object[]{userPswd, userNo});
    }
    
    public static int updateMember(int userNo, String userName, String userCellPhone) throws SQLException {
        return JdbcDao.update("UPDATE member SET userName=?, userCellPhone=? WHERE userNo = ?", new Object[]{userName, userCellPhone, userNo});
    }
    public static int updateCellPhone(int userNo, String userCellPhone) throws SQLException {
        return JdbcDao.update("UPDATE member SET userCellPhone=? WHERE userNo = ?", new Object[]{userCellPhone, userNo});
    }
    public static int updateName(int userNo, String userName) throws SQLException {
        return JdbcDao.update("UPDATE member SET userName=? WHERE userNo = ?", new Object[]{userName, userNo});
    }
    
    public static int verifyPswd(int userNo, String userPswd) throws SQLException {
        return JdbcDao.queryForInt("SELECT count(*) from member WHERE userNo = ? and userPswd=password(?)", new Object[]{userNo, userPswd});
    }
}
