package com.codeenator.dao;

import com.ithows.JdbcDao;
import com.ithows.ResultMap;
import com.ithows.util.DateTimeUtils;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;

public class AdminDAO {
                                                            // 유저 정렬 유형
    public static int USER_SORT_OPTION_LOGIN = 1;           // 접속순
    public static int USER_SORT_OPTION_NICKNAME = 2;        // 닉네임순
    public static int USER_SORT_OPTION_ALBUM = 3;           // 앨범수순
   
                                                            // 게시판 검색 유형
    public static int BOARD_SEARCH_OPTION_TITLE = 1;        // 제목
    public static int BOARD_SEARCH_OPTION_NICKNAME = 2;     // 닉네임
    
                                                            // 게시판 정렬 유형
    public static int BOARD_SORT_OPTION_RECENT = 1;         // 최신순
    public static int BOARD_SORT_OPTION_TITLE = 2;          // 제목순
    public static int BOARD_SORT_OPTION_NICKANME = 3;       // 닉네임순
    
                                                            // 댓글 검색 유형
    public static int COMMENT_SEARCH_OPTION_TITLE = 1;      // 제목
    public static int COMMENT_SEARCH_OPTION_NICKNAME = 2;   // 닉네임
    
                                                            // 댓글 정렬 유형
    public static int COMMENT_SORT_OPTION_RECENT = 1;       // 최신순
    public static int COMMENT_SORT_OPTION_TITLE = 2;        // 제목순
    public static int COMMENT_SORT_OPTION_NICKNAME = 3;     // 닉네임순
    
                                                            // 앨범 정렬 옵션
    public static int ALBUM_SORT_OPTION_RECENT = 1;         // 최신순
    public static int ALBUM_SORT_OPTION_TITLE = 2;          // 제목순
    public static int ALBUM_SORT_OPTION_NICKNAME = 3;       // 닉네임순
    public static int ALBUM_SORT_OPTINO_STATUS = 4;         // 상태순
    
    /**
     * 유저 목록 개수 조회
     * @param searchText    검색어
     * @return              유저 목록 개수
     * @throws SQLException 
     */
    public static int selectUserListCount(String searchText) throws SQLException {
        String query = "SELECT  COUNT(*)"
                + "     FROM    user"
                + "     WHERE   is_deleted = FALSE";
        
        if (searchText != null) {
            query += "  AND     nickname LIKE CONCAT('%', ?, '%')";
            
            return JdbcDao.queryForInt(query, new Object[]{searchText});
        } else {
            return JdbcDao.queryForInt(query, new Object[]{});
        }
    }
    
    
    /**
     * 유저 목록 조회
     * @param sortOption    정렬 유형
     * @param searchText    검색어
     * @param startCount    첫 목록 순번
     * @param count         개수
     * @return              유저 목록
     * @throws SQLException 
     */
    public static List<ResultMap> selectUserList(   int sortOption,
                                                    String searchText,
                                                    int startCount,
                                                    int count) throws SQLException {
        String query = "SELECT      u.seq,"
                + "                 u.userId AS id,"
                + "                 u.nickname,"
                + "                 u.comment,"
                + "                 u.login_count,"
                + "                 DATE_FORMAT(u.register_datetime, '%Y-%m-%d') AS datetime,"
                + "                 IFNULL("
                + "                         ("
                + "                             SELECT      COUNT(user_ref)"
                + "                             FROM        album"
                + "                             WHERE       user_ref = u.seq"
                + "                             AND         is_deleted = FALSE"
                + "                             GROUP BY    u.seq"
                + "                         )"
                + "                 , 0) AS album_count,"
                + "                 IFNULL("
                + "                         ("
                + "                             SELECT      COUNT(DISTINCT(album_ref))"
                + "                             FROM        album_view"
                + "                             WHERE       user_ref = u.seq"
                + "                             GROUP BY    u.seq"
                + "                         )"
                + "                 , 0) AS listen_count"
                + "     FROM        user u"
                + "     WHERE       u.is_deleted = FALSE";
        
        // 검색
        if (searchText != null) {
            query += "  AND         u.nickname LIKE CONCAT('%', ?, '%')";
        }
        
        query += "      GROUP BY    u.seq";
        
        // 정렬
        if (sortOption == USER_SORT_OPTION_LOGIN) {
            // TODO : 로그인 정렬 추가
            query += "  ORDER BY    u.login_count DESC, u.nickname ASC";
        } else if (sortOption == USER_SORT_OPTION_NICKNAME) {
            query += "  ORDER BY    u.nickname ASC";
        } else if (sortOption == USER_SORT_OPTION_ALBUM) {
            query += "  ORDER BY    album_count DESC, u.nickname ASC";
        }
        
        query += "      LIMIT       ?, ?";
        
        if (searchText != null) {
            return JdbcDao.queryForMapList(query, new Object[]{ searchText,
                                                                startCount,
                                                                count
            });
        } else {
            return JdbcDao.queryForMapList(query, new Object[]{ startCount,
                                                                count
            });
        }
    }
    
    
    /**
     * 유저 조회
     * @param seq           유저 번호
     * @return              유저
     * @throws SQLException 
     */
    public static ResultMap selectUserBySeq(int seq) throws SQLException {
        String query = "SELECT  *"
                + "     FROM    user"
                + "     WHERE   seq = ?"
                + "     AND     is_deleted = FALSE";
        
        return JdbcDao.queryForMapObject(query, new Object[]{seq});
    }
    
    
    /**
     * 게시글 목록 개수 조회
     * @param searchOption  검색 유형
     * @param searchText    검색어
     * @return              게시글 목록 개수
     * @throws SQLException 
     */
    public static int selectBoardContentListCount(int searchOption, String searchText) throws SQLException {
        String query = "    SELECT  COUNT(*)"
                + "         FROM    board b"
                + "         JOIN    board_content bc"
                + "         ON      b.seq = bc.board_ref"
                + "         AND     bc.is_deleted = FALSE"
                + "         JOIN    user u"
                + "         ON      u.seq = bc.user_ref"
                + "         WHERE   b.is_deleted = FALSE";
        
        // 검색
        if (searchText != null) {
            if (searchOption == BOARD_SEARCH_OPTION_TITLE) {
                query += "  AND     bc.title LIKE CONCAT('%', ?, '%')";
            } else if (searchOption == BOARD_SEARCH_OPTION_NICKNAME) {
                query += "  AND     u.nickname LIKE CONCAT('%', ?, '%')";
            }
            
            return JdbcDao.queryForInt(query, new Object[]{searchText});
        }
        
        return JdbcDao.queryForInt(query, new Object[]{});
    }
    
    
    /**
     * 게시글 목록 조회
     * @param sortOption    정렬 유형
     * @param searchOption  검색 유형
     * @param searchText    검색어
     * @param startCount    첫 목록 순번
     * @param count         개수
     * @return              게시글 목록
     * @throws SQLException 
     */
    public static List<ResultMap> selectBoardContentList(   int sortOption,
                                                            int searchOption,
                                                            String searchText,
                                                            int startCount,
                                                            int count) throws SQLException {
        String query = "    SELECT      b.name AS board_name,"
                + "                     bc.seq,"
                + "                     bc.title,"
                + "                     bc.content,"
                + "                     bc.view_count,"
                + "                     bc.is_secret,"
                + "                     DATE_FORMAT(bc.register_datetime, '%Y-%m-%d') AS datetime,"
                + "                     u.nickname AS user_nickname"
                + "         FROM        board b"
                + "         JOIN        board_content bc"
                + "         ON          b.seq = bc.board_ref"
                + "         AND         bc.is_deleted = FALSE"
                + "         JOIN        user u"
                + "         ON          u.seq = bc.user_ref"
                + "         WHERE       b.is_deleted = FALSE";
        
        // 검색
        if (searchText != null) {
            if (searchOption == BOARD_SEARCH_OPTION_TITLE) {
                query += "  AND         bc.title LIKE CONCAT('%', ?, '%')";
            } else if (searchOption == BOARD_SEARCH_OPTION_NICKNAME) {
                query += "  AND         u.nickname LIKE CONCAT('%', ?, '%')";
            }
        }
        
        // 정렬
        if (sortOption == BOARD_SORT_OPTION_RECENT) {
            query += "      ORDER BY    bc.seq DESC";
        } else if (sortOption == BOARD_SORT_OPTION_TITLE) {
            query += "      ORDER BY    bc.title ASC, bc.seq DESC";
        } else if (sortOption == BOARD_SORT_OPTION_NICKANME) {
            query += "      ORDER BY    u.nickname ASC, bc.seq DESC";
        }
        
        query += "          LIMIT       ?, ?";
        
        
        if (searchText != null) {
            if (searchOption == BOARD_SEARCH_OPTION_TITLE || searchOption == BOARD_SEARCH_OPTION_NICKNAME) {
                return JdbcDao.queryForMapList(query, new Object[]{ searchText,
                                                                    startCount,
                                                                    count

                });
            }
        }
        
        return JdbcDao.queryForMapList(query, new Object[]{ startCount,
                                                            count
        });
    }
    
    
    /**
     * 댓글 목록 개수 조회
     * @param searchOption  검색 유형
     * @param searchText    검색어
     * @return              댓글 목록 개수
     * @throws SQLException 
     */
    public static int selectCommentListCount(int searchOption, String searchText) throws SQLException {
        String query = "    SELECT      COUNT(*)"
                + "         FROM        board_comment bcm"
                + "         JOIN        user u"
                + "         ON          u.seq = bcm.user_ref"
                + "         LEFT JOIN   board_content bcn"
                + "         ON          bcn.seq = bcm.content_ref"
                + "         LEFT JOIN   album a"
                + "         ON          a.id = bcm.content_ref"
                + "         WHERE       bcm.comment_ref = 0"
                + "         AND         bcm.is_deleted = FALSE";
        
        // 검색
        if (searchText != null) {
            if (searchOption == COMMENT_SEARCH_OPTION_TITLE) {
                query += "  AND         ("
                        + "                 bcn.title LIKE CONCAT('%', ?, '%')"
                        + "                 OR"
                        + "                 a.name LIKE CONCAT('%', ?, '%')"
                        + "             )";
                
                return JdbcDao.queryForInt(query, new Object[]{ searchText,
                                                                searchText
                });
            } else if (searchOption == COMMENT_SEARCH_OPTION_NICKNAME) {
                query += "  AND         u.nickname LIKE CONCAT('%', ?, '%')";
                
                return JdbcDao.queryForInt(query, new Object[]{searchText});
            }
        } 
        
        return JdbcDao.queryForInt(query, new Object[]{});
    }
    
    
    /**
     * 댓글 목록 조회
     * @param sortOption    정렬 유형
     * @param searchOption  검색 유형
     * @param searchText    검색어
     * @param startCount    첫 목록 순번
     * @param count         개수
     * @return              댓글 목록
     * @throws SQLException 
     */
    public static List<ResultMap> selectCommentList(int sortOption,
                                                    int searchOption,
                                                    String searchText,
                                                    int startCount,
                                                    int count) throws SQLException {
        String query = "    SELECT      bcm.seq,"
                + "                     bcm.comment,"
                + "                     bcm.is_secret,"
                + "                     DATE_FORMAT(bcm.register_datetime, '%Y-%m-%d') AS datetime,"
                + "                     u.userId AS user_id,"
                + "                     u.nickname AS user_nickname,"
                + "                     IFNULL(bcn.seq, a.id) AS content,"
                + "                     IF(bcn.seq IS NOT NULL, 'board', 'album') AS content_type,"
                + "                     IFNULL(bcn.title, a.name) AS content_name,"
                + "                     ("
//                + "                         SELECT      JSON_ARRAYAGG("
                + "                         SELECT      CONCAT(\"[\", GROUP_CONCAT("
                + "                                                 JSON_OBJECT("
                + "                                                             'seq',              bc.seq,"
                + "                                                             'comment',          bc.comment,"
                + "                                                             'datetime',         DATE_FORMAT(bc.register_datetime, '%Y-%m-%d'),"
                + "                                                             'user_id',          u.userId,"
                + "                                                             'user_nickname',    u.nickname"
                + "                                                 )"
                + "                                     ) , \"]\") "
                + "                         FROM        board_comment bc"
                + "                         JOIN        user u"
                + "                         ON          u.seq = bc.user_ref"
                + "                         WHERE       bc.comment_ref = bcm.seq"
                + "                         AND         bc.is_deleted = FALSE"
                + "                         GROUP BY    bcm.seq"
                + "                     ) AS reply"
                + "         FROM        board_comment bcm"
                + "         JOIN        user u"
                + "         ON          u.seq = bcm.user_ref"
                + "         LEFT JOIN   board_content bcn"
                + "         ON          CONVERT(bcn.seq, char) = bcm.content_ref"
                + "         LEFT JOIN   album a"
                + "         ON          a.id = bcm.content_ref"
                + "         WHERE       bcm.comment_ref = 0"
                + "         AND         bcm.is_deleted = FALSE";
        
        // 검색
        if (searchText != null) {
            if (searchOption == COMMENT_SEARCH_OPTION_TITLE) {
                query += "  AND         ("
                        + "                 bcn.title LIKE CONCAT('%', ?, '%')"
                        + "                 OR"
                        + "                 a.name LIKE CONCAT('%', ?, '%')"
                        + "             )";
            } else if (searchOption == COMMENT_SEARCH_OPTION_NICKNAME) {
                query += "  AND         u.nickname LIKE CONCAT('%', ?, '%')";
            }
        }
        
        // 정렬
        if (sortOption == COMMENT_SORT_OPTION_RECENT) {
            query += "      ORDER BY    bcm.seq DESC";
        } else if (sortOption == COMMENT_SORT_OPTION_TITLE) {
            query += "      ORDER BY    content_name ASC, bcm.seq DESC";
        } else if (sortOption == COMMENT_SORT_OPTION_NICKNAME) {
            query += "      ORDER BY    u.nickname ASC, bcm.seq DESC";
        }
        
        query += "          LIMIT       ?, ?";
        
        if (searchText != null) {
            if (searchOption == COMMENT_SEARCH_OPTION_TITLE) {
                return JdbcDao.queryForMapList(query, new Object[]{ searchText,
                                                                    searchText,
                                                                    startCount,
                                                                    count
                });
            } else if (searchOption == COMMENT_SEARCH_OPTION_NICKNAME) {
                return JdbcDao.queryForMapList(query, new Object[]{ searchText,
                                                                    startCount,
                                                                    count
                });
            }
        }
        
        return JdbcDao.queryForMapList(query, new Object[]{ startCount,
                                                            count
        });
    }
    
    
    /**
     * 앨범 목록 개수 조회
     * @return              앪범 목록 개수
     * @throws SQLException 
     */
    public static int selectAlbumListCount() throws SQLException {
        String query = "SELECT  COUNT(*)"
                + "     FROM    album"
                + "     WHERE   is_deleted = FALSE";
        
        return JdbcDao.queryForInt(query, new Object[]{});
    }
    
    
    /**
     * 앨범 목록 조회
     * @param sortOption    정렬 유형
     * @param startCount    첫 목록 순번
     * @param count         개수
     * @return              앨범 목록
     * @throws SQLException 
     */
//    해결해결 여기서 pk 값도 같이 받아와야 한다.
    public static List<ResultMap> selectAlbumList(  int sortOption,
                                                    int startCount,
                                                    int count) throws SQLException {
        String query = "SELECT      a.id,"
                + "                 a.pk,"
                + "                 a.name,"
                + "                 a.status,"
                + "                 DATE_FORMAT(a.register_datetime, '%Y-%m-%d') AS datetime,"
                + "                 u.userId AS user_id,"
                + "                 u.nickname AS user_nickname,"
                + "                 IFNULL(("
                + "                         SELECT      COUNT(*)"
                + "                         FROM        album_view"
                + "                         WHERE       album_ref = a.id"
                + "                         GROUP BY    album_ref"
                + "                 ), 0) AS listen_count"
                + "     FROM        album a"
                + "     JOIN        user u"
                + "     ON          u.seq = a.user_ref"
                + "     WHERE       a.is_deleted = FALSE";
        
        if (sortOption == ALBUM_SORT_OPTION_RECENT) {
            query += "  ORDER BY     GREATEST(a.register_datetime, a.update_datetime) DESC";
        } else if (sortOption == ALBUM_SORT_OPTION_TITLE) {
            query += "  ORDER BY    a.name ASC,  GREATEST(a.register_datetime, a.update_datetime) DESC";
        } else if (sortOption == ALBUM_SORT_OPTION_NICKNAME) {
            query += "  ORDER BY    u.nickname ASC,  GREATEST(a.register_datetime, a.update_datetime) DESC";
        } else if (sortOption == ALBUM_SORT_OPTINO_STATUS) {
            query += "  ORDER BY    a.status ASC,  GREATEST(a.register_datetime, a.update_datetime) DESC";
        }
        
        query += "      LIMIT       ?, ?";
        
        return JdbcDao.queryForMapList(query, new Object[]{ startCount,
                                                            count
        });
    }
    
    
    /**
     * 앨범 조회
     * @param id            앨범 아이디
     * @return              앨범
     * @throws SQLException 
     */
    public static ResultMap selectAlbum(String id) throws SQLException {
        String query = "SELECT      *,"
                + "                 CONCAT(f.path, '/', f.stored_name) AS thumbnail"
                + "     FROM        album a"
                + "     LEFT JOIN   file f"
                + "     ON          f.stored_name = a.thumbnail"
                + "     AND         f.is_deleted = FALSE"
                + "     WHERE       a.id = ?"
                + "     AND         a.is_deleted = FALSE";
        
        return JdbcDao.queryForMapObject(query, new Object[]{id});
    }
    
    
    /**
     * 앨범 조회
     * @param id            앨범 아이디
     * @return              앨범
     * @throws SQLException 
     */
    public static ResultMap selectDetailAlbumBySeq(String id) throws SQLException {
        String query = "SELECT      a.id,"
                + "                 a.name,"
                + "                 a.introduction,"
                + "                 a.category,"
                + "                 a.user_tag,"
                + "                 a.admin_tag,"
                + "                 ("
                + "                     SELECT  CONCAT(path, '/', stored_name)"
                + "                     FROM    file"
                + "                     WHERE   stored_name = a.thumbnail"
                + "                 ) AS thumbnail,"
                + "                 u.userId AS user_id,"
                + "                 u.nickname AS user_nickname"
                + "     FROM        album a"
                + "     JOIN        user u"
                + "     ON          u.seq = a.user_ref"
                + "     WHERE       a.id = ?"
                + "     AND         a.is_deleted = FALSE"
                + "     GROUP BY    a.id";
        
        ResultMap resultMap = JdbcDao.queryForMapObject(query, new Object[]{id});
        
        // 챕터, 컨텐츠 정보 가져오기
        if (resultMap != null) {
            query = "   SELECT      ach.id,"
                    + "             ach.name,"
                    + "             aco.id AS content_id,"
                    + "             aco.type AS content_type,"
                    + "             aco.name AS content_name,"
                    + "             IF(aco.content IS NOT NULL, aco.content, CONCAT(f.path, '/', f.stored_name)) AS content_link"
                    + " FROM        album_chapter ach"
                    + " JOIN        album_content aco"
                    + " ON          ach.id = aco.chapter_ref"
                    + " LEFT JOIN   file f"
                    + " ON          f.stored_name = aco.file"
                    + " AND         f.is_deleted = FALSE"
                    + " WHERE       ach.album_ref = ?"
                    + " ORDER BY    ach.sort, aco.sort";
            
            List<ResultMap> resultList = JdbcDao.queryForMapList(query, new Object[]{id});
            List<ResultMap> chapterList = null;
            
            if (resultList != null) {
                chapterList = new ArrayList<>();
                
                Map<String, ResultMap> tempMap = new LinkedHashMap<>();
                
                for (ResultMap map : resultList) {
                    String chapterId = map.getString("id");
                    
                    ResultMap contentMap = new ResultMap();
                    contentMap.put("id", map.getString("content_id"));
                    contentMap.put("type", map.getInt("content_type"));
                    contentMap.put("name", map.getString("content_name"));
                    contentMap.put("link", map.getString("content_link"));
                    
                    if (tempMap.containsKey(chapterId)) {
                        ResultMap chapterMap = tempMap.get(chapterId);
                        List<ResultMap> contentList = (List<ResultMap>) chapterMap.get("content");
                        contentList.add(contentMap);
                    } else {
                        List<ResultMap> contentList = new ArrayList<>();
                        contentList.add(contentMap);
                        
                        ResultMap chapterMap = new ResultMap();
                        chapterMap.put("name", map.getString("name"));
                        chapterMap.put("content", contentList);

                        tempMap.put(chapterId, chapterMap);
                    }
                }
                
                for (Map.Entry<String, ResultMap> entry : tempMap.entrySet()) {
                    ResultMap chapterMap = entry.getValue();
                    chapterMap.put("id", entry.getKey());
                    
                    chapterList.add(chapterMap);
                }
                
                resultMap.put("chapter", chapterList);
            }
        }
        
        return resultMap;
    }
    
    
    /**
     * 앨범 등록 거절 사유 조회
     * @param id            앨범 아이디
     * @return              앨범 등록 거절 사유
     * @throws SQLException 
     */
    public static ResultMap selectAlbumDenialReason(String id) throws SQLException {
        String query = "SELECT  IFNULL(admin_comment, '') as reason"
                + "     FROM    album"
                + "     WHERE   id = ?"
                + "     AND     is_deleted = FALSE";
        
        return JdbcDao.queryForMapObject(query, new Object[]{id});
    }
    
    
    /**
     * 포털페이지 설정 정보 조회
     * @return              설정 정보
     * @throws SQLException 
     */
    public static ResultMap selectPortalSetting() throws SQLException {
        String query = "SELECT  ("
//                + "                 SELECT      JSON_ARRAYAGG("
                + "                 SELECT      CONCAT(\"[\", GROUP_CONCAT("
                + "                                             JSON_OBJECT("
                + "                                                         'link', b.link,"
                + "                                                         'file', CONCAT(f.path, '/', f.stored_name)"
                + "                                             )"
                + "                             ) , \"]\") "
                + "                 FROM        ("
                + "                                 SELECT  JSON_UNQUOTE(JSON_EXTRACT(banner, '$[0].link')) as link,"
                + "                                         JSON_UNQUOTE(JSON_EXTRACT(banner, '$[0].file')) as thumbnail"
                + "                                 FROM    portal"
                + "                                 WHERE   seq = 1"
                + "                                 UNION ALL"
                + "                                 SELECT  JSON_UNQUOTE(JSON_EXTRACT(banner, '$[1].link')) as link,"
                + "                                         JSON_UNQUOTE(JSON_EXTRACT(banner, '$[1].file')) as thumbnail"
                + "                                 FROM    portal"
                + "                                 WHERE   seq = 1"
                + "                                 UNION ALL"
                + "                                 SELECT  JSON_UNQUOTE(JSON_EXTRACT(banner, '$[2].link')) as link,"
                + "                                         JSON_UNQUOTE(JSON_EXTRACT(banner, '$[2].file')) as thumbnail"
                + "                                 FROM    portal"
                + "                                 WHERE   seq = 1"
                + "                             ) AS b"
                + "                 LEFT JOIN   file f"
                + "                 ON          f.stored_name = b.thumbnail"
                + "                 WHERE       b.thumbnail IS NOT NULL"
                + "             ) AS banner,"
                + "             album_sort,"
                + "             album_count,"
                + "             enable_popup,"
                + "             popup_width,"
                + "             popup_height,"
                + "             popup_content"
                + "     FROM    portal"
                + "     WHERE   seq = 1";
        
        return JdbcDao.queryForMapObject(query, new Object[]{});
    }
    
    
    /**
     * 포털페이지 배너 이미지 조회
     * @return                  이미지 목록
     * @throws SQLException 
     */
    public static List<String> selectPortalBannerImages() throws SQLException {
        String query = "SELECT      GROUP_CONCAT(CONCAT(f.path, '/', f.stored_name)) AS files"
                + "     FROM        ("
                + "                                 SELECT  JSON_UNQUOTE(JSON_EXTRACT(banner, '$[0].file')) as image"
                + "                                 FROM    portal"
                + "                                 WHERE   seq = 1"
                + "                                 UNION ALL"
                + "                                 SELECT  JSON_UNQUOTE(JSON_EXTRACT(banner, '$[1].file')) as image"
                + "                                 FROM    portal"
                + "                                 WHERE   seq = 1"
                + "                                 UNION ALL"
                + "                                 SELECT  JSON_UNQUOTE(JSON_EXTRACT(banner, '$[2].file')) as image"
                + "                                 FROM    portal"
                + "                                 WHERE   seq = 1"
                + "                 ) AS b"
                + "     LEFT JOIN   file f"
                + "     ON          f.stored_name = b.image"
                + "     WHERE       b.image IS NOT NULL";
        
        String files = JdbcDao.queryForString(query, new Object[]{});
        
        List<String> thumbnailList = Arrays.asList(files.split(","));
        
        return thumbnailList;
    }
    
    
    /**
     * 유저 삭제
     * @param seq           유저 번호
     * @return              삭제 여부
     * @throws SQLException 
     */
    public static int updateDisableUser(int seq) throws SQLException {
        String query = "UPDATE  user"
                + "     SET     is_deleted = TRUE"
                + "     WHERE   seq = ?"
                + "     AND     is_deleted = FALSE";
        
        return JdbcDao.update(query, new Object[]{seq});
    }
    
    
    /**
     * 유저 코멘트 입력
     * @param seq           유저 번호
     * @param comment       코멘트
     * @return              코멘트 입력 여부
     * @throws SQLException 
     */
    public static int updateUserComment(int seq, String comment) throws SQLException {
        String query = "UPDATE  user"
                + "     SET     comment = ?"
                + "     WHERE   seq = ?";
        
        return JdbcDao.update(query, new Object[]{  comment,
                                                    seq
        });        
    }
    
    
    /**
     * 게시글 공개 활성화/비활성화
     * @param seq           게시글 번호
     * @param enable        공개 활성화/비활성화
     * @return              활성화 여부
     * @throws SQLException 
     */
    public static int updateEnableViewContent(int seq, boolean enable) throws SQLException {
        String query = "UPDATE  board_content"
                + "     SET     is_secret = IF(?, false, true)"
                + "     WHERE   seq = ?";
        
        return JdbcDao.update(query, new Object[]{  enable,
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
                + "     SET     is_deleted = TRUE"
                + "     WHERE   seq = ?"
                + "     AND     is_deleted = FALSE";
        
        return JdbcDao.update(query, new Object[]{seq});
    }
    
    
    /**
     * 댓글 공개 활성화/비활성화
     * @param seq           댓글 번호
     * @param enable        공개 활성화/비활성화
     * @return              활성화 여부
     * @throws SQLException 
     */
    public static int updateEnableViewComment(int seq, boolean enable) throws SQLException {
        String query = "UPDATE  board_comment"
                + "     SET     is_secret = IF(?, false, true)"
                + "     WHERE   seq = ?";
        
        return JdbcDao.update(query, new Object[]{  enable,
                                                    seq
        });
    }
    
    
    public static int updateDisableComment(int seq) throws SQLException {
        String query = "UPDATE  board_comment"
                + "     SET     is_deleted = TRUE"
                + "     WHERE   seq = ?"
                + "     AND     is_deleted = FALSE";
        
        return JdbcDao.update(query, new Object[]{seq});
    }
    
    /**
     * 앨범 수정
     * @param id            앨범 아이디
     * @param name          제목
     * @param introduction  소개
     * @param category      카테고리
     * @param userTag       사용자 태그
     * @param adminTag      관리자 태그
     * @param thumbnail     썸네일
     * @return              수정 여부
     * @throws SQLException 
     */
    public static int updateAlbum(  String id,
                                    String name,
                                    String introduction,
                                    String category,
                                    String userTag,
                                    String adminTag,
                                    String thumbnail) throws SQLException {
        String query = "UPDATE  album"
                + "     SET     name = ?,"
                + "             introduction = ?,"
                + "             category = ?,"
                + "             user_tag = ?,"
                + "             admin_tag = ?,"
                + "             thumbnail = ?"
                + "     WHERE   id = ?";
        
        return JdbcDao.update(query, new Object[]{  name,
                                                    introduction,
                                                    category,
                                                    userTag,
                                                    adminTag,
                                                    thumbnail,
                                                    id
        });
    }
    
    
    /**
     * 앨범 상태 변경
     * @param id            앨범 아이디
     * @param status        상태
     * @return              변경 여부
     * @throws SQLException 
     */
    public static int updateAlbumStatus(String id, String status) throws SQLException {
        String query = "UPDATE  album"
                + "     SET     status = ?"
                + "     WHERE   id = ?";
        
        return JdbcDao.update(query, new Object[]{  status,
                                                    id
        });
    }
    
    
    /**
     * 앨범 등록 거절 사유 작성
     * @param id            앨범 아이디
     * @param reason        등록 거절 사유
     * @return              거절 사유 작성 여부
     * @throws SQLException 
     */
    public static int updateAlbumDenialReason(String id, String reason) throws SQLException {
        String query = "UPDATE  album"
                + "     SET     status = '등록거절',"
                + "             admin_comment = ?"
                + "     WHERE   id = ?";
        
        return JdbcDao.update(query, new Object[]{  reason,
                                                    id
        });
    }
    
    
    /**
     * 앨범 삭제
     * @param id            앨범 아이디
     * @return              삭제 여부
     * @throws SQLException 
     */
    public static int updateDisableAlbum(String id) throws SQLException {
        String query = "UPDATE  album"
                + "     SET     is_deleted = TRUE"
                + "     WHERE   id = ?";
        
        return JdbcDao.update(query, new Object[]{id});
    }
    
    
    /**
     * 포털페이지 설정 정보 저장
     * @param bannerArray    배너 정보
     * @param albumSort     앨범 정렬 유형
     * @param albumCount    앨범 출력 개수
     * @param enablePopup   팝업 활성화 여부
     * @param popupWidth    팝업 너비
     * @param popupHeight   팝업 높이
     * @param popupContent  팝업 내용
     * @return              저장 여부
     * @throws SQLException 
     */
    public static int updatePortalSetting(  JSONArray bannerArray,
                                            int albumSort,
                                            int albumCount,
                                            boolean enablePopup,
                                            int popupWidth,
                                            int popupHeight,
                                            String popupContent) throws SQLException {
        String query = "UPDATE  portal"
                + "     SET     banner = ?,"
                + "             album_sort = ?,"
                + "             album_count = ?,"
                + "             enable_popup = ?,"
                + "             popup_width = ?,"
                + "             popup_height = ?,"
                + "             popup_content = ?,"
                + "             update_datetime = ?"
                + "     WHERE   seq = 1";
        
        String datetime = DateTimeUtils.getTimeDateNow();                       // yyyy-MM-dd HH:mm:ss
        
        String banner = null;
        if (bannerArray != null && !bannerArray.isEmpty()) {
            banner = bannerArray.toString();
        }
        
        return JdbcDao.update(query, new Object[]{  banner,
                                                    albumSort,
                                                    albumCount,
                                                    enablePopup,
                                                    popupWidth,
                                                    popupHeight,
                                                    popupContent,
                                                    datetime
        });
    }
}
