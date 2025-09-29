package com.codeenator.dao;

import com.ithows.JdbcDao;
import com.ithows.util.DateTimeUtils;
import java.sql.SQLException;

public class UserDAO {
                                                            // 유저 유형
    public static final int USER_TYPE_ADMIN = 1;            // 관리자
    public static final int USER_TYPE_PUBLIC = 2;           // 일반 유저
    
    /**
     * 아이디를 통해 유저 번호 조회
     * - 아이디 중복 확인 시 사용
     * @param id            아이디
     * @return              아이디 중복 여부
     * @throws SQLException 
     */
    public static int selectSeqById(String id) throws SQLException {
        String query = "SELECT  seq"
                + "     FROM    user"
                + "     WHERE   userId = ?"
                + "     AND     is_deleted = FALSE";
        
        return JdbcDao.queryForInt(query, new Object[]{id});
    }
    
    
    /**
     * 이메일을 통해 유저 번호 조회
     * - 이메일 중복 확인 시 사용
     * @param email         이메일
     * @return              유저 번호
     * @throws SQLException 
     */
    public static int selectSeqByEmail(String email) throws SQLException {
        String query = "SELECT  seq"
                + "     FROM    user"
                + "     WHERE   email = ?"
                + "     AND     is_deleted = FALSE";
        
        return JdbcDao.queryForInt(query, new Object[]{email});
    }
    
    
    /**
     * 닉네임을 통해 유저 번호 조회
     * - 닉네임 중복 확인 시 사용
     * @param nickname      닉네임
     * @return              유저 번호
     * @throws SQLException 
     */
    public static int selectSeqByNickname(String nickname) throws SQLException {
        String query = "SELECT  seq"
                + "     FROM    user"
                + "     WHERE   nickname = ?"
                + "     AND     is_deleted = FALSE";
        
        return JdbcDao.queryForInt(query, new Object[]{nickname});
    }

    
    /**
     * 유저 번호와 비밀번호를 통해 유저 번호 조회
     * - 현재 비밀번호 확인 사용
     * @param seq           유저 번호
     * @param password      비밀번호
     * @return              유저 번호
     * @throws SQLException 
     */
    public static int selectSeqBySeqAndPassword(int seq, String password) throws SQLException {
        String query = "SELECT  seq"
                + "     FROM    user"
                + "     WHERE   seq = ?"
                + "     AND     userPswd = password(?)";
        
        return JdbcDao.queryForInt(query, new Object[]{ seq, 
                                                        password
        });
    }
    
    
    /**
     * 아이디와 비밀번호를 통해 유저 번호 조회
     * - 비밀번호 찾기 시 사용
     * @param id            아이디
     * @param email         이메일
     * @return              유저 번호
     * @throws SQLException 
     */
    public static int selectSeqByIdAndEmail(String id, String email) throws SQLException {
        String query = "SELECT  seq"
                + "     FROM    user"
                + "     WHERE   userId = ?"
                + "     AND     email = ?"
                + "     AND     is_deleted = FALSE";
        
        return JdbcDao.queryForInt(query, new Object[]{ id, 
                                                        email
        });
    }
    
    
    /**
     * 이메일을 통해 아이디 조회
     * - 아이디 찾기 시 사용
     * @param email         이메일
     * @return              아이디
     * @throws SQLException 
     */
    public static String selectIdByEmail(String email) throws SQLException {
        String query = "SELECT  userId"
                + "     FROM    user"
                + "     WHERE   email = ?"
                + "     AND     is_deleted = FALSE";
        
        return JdbcDao.queryForString(query, new Object[]{email});
    }
    
    
    /**
     * 회원가입
     * @param type          유저 유형
     * @param id            아이디
     * @param password      비밀번호
     * @param email         이메일
     * @param nickname      닉네임
     * @return              회원가입 여부
     * @throws SQLException 
     */
    public static int insertUser(   int type,
                                    String id,
                                    String password,
                                    String email,
                                    String nickname) throws SQLException {
        String query = "INSERT INTO user(   type,"
                + "                         userId,"
                + "                         userPswd,"
                + "                         email,"
                + "                         nickname,"
                + "                         register_datetime"
                + "     )"
                + "     VALUES( ?, ?, password(?), ?, ?,"
                + "             ?"
                + "     )";
        
        String datetime = DateTimeUtils.getTimeDateNow();                       // yyyy-MM-dd HH:mm:ss
        
        return JdbcDao.update(query, new Object[]{  type,
                                                    id,
                                                    password,
                                                    email,
                                                    nickname,
                                                    datetime
        });
    }
    
    
    /**
     * 회원정보 변경
     * @param seq           유저 번호
     * @param email         이메일
     * @param nickname      닉네임
     * @return              변경 여부
     * @throws SQLException 
     */
    public static int updateUser(   int seq,
                                    String email,
                                    String nickname) throws SQLException {
        String query = "UPDATE  user"
                + "     SET     email = ?,"
                + "             nickname = ?,"
                + "             update_datetime = ?"
                + "     WHERE   seq = ?";
        
        String datetime = DateTimeUtils.getTimeDateNow();                       // yyyy-MM-dd HH:mm:ss
        
        return JdbcDao.update(query, new Object[]{  email,
                                                    nickname,
                                                    datetime,
                                                    seq
        });
    }
    
    
    /**
     * 비밀번호 변경
     * @param seq           유저 번호
     * @param password      비밀번호
     * @return              변경 여부
     * @throws SQLException 
     */
    public static int updatePassword(int seq, String password) throws SQLException {
        String query = "UPDATE  user"
                + "     SET     userPswd = password(?),"
                + "             update_datetime = ?"
                + "     WHERE   seq = ?";
        
        String datetime = DateTimeUtils.getTimeDateNow();                       // yyyy-MM-dd HH:mm:ss
        
        return JdbcDao.update(query, new Object[]{  password,
                                                    datetime,
                                                    seq
        });
    }
    
    
    /**
     * 접속 수 증가
     * @param seq           유저 번호
     * @return              증가 여부
     */
    public static int updateLoginCount(int seq) {
        String query = "UPDATE  user"
                + "     SET     login_count = login_count + 1"
                + "     WHERE   seq = ?";
        
        try {
            return JdbcDao.update(query, new Object[]{seq});
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
    
    
    /**
     * 회원 탈퇴
     * @param seq           유저 번호
     * @return              탈퇴 여부
     * @throws SQLException 
     */
    public static int updateDisableUser(int seq) throws SQLException {
        String query = "UPDATE  user"
                + "     SET     is_deleted = TRUE,"
                + "             delete_datetime = ?"
                + "     WHERE   seq = ?"
                + "     AND     is_deleted = FALSE";
        
        String datetime = DateTimeUtils.getTimeDateNow();                       // yyyy-MM-dd HH:mm:ss
        
        return JdbcDao.update(query, new Object[]{  datetime,
                                                    seq
        });
    }
}
