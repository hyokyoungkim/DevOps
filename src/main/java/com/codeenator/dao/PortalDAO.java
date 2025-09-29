package com.codeenator.dao;

import com.ithows.JdbcDao;
import com.ithows.ResultMap;
import java.sql.SQLException;
import java.util.List;

public class PortalDAO {
                                                            // 앨범 정렬 유형
    public static int ALBUM_SORT_OPTION_RECOMMEND = 1;      // 추천순
    public static int ALBUM_SORT_OPTION_NAME = 2;           // 이름순
    public static int ALBUM_SORT_OPTION_RECENT = 3;         // 최신등록순
    
    /**
     * 팝업 조회
     * @return              팝업 정보
     * @throws SQLException 
     */
    public static ResultMap selectPopup() throws SQLException {
        String query = "SELECT  enable_popup,"
                + "             popup_width,"
                + "             popup_height,"
                + "             popup_content"
                + "     FROM    portal"
                + "     WHERE   seq = 1";
        
        return JdbcDao.queryForMapObject(query, new Object[]{});
    }
    
    
    /**
     * 배너 조회
     * @return              배너 정보
     * @throws SQLException 
     */
    public static String selectBanner() throws SQLException {
//        String query = "SELECT      JSON_ARRAYAGG("
        String query = "SELECT      CONCAT(\"[\", GROUP_CONCAT("
                + "                                 JSON_OBJECT("
                + "                                             'link', b.link,"
                + "                                             'file', CONCAT(f.path, '/', f.stored_name)"
                + "                                 )"
                + "                 ) , \"]\") "
                + "     FROM        ("
                + "                     SELECT      JSON_UNQUOTE(JSON_EXTRACT(banner, '$[0].link')) as link,"
                + "                                 JSON_UNQUOTE(JSON_EXTRACT(banner, '$[0].file')) as thumbnail"
                + "                     FROM        portal"
                + "                     WHERE       seq = 1"
                + "                     UNION ALL"
                + "                     SELECT      JSON_UNQUOTE(JSON_EXTRACT(banner, '$[1].link')) as link,"
                + "                                 JSON_UNQUOTE(JSON_EXTRACT(banner, '$[1].file')) as thumbnail"
                + "                     FROM        portal"
                + "                     WHERE       seq = 1"
                + "                     UNION ALL"
                + "                     SELECT      JSON_UNQUOTE(JSON_EXTRACT(banner, '$[2].link')) as link,"
                + "                                 JSON_UNQUOTE(JSON_EXTRACT(banner, '$[2].file')) as thumbnail"
                + "                     FROM        portal"
                + "                     WHERE       seq = 1"
                + "                 ) AS b"
                + "     LEFT JOIN   file f"
                + "     ON          f.stored_name = b.thumbnail"
                + "     AND         f.is_deleted = FALSE"
                + "     WHERE       b.thumbnail IS NOT NULL";
        
        return JdbcDao.queryForString(query, new Object[]{});
    }
    
    
    /**
     * 앨범 설정 정보 조회
     * @return              앨범 설정 정보
     * @throws SQLException 
     */
    public static ResultMap selectAlbumSetting() throws SQLException {
        String query = "SELECT  album_sort,"
                + "             album_count"
                + "     FROM    portal"
                + "     WHERE   seq = 1";
        
        return JdbcDao.queryForMapObject(query, new Object[]{});
    }
    
    
    /**
     * 추천 앨범 조회
     * - listen_count : 앨범 조회 수
     * @param sortOption    정렬 유형
     * @param count         개수
     * @return              추천 앨범
     * @throws SQLException 
     */
//    해결해결 여기에 pk 값이 있으면 안가져오는 쿼리를 추가한다
    public static List<ResultMap> selectAlbumList(int sortOption, int count) throws SQLException {
        String query = "SELECT      a.id,"
                + "                 a.name,"
                + "                 a.category,"
                + "                 a.user_tag,"
                + "                 a.admin_tag,"
                + "                 ("
                + "                     SELECT  CONCAT(path, '/', stored_name)"
                + "                     FROM    file"
                + "                     WHERE   stored_name = a.thumbnail"
                + "                 ) AS thumbnail,"
                + "                 ("
                + "                     SELECT  COUNT(*)"
                + "                     FROM    album_view"
                + "                     WHERE   album_ref = a.id"
                + "                 ) AS listen_count,"
                + "                 ("
                + "                     SELECT  COUNT(user_ref)"
                + "                     FROM    album_recommend"
                + "                     WHERE   album_ref = a.id"
                + "                 ) AS recommend_count,"
                + "                 u.nickname AS user_nickname"
                + "     FROM        album a"
                + "     JOIN        user u"
                + "     ON          u.seq = a.user_ref"
                + "     AND         u.is_deleted = FALSE"
                + "     WHERE       a.is_deleted = FALSE"
                + "     AND         a.pk IS NULL"
                + "     AND         a.status = '등록'";
        
        // 정렬
        if (sortOption == ALBUM_SORT_OPTION_RECOMMEND) {
            query += "  ORDER BY    recommend_count DESC, a.register_datetime DESC";
        } else if (sortOption == ALBUM_SORT_OPTION_NAME) {
            query += "  ORDER BY    a.name ASC, a.register_datetime DESC";
        } else if (sortOption == ALBUM_SORT_OPTION_RECENT) {
            query += "  ORDER BY    a.register_datetime DESC";
        }
        
        query += "      LIMIT       ?";
        
        return JdbcDao.queryForMapList(query, new Object[]{count});
    }
    
    
    /**
     * 신규 앨범 목록 조회
     * @param count         개수
     * @return              신규 앨범 목록
     * @throws SQLException 
     */
//    해결해결 여기에 pk 값 있으면 안보여지게 해야함
    public static List<ResultMap> selectNewAlbumList(int count) throws SQLException {
        String query = "SELECT      a.id,"
                + "                 a.name,"
                + "                 a.category,"
                + "                 a.user_tag,"
                + "                 a.admin_tag,"
                + "                 ("
                + "                     SELECT  CONCAT(path, '/', stored_name)"
                + "                     FROM    file"
                + "                     WHERE   stored_name = a.thumbnail"
                + "                 ) AS thumbnail,"
                + "                 ("
                + "                     SELECT  COUNT(*)"
                + "                     FROM    album_view"
                + "                     WHERE   album_ref = a.id"
                + "                 ) AS listen_count,"
                + "                 ("
                + "                     SELECT  COUNT(user_ref)"
                + "                     FROM    album_recommend"
                + "                     WHERE   album_ref = a.id"
                + "                 ) AS recommend_count,"
                + "                 u.nickname AS user_nickname"
                + "     FROM        album a"
                + "     JOIN        user u"
                + "     ON          u.seq = a.user_ref"
                + "     AND         u.is_deleted = FALSE"
                + "     WHERE       a.is_deleted = FALSE"
                + "     AND         a.pk IS NULL"
                + "     AND         a.status = '등록'"
                + "     ORDER BY    a.register_datetime DESC"
                + "     LIMIT       ?";
        
        return JdbcDao.queryForMapList(query, new Object[]{count});
    }
    
    
    /**
     * 수강평 목록 조회
     * @param count         개수
     * @return              수강평 목록
     * @throws SQLException 
     */
    public static List<ResultMap> selectAlbumCommentList(int count) throws SQLException {
        String query = "SELECT      a.name AS album_name,"
                + "                 bc.comment,"
                + "                 DATE_FORMAT(bc.register_datetime, '%Y-%m-%d') AS datetime,"
                + "                 ("
                + "                     SELECT  nickname"
                + "                     FROM    user"
                + "                     WHERE   seq = bc.user_ref"
                + "                 ) AS user_nickname"
                + "     FROM        album a"
                + "     JOIN        board_comment bc"
                + "     ON          a.id = bc.content_ref"
                + "     AND         bc.comment_ref = 0"
                + "     AND         bc.is_deleted = FALSE"
                + "     WHERE       a.is_deleted = FALSE"
                + "     ORDER BY    bc.seq DESC"
                + "     LIMIT       ?";
        
        return JdbcDao.queryForMapList(query, new Object[]{count});
    }
    
    
    /**
     * 앨범수, 수강수 조회
     * @return              앨범수, 하루 앨범 열람 수
     * @throws SQLException 
     */
    public static ResultMap selectAlbumCount() throws SQLException {
        String query = "SELECT  ("
                + "                 SELECT  COUNT(*)"
                + "                 FROM    album"
                + "                 WHERE   is_deleted = FALSE"
                + "             ) AS album_count,"
                + "             ("
                + "                 SELECT  SUM(size)"
                + "                 FROM    ("
                + "                             SELECT      COUNT(*) AS size"
                + "                             FROM        album a"
                + "                             JOIN        album_view av"
                + "                             ON          a.id = av.album_ref"
                + "                             WHERE       a.is_deleted = FALSE"
                + "                             AND         DATE_FORMAT(av.register_datetime, '%Y-%m-%d') = DATE_FORMAT(NOW(), '%Y-%m-%d')"
                + "                             GROUP BY    av.album_ref"
                + "                         ) AS listen"
                + "             ) AS listen_count";
        
        return JdbcDao.queryForMapObject(query, new Object[]{});
    }
}
