package com.codeenator.dao;

import com.ithows.JdbcDao;
import com.ithows.util.DateTimeUtils;
import java.sql.SQLException;
import org.json.JSONObject;

public class LogDAO {
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
                                                                                // 관리자 인터페이스 (1)
                                                                                // 유저 관련 (00)
    public static final int ADMIN_GET_USER_LIST = 10000;                        // - 유저 목록 조회
    public static final int ADMIN_INIT_USER_PSWD = 10001;                       // - 유저 비밀번호 초기화
    public static final int ADMIN_SEND_USER_EMAIL = 10002;                      // - 유저 이메일 전송
    public static final int ADMIN_SET_USER_COMMENT = 10003;                     // - 유저 코멘트 입력
    public static final int ADMIN_DELETE_USER = 10004;                          // - 유저 삭제
    
                                                                                // 게시글 관련 (01)
    public static final int ADMIN_GET_BOARD_CONTENT_LIST = 10100;               // - 게시글 목록 조회
    public static final int ADMIN_ENABLE_VIEW_BOARD_CONTENT = 10101;            // - 게시글 공개 활성화/비활성화
    public static final int ADMIN_DELETE_BOARD_CONTENT = 10102;                 // - 게시글 삭제
    public static final int ADMIN_GET_BOARD_COMMENT_LIST = 10103;               // - 댓글 목록 조회
    public static final int ADMIN_ENABLE_VIEW_BOARD_COMMENT = 10104;            // - 댓글 공개 활성화/비활성화
    public static final int ADMIN_DELETE_BOARD_COMMENT = 10105;                 // - 댓글 삭제
    
                                                                                // 앨범 관련 (02)
    public static final int ADMIN_GET_ALBUM_LIST = 10200;                       // - 앨범 목록 조회
    public static final int ADMIN_GET_ALBUM = 10201;                            // - 앨범 조회
    public static final int ADMIN_MODIFY_ALBUM = 10202;                         // - 앨범 수정
    public static final int ADMIN_MODIFY_ALBUM_STATUS = 10203;                  // - 앨범 상태 변경
    public static final int ADMIN_GET_ALBUM_DENIAL_REASON = 10204;              // - 앨범 등록 거절 사유 조회
    public static final int ADMIN_SET_ALBUM_DENIAL_REASON = 10205;              // - 앨범 등록 거절 사유 작성
    public static final int ADMIN_DELETE_ALBUM = 10206;                         // - 앨범 삭제
    
                                                                                // 포털 관련 (03)
    public static final int ADMIN_GET_PORTAL_SETTING = 10301;                   // - 포털페이지 설정 정보 조회
    public static final int ADMIN_SET_PORTAL_SETTING = 10302;                   // - 포털페이지 설정 정보 저장
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
                                                                                // 유저 인터페이스 (2)
                                                                                // 유저 관련 (00)
    public static final int USER_JOIN = 20000;                                  // - 회원가입     
    public static final int USER_IS_USED_ID = 20001;                            // - 아이디 중복 확인
    public static final int USER_GET_CIPHER = 20002;                            // - 이메일 인증번호 발송
    public static final int USER_CHECK_CIPHER = 20003;                          // - 이메일 인증번호 확인
    public static final int USER_IS_USED_NICKNAME = 20004;                      // - 닉네임 중복확인
    public static final int USER_LOGIN = 20005;                                 // - 로그인
    public static final int USER_LOGOUT = 20006;                                // - 로그아웃
    public static final int USER_FIND_ID = 20007;                               // - 아이디 찾기
    public static final int USER_FIND_PSWD = 20008;                             // - 비밀번호 찾기
    public static final int USER_GET_USER = 20009;                              // - 회원정보 조회
    public static final int USER_MODIFY_USER = 20010;                           // - 회원정보 변경
    public static final int USER_MODIFY_PSWD = 20011;                           // - 비밀번호 변경
    public static final int USER_IS_USED_PSWD = 20012;                          // - 현재 비밀번호 확인
    public static final int USER_WITHDRAWAL = 20013;                            // - 회원탈퇴
    
                                                                                // 게시판 인터페이스 (01)
    public static final int USER_GET_NOTICE_LIST = 20100;                       // - 공지사항 게시글 목록 조회    
    public static final int USER_GET_NOTICE = 20101;                            // - 공지사항 게시글 조회
    public static final int USER_SET_NOTICE = 20102;                            // - 공지사항 게시글 생성
    public static final int USER_MODIFY_NOTICE = 20103;                         // - 공지사항 게시글 수정
    public static final int USER_DELETE_NOTICE = 20104;                         // - 공지사항 게시글 삭제
    public static final int USER_GET_QUESTION_LIST = 20105;                     // - QnA 게시글 목록 조회
    public static final int USER_GET_QUESTION = 20106;                          // - QnA 게시글 조회
    public static final int USER_SET_QUESTION = 20107;                          // - QnA 게시글 생성
    public static final int USER_MODIFY_QUESTION = 20108;                       // - QnA 게시글 수정
    public static final int USER_DELETE_QUESTION = 20109;                       // - QnA 게시글 삭제
    public static final int USER_GET_BOARD_COMMENT_LIST = 20110;                // - 댓글 목록 조회
    public static final int USER_SET_BOARD_COMMENT = 20111;                     // - 댓글 작성
    public static final int USER_MODIFY_BOARD_COMMENT = 20112;                  // - 댓글 수정
    public static final int USER_DELETE_BOARD_COMMENT = 20113;                  // - 댓글 삭제
    
                                                                                // 앨범 인터페이스 (02)
    public static final int USER_GET_ALBUM_LIST = 20200;                        // - 앨범 목록 조회
    public static final int USER_GET_ALBUM = 20201;                             // - 앨범 조회
    public static final int USER_SET_ALBUM = 20202;                             // - 앨범 생성
    public static final int USER_MODIFY_ALBUM = 20203;                          // - 앨범 수정                 
    public static final int USER_DELETE_ALBUM = 20204;                          // - 앨범 삭제
    public static final int USER_GET_ALBUM_CONTENT = 20205;                     // - 컨텐츠 조회
    public static final int USER_GET_ALBUM_COMMENT_LIST = 20206;                // - 앨범 수강평 목록 조회
    public static final int USER_SET_ALBUM_COMMENT = 20207;                     // - 앨범 수강평, 수강평 댓글 작성
    public static final int USER_MODIFY_ALBUM_COMMENT = 20208;                  // - 앨범 수강평, 수강평 댓글 수정
    public static final int USER_DELETE_ALBUM_COMMENT = 20209;                  // - 앨범 수강평, 수강평 댓글 삭제
    public static final int USER_SHARE_ALBUM_LINK = 20210;                      // - 앨범 링크 공유
    public static final int USER_GET_LISTEN_ALBUM_LIST = 20211;                 // - 수강 앨범 목록 조회
    public static final int USER_DELETE_LISTEN_ALBUM = 20212;                   // - 수강 삭제
    public static final int USER_GET_MY_ALBUM_LIST = 20213;                     // - 내 앨범 목록 조회
    public static final int USER_GET_MY_ALBUM_COMMENT_LIST = 20214;             // - 내 앨범 수강평 목록 조회
    public static final int USER_RECOMMEND_ALBUM = 20215;                       // - 앨범 추천/추천 취소
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
                                                                                // 파일 인터페이스 (3)
    public static final int FILE_UPLOAD = 30000;                                // - 파일 업로드
    
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    /**
     * 로그 저장
     * @param api               인터페이스 번호
     * @param userRef           유저 번호
     * @param agent             유저 접근 기기
     * @param queryObject       요청 파라미터(query string)
     * @param requestObject     요청 파라미터(body)
     * @param responseObject    응답 파라미터
     * @return 
     */
    public static int insertLog(int api,
                                Integer userRef,
                                String agent,
                                JSONObject queryObject,
                                JSONObject requestObject,
                                JSONObject responseObject) {
        String query = "INSERT INTO log(api,"
                + "                     user_ref,"
                + "                     agent,"
                + "                     query_string,"
                + "                     request,"
                + "                     response,"
                + "                     request_datetime"
                + "     )"
                + "     VALUES( ?, ?, ?, ?, ?,"
                + "             ?, ?"
                + "     )";
        
        String datetime = DateTimeUtils.getTimeDateNow();                       // yyyy-MM-dd HH:mm:ss
        
        String queryString = null;
        if (queryObject != null && !queryObject.isEmpty()) {
            queryString = queryObject.toString();
        }
        
        String request = null;
        if (requestObject != null && !requestObject.isEmpty()) {
            request = requestObject.toString();
        }
        
        String response = null;
        if (responseObject != null && !responseObject.isEmpty()) {
            response = responseObject.toString();
        }
        
        try {
            return JdbcDao.update(query, new Object[]{  api,
                                                        userRef,
                                                        agent,
                                                        queryString,
                                                        request,
                                                        response,
                                                        datetime
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return 0;
    }
    
    
    /**
     * 접속 여부 확인
     * @param userRef   유저 번호
     * @return          접속 
     */
    public static int selectIsLogin(int userRef) {
        String query = "SELECT  COUNT(*)"
                + "     FROM    log"
                + "     WHERE   user_ref = ?"
                + "     AND     api = ?"
                + "     AND     JSON_EXTRACT(response, '$.result') = 'success'"
                + "     AND     DATE_FORMAT(request_datetime, '%Y-%m-%d') = DATE_FORMAT(NOW(), '%Y-%m-%d')";
        
        try {
            return JdbcDao.queryForInt(query, new Object[]{ userRef,
                                                            USER_LOGIN
            });
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        return -1;
    }
}
