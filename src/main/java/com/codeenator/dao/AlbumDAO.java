package com.codeenator.dao;

import com.codeenator.utils.RandomUtils;
import com.ithows.JdbcDao;
import com.ithows.ResultMap;
import com.ithows.util.DateTimeUtils;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AlbumDAO {
    // 아이디 길이

    private static final int ALBUM_ID_LENGTH = 6;               // 앨범
    private static final int CHAPTER_ID_LENGTH = 8;             // 챕터
    private static final int CONTENT_ID_LENGTH = 10;            // 컨텐츠

    // 검색 유형
    public static final int SEARCH_OPTION_ALL = 1;              // 앨범명, 태그
    public static final int SEARCH_OPTION_NAME = 2;             // 앨범명
    public static final int SEARCH_OPTION_TAG = 3;              // 태그

    // 앨범 정렬 유형
    public static final int ALBUM_SORT_OPTION_RECOMMEND = 1;    // 추천순
    public static final int ALBUM_SORT_OPTION_NAME = 2;         // 이름순
    public static final int ALBUM_SORT_OPTION_RECENT = 3;       // 최신순

    // 내 앨범 정렬 유형
    public static final int MY_ALBUM_SORT_OPTION_RECENT = 1;    // 최신순
    public static final int MY_ALBUM_SORT_OPTION_LISTEN = 2;    // 인기순
    public static final int MY_ALBUM_SORT_OPTION_NAME = 3;      // 이름순

    /**
     * 앨범 목록 개수 조회
     *
     * @param category 카테고리
     * @param searchOption 검색 유형
     * @param searchText 검색어
     * @return 목록 개수
     * @throws SQLException
     */
//    해결해결 여기에 pk 값이 없는 앨범의 갯수만 나오게 해야함 == 이거 바탕으로 페이징할거라 안보여질거면 카운트도 안돼야함
    public static int selectAlbumListCount(String category,
            int searchOption,
            String searchText) throws SQLException {
        String query = "    SELECT  COUNT(*) "
                + "         FROM    album "
                + "         WHERE   is_deleted = FALSE"
                + "         AND (pk is null or pk = \"\") "
                + "         AND status = '등록'";

        if (category != null) {
            query += "      AND     category = ?";
        }

        if (searchText != null) {
            if (searchOption == SEARCH_OPTION_ALL) {
                query += "  AND     ("
                        + "             name LIKE CONCAT('%', ?, '%')"
                        + "             OR"
                        + "             user_tag LIKE CONCAT('%', ?, '%')"
                        + "         )";
            } else if (searchOption == SEARCH_OPTION_NAME) {
                query += "  AND     name LIKE CONCAT('%', ?, '%')";
            } else if (searchOption == SEARCH_OPTION_TAG) {
                query += "  AND     user_tag LIKE CONCAT('%', ?, '%')";
            }
        }

        if (searchText != null && category != null) {
            if (searchOption == SEARCH_OPTION_ALL) {
                return JdbcDao.queryForInt(query, new Object[]{category,
                    searchText,
                    searchText
                });
            } else if (searchOption == SEARCH_OPTION_NAME || searchOption == SEARCH_OPTION_TAG) {
                return JdbcDao.queryForInt(query, new Object[]{category,
                    searchText
                });
            }
        } else if (searchText != null) {
            if (searchOption == SEARCH_OPTION_ALL) {
                return JdbcDao.queryForInt(query, new Object[]{searchText,
                    searchText
                });
            } else if (searchOption == SEARCH_OPTION_NAME || searchOption == SEARCH_OPTION_TAG) {
                return JdbcDao.queryForInt(query, new Object[]{searchText});
            }
        } else if (category != null) {
            return JdbcDao.queryForInt(query, new Object[]{category});
        }

        return JdbcDao.queryForInt(query, new Object[]{});
    }

    /**
     * 앨범 목록 조회
     *
     * @param category 카테고리
     * @param sortOption 정렬 유형
     * @param searchOption 검색 유형
     * @param searchText 검색어
     * @param startCount 첫 목록 순번
     * @param count 개수
     * @return 앨범 목록
     * @throws SQLException
     */
//    해결해결 여기서 pk값이 있는 앨범은 안보여지게 해야함
    public static List<ResultMap> selectAlbumList(String category,
            int sortOption,
            int searchOption,
            String searchText,
            int startCount,
            int count) throws SQLException {
        String query = "    SELECT      a.id,"
                + "                     a.name,"
                + "                     a.category,"
                + "                     a.user_tag,"
                + "                     a.admin_tag,"
                + "                     DATE_FORMAT(a.register_datetime, '%Y-%m-%d') AS datetime,"
                + "                     u.nickname AS user_nickname,"
                + "                     ("
                + "                         SELECT  CONCAT(path, '/', stored_name)"
                + "                         FROM    file"
                + "                         WHERE   stored_name = a.thumbnail"
                + "                     ) AS thumbnail,"
                + "                     COUNT(*) AS listen_count,"
                + "                     COUNT(DISTINCT(ar.user_ref)) AS recommend_count"
                + "         FROM        album a"
                + "         LEFT JOIN   album_view av"
                + "         ON          a.id = av.album_ref"
                + "         LEFT JOIN   album_recommend ar"
                + "         ON          a.id = ar.album_ref"
                + "         JOIN        user u"
                + "         ON          u.seq = a.user_ref"
                + "         WHERE       a.is_deleted = FALSE"
                + "         AND         (a.pk IS NULL or a.pk = \"\")"
                + "         AND         a.status = '등록'";

        if (category != null) {
            query += "      AND         a.category = ?";
        }

        // 검색
        if (searchText != null) {
            if (searchOption == SEARCH_OPTION_ALL) {
                query += "  AND         ("
                        + "                 a.name LIKE CONCAT('%', ?, '%')"
                        + "                 OR"
                        + "                 a.user_tag LIKE CONCAT('%', ?, '%')"
                        + "             )";
            } else if (searchOption == SEARCH_OPTION_NAME) {
                query += "  AND         a.name LIKE CONCAT('%', ?, '%')";
            } else if (searchOption == SEARCH_OPTION_TAG) {
                query += "  AND         a.user_tag LIKE CONCAT('%', ?, '%')";
            }
        }

        query += "          GROUP BY    a.id";

        // 정렬
        if (sortOption == ALBUM_SORT_OPTION_RECOMMEND) {
            query += "      ORDER BY    recommend_count DESC, GREATEST(a.register_datetime, a.update_datetime) DESC";
        } else if (sortOption == ALBUM_SORT_OPTION_NAME) {
            query += "      ORDER BY    a.name ASC, GREATEST(a.register_datetime, a.update_datetime) DESC";
        } else if (sortOption == ALBUM_SORT_OPTION_RECENT) {
            query += "      ORDER BY    GREATEST(a.register_datetime, a.update_datetime) DESC";
        }

        query += "          LIMIT       ?, ?";

        if (searchText != null & category != null) {
            if (searchOption == SEARCH_OPTION_ALL) {
                return JdbcDao.queryForMapList(query, new Object[]{category,
                    searchText,
                    searchText,
                    startCount,
                    count
                });
            } else if (searchOption == SEARCH_OPTION_NAME || searchOption == SEARCH_OPTION_TAG) {
                return JdbcDao.queryForMapList(query, new Object[]{category,
                    searchText,
                    startCount,
                    count
                });
            }
        } else if (searchText != null) {
            if (searchOption == SEARCH_OPTION_ALL) {
                return JdbcDao.queryForMapList(query, new Object[]{searchText,
                    searchText,
                    startCount,
                    count
                });
            } else if (searchOption == SEARCH_OPTION_NAME || searchOption == SEARCH_OPTION_TAG) {
                return JdbcDao.queryForMapList(query, new Object[]{searchText,
                    startCount,
                    count
                });
            }
        } else if (category != null) {
            return JdbcDao.queryForMapList(query, new Object[]{category,
                startCount,
                count
            });
        }

        return JdbcDao.queryForMapList(query, new Object[]{startCount,
            count
        });
    }

    /**
     * 앨범 아이디 사용 여부 확인
     *
     * @param id 앨범 아이디
     * @return 앨범 아이디 사용 여부
     */
    public static boolean selectUsedAlbumId(String id) {
        String query = "SELECT  COUNT(*)"
                + "     FROM    album"
                + "     WHERE   id = ?";

        int result = 0;

        try {
            result = JdbcDao.queryForInt(query, new Object[]{id});
        } catch (SQLException e) {
            e.printStackTrace();

            result = 0;
        }

        return result > 0;
    }

    /**
     * 앨범 조회
     *
     * @param id 앨범 아이디
     * @return 앨범
     * @throws SQLException
     */
    public static ResultMap selectAlbum(String id) throws SQLException {
        String query = "SELECT  *"
                + "     FROM    album"
                + "     WHERE   id = ?"
                + "     AND     is_deleted = FALSE";

        return JdbcDao.queryForMapObject(query, new Object[]{id});
    }

    /**
     * 앨범 조회
     *
     * @param id 앨범 아이디
     * @return 앨범
     * @throws SQLException
     */
//    해결해결 여기에 pk값까지 같이 넘겨주게 해야된다
    public static ResultMap selectDetailAlbum(String id) throws SQLException {
        String query = "SELECT      a.id,"
                + "                 a.pk,"
                + "                 a.user_ref,"
                + "                 a.name,"
                + "                 a.introduction,"
                + "                 a.category,"
                + "                 a.user_tag,"
                + "                 CONCAT(f.path, '/', f.stored_name) AS thumbnail"
                + "     FROM        album a"
                + "     LEFT JOIN   file f"
                + "     ON          a.thumbnail = f.stored_name"
                + "     WHERE       a.id = ?"
                + "     AND         a.is_deleted = FALSE";

        ResultMap resultMap = JdbcDao.queryForMapObject(query, new Object[]{id});

        // 챕터, 컨텐츠 정보 가져오기
        if (resultMap != null) {
            query = "       SELECT      ach.id,"
                    + "                 ach.name,"
                    + "                 aco.id AS content_id,"
                    + "                 aco.type AS content_type,"
                    + "                 aco.name AS content_name,"
                    + "                 aco.content AS content_content,"
                    + "                 CONCAT(f.path, '/', f.stored_name) AS content_file"
                    + "     FROM        album_chapter ach"
                    + "     JOIN        album_content aco"
                    + "     ON          ach.id = aco.chapter_ref"
                    + "     LEFT JOIN   file f"
                    + "     ON          aco.file = f.stored_name"
                    + "     AND         f.is_deleted = FALSE"
                    + "     WHERE       ach.album_ref = ?"
                    + "     ORDER BY    ach.sort, aco.sort";

            List<ResultMap> resultList = JdbcDao.queryForMapList(query, new Object[]{id});
            List<ResultMap> chapterList = new ArrayList<>();

            if (resultList != null) {
                Map<String, ResultMap> tempMap = new LinkedHashMap<>();

                for (ResultMap map : resultList) {
                    String chapterId = map.getString("id");

                    ResultMap contentMap = new ResultMap();
                    contentMap.put("id", map.getString("content_id"));
                    contentMap.put("type", map.getInt("content_type"));
                    contentMap.put("name", map.getString("content_name"));
                    contentMap.put("content", map.getString("content_content"));
                    contentMap.put("file", map.getString("content_file"));

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
                    System.out.println(entry + " DSAFKLJVBHJSDKLFBHJSLDFKBHJSDF");
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
     * 앨범 조회
     *
     * @param id 앨범 아이디
     * @param userRef 유저 번호
     * @return 앨범
     * @throws SQLException
     */
//    해결해결 여기에 pk값이 있을때와 없을때 케이스를 추가한다 따로 걸러서 안해도됨
    public static ResultMap selectDetailAlbum(String id, Integer userRef) throws SQLException {
        String query = "SELECT      a.id,"
                + "                 a.name,"
                + "                 a.introduction,"
                + "                 a.category,"
                + "                 a.user_tag,"
                + "                 ("
                + "                     SELECT  CONCAT(path, '/', stored_name)"
                + "                     FROM    file"
                + "                     WHERE   stored_name = a.thumbnail"
                + "                 ) AS thumbnail,"
                + "                 u.userId AS user_id,"
                + "                 u.nickname AS user_nickname,"
                + "                 IF (ar.seq IS NOT NULL, true, false) AS enable_recommend"
                + "     FROM        album a"
                + "     JOIN        user u"
                + "     ON          u.seq = a.user_ref"
                + "     LEFT JOIN   album_recommend ar"
                + "     ON          a.id = ar.album_ref"
                + "     AND         ar.user_ref = ?"
                + "     WHERE       a.id = ?"
                + "     AND         a.is_deleted = FALSE"
                + "     GROUP BY    a.id";

        ResultMap resultMap = JdbcDao.queryForMapObject(query, new Object[]{userRef,
            id
        });
        System.out.println("resultMap is : " + resultMap);
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
     * 앨범 열람 이력 조회
     *
     * @param id 앨범 아이디
     * @param userRef 유저 번호
     * @return 앨범 열람 여부
     */
    public static boolean selectAlbumView(String id, int userRef) {
        String query = "SELECT  COUNT(*)"
                + "     FROM    album_view"
                + "     WHERE   album_ref = ?"
                + "     AND     user_ref = ?";

        int result = 0;

        try {
            result = JdbcDao.queryForInt(query, new Object[]{id,
                userRef
            });
        } catch (SQLException e) {
            e.printStackTrace();

            result = 0;
        }

        return result > 0;
    }

    /**
     * 앨범 챕터 아이디 사용 여부 확인
     *
     * @param id 챕터 아이디
     * @return 앨범 챕터 아이디 사용 여부
     */
    public static boolean selectUsedChapterId(String id) {
        String query = "SELECT  COUNT(*)"
                + "     FROM    album_chapter"
                + "     WHERE   id = ?";

        int result = 0;

        try {
            result = JdbcDao.queryForInt(query, new Object[]{id});
        } catch (SQLException e) {
            e.printStackTrace();

            result = 0;
        }

        return result > 0;
    }

    /**
     * 앨범 컨텐츠 아이디 사용 여부 확인
     *
     * @param id 컨텐츠 아이디
     * @return 앨범 컨텐츠 아이디 사용 여부
     */
    public static boolean selectUsedContentId(String id) {
        String query = "SELECT  COUNT(*)"
                + "     FROM    album_content"
                + "     WHERE   id = ?";

        int result = 0;

        try {
            result = JdbcDao.queryForInt(query, new Object[]{id});
        } catch (SQLException e) {
            e.printStackTrace();

            result = 0;
        }

        return result > 0;
    }

    /**
     * 컨텐츠 조회
     *
     * @param id 컨텐츠 아이디
     * @return 컨텐츠
     * @throws SQLException
     */
    public static ResultMap selectDetailContent(String id) throws SQLException {
        String query = "SELECT      aco.id,"
                + "                 aco.type,"
                + "                 aco.name,"
                + "                 IFNULL(aco.content, CONCAT(f.path, '/', f.stored_name)) AS link,"
                + "                 ("
                + "                     SELECT      saco.id"
                + "                     FROM        album sa"
                + "                     JOIN        album_chapter sach"
                + "                     ON          sa.id = sach.album_ref"
                + "                     JOIN        album_content saco"
                + "                     ON          sach.id = saco.chapter_ref"
                + "                     WHERE       sa.id = aco.album_ref"
                + "                     AND         ("
                + "                                     sach.sort < ach.sort"
                + "                                     OR"
                + "                                     (sach.sort = ach.sort AND saco.sort < aco.sort)"
                + "                                 )"
                + "                     ORDER BY    sach.sort DESC, saco.sort DESC"
                + "                     LIMIT       1"
                + "                 ) AS prev,"
                + "                 ("
                + "                     SELECT      saco.id"
                + "                     FROM        album sa"
                + "                     JOIN        album_chapter sach"
                + "                     ON          sa.id = sach.album_ref"
                + "                     JOIN        album_content saco"
                + "                     ON          sach.id = saco.chapter_ref"
                + "                     WHERE       sa.id = aco.album_ref"
                + "                     AND         ("
                + "                                     sach.sort > ach.sort"
                + "                                     OR"
                + "                                     (sach.sort = ach.sort AND saco.sort > aco.sort)"
                + "                                 )"
                + "                     ORDER BY    sach.sort, saco.sort"
                + "                     LIMIT       1"
                + "                 ) AS next"
                + "     FROM        album_chapter ach"
                + "     JOIN        album_content aco"
                + "     ON          ach.id = aco.chapter_ref"
                + "     LEFT JOIN   file f"
                + "     ON          f.stored_name = aco.file"
                + "     WHERE       aco.id = ?";

        return JdbcDao.queryForMapObject(query, new Object[]{id});
    }

    /**
     * 수강 앨범 목록 개수 조회
     *
     * @param userRef 유저 번호
     * @return 수강 앨범 목록 개수
     * @throws SQLException
     */
    public static int selectListenAlbumListCount(int userRef) throws SQLException {
        String query = "SELECT  COUNT(*)"
                + "     FROM    ("
                + "                 SELECT      a.id"
                + "                 FROM        album_view av"
                + "                 JOIN        album a"
                + "                 ON          a.id = av.album_ref"
                + "                 AND         a.is_deleted = FALSE"
                + "                 WHERE       av.user_ref = ?"
                + "                 GROUP BY    av.album_ref"
                + "             ) AS view";

        return JdbcDao.queryForInt(query, new Object[]{userRef});
    }

    /**
     * 수강 앨범 목록 조회
     *
     * @param userRef 유저 번호
     * @param startCount 첫 게시글 순번
     * @param count 개수
     * @return 수강 앨범 목록
     * @throws SQLException
     */
    public static List<ResultMap> selectListenAlbumList(int userRef,
            int startCount,
            int count) throws SQLException {
        String query = "SELECT      av.album_ref AS album,"
                + "                 DATE_FORMAT(MIN(av.register_datetime), '%Y-%m-%d') AS first_datetime,"
                + "                 DATE_FORMAT(MAX(av.register_datetime), '%Y-%m-%d') AS recent_datetime,"
                + "                 COUNT(av.album_ref) AS listen_count,"
                + "                 a.name"
                + "     FROM        album_view av"
                + "     JOIN        album a"
                + "     ON          a.id = av.album_ref"
                + "     AND         a.is_deleted = FALSE"
                + "     WHERE       av.user_ref = ?"
                + "     GROUP BY    av.album_ref"
                + "     ORDER BY    MAX(av.register_datetime) DESC, a.name ASC"
                + "     LIMIT       ?, ?";

        return JdbcDao.queryForMapList(query, new Object[]{userRef,
            startCount,
            count
        });
    }

    /**
     * 수강 앨범 조회
     *
     * @param albumRef 앨범 아이디
     * @param userRef 유저 번호
     * @return 앨범
     * @throws SQLException
     */
    public static ResultMap selectListenAlbum(String albumRef,
            int userRef) throws SQLException {
        String query = "SELECT      *"
                + "     FROM        album_view"
                + "     WHERE       album_ref = ?"
                + "     AND         user_ref = ?"
                + "     GROUP BY    album_ref";

        return JdbcDao.queryForMapObject(query, new Object[]{albumRef,
            userRef
        });
    }

    /**
     * 내 앨범 목록 개수 조회
     *
     * @param userRef 유저 번호
     * @param filter 필터
     * @return 내 앨범 목록 개수
     * @throws SQLException
     */
    public static int selectMyAlbumListCount(int userRef, String filter) throws SQLException {
        String query = "SELECT      COUNT(*)"
                + "     FROM        ("
                + "                     SELECT      a.id"
                + "                     FROM        album a"
                + "                     LEFT JOIN   album_view av"
                + "                     ON          a.id = av.album_ref"
                + "                     WHERE       a.user_ref = ?"
                + "                     AND         a.is_deleted = FALSE";

        // 필터
        if (filter != null) {
            query += "                  AND         a.status = ?";
        }

        query += "                      GROUP BY    a.id"
                + "                 ) AS album";

        if (filter != null) {
            return JdbcDao.queryForInt(query, new Object[]{userRef,
                filter
            });
        } else {
            return JdbcDao.queryForInt(query, new Object[]{userRef});
        }
    }

    /**
     * 내 앨범 목록 조회
     *
     * @param userRef 유저 번호
     * @param filter 필터
     * @param sortOption 정렬 유형
     * @param startCount 첫 목록 순번
     * @param count 개수
     * @return 내 앨범 목록
     * @throws SQLException
     */
//    해결해결 여기서 pk 정보도 추가로 가져와야한다
    public static List<ResultMap> selectMyAlbumList(int userRef,
            String filter,
            int sortOption,
            int startCount,
            int count) throws SQLException {
        String query = "SELECT      a.id,"
                + "                 a.pk,"
                + "                 a.name,"
                + "                 a.user_tag,"
                + "                 DATE_FORMAT(a.register_datetime, '%Y-%m-%d') AS datetime,"
                + "                 ("
                + "                     SELECT  CONCAT(path, '/', stored_name)"
                + "                     FROM    file"
                + "                     WHERE   stored_name = a.thumbnail"
                + "                     AND     is_deleted = FALSE"
                + "                 ) AS thumbnail,"
                + "                 a.status,"
                + "                 COUNT(DISTINCT(av.user_ref)) AS listen_count"
                + "     FROM        album a"
                + "     LEFT JOIN   album_view av"
                + "     ON          a.id = av.album_ref"
                + "     WHERE       a.user_ref = ?"
                + "     AND         a.is_deleted = FALSE";

        // 필터
        if (filter != null) {
            query += "  AND         a.status = ?";
        }

        query += "      GROUP BY    a.id";

        // 정렬
        if (sortOption == MY_ALBUM_SORT_OPTION_RECENT) {
            query += "  ORDER BY    a.register_datetime DESC";
        } else if (sortOption == MY_ALBUM_SORT_OPTION_LISTEN) {
            query += "  ORDER BY    listen_count DESC, a.name ASC";
        } else if (sortOption == MY_ALBUM_SORT_OPTION_NAME) {
            query += "  ORDER BY    a.name ASC";
        }

        if (filter != null) {
            return JdbcDao.queryForMapList(query, new Object[]{userRef,
                filter
            });
        } else {
            return JdbcDao.queryForMapList(query, new Object[]{userRef});
        }
    }

    /**
     * 내 앨범 수강평 목록 개수 조회
     *
     * @param userRef 유저 번호
     * @return 내 앨범 수강평 목록 개수
     * @throws SQLException
     */
    public static int selectMyAlbumCommentListCount(int userRef) throws SQLException {
        String query = "SELECT  COUNT(*)"
                + "     FROM    album a"
                + "     JOIN    board_comment bc"
                + "     ON      a.id = bc.content_ref"
                + "     AND     bc.comment_ref = 0"
                + "     AND     bc.is_deleted = FALSE"
                + "     WHERE   a.user_ref = ?"
                + "     AND     a.is_deleted = FALSE";

        return JdbcDao.queryForInt(query, new Object[]{userRef});
    }

    /**
     * 내 앨범 수강평 목록 조회
     *
     * @param userRef 유저 번호
     * @param startCount 첫 목록 순번
     * @param count 개수
     * @return 내 앨범 수강평 목록
     * @throws SQLException
     */
    public static List<ResultMap> selectMyAlbumCommentList(int userRef,
            int startCount,
            int count) throws SQLException {
        String query = "SELECT      a.id AS album,"
                + "                 a.name AS album_name,"
                + "                 bc.seq,"
                + "                 bc.comment,"
                + "                 bc.datetime,"
                + "                 bc.user_id,"
                + "                 bc.user_nickname,"
                + "                 bc.reply"
                + "     FROM        album a"
                + "     JOIN        ("
                + "                     SELECT      bc1.seq,"
                + "                                 bc1.content_ref,"
                + "                                 bc1.comment,"
                + "                                 DATE_FORMAT(bc1.register_datetime, '%Y-%m-%d') AS datetime,"
                + "                                 u.userId AS user_id,"
                + "                                 u.nickname AS user_nickname,"
                + "                                 IF(bc2.seq, JSON_OBJECT("
                + "                                                         'seq',      bc2.seq,"
                + "                                                         'comment',  bc2.comment,"
                + "                                                         'datetime', DATE_FORMAT(bc2.register_datetime, '%Y-%m-%d')"
                + "                                             ),"
                + "                                 NULL) AS reply"
                + "                     FROM        board_comment bc1"
                + "                     LEFT JOIN   board_comment bc2"
                + "                     ON          bc1.seq = bc2.comment_ref"
                + "                     AND         bc2.is_deleted = FALSE"
                + "                     JOIN        user u"
                + "                     ON          u.seq = bc1.user_ref"
                + "                     WHERE       bc1.comment_ref = 0"
                + "                     AND         bc1.is_deleted = FALSE"
                + "                     ORDER BY    bc2.seq DESC"
                + "                 ) AS bc"
                + "     ON          a.id = bc.content_ref"
                + "     WHERE       a.user_ref = ?"
                + "     AND         a.is_deleted = FALSE"
                + "     ORDER BY    bc.seq DESC"
                + "     LIMIT       ?, ?";

        return JdbcDao.queryForMapList(query, new Object[]{userRef,
            startCount,
            count
        });
    }

    /**
     * 앨범 추천 이력 조회
     *
     * @param id 앨범 아이디
     * @param userRef 유저 번호
     * @return 앨범 추천 이력 여부
     */
    public static boolean selectRecommendAlbum(String id, int userRef) {
        String query = "SELECT  COUNT(*)"
                + "     FROM    album_recommend"
                + "     WHERE   album_ref = ?"
                + "     AND     user_ref = ?";

        int result = 0;

        try {
            result = JdbcDao.queryForInt(query, new Object[]{id,
                userRef
            });
        } catch (SQLException e) {
            e.printStackTrace();

            result = 0;
        }

        return result > 0;
    }

    /**
     * 앨범 생성
     *
     * @param uuid UUID
     * @param userRef 유저 번호
     * @param name 이름
     * @param introduction 소개
     * @param category 카테고리
     * @param thumbnail 썸네일
     * @param tag 태그
     * @return 생성 여부
     * @throws SQLException
     */
//    해결해결 pk값 있는경우 같이 입력해줘야함
    public static int insertAlbum(String uuid,
            String pk,
            int userRef,
            String name,
            String introduction,
            String category,
            String thumbnail,
            String tag) throws SQLException {
        String query;
        String datetime = DateTimeUtils.getTimeDateNow();                       // yyyy-MM-dd HH:mm:ss
        int rs = 0;
        if (!pk.equals(null)) { //pk 값이 없을때
            query = "INSERT INTO album(  id,"
                    + "                         pk,"
                    + "                         user_ref,"
                    + "                         name,"
                    + "                         introduction,"
                    + "                         category,"
                    + "                         thumbnail,"
                    + "                         user_tag,"
                    + "                         register_datetime"
                    + "     )"
                    + "     VALUES( ?, ?, ?, ?, ?,"
                    + "             ?, ?, ?, ?"
                    + "     )";
            rs = JdbcDao.update(query, new Object[]{uuid,
                pk,
                userRef,
                name,
                introduction,
                category,
                thumbnail,
                tag,
                datetime
            });
        } else {
            query = "INSERT INTO album(  id,"
                    + "                         user_ref,"
                    + "                         name,"
                    + "                         introduction,"
                    + "                         category,"
                    + "                         thumbnail,"
                    + "                         user_tag,"
                    + "                         register_datetime"
                    + "     )"
                    + "     VALUES( ?, ?, ?, ?, ?,"
                    + "             ?, ?, ?"
                    + "     )";
            rs = JdbcDao.update(query, new Object[]{uuid,
                userRef,
                name,
                introduction,
                category,
                thumbnail,
                tag,
                datetime
            });
        }

        return rs;
    }

    /**
     * 챕터 생성
     *
     * @param chapterList 앨범 챕터 목록
     * @return 생성 여부
     * @throws SQLException
     */
    public static int insertChapterList(List<ResultMap> chapterList) throws SQLException {
        String query = "INSERT INTO album_chapter(  id,"
                + "                                 album_ref,"
                + "                                 name,"
                + "                                 sort,"
                + "                                 register_datetime"
                + "     )"
                + "     VALUES";

        String datetime = DateTimeUtils.getTimeDateNow();                       // yyyy-MM-dd HH:mm:ss        

        if (chapterList != null) {
            query += chapterList.stream()
                    .map(item
                            -> "("
                    + "'" + item.getString("id") + "',"
                    + "'" + item.getString("album") + "',"
                    + "'" + item.getString("name") + "',"
                    + "'" + item.getInt("sort") + "',"
                    + "'" + datetime + "'"
                    + ")"
                    )
                    .collect(Collectors.joining(","));

            return JdbcDao.update(query, new Object[]{});
        } else {
            return 0;
        }
    }

    /**
     * 컨텐츠 생성
     *
     * @param contentList 컨텐츠 목록
     * @return 생성 여부
     * @throws SQLException
     */
    public static int insertContentList(List<ResultMap> contentList) throws SQLException {
        String query = "INSERT INTO album_content(  id,"
                + "                                 album_ref,"
                + "                                 chapter_ref,"
                + "                                 type,"
                + "                                 name,"
                + "                                 content,"
                + "                                 file,"
                + "                                 sort,"
                + "                                 register_datetime"
                + "     )"
                + "     VALUES";

        String datetime = DateTimeUtils.getTimeDateNow();                       // yyyy-MM-dd HH:mm:ss

        if (contentList != null) {
            query += contentList.stream()
                    .map(item
                            -> "("
                    + "'" + item.getString("id") + "',"
                    + "'" + item.getString("album") + "',"
                    + "'" + item.getString("chapter") + "',"
                    + "'" + item.getInt("type") + "',"
                    + (item.containsKey("name") ? "'" + item.getString("name") + "'" : null) + ","
                    + (item.containsKey("content") ? "'" + item.getString("content") + "'" : null) + ","
                    + (item.containsKey("file") ? "'" + item.getString("file") + "'" : null) + ","
                    + "'" + item.getInt("sort") + "',"
                    + "'" + datetime + "'"
                    + ")"
                    )
                    .collect(Collectors.joining(","));

            return JdbcDao.update(query, new Object[]{});
        } else {
            return 0;
        }
    }

    /**
     * 앨범 조회 이력 생성
     *
     * @param albumRef 앨범 아이디
     * @param userRef 유저 번호
     * @return 생성 여부
     * @throws SQLException
     */
    public static int insertAlbumView(String albumRef, Integer userRef) throws SQLException {
        String query = "INSERT INTO album_view( album_ref,"
                + "                             user_ref,"
                + "                             register_datetime"
                + "     )"
                + "     VALUES(?, ?, ?)";

        String datetime = DateTimeUtils.getTimeDateNow();                       // yyyy-MM-dd HH:mm:ss

        return JdbcDao.update(query, new Object[]{albumRef,
            userRef,
            datetime
        });
    }

    /**
     * 앨범 추천
     *
     * @param albumRef 앨범 번호
     * @param userRef 유저 번호
     * @return 추천 여부
     * @throws SQLException
     */
    public static int insertRecommendAlbum(String albumRef, int userRef) throws SQLException {
        String query = "INSERT INTO album_recommend(album_ref,"
                + "                                 user_ref,"
                + "                                 register_datetime"
                + "     )"
                + "     VALUES(?, ?, ?)";

        String datetime = DateTimeUtils.getTimeDateNow();                       // yyyy-MM-dd HH:mm:ss

        return JdbcDao.update(query, new Object[]{albumRef,
            userRef,
            datetime
        });
    }

    /**
     * 앨범 수정
     *
     * @param id 아이디
     * @param pk 비공개 앨범 키값
     * @param name 이름
     * @param introduction 소개
     * @param category 카테고리
     * @param thunmnail 썸네일
     * @param tag 태그
     * @return 수정 여부
     * @throws SQLException
     */
//    해결해결 여기에 pk값 있으면 같이 들어가게 해야한다
    public static int updateAlbum(String id,
            String pk,
            String name,
            String introduction,
            String category,
            String thunmnail,
            String tag) throws SQLException {
        if (pk != null) {
            String query = "UPDATE  album"
                    + "     SET     name = ?,"
                    + "             pk = ?,"
                    + "             introduction = ?,"
                    + "             category = ?,"
                    + "             thumbnail = ?,"
                    + "             user_tag = ?,"
                    + "             update_datetime = ?"
                    + "     WHERE   id = ?";

            String datetime = DateTimeUtils.getTimeDateNow();                       // yyyy-MM-dd HH:mm:ss

            return JdbcDao.update(query, new Object[]{name,
                pk,
                introduction,
                category,
                thunmnail,
                tag,
                datetime,
                id
            });

        } else {
            String query = "UPDATE  album"
                    + "     SET     name = ?,"
                    + "             introduction = ?,"
                    + "             category = ?,"
                    + "             thumbnail = ?,"
                    + "             user_tag = ?,"
                    + "             update_datetime = ?"
                    + "     WHERE   id = ?";

            String datetime = DateTimeUtils.getTimeDateNow();                       // yyyy-MM-dd HH:mm:ss

            return JdbcDao.update(query, new Object[]{name,
                introduction,
                category,
                thunmnail,
                tag,
                datetime,
                id
            });
        }
    }

    /**
     * 앨범 삭제
     *
     * @param id 앨범 아이디
     * @return 삭제 여부
     * @throws SQLException
     */
    public static int updateDisableAlbum(String id) throws SQLException {
        String query = "UPDATE  album"
                + "     SET     thumbnail = null,"
                + "             is_deleted = TRUE,"
                + "             delete_datetime = ?"
                + "     WHERE   id = ?"
                + "     AND     is_deleted = FALSE";

        String datetime = DateTimeUtils.getTimeDateNow();                       // yyyy-MM-dd HH:mm:ss

        return JdbcDao.update(query, new Object[]{datetime,
            id
        });
    }

    /**
     * 앨범 챕터 삭제
     *
     * @param chapterList 앨범 챕터 목록
     * @return 삭제 여부
     */
    public static int deleteAlbumChapterList(List<ResultMap> chapterList) {
        String query = "DELETE FROM     album_chapter"
                + "     WHERE           id IN (";

        if (chapterList != null) {
            query += chapterList.stream()
                    .map(map -> "'" + map.getString("id") + "'")
                    .collect(Collectors.joining(","));

            query += "                   )";

            try {
                return JdbcDao.update(query, new Object[]{});
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return -1;
    }

    /**
     * 앨범 컨텐츠 삭제
     *
     * @param contentList 앨범 컨텐츠 목록
     * @return 삭제 여부
     */
    public static int deleteAlbumCotentList(List<ResultMap> contentList) {
        String query = "DELETE FROM     album_content"
                + "     WHERE           id IN (";

        if (contentList != null) {
            query += contentList.stream()
                    .map(map -> "'" + map.getString("id") + "'")
                    .collect(Collectors.joining(","));

            query += "                  )";

            try {
                return JdbcDao.update(query, new Object[]{});
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return -1;
    }

    /**
     * 수강 삭제
     *
     * @param albumRef 앨범 아이디
     * @param userRef 유저 번호
     * @return 삭제 여부
     * @throws SQLException
     */
    public static int deleteListenAlbum(String albumRef, int userRef) throws SQLException {
        String query = "DELETE FROM album_view"
                + "     WHERE       album_ref = ?"
                + "     AND         user_ref = ?";

        return JdbcDao.update(query, new Object[]{albumRef,
            userRef
        });
    }

    /**
     * 앨범 추천 취소
     *
     * @param albumRef 앨범 번호
     * @param userRef 유저 번호
     * @return 취소 여부
     * @throws SQLException
     */
    public static int deleteRecommendAlbum(String albumRef, int userRef) throws SQLException {
        String query = "DELETE FROM album_recommend"
                + "     WHERE       album_ref = ?"
                + "     AND         user_ref = ?";

        return JdbcDao.update(query, new Object[]{albumRef,
            userRef
        });
    }

    public static ResultMap getAlbumPk(String id) {
        String query = "SELECT CASE " +
               "        WHEN pk IS NULL THEN \"\" " +
               "        ELSE pk " +
               "        END AS pk, user_ref FROM album WHERE id = ?";
        try {
            return JdbcDao.queryForMapObject(query, new Object[]{id});
        } catch (SQLException e) {
            return null;
        }

    }

    /**
     * 앨범 아이디 생성
     *
     * @return 앨범 아이디
     */
    public static String createAlbumId() {
        boolean isExist = false;
        String id = null;

        while (!isExist) {
            id = RandomUtils.create(ALBUM_ID_LENGTH);

            if (!selectUsedAlbumId(id)) {
                isExist = true;
            }
        }

        return id;
    }

    /**
     * 앨범 챕터 아이디 생성
     *
     * @return 앨범 챕터 아이디
     */
    public static String createChapterId() {
        boolean isExist = false;
        String id = null;

        while (!isExist) {
            id = RandomUtils.create(CHAPTER_ID_LENGTH);

            if (!selectUsedChapterId(id)) {
                isExist = true;
            }
        }

        return id;
    }

    /**
     * 앨범 컨텐츠 아이디 생성
     *
     * @return 앨범 컨텐츠 아이디
     */
    public static String createContentId() {
        boolean isExist = false;
        String id = null;

        while (!isExist) {
            id = RandomUtils.create(CONTENT_ID_LENGTH);

            if (!selectUsedContentId(id)) {
                isExist = true;
            }
        }

        return id;
    }
}
