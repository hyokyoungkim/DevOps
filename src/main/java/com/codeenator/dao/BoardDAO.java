package com.codeenator.dao;

import com.ithows.JdbcDao;
import com.ithows.ResultMap;
import com.ithows.util.DateTimeUtils;
import java.sql.SQLException;
import java.util.List;
import java.util.stream.Collectors;

public class BoardDAO {
                                                        // 게시판 유형
    public static int BOARD_TYPE_NOTICE = 1;            // 공지사항
    public static int BOARD_TYPE_QUESTION = 2;          // Q&A
    
                                                        // 검색 유형
    public static int SEARCH_OPTION_ALL = 1;            // 제목, 내용
    public static int SEARCH_OPTION_TITLE = 2;          // 제목
    public static int SEARCH_OPTION_CONTENT = 3;        // 내용
    
    /**
     * 게시글 조회
     * @param seq           게시글 번호
     * @return              게시글
     * @throws SQLException 
     */
    public static ResultMap selectContentBySeq(int seq) throws SQLException {
        String query = "SELECT  *"
                + "     FROM    board_content"
                + "     WHERE   seq = ?"
                + "     AND     is_deleted = FALSE";
        
        ResultMap resultMap = JdbcDao.queryForMapObject(query, new Object[]{seq});
        
        if (resultMap != null) {
            // 첨부파일
            query = "   SELECT  CONCAT(f.path, '/', f.stored_name) AS file"
                    + " FROM    board_content bc"
                    + " JOIN    file f"
                    + " ON      FIND_IN_SET(f.stored_name, bc.file) > 0"
                    + " WHERE   seq = ?";
            
            List<ResultMap> resultList = JdbcDao.queryForMapList(query, new Object[]{seq});
            
            // ResultMap convert to String
            List<String> fileList = resultList.stream()
                                                .map(map -> map.getString("file"))
                                                .collect(Collectors.toList());
            
            resultMap.put("file", fileList);
        }
        
        return resultMap;
    }
    
    
    /**
     * 공지사항 게시글 목록 개수 조회
     * @param searchOption  검색 유형
     * @param searchText    검색어    
     * @return              게시글 목록 개수
     * @throws SQLException 
     */
    public static int selectNoticeListCount(int searchOption, String searchText) throws SQLException {
        String query = "    SELECT  COUNT(*)"
                + "         FROM    board b"
                + "         JOIN    board_content bc"
                + "         ON      b.seq = bc.board_ref"
                + "         AND     bc.is_secret = FALSE"
                + "         AND     bc.is_deleted = FALSE"
                + "         WHERE   b.seq = ?";
        
        // 검색 
        if (searchText != null) {
            if (searchOption == SEARCH_OPTION_ALL) {
                query += "  AND     ("
                        + "             bc.title LIKE CONCAT('%', ?, '%')"
                        + "             OR"
                        + "             bc.content LIKE CONCAT('%', ?, '%')"
                        + "         )";
                
                return JdbcDao.queryForInt(query, new Object[]{ BOARD_TYPE_NOTICE,
                                                                searchText,
                                                                searchText
                });
            } else if (searchOption == SEARCH_OPTION_TITLE) {
                query += "  AND     bc.title LIKE CONCAT('%', ?, '%')";
                
                return JdbcDao.queryForInt(query, new Object[]{ BOARD_TYPE_NOTICE,
                                                                searchText
                });
            } else if (searchOption == SEARCH_OPTION_CONTENT) {
                query += "  AND     bc.content LIKE CONCAT('%', ?, '%')";
                
                return JdbcDao.queryForInt(query, new Object[]{ BOARD_TYPE_NOTICE,
                                                                searchText
                });
            }
        } 
        
        return JdbcDao.queryForInt(query, new Object[]{BOARD_TYPE_NOTICE});
    }
    
    
    /**
     * 공지사항 게시글 목록 조회
     * @param searchOption  검색 유형
     * @param searchText    검색어
     * @param startCount    첫 게시물 순번
     * @param count         개수
     * @return              게시글 목록
     * @throws SQLException 
     */
    public static List<ResultMap> selectNoticeList( int searchOption,
                                                    String searchText,
                                                    int startCount,
                                                    int count) throws SQLException {
        String query = "    SELECT      bc.seq,"
                + "                     bc.title,"
                + "                     bc.view_count,"
                + "                     DATE_FORMAT(bc.register_datetime, '%Y-%m-%d') AS datetime,"
                + "                     u.nickname"
                + "         FROM        board b"
                + "         JOIN        board_content bc"
                + "         ON          b.seq = bc.board_ref"
                + "         AND         bc.is_secret = FALSE"
                + "         AND         bc.is_deleted = FALSE"
                + "         JOIN        user u"
                + "         ON          u.seq = bc.user_ref"
                + "         WHERE       b.seq = ?";
        
        // 검색
        if (searchText != null) {
            if (searchOption == SEARCH_OPTION_ALL) {
                query += "  AND         ("
                        + "                 bc.title LIKE CONCAT('%', ?, '%')"
                        + "                 OR"
                        + "                 bc.content LIKE CONCAT('%', ?, '%')"
                        + "             )";
            } else if (searchOption == SEARCH_OPTION_TITLE) {
                query += "  AND         bc.title LIKE CONCAT('%', ?, '%')";
            } else if (searchOption == SEARCH_OPTION_CONTENT) {
                query += "  AND         bc.content LIKE CONCAT('%', ?, '%')";
            }
        }

        query += "          ORDER BY    bc.seq DESC"
                + "         LIMIT       ?, ?";
        
        if (searchText != null) {
            if (searchOption == SEARCH_OPTION_ALL) {
                return JdbcDao.queryForMapList(query, new Object[]{ BOARD_TYPE_NOTICE,
                                                                    searchText,
                                                                    searchText,
                                                                    startCount,
                                                                    count
                });
            } else if (searchOption == SEARCH_OPTION_TITLE || searchOption == SEARCH_OPTION_CONTENT) {
                return JdbcDao.queryForMapList(query, new Object[]{ BOARD_TYPE_NOTICE,
                                                                    searchText,
                                                                    startCount,
                                                                    count
                });
            }
        }
        
        return JdbcDao.queryForMapList(query, new Object[]{ BOARD_TYPE_NOTICE,
                                                            startCount,
                                                            count
        });
    }
    
    
    /**
     * 공지사항 게시글 조회
     * @param seq           게시글 번호
     * @return              게시글
     * @throws SQLException 
     */
    public static ResultMap selectNoticeBySeq(int seq) throws SQLException {
        String query = "SELECT  *"
                + "     FROM    board_content"
                + "     WHERE   board_ref = ?"
                + "     AND     seq = ?"
                + "     AND     is_deleted = FALSE";
        
        ResultMap resultMap = JdbcDao.queryForMapObject(query, new Object[]{BOARD_TYPE_NOTICE,
                                                                            seq
        });
        
        if (resultMap != null) {
            // 첨부파일
            query = "   SELECT  CONCAT(f.path, '/', f.stored_name) AS file"
                    + " FROM    board_content bc"
                    + " JOIN    file f"
                    + " ON      FIND_IN_SET(f.stored_name, bc.file) > 0"
                    + " WHERE   seq = ?";
            
            List<ResultMap> resultList = JdbcDao.queryForMapList(query, new Object[]{seq});
            
            // ResultMap convert to String
            List<String> fileList = resultList.stream()
                                                .map(map -> map.getString("file"))
                                                .collect(Collectors.toList());
            
            resultMap.put("file", fileList);
        }
            
        return resultMap;
    }
    
    
    /**
     * 공지사항 게시글 조회
     * @param seq           게시글 번호
     * @return              게시글
     * @throws SQLException 
     */
    public static ResultMap selectDetailNoticeBySeq(int seq) throws SQLException {
        String query = "SELECT  bc.seq,"
                + "             bc.title,"
                + "             bc.content,"
                + "             ("
                + "                 SELECT  IFNULL("
//                + "                             JSON_ARRAYAGG("
                + "                             CONCAT(\"[\", GROUP_CONCAT("
                + "                                         JSON_OBJECT("
                + "                                                     'path', CONCAT(path, '/', stored_name),"
                + "                                                     'name', original_name"
                + "                                         )"
                + "                             ) , \"]\") "
                + "                         , NULL)"
                + "                 FROM    file"
                + "                 WHERE   FIND_IN_SET(stored_name, bc.file)"
                + "                 AND     is_deleted = FALSE"
                + "             ) AS file,"
                + "             bc.view_count,"
                + "             DATE_FORMAT(bc.register_datetime, '%Y-%m-%d') AS datetime,"
                + "             u.nickname AS user_nickname"
                + "     FROM    board_content bc"
                + "     JOIN    user u"
                + "     ON      u.seq = bc.user_ref"
                + "     WHERE   bc.board_ref = ?"
                + "     AND     bc.seq = ?"
                + "     AND     bc.is_deleted = FALSE";
        
        return JdbcDao.queryForMapObject(query, new Object[]{   BOARD_TYPE_NOTICE,
                                                                seq
        });
    }
    
    
    /**
     * Q&A 게시글 목록 개수 조회
     * @param searchOption  검색 유형
     * @param searchText    검색어
     * @return              게시글 목록 개수
     * @throws SQLException 
     */
    public static int selectQuestionListCount(int searchOption, String searchText) throws SQLException {
        String query = "    SELECT  COUNT(*)"
                + "         FROM    board b"
                + "         JOIN    board_content bc"
                + "         ON      b.seq = bc.board_ref"
                + "         AND     bc.is_secret = FALSE"
                + "         AND     bc.is_deleted = FALSE"
                + "         JOIN    user u"
                + "         ON      u.seq = bc.user_ref"
                + "         WHERE   b.seq = ?";
        
        // 검색
        if (searchText != null) {
            if (searchOption == SEARCH_OPTION_ALL) {
                query += "  AND     ("
                        + "             bc.title LIKE CONCAT('%', ?, '%')"
                        + "             OR"
                        + "             bc.content LIKE CONCAT('%', ?, '%')"
                        + "         )";
            } else if (searchOption == SEARCH_OPTION_TITLE) {
                query += "  AND     bc.title LIKE CONCAT('%', ?, '%')";
            } else if (searchOption == SEARCH_OPTION_CONTENT) {
                query += "  AND     bc.content LIKE CONCAT('%', ?, '%')";
            }
        }

        if (searchText != null) {
            if (searchOption == SEARCH_OPTION_ALL) {
                return JdbcDao.queryForInt(query, new Object[]{ BOARD_TYPE_QUESTION,
                                                                searchText,
                                                                searchText
                });
            } else if (searchOption == SEARCH_OPTION_TITLE || searchOption == SEARCH_OPTION_CONTENT) {
                return JdbcDao.queryForInt(query, new Object[]{ BOARD_TYPE_QUESTION,
                                                                searchText
                });
            }
        }
        
        return JdbcDao.queryForInt(query, new Object[]{BOARD_TYPE_QUESTION});
    }
    
    
    /**
     * Q&A 게시글 목록 조회
     * @param searchOption  검색 유형
     * @param searchText    검색어
     * @param startCount    첫 게시물 순번
     * @param count         개수
     * @return              게시글 목록
     * @throws SQLException 
     */
    public static List<ResultMap> selectQuestionList(   int searchOption,
                                                        String searchText,
                                                        int startCount,
                                                        int count) throws SQLException {
        String query = "    SELECT      bc.seq,"
                + "                     bc.title,"
                + "                     bc.view_count,"
                + "                     bc.is_secret,"
                + "                     DATE_FORMAT(bc.register_datetime, '%Y-%m-%d') AS datetime,"
                + "                     u.userId AS user_id,"
                + "                     u.nickname AS user_nickname"
                + "         FROM        board b"
                + "         JOIN        board_content bc"
                + "         ON          b.seq = bc.board_ref"
                + "         AND         bc.is_secret = FALSE"
                + "         AND         bc.is_deleted = FALSE"
                + "         JOIN        user u"
                + "         ON          u.seq = bc.user_ref"
                + "         WHERE       b.seq = ?";
        
        // 검색
        if (searchOption > 0 && searchText != null) {
            if (searchOption == SEARCH_OPTION_ALL) {
                query += "  AND         ("
                        + "                 bc.title LIKE CONCAT('%', ?, '%')"
                        + "                 OR"
                        + "                 bc.content LIKE CONCAT('%', ?, '%')"
                        + "             )";
            } else if (searchOption == SEARCH_OPTION_TITLE) {
                query += "  AND         bc.title LIKE CONCAT('%', ?, '%')";
            } else if (searchOption == SEARCH_OPTION_CONTENT) {
                query += "  AND         bc.content LIKE CONCAT('%', ?, '%')";
            }
        } 
        
        query += "      ORDER BY    bc.seq DESC"
                + "     LIMIT       ?, ?";
        
        if (searchOption > 0 && searchText != null) {
            if (searchOption == SEARCH_OPTION_ALL) {
                return JdbcDao.queryForMapList(query, new Object[]{ BOARD_TYPE_QUESTION,
                                                                    searchText,
                                                                    searchText,
                                                                    startCount,
                                                                    count
                });
            } else {
                return JdbcDao.queryForMapList(query, new Object[]{ BOARD_TYPE_QUESTION,
                                                                    searchText,
                                                                    startCount,
                                                                    count
                });
            }
        }
        
        return JdbcDao.queryForMapList(query, new Object[]{ BOARD_TYPE_QUESTION,
                                                            startCount,
                                                            count
        });
    }
    
    
    /**
     * Q&A 게시글 조회
     * @param seq           게시글 번호
     * @return              게시글
     * @throws SQLException 
     */
    public static ResultMap selectQuestionBySeq(int seq) throws SQLException {
        String query = "SELECT  *"
                + "     FROM    board_content bc"
                + "     WHERE   board_ref = ?"
                + "     AND     seq = ?"
                + "     AND     is_deleted = FALSE";
        
        ResultMap resultMap = JdbcDao.queryForMapObject(query, new Object[]{BOARD_TYPE_QUESTION,
                                                                            seq
        });
                
        if (resultMap != null) {
            // 첨부파일
            query = "   SELECT  CONCAT(f.path, '/', f.stored_name) AS file"
                    + " FROM    board_content bc"
                    + " JOIN    file f"
                    + " ON      FIND_IN_SET(f.stored_name, bc.file) > 0"
                    + " WHERE   seq = ?";
            
            List<ResultMap> resultList = JdbcDao.queryForMapList(query, new Object[]{seq});
            
            // ResultMap convert to String
            List<String> fileList = resultList.stream()
                                                .map(map -> map.getString("file"))
                                                .collect(Collectors.toList());
            
            resultMap.put("file", fileList);
        }
            
        return resultMap;
    }
    
    
    /**
     * Q&A 게시글 조회
     * @param seq           게시글 번호
     * @return              게시글
     * @throws SQLException 
     */
    public static ResultMap selectDetailQuestionBySeq(int seq) throws SQLException {
        String query = "SELECT  bc.seq,"
                + "             bc.title,"
                + "             bc.content,"
                + "             ("
//                + "                 SELECT  IFNULL("
//                + "                             JSON_ARRAYAGG("
                + "                 SELECT  IF("
                + "                             CONCAT(\"[\", GROUP_CONCAT("
                + "                                         JSON_OBJECT("
                + "                                                     'path', CONCAT(path, '/', stored_name),"
                + "                                                     'name', original_name"
                + "                                         )"
                + "                             ) , \"]\") "
//                + "                         , NULL)"
                + "                         , '[]')"
                + "                 FROM    file"
                + "                 WHERE   FIND_IN_SET(stored_name, bc.file)"
                + "                 AND     is_deleted = FALSE"
                + "             ) AS file,"
                + "             bc.view_count,"
                + "             DATE_FORMAT(bc.register_datetime, '%Y-%m-%d') AS datetime,"
                + "             u.userId AS user_id,"
                + "             u.nickname AS user_nickname"
                + "     FROM    board_content bc"
                + "     JOIN    user u"
                + "     ON      u.seq = bc.user_ref"
                + "     WHERE   bc.board_ref = ?"
                + "     AND     bc.seq = ?"
                + "     AND     bc.is_deleted = FALSE";
        
String query2 = "SELECT bc.seq, "
              + "bc.title, "
              + "bc.content, "
              + "( "
              + "    SELECT IF( "
              + "        GROUP_CONCAT( "
              + "            JSON_OBJECT( "
              + "                'path', CONCAT(f.path, '/', f.stored_name), "
              + "                'name', f.original_name "
              + "            ) "
              + "        ) IS NULL, "
              + "        '[]', "
              + "        CONCAT('[', GROUP_CONCAT( "
              + "            JSON_OBJECT( "
              + "                'path', CONCAT(f.path, '/', f.stored_name), "
              + "                'name', f.original_name "
              + "            ) "
              + "        ), ']') "
              + "    ) AS file "
              + "    FROM file f "
              + "    WHERE FIND_IN_SET(f.stored_name, bc.file) "
              + "    AND f.is_deleted = FALSE "
              + ") AS file, "
              + "bc.view_count, "
              + "DATE_FORMAT(bc.register_datetime, '%Y-%m-%d') AS datetime, "
              + "u.userId AS user_id, "
              + "u.nickname AS user_nickname "
              + "FROM board_content bc "
              + "JOIN user u ON u.seq = bc.user_ref "
              + "WHERE bc.board_ref = ? "
              + "AND bc.seq = ? "
              + "AND bc.is_deleted = FALSE;";


        
        return JdbcDao.queryForMapObject(query2, new Object[]{   BOARD_TYPE_QUESTION,
                                                                seq
        });
    }
    
    
    /**
     * 댓글/수강평 목록 개수 조회
     * @param contentRef    게시물/앨범 번호
     * @return              댓글 목록 개수
     * @throws SQLException 
     */
    public static int selectCommentListCount(String contentRef) throws SQLException {
        String query = "SELECT  COUNT(*)"
                + "     FROM    board_comment"
                + "     WHERE   content_ref = ?"
                + "     AND     comment_ref = 0"
                + "     AND     is_deleted = FALSE";
        
        return JdbcDao.queryForInt(query, new Object[]{contentRef});
    }
    
    
    /**
     * 댓글/수강평 목록 조회
     * @param contentRef    게시글/앨범 번호
     * @param startCount    첫 게시물 순번
     * @param count         개수
     * @return              댓글 목록
     * @throws SQLException 
     */
    public static List<ResultMap> selectCommentList(String contentRef,
                                                    int startCount,
                                                    int count) throws SQLException {
        String query = "SELECT      bc1.seq,"
                + "                 bc1.comment,"
                + "                 DATE_FORMAT(bc1.register_datetime, '%Y-%m-%d') AS datetime,"
//                + "                 IF(bc2.seq, JSON_ARRAYAGG("
                + "                 IF(bc2.seq, CONCAT(\"[\", GROUP_CONCAT("
                + "                                             JSON_OBJECT("
                + "                                                         'seq',              bc2.seq,"
                + "                                                         'comment',          bc2.comment,"
                + "                                                         'datetime',         bc2.datetime,"
                + "                                                         'user_id',          bc2.id,"
                + "                                                         'user_nickname',    bc2.nickname"
                + "                                             )"
                + "                             ) , \"]\") "
                + "                 , '[]') AS reply,"
                + "                 u.userId AS user_id,"
                + "                 u.nickname AS user_nickname"
                + "     FROM        board_comment bc1"
                + "     LEFT JOIN   ("
                + "                     SELECT  bc.seq,"
                + "                             bc.comment_ref,"
                + "                             bc.comment,"
                + "                             DATE_FORMAT(bc.register_datetime, '%Y-%m-%d') AS datetime,"
                + "                             u.userId AS id,"
                + "                             u.nickname AS nickname"
                + "                     FROM    board_comment bc"
                + "                     JOIN    user u"
                + "                     ON      u.seq = bc.user_ref"
                + "                     WHERE   bc.comment_ref != 0"
                + "                     AND     bc.is_deleted = FALSE"
                + "                     AND     bc.is_secret = FALSE"
                + "                 ) AS bc2"
                + "     ON          bc1.seq = bc2.comment_ref"
                + "     JOIN        user u"
                + "     ON          u.seq = bc1.user_ref"
                + "     WHERE       bc1.content_ref = ?"
                + "     AND         bc1.comment_ref = 0"
                + "     AND         bc1.is_deleted = FALSE"
                + "     AND         bc1.is_secret = FALSE"
                + "     GROUP BY    bc1.seq"
                + "     ORDER BY    bc1.seq ASC"
                + "     LIMIT       ?, ?";
        
        return JdbcDao.queryForMapList(query, new Object[]{ contentRef,
                                                            startCount,
                                                            count
        });
    }
    
    
    /**
     * 댓글 조회
     * @param seq           댓글 번호
     * @return              댓글
     * @throws SQLException 
     */
    public static ResultMap selectCommentBySeq(int seq) throws SQLException {
        String query = "SELECT  *"
                + "     FROM    board_comment"
                + "     WHERE   seq = ?"
                + "     AND     is_deleted = FALSE";
        
        return JdbcDao.queryForMapObject(query, new Object[]{seq});
    }
    
    
    /**
     * 공지사항 게시글 생성
     * @param userRef       유저 번호
     * @param title         제목
     * @param content       내용
     * @param fileList      첨부파일
     * @return              생성 여부
     * @throws SQLException 
     */
    public static int insertNotice( int userRef,
                                    String title,
                                    String content,
                                    List<String> fileList) throws SQLException {
        String query = "INSERT INTO board_content(  board_ref,"
                + "                                 user_ref,"
                + "                                 title,"
                + "                                 content,"
                + "                                 file,"
                + "                                 register_datetime"
                + "     )"
                + "     VALUES( ?, ?, ?, ?, ?,"
                + "             ?"
                + "     )";
        
        String datetime = DateTimeUtils.getTimeDateNow();                       // yyyy-MM-dd HH:mm:ss
        
        // 파일 정보에서 UUID만 가져오기
        String file = null;
        if (fileList != null && !fileList.isEmpty()) {
            file = fileList.stream()
                        .map(item -> item.substring(item.lastIndexOf("/") + 1))
                        .collect(Collectors.joining(","));
        }
        
        return JdbcDao.update(query, new Object[]{  BOARD_TYPE_NOTICE,
                                                    userRef,
                                                    title,
                                                    content,
                                                    file,
                                                    datetime
        });
    }
    
    
    /**
     * Q&A 게시글 생성
     * @param userRef       유저 번호
     * @param title         제목
     * @param content       내용
     * @param fileList      첨부파일
     * @return              생성 여부
     * @throws SQLException 
     */
    public static int insertQuestion(   int userRef,
                                        String title,
                                        String content,
                                        List<String> fileList) throws SQLException {
        String query = "INSERT INTO board_content(  board_ref,"
                + "                                 user_ref,"
                + "                                 title,"
                + "                                 content,"
                + "                                 file,"
                + "                                 register_datetime"
                + "     )"
                + "     VALUES( ?, ?, ?, ?, ?,"
                + "             ?"
                + "     )";
        
        String datetime = DateTimeUtils.getTimeDateNow();                       // yyyy-MM-dd HH:mm:ss

        // 파일 정보에서 UUID만 가져오기
        String file = null;
        if (fileList != null && !fileList.isEmpty()) {
            file = fileList.stream()
                    .map(item -> item.substring(item.lastIndexOf("/") + 1))
                    .collect(Collectors.joining(","));
        }        
        
        return JdbcDao.update(query, new Object[]{  BOARD_TYPE_QUESTION,
                                                    userRef,
                                                    title,
                                                    content,
                                                    file,
                                                    datetime
        });
    }
    
    
    /**
     * 댓글 작성
     * @param contentRef    게시글 번호
     * @param userRef       유저 번호
     * @param commentRef    댓글 번호
     * @param comment       댓글
     * @return              작성 여부
     * @throws SQLException 
     */
    public static int insertComment(String contentRef,
                                    int userRef,
                                    int commentRef,
                                    String comment) throws SQLException {
        String query = "INSERT INTO board_comment(  content_ref,"
                + "                                 user_ref,"
                + "                                 comment_ref,"
                + "                                 comment,"
                + "                                 register_datetime"
                + "     )"
                + "     VALUES(?, ?, ?, ?, ?)";
        
        String datetime = DateTimeUtils.getTimeDateNow();                       // yyyy-MM-dd HH:mm:ss
        
        return JdbcDao.update(query, new Object[]{  contentRef,
                                                    userRef,
                                                    commentRef,
                                                    comment,
                                                    datetime
        });
    }
    
    
    /**
     * 공지사항 게시글 수정
     * @param seq           게시글 번호
     * @param title         제목
     * @param content       내용
     * @param fileList      첨부파일 목록
     * @return              수정 여부
     * @throws SQLException 
     */
    public static int updateNotice( int seq,
                                    String title,
                                    String content,
                                    List<String> fileList) throws SQLException {
        String query = "UPDATE  board_content"
                + "     SET     title = ?,"
                + "             content = ?,"
                + "             file = ?,"
                + "             update_datetime = ?"
                + "     WHERE   seq = ?"
                + "     AND     is_deleted = FALSE";
        
        String datetime = DateTimeUtils.getTimeDateNow();                       // yyyy-MM-dd HH:mm:ss
        
        // 파일 경로에서 이름만 가져오기
        String file = null;
        if (fileList != null && !fileList.isEmpty()) {
            file = fileList.stream()
                            .map(item -> item.substring(item.lastIndexOf("/") + 1))
                            .collect(Collectors.joining(","));
        }
        
        return JdbcDao.update(query, new Object[]{  title,
                                                    content,
                                                    file,
                                                    datetime,
                                                    seq
        });
    }
    
    
    /**
     * Q&A 게시글 수정
     * @param seq           게시글 번호
     * @param title         제목
     * @param content       내용
     * @param fileList      첨부파일 목록
     * @return              수정 여부
     * @throws SQLException 
     */
    public static int updateQuestion(   int seq,
                                        String title,
                                        String content,
                                        List<String> fileList) throws SQLException {
        String query = "UPDATE  board_content"
                + "     SET     title = ?,"
                + "             content = ?,"
                + "             file = ?,"
                + "             update_datetime = ?"
                + "     WHERE   seq = ?"
                + "     AND     is_deleted = FALSE";
        
        String datetime = DateTimeUtils.getTimeDateNow();                       // yyyy-MM-dd HH:mm:ss
        
        // 파일 경로에서 이름만 가져오기
        String file = null;
        if (fileList != null && !fileList.isEmpty()) {
            file = fileList.stream()
                    .map(item -> item.substring(item.lastIndexOf("/") + 1))
                    .collect(Collectors.joining(","));
        }
        
        return JdbcDao.update(query, new Object[]{  title,
                                                    content,
                                                    file,
                                                    datetime,
                                                    seq
        });
    }
    
    
    /**
     * 게시글 조회수 증가
     * @param seq           게시글 번호
     * @return              증가 여부
     * @throws SQLException 
     */
    public static int updateViewCount(int seq) throws SQLException {
        String query = "UPDATE  board_content"
                + "     SET     view_count = view_count + 1"
                + "     WHERE   seq = ?";
        
        return JdbcDao.update(query, new Object[]{seq});
    }
    
    
    /**
     * 댓글 수정
     * @param seq           댓글 번호
     * @param comment       댓글
     * @return              수정 여부
     * @throws SQLException 
     */
    public static int updateComment(int seq, String comment) throws SQLException {
        String query = "UPDATE  board_comment"
                + "     SET     comment = ?,"
                + "             update_datetime = ?"
                + "     WHERE   seq = ?";
        
        String datetime = DateTimeUtils.getTimeDateNow();                       // yyyy-MM-dd HH:mm:ss
        
        return JdbcDao.update(query, new Object[]{  comment,
                                                    datetime,
                                                    seq
        });
    }
    
    
    /**
     * 게시글 삭제
     * @param seq           게시글 번호
     * @return              삭제 여부
     * @throws SQLException 
     */
    public static int updateDisableContent(int seq) throws SQLException {
        String query = "UPDATE  board_content"
                + "     SET     file = null,"
                + "             is_deleted = TRUE,"
                + "             delete_datetime = ?"
                + "     WHERE   seq = ?"
                + "     AND     is_deleted = FALSE";
        
        String datetime = DateTimeUtils.getTimeDateNow();                       // yyyy-MM-dd HH:mm:ss
        
        return JdbcDao.update(query, new Object[]{  datetime,
                                                    seq
        });
    }
    
    
    /**
     * 댓글 삭제
     * @param seq           댓글 번호
     * @return              삭제 여부
     * @throws SQLException 
     */
    public static int updateDisableComment(int seq) throws SQLException {
        String query = "UPDATE  board_comment"
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
