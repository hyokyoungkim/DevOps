package com.ithows.controller;

import com.codeenator.dao.AdminDAO;
import com.codeenator.dao.BoardDAO;
import com.codeenator.dao.FileDAO;
import com.codeenator.dao.LogDAO;
import com.codeenator.dao.UserDAO;
import com.codeenator.model.Response;
import com.codeenator.utils.FileUtils;
import com.codeenator.utils.MailUtils;
import com.codeenator.utils.PageUtils;
import com.codeenator.utils.RandomUtils;
import com.codeenator.utils.ThumbnailUtils;
import com.ithows.BaseDebug;
import com.ithows.ResultMap;
import com.ithows.SessionInfo;
import com.ithows.base.ControllerClassInfo;
import com.ithows.base.ControllerMethodInfo;
import com.ithows.service.UploadConst;
import com.ithows.util.HttpUtils;
import com.ithows.util.UtilJSON;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 관리자 API Controller
 * 
 * - 유저 목록 조회 ------------------------------------ {@link #getUserList(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 유저 비밀번호 초기화 ------------------------------- {@link #initPassword(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 유저 이메일 전송 ---------------------------------- {@link #sendEmail(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 유저 코멘트 입력 ---------------------------------- {@link #setComment(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 유저 삭제 ---------------------------------------- {@link #deleteUser(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * 
 * - 게시글 목록 조회 ---------------------------------- {@link #getContentList(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 게시글 공개 활성화/비활성화 ------------------------- {@link #enableViewContent(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 게시글 삭제 -------------------------------------- {@link #deleteContent(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 댓글 목록 조회 ------------------------------------ {@link #getCommentList(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 댓글 공개 활성화/비활성화 --------------------------- {@link #enableViewComment(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 댓글 삭제 ---------------------------------------- {@link #deleteComment(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * 
 * - 앨범 목록 조회 ------------------------------------ {@link #getAlbumList(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 앨범 조회 ---------------------------------------- {@link #getAlbum(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 앨범 수정 ---------------------------------------- {@link #modifyAlbum(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 앨범 상태 변경 ------------------------------------ {@link #modifyStatus(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 앨범 등록 거절 사유 조회 ---------------------------- {@link #getAlbumDenialReason(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 앨범 등록 거절 사유 작성 ---------------------------- {@link #setAlbumDenialReason(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 앨범 삭제 ----------------------------------------- {@link #deleteAlbum(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * 
 * - 포털페이지 설정 정보 조회 --------------------------- {@link #getSetting(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 포털페이지 설정 정보 저장 --------------------------- {@link #setSetting(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 */
@ControllerClassInfo(controllerPage="/admin/_admin.jsp")
public class AdminController {
    
    static {
        BaseDebug.info("***AdminController.class Loading!!");
    }
    
    /**
     * 유저 목록 조회
     */
    @ControllerMethodInfo(id = "/api/admin/user/getUserList.do")
    public String getUserList(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.ADMIN_GET_USER_LIST;                                   // 인터페이스 번호
        int type = UserDAO.USER_TYPE_PUBLIC;                                    // 유저 유형
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        int page = HttpUtils.getParameterInt(request, "page", -1);                          // 페이지 번호
        int sortOption = HttpUtils.getParameterInt(request, "sort_option", -1);             // 정렬 유형
        String searchText = HttpUtils.getParameterString(request, "search_text", null);     // 검색어
        
        JSONObject queryObject = new JSONObject();
        queryObject.put("page", page != -1? page : null);
        queryObject.put("sort_option", sortOption != -1? sortOption : null);
        queryObject.put("search_text", searchText);
        
        page = page != -1? page : 1;
        sortOption = sortOption != -1? sortOption : AdminDAO.USER_SORT_OPTION_LOGIN;
        
        JSONObject responseObject = new JSONObject();
        
        // session
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (sessionMap != null) {
            type = sessionMap.getInt("type");
            user = sessionMap.getInt("seq");
        }
        
        if (user == null || type != UserDAO.USER_TYPE_ADMIN) {
            // 비로그인, 관리자가 아닌 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
            
            return "/ResultJSON.jsp";
        }
                
        List<ResultMap> userList = null;                                        // 유저 목록
        ResultMap pageMap = null;
        
        try {
            int contentCount = 10;                                              // 목록 출력 개수
            int count = AdminDAO.selectUserListCount(searchText);               // 유저 목록 개수 조회
            
            pageMap = PageUtils.getPagination(count, page, contentCount);       // 페이징
            int totalPage = pageMap.getInt("totalPage");
            if (page <= totalPage || page == 1) {
                // 올바른 페이지 번호에 접근한 경우
                int startCount = pageMap.getInt("startCount");
                userList = AdminDAO.selectUserList(sortOption, searchText, startCount, count);      // 유저 목록 조회
            } else {
                // 없는 페이지 번호에 접근한 경우
                responseObject.put(Response.RESULT, Response.Result.ERROR);
                responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
                request.setAttribute(Response.RESULT, responseObject);
                
                LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
                
                return "/ResultJSON.jsp";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "잠시 후 다시 시도해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (userList != null) {
            // 유저 목록 조회 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "");
            responseObject.put(Response.MAP, UtilJSON.mapToJSon((Map<String, Object>) pageMap));
            responseObject.put(Response.LIST, UtilJSON.convertArrayListToJSONArray((ArrayList) userList));
        } else {
            // 유저 목록 조회 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "유저 목록 조회에 실패하였습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
        
        LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 유저 비밀번호 초기화
     */
    @ControllerMethodInfo(id = "/api/admin/user/initPassword.do")
    public String initPassword(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.ADMIN_INIT_USER_PSWD;                                  // 인터페이스 번호
        int type = UserDAO.USER_TYPE_PUBLIC;                                    // 유저 유형
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        int seq = HttpUtils.getParameterInt(request, "seq", -1);                            // 유저 번호
        boolean enableEmail = HttpUtils.getParameterBoolean(request, "enable_email");       // 이메일 전송 여부
        
        JSONObject queryObject = new JSONObject();
        queryObject.put("seq", seq != -1? seq : null);
        queryObject.put("enable_email", enableEmail);
        
        JSONObject responseObject = new JSONObject();
        
        // session
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (sessionMap != null) {
            type = sessionMap.getInt("type");
            user = sessionMap.getInt("seq");
        }
        
        if (user == null || type != UserDAO.USER_TYPE_ADMIN) {
            // 비로그인, 관리자가 아닌 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (seq < 1) {
            // 유저 번호가 없는 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        int result = 0;                                                         // 비밀번호 초기화 결과
        
        try {
            String password = null;
            
            ResultMap userMap = AdminDAO.selectUserBySeq(seq);                  // 유저 조회
            if (userMap != null) {
                // 유저 조회 성공
                password = RandomUtils.createUpper(6);                          // 6자리 비밀번호 생성
                
                result = UserDAO.updatePassword(seq, password);                 // 비밀번호 변경
            } else {
                // 유저 조회 실패
                responseObject.put(Response.RESULT, Response.Result.FAILED);
                responseObject.put(Response.MESSAGE, "유저 조회에 실패하였습니다.");
                request.setAttribute(Response.RESULT, responseObject);
                
                LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
                
                return "/ResultJSON.jsp";
            }
            
            if (enableEmail & result > 0) {
                boolean isSucceed = false;
                String email = userMap.getString("email");
                
                if (email != null && !"".equals(email)) {
                    // 이메일 제목 및 내용
                    String title = "[Codeenator] 비밀번호 초기화";
                    
                    StringBuilder sb = new StringBuilder();
                    sb.append("안녕하세요. Codeenator입니다.").append("<br>");
                    sb.append("비밀번호 초기화 결과입니다.").append("<br>");
                    sb.append("회원님의 비밀번호를 임시 비밀번호로 발급해드렸습니다.").append("<br>");
                    sb.append("임시 비밀번호로 로그인하시고, 비밀번호를 변경해주시길 바랍니다.").append("<br><br>");
                    sb.append("임시 비밀번호는 ").append(password).append(" 입니다.").append("<br><br>");
                    sb.append("해당 서비스를 이용 중이 아니시라면, 이메일을 삭제해주시길 바랍니다.");
                    
                    String content = sb.toString();

                    isSucceed = MailUtils.sendMail(email, title, content);      // 이메일 전송
                }
                
                if (!isSucceed) {
                    // 이메일 전송 실패
                    responseObject.put(Response.RESULT, Response.Result.FAILED);
                    responseObject.put(Response.MESSAGE, "이메일 전송에 실패하였습니다.");
                    request.setAttribute(Response.RESULT, responseObject);
                    
                    LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
                    
                    return "/ResultJSON.jsp";
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잠시 후 다시 시도해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (result > 0) {
            // 비밀번호 초기화 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "비밀번호가 초기화되었습니다.");
        } else {
            // 비밀번호 초기화 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "비밀번호 초기화가 실패하였습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
        
        LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 유저 이메일 전송
     */
    @ControllerMethodInfo(id = "/api/admin/user/sendEmail.do")
    public String sendEmail(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.ADMIN_SEND_USER_EMAIL;                                 // 인터페이스 번호
        int type = UserDAO.USER_TYPE_PUBLIC;                                    // 유저 유형
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        int seq = HttpUtils.getParameterInt(request, "seq", -1);                            // 유저 번호
        
        JSONObject queryObject = new JSONObject();
        queryObject.put("seq", seq != -1? seq : null);
       
        JSONObject requestObject = HttpUtils.getBodyJson(request);
        JSONObject responseObject = new JSONObject();
        
        // session
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (sessionMap != null) {
            type = sessionMap.getInt("type");
            user = sessionMap.getInt("seq");
        }
        
        if (user == null || type != UserDAO.USER_TYPE_ADMIN) {
            // 비로그인, 관리자가 아닌 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (seq < 1) {
            // 유저 번호가 없는 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        boolean isSucceed = false;                                              // 이메일 전송 결과
        
        try {
            // 필수 입력 사항
            String title = requestObject.getString("title");                    // 제목
            String content = requestObject.getString("content");                // 내용
            
            ResultMap userMap = AdminDAO.selectUserBySeq(seq);                  // 유저 조회
            if (userMap != null) {
                // 유저 조회 성공
                String email = userMap.getString("email");
                
                if (email != null && !"".equals(email)) {
                    isSucceed = MailUtils.sendMail(email, title, content);      // 이메일 전송
                }
            } else {
                // 유저 조회 실패
                responseObject.put(Response.RESULT, Response.Result.FAILED);
                responseObject.put(Response.MESSAGE, "유저 조회에 실패하였습니다.");
                request.setAttribute(Response.RESULT, responseObject);
                
                LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
                
                return "/ResultJSON.jsp";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
            
            return "/ResultJSON.jsp";
        } catch (SQLException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잠시 후 다시 시도해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (isSucceed) {
            // 이메일 전송 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "이메일이 전송되었습니다.");
        } else {
            // 이메일 전송 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "이메일 전송에 실패하였습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
        
        LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 유저 코멘트 입력
     */
    @ControllerMethodInfo(id = "/api/admin/user/setComment.do")
    public String setComment(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.ADMIN_SET_USER_COMMENT;                                // 인터페이스 번호
        int type = UserDAO.USER_TYPE_PUBLIC;                                    // 유저 유형
        Integer user = null;                                                    // 유저 번호        
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        int seq = HttpUtils.getParameterInt(request, "seq", -1);                            // 유저 번호
        
        JSONObject queryObject = new JSONObject();
        queryObject.put("seq", seq != -1? seq : null);
        
        JSONObject requestObject = HttpUtils.getBodyJson(request);
        JSONObject responseObject = new JSONObject();
        
        // session
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (sessionMap != null) {
            type = sessionMap.getInt("type");
            user = sessionMap.getInt("seq");
        }
        
        if (user == null || type != UserDAO.USER_TYPE_ADMIN) {
            // 비로그인, 관리자가 아닌 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (seq < 1) {
            // 유저 번호가 없는 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        int result = 0;                                                         // 코멘트 입력 결과
        
        try {
            // 필수 입력 사항
            String comment = requestObject.getString("comment");                // 코멘트
            
            ResultMap userMap = AdminDAO.selectUserBySeq(seq);                  // 유저 조회
            if (userMap != null) {
                // 유저 조회 성공
                result = AdminDAO.updateUserComment(seq, comment);              // 코멘트 입력
            } else {
                // 유저 조회 실패
                responseObject.put(Response.RESULT, Response.Result.FAILED);
                responseObject.put(Response.MESSAGE, "유저 조회에 실패하였습니다.");
                request.setAttribute(Response.RESULT, responseObject);
            
                LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
                
                return "/ResultJSON.jsp";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잠시후 다시 시도해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (result > 0) {
            // 코멘트 입력 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "코멘트가 입력되었습니다.");
        } else {
            // 코멘트 입력 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "코멘트 입력에 실패하였습니다.");
        }
    
        request.setAttribute(Response.RESULT, responseObject);
            
        LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 유저 삭제
     */
    @ControllerMethodInfo(id = "/api/admin/user/deleteUser.do")
    public String deleteUser(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.ADMIN_DELETE_USER;                                     // 인터페이스 번호
        int type = UserDAO.USER_TYPE_PUBLIC;                                    // 유저 유형
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        int seq = HttpUtils.getParameterInt(request, "seq", -1);                            // 유저 번호
        
        JSONObject queryObject = new JSONObject();
        queryObject.put("seq", seq != -1? seq : null);
        
        JSONObject responseObject = new JSONObject();
        
        // session
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (sessionMap != null) {
            type = sessionMap.getInt("type");
            user = sessionMap.getInt("seq");
        }
        
        if (user == null || type != UserDAO.USER_TYPE_ADMIN) {
            // 비로그인, 관리자가 아닌 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (seq < 1) {
            // 유저 번호가 없는 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        int result = 0;                                                         // 유저 삭제 결과
        
        try {
            ResultMap userMap = AdminDAO.selectUserBySeq(seq);                  // 유저 조회
            if (userMap != null) {
                // 유저 조회 성공
                result = AdminDAO.updateDisableUser(seq);                       // 유저 삭제
            } else {
                // 유저 조회 실패
                responseObject.put(Response.RESULT, Response.Result.FAILED);
                responseObject.put(Response.MESSAGE, "유저 조회에 실패하였습니다.");
                request.setAttribute(Response.RESULT, responseObject);
                
                LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
                
                return "/ResultJSON.jsp";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잠시 후 다시 시도해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (result > 0) {
            // 유저 삭제 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "유저가 삭제되었습니다.");
        } else {
            // 유저 삭제 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "유저 삭제에 실패하였습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
        
        LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 게시글 목록 조회
     */
    @ControllerMethodInfo(id = "/api/admin/board/getContentList.do")
    public String getContentList(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.ADMIN_GET_BOARD_CONTENT_LIST;                          // 인터페이스 번호
        int type = UserDAO.USER_TYPE_PUBLIC;                                    // 유저 유형
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        int page = HttpUtils.getParameterInt(request, "page", -1);                          // 페이지 번호
        int sortOption = HttpUtils.getParameterInt(request, "sort_option", -1);             // 정렬 유형
        int searchOption = HttpUtils.getParameterInt(request, "search_option", -1);         // 검색 유형
        String searchText = HttpUtils.getParameterString(request, "search_text", null);     // 검색어
        
        JSONObject queryObject = new JSONObject();
        queryObject.put("page", page != -1? page : null);
        queryObject.put("sort_option", sortOption != -1? sortOption : null);
        queryObject.put("search_option", searchOption != -1? searchOption : null);
        queryObject.put("search_text", searchText);
        
        page = page != -1? page : 1;
        sortOption = sortOption != -1? sortOption : AdminDAO.BOARD_SORT_OPTION_RECENT;
        searchOption = searchOption != -1? searchOption : AdminDAO.BOARD_SEARCH_OPTION_TITLE;
        
        JSONObject responseObject = new JSONObject();
        
        // session
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (sessionMap != null) {
            type = sessionMap.getInt("type");
            user = sessionMap.getInt("seq");
        }
        
        if (user == null || type != UserDAO.USER_TYPE_ADMIN) {
            // 비로그인, 관리자가 아닌 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
            
            return "/ResultJSON.jsp";            
        }
        
        List<ResultMap> contentList = null;                                     // 게시글 목록
        ResultMap pageMap = null;
        
        try {
            int contentCount = 10;                                              // 목록 출력 개수
            int count = AdminDAO.selectBoardContentListCount(searchOption, searchText);     // 게시글 목록 개수 조회
            
            pageMap = PageUtils.getPagination(count, page, contentCount);       // 페이징
            int totalPage = pageMap.getInt("totalPage");
            if (page <= totalPage || page == 1) {
                // 올바른 페이지 번호에 접근한 경우
                int startCount = pageMap.getInt("startCount");
                contentList = AdminDAO.selectBoardContentList(sortOption, searchOption, searchText, startCount, contentCount);      // 게시글 목록 조회
            } else {
                // 없는 페이지 번호에 접근한 경우
                responseObject.put(Response.RESULT, Response.Result.ERROR);
                responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
                request.setAttribute(Response.RESULT, responseObject);
                
                LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
                
                return "/ResultJSON.jsp";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잠시 후 다시 시도해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (contentList != null) {
            // 게시글 목록 조회 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "");
            responseObject.put(Response.MAP, UtilJSON.mapToJSon((Map<String, Object>) pageMap));
            responseObject.put(Response.LIST, UtilJSON.convertArrayListToJSONArray((ArrayList) contentList));
        } else {
            // 게시글 목록 조회 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "게시글 목록 조회에 실패하였습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
        
        LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 게시글 공개 활성화/비활성화
     */
    @ControllerMethodInfo(id = "/api/admin/board/enableViewContent.do")
    public String enableViewContent(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.ADMIN_ENABLE_VIEW_BOARD_CONTENT;                       // 인터페이스 번호
        int type = UserDAO.USER_TYPE_PUBLIC;                                    // 유저 유형
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        int seq = HttpUtils.getParameterInt(request, "seq", -1);                            // 게시글 번호
        
        JSONObject queryObject = new JSONObject();
        queryObject.put("seq", seq != -1? seq : null);
        
        JSONObject reuqestObject = HttpUtils.getBodyJson(request);
        JSONObject responseObject = new JSONObject();
        
        // session
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (sessionMap != null) {
            type = sessionMap.getInt("type");
            user = sessionMap.getInt("seq");
        }
        
        if (user == null || type != UserDAO.USER_TYPE_ADMIN) {
            // 비로그인, 관리자가 아닌 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, reuqestObject, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (seq < 1) {
            // 게시글 번호가 없는 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, reuqestObject, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        int result = 0;                                                         // 게시글 공개 활성화/비활성화 결과
        boolean enable = false;                                                 
        
        try {
            // 필수 입력 사항
            enable = reuqestObject.getBoolean("enable");                        // 게시글 공개 활성화/비활성화 여부
            
            ResultMap contentMap = BoardDAO.selectContentBySeq(seq);            // 게시글 조회
            if (contentMap != null) {
                // 게시글 조회 성공
                result = AdminDAO.updateEnableViewContent(seq, enable);         // 게시글 공개 활성화/비활성화
            } else {
                // 게시글 조회 실패
                responseObject.put(Response.RESULT, Response.Result.FAILED);
                responseObject.put(Response.MESSAGE, "게시글 조회에 실패하였습니다.");
                request.setAttribute(Response.RESULT, responseObject);
                
                LogDAO.insertLog(api, user, userAgent, queryObject, reuqestObject, responseObject);
                
                return "/ResultJSON.jsp";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, reuqestObject, responseObject);
            
            return "/ResultJSON.jsp";
        } catch (SQLException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잠시 후 다시 시도해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, reuqestObject, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (result > 0) {
            // 게시글 공개 활성화/비활성화 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            
            if (enable) {
                responseObject.put(Response.MESSAGE, "게시글 공개가 활성화되었습니다.");
            } else {
                responseObject.put(Response.MESSAGE, "게시글 공개가 비활성화되었습니다.");
            }
        } else {
            // 게시글 공개 활성화/비활성화 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            
            if (enable) {
                responseObject.put(Response.MESSAGE, "게시글 공개 활성화가 실패하였습니다.");
            } else {
                responseObject.put(Response.MESSAGE, "게시글 공개 비활성화가 실패하였습니다.");
            }
        }
        
        request.setAttribute(Response.RESULT, responseObject);
        
        LogDAO.insertLog(api, user, userAgent, queryObject, reuqestObject, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 게시글 삭제
     */
    @ControllerMethodInfo(id = "/api/admin/board/deleteContent.do")
    public String deleteContent(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.ADMIN_DELETE_BOARD_CONTENT;                            // 인터페이스 번호
        int type = UserDAO.USER_TYPE_PUBLIC;                                    // 유저 유형
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        int seq = HttpUtils.getParameterInt(request, "seq", -1);                            // 유저 번호
        
        JSONObject queryObject = new JSONObject();
        queryObject.put("seq", seq != -1? seq : null);
        
        JSONObject responseObject = new JSONObject();
        
        // session
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (sessionMap != null) {
            type = sessionMap.getInt("type");
            user = sessionMap.getInt("seq");
        }
        
        if (user == null || type != UserDAO.USER_TYPE_ADMIN) {
            // 비로그인, 관리자가 아닌 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
        
            return "/ResultJSON.jsp";
        }
        
        if (seq < 1) {
            // 게시글 번호가 없는 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        int result = 0;                                                         // 게시글 삭제 결과
        
        try {
            ResultMap contentMap = BoardDAO.selectContentBySeq(seq);            // 게시글 조회
            if (contentMap != null) {
                // 게시글 조회 성공
                
                // 첨부파일 확인
                String contextPath = UploadConst.contextPath();
                
                List<String> fileList = (List<String>) contentMap.get("file");                // 기존 첨부파일
                
                // 파일 삭제
                for (String filePath : fileList) {
                    FileUtils.delete(contextPath + filePath);
                }
                
                FileDAO.deleteFileList(fileList);                            // 파일 삭제
                
                result = AdminDAO.updateDisableContent(seq);                    // 게시글 삭제
            } else {
                // 게시글 조회 실패
                responseObject.put(Response.RESULT, Response.Result.FAILED);
                responseObject.put(Response.MESSAGE, "게시글 조회에 실패하였습니다.");
                request.setAttribute(Response.RESULT, responseObject);
            
                LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
                
                return "/ResultJSON.jsp";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잠시 후 다시 시도해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (result > 0) {
            // 게시글 삭제 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "게시글이 삭제되었습니다.");
        } else {
            // 게시글 삭제 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "게시글 삭제가 실패하였습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
            
        LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 댓글 목록 조회
     */
    @ControllerMethodInfo(id = "/api/admin/board/getCommentList.do")
    public String getCommentList(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.ADMIN_GET_BOARD_COMMENT_LIST;                          // 인터페이스 번호
        int type = UserDAO.USER_TYPE_PUBLIC;                                    // 유저 유형
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        int page = HttpUtils.getParameterInt(request, "page", -1);                          // 페이지 번호
        int sortOption = HttpUtils.getParameterInt(request, "sort_option", -1);             // 정렬 유형 
        int searchOption = HttpUtils.getParameterInt(request, "search_option", -1);         // 검색 유형
        String searchText = HttpUtils.getParameterString(request, "search_text", null);     // 검색어
        
        JSONObject queryObject = new JSONObject();
        queryObject.put("page", page != -1? page : null);
        queryObject.put("sort_option", sortOption != -1? sortOption : null);
        queryObject.put("search_option", searchOption != -1? searchOption : null);
        
        page = page != -1? page : 1;
        sortOption = sortOption != -1? sortOption : AdminDAO.COMMENT_SORT_OPTION_RECENT;
        searchOption = searchOption != -1? searchOption : AdminDAO.COMMENT_SEARCH_OPTION_TITLE;
        
        JSONObject responseObject = new JSONObject();
        
        // session
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (sessionMap != null) {
            type = sessionMap.getInt("type");
            user = sessionMap.getInt("seq");
        }
        
        if (user == null || type != UserDAO.USER_TYPE_ADMIN) {
            // 비로그인, 관리자가 아닌 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        List<ResultMap> commentList = null;                                     // 댓글 목록
        ResultMap pageMap = null;
        
        try {
            int contentCount = 10;                                                  // 목록 출력 개수
            int count = AdminDAO.selectCommentListCount(searchOption, searchText);  // 댓글 목록 개수 조회
            
            pageMap = PageUtils.getPagination(count, page, contentCount);
            int totalPage = pageMap.getInt("totalPage");
            if (page <= totalPage || page == 1) {
                // 올바른 페이지 번호에 접근한 경우
                int startCount = pageMap.getInt("startCount");
                commentList = AdminDAO.selectCommentList(sortOption, searchOption, searchText, startCount, contentCount);   // 댓글 목록 조회
            } else {
                // 없는 페이지 벊소에 접근한 경우
                responseObject.put(Response.RESULT, Response.Result.ERROR);
                responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
                request.setAttribute(Response.RESULT, responseObject);
                
                LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
                
                return "/ResultJSON.jsp";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잠시 후 다시 시도해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
                
            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (commentList != null) {
            // 댓글 목록 조회 성공
            for (ResultMap commentMap : commentList) {
                if (commentMap.containsKey("reply") && commentMap.get("reply") != null) {
                    JSONArray replyArray = commentMap.getJSONArray("reply");
                    commentMap.put("reply", UtilJSON.convertJSONArrayToArrayList(replyArray));
                }
            }
            
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "");
            responseObject.put(Response.MAP, UtilJSON.mapToJSon((Map<String, Object>) pageMap));
            responseObject.put(Response.LIST, UtilJSON.convertArrayListToJSONArray((ArrayList) commentList));
        } else {
            // 댓글 목록 조회 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "댓글 목록 조회에 실패하였습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
                
        LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 댓글 공개 활성화/비활성화
     */
    @ControllerMethodInfo(id = "/api/admin/board/enableViewComment.do")
    public String enableViewComment(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.ADMIN_ENABLE_VIEW_BOARD_COMMENT;                       // 인터페이스 번호
        int type = UserDAO.USER_TYPE_PUBLIC;                                    // 유저 유형
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        int seq = HttpUtils.getParameterInt(request, "seq", -1);                            // 유저 번호
        
        JSONObject queryObject = new JSONObject();
        queryObject.put("seq", seq != -1? seq : null);
        
        JSONObject requestObject = HttpUtils.getBodyJson(request);
        JSONObject responseObject = new JSONObject();
        
        // session
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (sessionMap != null) {
            type = sessionMap.getInt("type");
            user = sessionMap.getInt("seq");
        }
        
        if (user == null || type != UserDAO.USER_TYPE_ADMIN) {
            // 비로그인, 관리자가 아닌 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
        
            return "/ResultJSON.jsp";            
        }
        
        if (seq < 1) {
            // 댓글 번호가 없는 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        int result = 0;                                                         // 댓글 공개 활성화/비활성화 결과
        boolean enable = false;
        
        try {
            // 필수 입력 사항
            enable = requestObject.getBoolean("enable");                        // 댓글 공개 활성화/비활성화 여부
            
            ResultMap commentMap = BoardDAO.selectCommentBySeq(seq);            // 댓글 조회
            if (commentMap != null) {
                // 댓글 조회 성공
                result = AdminDAO.updateEnableViewComment(seq, enable);         // 댓글 공개 활성화/비활성화
            } else {
                // 댓글 조회 실패
                responseObject.put(Response.RESULT, Response.Result.FAILED);
                responseObject.put(Response.MESSAGE, "댓글 조회에 실패하였습니다.");
                request.setAttribute(Response.RESULT, responseObject);
            
                LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
                
                return "/ResultJSON.jsp";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
            
            return "/ResultJSON.jsp";
        } catch (SQLException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잠시 후 다시 시도해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (result > 0) {
            // 댓글 공개 활성화/비활성화 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            
            if (enable) {
                responseObject.put(Response.MESSAGE, "댓글 공개가 활성화되었습니다.");
            } else {
                responseObject.put(Response.MESSAGE, "댓글 공개가 비활성화되었습니다.");
            }
        } else {
            // 댓글 공개 활성화/비활성화 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            
            if (enable) {
                responseObject.put(Response.MESSAGE, "댓글 공개 활성화가 실패하였습니다.");
            } else {
                responseObject.put(Response.MESSAGE, "댓글 공개 비활성화가 실패하였습니다.");
            }
        }
        
        request.setAttribute(Response.RESULT, responseObject);
            
        LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 댓글 삭제
     */
    @ControllerMethodInfo(id = "/api/admin/board/deleteComment.do")
    public String deleteComment(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.ADMIN_DELETE_BOARD_COMMENT;                            // 인터페이스 번호
        int type = UserDAO.USER_TYPE_PUBLIC;                                    // 유저 유형
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        int seq = HttpUtils.getParameterInt(request, "seq", -1);                            // 댓글 번호
        
        JSONObject queryObject = new JSONObject();
        queryObject.put("seq", seq != -1? seq : null);
        
        JSONObject responseObject = new JSONObject();
        
        // session
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (sessionMap != null) {
            type = sessionMap.getInt("type");
            user = sessionMap.getInt("seq");
        }
        
        if (user == null || type != UserDAO.USER_TYPE_ADMIN) {
            // 비로그인, 관리자가 아닌 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (seq < 1) {
            // 댓글 번호가 없는 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        int result = 0;                                                         // 댓글 삭제 결과
        
        try {
            ResultMap commentMap = BoardDAO.selectCommentBySeq(seq);            // 댓글 조회
            if (commentMap != null) {
                // 댓글 조회 성공
                result = AdminDAO.updateDisableComment(seq);                    // 댓글 삭제
            } else {
                // 댓글 조회 실패
                responseObject.put(Response.RESULT, Response.Result.FAILED);
                responseObject.put(Response.MESSAGE, "댓글 조회에 실패하였습니다.");
                request.setAttribute(Response.RESULT, responseObject);
                
                LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
                
                return "/ResultJSON.jsp";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잠시 후 다시 시도해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (result > 0) {
            // 댓글 삭제 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "댓글이 삭제되었습니다.");
        } else {
            // 댓글 삭제 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "댓글 삭제가 실패하였습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
        
        LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 앨범 목록 조회
     */
//    해결해결 여기에 pk 값이 포함되어 넘어올 수 있게 해야한다
    @ControllerMethodInfo(id = "/api/admin/album/getAlbumList.do")
    public String getAlbumList(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.ADMIN_GET_ALBUM_LIST;                                  // 인터페이스 번호
        int type = UserDAO.USER_TYPE_PUBLIC;                                    // 유저 유형
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        int page = HttpUtils.getParameterInt(request, "page", -1);                          // 페이지 번호
        int sortOption = HttpUtils.getParameterInt(request, "sort_option", -1);             // 정렬 유형
        
        JSONObject queryObject = new JSONObject();
        queryObject.put("page", page != -1? page : null);
        queryObject.put("sort_option", sortOption != -1? sortOption : null);
        
        page = page != -1? page : 1;
        sortOption = sortOption != -1? sortOption : AdminDAO.ALBUM_SORT_OPTION_RECENT;
        
        JSONObject responseObject = new JSONObject();
        
        // session
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (sessionMap != null) {
            type = sessionMap.getInt("type");
            user = sessionMap.getInt("seq");
        }
        
        if (user == null || type != UserDAO.USER_TYPE_ADMIN) {
            // 비로그인, 관리자가 아닌 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        List<ResultMap> albumList = null;                                       // 앨범 목록
        ResultMap pageMap = null;

        try {
//            관리자 모드는 다보이니까 카운트 값 변경 필요 없음
            int contentCount = 10;                                              // 목록 출력 개수
            int count = AdminDAO.selectAlbumListCount();                        // 앨범 목록 개수 조회
            
            pageMap = PageUtils.getPagination(count, page, contentCount);       // 페이징
            int totalPage = pageMap.getInt("totalPage");
            if (page <= totalPage || page == 1) {
                // 올바른 페이지 번호에 접근한 경우
                int startCount = pageMap.getInt("startCount");
//                여기서 pk 값 포함해서 받아와야 한다
                albumList = AdminDAO.selectAlbumList(sortOption, startCount, contentCount);     // 앨범 목록 조회
            } else {
                // 없는 페이지 번호에 접근한 경우
                responseObject.put(Response.RESULT, Response.Result.ERROR);
                responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
                request.setAttribute(Response.RESULT, responseObject);
                
                LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
                
                return "/ResultJSON.jsp";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잠시 후 다시 시도해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (albumList != null) {
            // 앨범 조회 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "");
            responseObject.put(Response.MAP, UtilJSON.mapToJSon((Map<String, Object>) pageMap));
            responseObject.put(Response.LIST, UtilJSON.convertArrayListToJSONArray((ArrayList) albumList));
        } else {
            // 앨범 조회 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "앨범 목록 조회에 실패하였습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
        
        LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 앨범 조회
     */
    @ControllerMethodInfo(id = "/api/admin/album/getAlbum.do")
    public String getAlbum(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.ADMIN_GET_ALBUM;                                       // 인터페이스 번호
        int type = UserDAO.USER_TYPE_PUBLIC;                                    // 유저 유형
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        String id = HttpUtils.getParameterString(request, "id", null);                      // 앨범 아이디
        
        JSONObject queryObject = new JSONObject();
        queryObject.put("id", id);
        
        JSONObject responseObject = new JSONObject();
        
        // session
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (sessionMap != null) {
            type = sessionMap.getInt("type");
            user = sessionMap.getInt("seq");
        }
        
        if (user == null || type != UserDAO.USER_TYPE_ADMIN) {
            // 비로그인, 관리자가 아닌 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (id == null) {
            // 앨범 아이디가 없는 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        ResultMap albumMap = null;                                              // 앨범
        
        try {
            albumMap = AdminDAO.selectDetailAlbumBySeq(id);                     // 앨범 조회
            if (albumMap != null) {
                // 앨범 조회 성공                
                responseObject.put(Response.RESULT, Response.Result.SUCCESS);
                responseObject.put(Response.MESSAGE, "");
                responseObject.put(Response.MAP, UtilJSON.mapToJSon((Map<String, Object>) albumMap));
            } else {
                // 앨범 조회 실패
                responseObject.put(Response.RESULT, Response.Result.FAILED);
                responseObject.put(Response.MESSAGE, "앨범 조회에 실패하였습니다.");
            }            
        } catch (SQLException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잠시 후 다시 시도해주세요.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
        
        LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 앨범 수정
     */
    @ControllerMethodInfo(id = "/api/admin/album/modifyAlbum.do")
    public String modifyAlbum(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.ADMIN_MODIFY_ALBUM;                                    // 인터페이스 번호
        int type = UserDAO.USER_TYPE_PUBLIC;                                    // 유저 유형
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        String id = HttpUtils.getParameterString(request, "id", null);                      // 앨범 아이디
        
        JSONObject queryObject = new JSONObject();
        queryObject.put("id", id);
        
        JSONObject requestObject = HttpUtils.getBodyJson(request);
        JSONObject responseObject = new JSONObject();
        
        // session
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (sessionMap != null) {
            type = sessionMap.getInt("type");
            user = sessionMap.getInt("seq");
        }
        
        if (user == null || type != UserDAO.USER_TYPE_ADMIN) {
            // 비로그인, 관리자가 아닌 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (id == null) {
            // 앨범 번호가 없는 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        int result = 0;                                                         // 앨범 수정 결과
        
        try {
            // 필수 입력 사항
            String name = requestObject.getString("name");                      // 이름
            String introduction = requestObject.getString("introduction");      // 소개
                
            // 선택 입력 사항
            String category = !requestObject.isNull("category")? requestObject.getString("category") : null;        // 카테고리
            String userTag = !requestObject.isNull("user_tag")? requestObject.getString("user_tag") : null;         // 사용자 태그
            String adminTag = !requestObject.isNull("admin_tag")? requestObject.getString("admin_tag") : null;      // 관리자 태그
            String thumbnail = !requestObject.isNull("thumbnail")? requestObject.getString("thumbnail") : null;     // 썸네일
            
            ResultMap albumMap = AdminDAO.selectAlbum(id);                      // 앨범 조회
            if (albumMap != null) {
                // 앨범 조회 성공
                String contextPath = UploadConst.contextPath();
                String tempPath = FileUtils.getTempPath();
                
                List<String> addFileList = new ArrayList<>();
                List<String> removeFileList = new ArrayList<>();
                
                String oldThumbnail = albumMap.getString("thumbnail");
                if (thumbnail == null || "".equals(thumbnail)) {
                    // 썸네일 생성 및 기존 썸네일 삭제
                    thumbnail = ThumbnailUtils.createThumbnail(name);           // 썸네일 생성
                    addFileList.add(thumbnail);
                    
                    removeFileList.add(oldThumbnail);
                } else if (!oldThumbnail.equals(thumbnail)) {
                    // 기존 썸네일 삭제 및 입력된 썸네일 이동
                    addFileList.add(thumbnail);
                    removeFileList.add(oldThumbnail);
                }
                
                thumbnail = thumbnail.substring(thumbnail.lastIndexOf("/") + 1);
                
                // 파일 삭제
                for (String filePath : removeFileList) {
                    FileUtils.delete(contextPath + filePath);
                }
                
                FileDAO.deleteFileList(removeFileList);                            // 파일 삭제
                
                // 파일 이동 (temp to upload path)
                for (String filePath : addFileList) {
                    // String file 은 /file-path/file-name 으로 구성
                    int index = filePath.lastIndexOf("/");

                    String uploadPath = filePath.substring(0, index);
                    String fileName = filePath.substring(index + 1);

                    File tempFile = new File(contextPath + tempPath, fileName);
                    File uploadFile = new File(contextPath + uploadPath, fileName);

                    FileUtils.copy(tempFile, uploadFile, true);      
                }
                
                FileDAO.updateEnableUpload(addFileList);                           // 파일 업로드 활성화
                
                result = AdminDAO.updateAlbum(id, name, introduction, category, userTag, adminTag, thumbnail);    // 앨범 수정
            } else {
                // 앨범 조회 실패
                responseObject.put(Response.RESULT, Response.Result.FAILED);
                responseObject.put(Response.MESSAGE, "앨범 조회가 실패하였습니다.");
                request.setAttribute(Response.RESULT, responseObject);
                
                LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
                
                return "/ResultJSON.jsp";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
            
            return "/ResultJSON.jsp";
        } catch (IOException | FontFormatException | SQLException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잠시 후 다시 시도해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (result > 0) {
            // 앨범 수정 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "앨범이 수정되었습니다.");
        } else {
            // 앨범 수정 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "앨범 수정이 실패하였습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
        
        LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 앨범 상태 변경
     */
    @ControllerMethodInfo(id = "/api/admin/album/modifyStatus.do")
    public String modifyStatus(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.ADMIN_MODIFY_ALBUM_STATUS;                             // 인터페이스 번호
        int type = UserDAO.USER_TYPE_PUBLIC;                                    // 유저 유형
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        String id = HttpUtils.getParameterString(request, "id", null);                      // 앨범 아이디
        
        JSONObject queryObject = new JSONObject();
        queryObject.put("id", id);
        
        JSONObject requestObject = HttpUtils.getBodyJson(request);
        JSONObject responseObject = new JSONObject();
        
        // session
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (sessionMap != null) {
            type = sessionMap.getInt("type");
            user = sessionMap.getInt("seq");
        }
        
        if (user == null || type != UserDAO.USER_TYPE_ADMIN) {
            // 비로그인, 관리자가 아닌 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (id == null) {
            // 앨범 아이디가 없는 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        int result = 0;                                                         // 앨범 상태 변경 결과
        
        try { 
            // 필수 입력 사항
            String status = requestObject.getString("status");                  // 상태
            
            ResultMap albumMap = AdminDAO.selectAlbum(id);                      // 앨범 조회
            if (albumMap != null) {
                // 앨범 조회 성공    
                result = AdminDAO.updateAlbumStatus(id, status);                // 앨범 상태 변경
            } else {
                // 앨범 조회 실패
                responseObject.put(Response.RESULT, Response.Result.FAILED);
                responseObject.put(Response.MESSAGE, "앨범 조회에 실패하였습니다.");
                request.setAttribute(Response.RESULT, responseObject);
                
                LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
                
                return "/ResultJSON.jsp";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
            
            return "/ResultJSON.jsp";
        } catch (SQLException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잠시 후 다시 시도해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (result > 0) {
            // 앨범 상태 변경 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "앨범 상태가 변경되었습니다.");
        } else {
            // 앨범 상태 변경 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "앨범 상태 변경이 실패하였습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
        
        LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 앨범 등록 거절 사유 조회
     */
    @ControllerMethodInfo(id = "/api/admin/album/getAlbumDenialReason.do")
    public String getAlbumDenialReason(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.ADMIN_GET_ALBUM_DENIAL_REASON;                         // 인터페이스 번호
        int type = UserDAO.USER_TYPE_PUBLIC;                                    // 유저 유형
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        String id = HttpUtils.getParameterString(request, "id", null);                      // 앨범 아이디
        
        JSONObject queryObject = new JSONObject();
        queryObject.put("id", id);
        
        JSONObject responseObject = new JSONObject();
        
        // session
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (session != null) {
            type = sessionMap.getInt("type");
            user = sessionMap.getInt("seq");
        }
        
        if (user == null || type != UserDAO.USER_TYPE_ADMIN) {
            // 비로그인, 관리자가 아닌 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (id == null) {
            // 앨범 번호가 없는 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        ResultMap reasonMap = null;                                             // 앨범
        
        try {
            ResultMap albumMap = AdminDAO.selectAlbum(id);                      // 앨범 조회
            if (albumMap != null) {
                // 앨범 조회 성공
                reasonMap = AdminDAO.selectAlbumDenialReason(id);               // 앨범 등록 거절 사유 조회
            } else {
                // 앨범 조회 실패
                responseObject.put(Response.RESULT, Response.Result.FAILED);
                responseObject.put(Response.MESSAGE, "앨범 조회에 실패하였습니다.");
                request.setAttribute(Response.RESULT, responseObject);
                
                LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
                
                return "/ResultJSON.jsp";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잠시 후 다시 시도해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (reasonMap != null) {
            // 거절 사유 조회 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "");
            responseObject.put(Response.MAP, UtilJSON.mapToJSon((Map<String, Object>) reasonMap));
        } else {
            // 거절 사유 조회 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "거절 사유 조회가 실패하였습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
        
        LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 앨범 등록 거절 사유 작성
     */
    @ControllerMethodInfo(id = "/api/admin/album/setAlbumDenialReason.do")
    public String setAlbumDenialReason(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.ADMIN_SET_ALBUM_DENIAL_REASON;                         // 인터페이스 번호
        int type = UserDAO.USER_TYPE_PUBLIC;                                    // 유저 유형
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        String id = HttpUtils.getParameterString(request, "id", null);                      // 앨범 아이디
        
        JSONObject queryObject = new JSONObject();
        queryObject.put("id", id);
        
        JSONObject requestObject = HttpUtils.getBodyJson(request);
        JSONObject responseObject = new JSONObject();
        
        // session
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (sessionMap != null) {
            type = sessionMap.getInt("type");
            user = sessionMap.getInt("seq");
        }
        
        if (user == null || type != UserDAO.USER_TYPE_ADMIN) {
            // 비로그인, 관리자가 아닌 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (id == null) {
            // 앨범 번호가 없는 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        int result = 0;                                                         // 거절 사유 작성 결과
        
        try {
            // 필수 입력 사항
            String reason = requestObject.getString("reason");                  // 거절 사유
            
            ResultMap albumMap = AdminDAO.selectAlbum(id);                      // 앨범 조회
            if (albumMap != null) {
                // 앨범 조회 성공
                result = AdminDAO.updateAlbumDenialReason(id, reason);          // 거절 사유 작성
            } else {
                // 앨범 조회 실패
                responseObject.put(Response.RESULT, Response.Result.FAILED);
                responseObject.put(Response.MESSAGE, "앨범 조회에 실패하였습니다.");
                request.setAttribute(Response.RESULT, responseObject);
                
                LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
                
                return "/ResultJSON.jsp";
            }
        } catch (JSONException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
            
            return "/ResultJSON.jsp";
        } catch (SQLException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잠시 후 다시 시도해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (result > 0) {
            // 거절 사유 작성 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "거절 사유가 작성되었습니다.");
        } else {
            // 거절 사유 작성 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "거절 사유 작성이 실패하였습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
        
        LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 앨범 삭제
     */
    @ControllerMethodInfo(id = "/api/admin/album/deleteAlbum.do")
    public String deleteAlbum(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.ADMIN_DELETE_ALBUM;                                    // 인터페이스 번호
        int type = UserDAO.USER_TYPE_PUBLIC;                                    // 유저 유형
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        String id = HttpUtils.getParameterString(request, "id", null);                      // 앨범 아이디
        
        JSONObject queryObject = new JSONObject();
        queryObject.put("id", id);
        
        JSONObject responseObject = new JSONObject();
        
        // session
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (sessionMap != null) {
            type = sessionMap.getInt("type");
            user = sessionMap.getInt("seq");
        }
        
        if (user == null || type != UserDAO.USER_TYPE_ADMIN) {
            // 비로그인, 관리자가 아닌 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (id == null) {
            // 앨범 아이디가 없는 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        int result = 0;                                                         // 앨범 삭제 결과
        
        try {
            ResultMap albumMap = AdminDAO.selectAlbum(id);                      // 앨범 조회
            if (albumMap != null) {
                // 앨범 조회 성공
                result = AdminDAO.updateDisableAlbum(id);                      // 앨범 삭제
            } else {
                // 앨범 조회 실패
                responseObject.put(Response.RESULT, Response.Result.FAILED);
                responseObject.put(Response.MESSAGE, "앨범 조회에 실패하였습니다.");
                request.setAttribute(Response.RESULT, responseObject);
                
                LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
                
                return "/ResultJSON.jsp";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잠시 후 다시 시도해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (result > 0) {
            // 앨범 삭제 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "앨범이 삭제되었습니다.");
        } else {
            // 앨범 삭제 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "앨범 삭제가 실패하였습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
        
        LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 포털페이지 설정 정보 조회
     */
    @ControllerMethodInfo(id = "/api/admin/portal/getSetting.do")
    public String getSetting(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.ADMIN_GET_PORTAL_SETTING;                              // 인터페이스 번호
        int type = UserDAO.USER_TYPE_PUBLIC;                                    // 유저 유형
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        
        JSONObject responseObject = new JSONObject();
        
        // session
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (sessionMap != null) {
            type = sessionMap.getInt("type");
            user = sessionMap.getInt("seq");
        }
        
        if (user == null || type != UserDAO.USER_TYPE_ADMIN) {
            // 비로그인, 관리자가 아닌 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, null, null, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        ResultMap settingMap = null;                                            // 설정 정보
        
        try {
            settingMap = AdminDAO.selectPortalSetting();                        // 설정 정보 조회
        } catch (SQLException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잠시 후 다시 시도해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, null, null, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (settingMap != null) {
            // 설정 정보 조회 성공
            if (settingMap.containsKey("banner") && settingMap.get("banner") != null) {
                settingMap.put("banner", UtilJSON.convertJSONArrayToArrayList(settingMap.getJSONArray("banner")));
            }

            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "");
            responseObject.put(Response.MAP, UtilJSON.mapToJSon((Map<String, Object>) settingMap));
        } else {
            // 설정 정보 조회 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "설정 정보 조회에 실패하였습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
        
        LogDAO.insertLog(api, user, userAgent, null, null, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 포털페이지 설정 정보 저장
     */
    @ControllerMethodInfo(id = "/api/admin/portal/setSetting.do")
    public String setSetting(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.ADMIN_SET_PORTAL_SETTING;                              // 인터페이스 번호
        int type = UserDAO.USER_TYPE_PUBLIC;                                    // 유저 유형
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        
        JSONObject requestObject = HttpUtils.getBodyJson(request);
        JSONObject responseObject = new JSONObject();
        
        // session
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (sessionMap != null) {
            type = sessionMap.getInt("type");
            user = sessionMap.getInt("seq");
        }
        
        if (user == null || type != UserDAO.USER_TYPE_ADMIN) {
            // 비로그인, 관리자가 아닌 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, null, requestObject, responseObject);
            
            return "/ResultJSON.jsp";
        }

        int result = 0;                                                         // 설정 정보 저장 결과
        
        try {
            // 선택 입력 사항
            JSONArray bannerArray = !requestObject.isNull("banner")? requestObject.getJSONArray("banner") : new JSONArray();    // 배너
            
            // 앨범
            int albumSort = !requestObject.isNull("album_sort")? requestObject.getInt("album_sort") : 1;                        // 앨범 정렬 유형
            int albumCount = !requestObject.isNull("album_count")? requestObject.getInt("album_count") : 10;                    // 앨범 목록 출력 개수
            
            // 팝업
            boolean enablePopup = !requestObject.isNull("popup_enable")? requestObject.getBoolean("popup_enable") : false;      // 팝업 활성화 여부
            int popupWidth = !requestObject.isNull("popup_width")? requestObject.getInt("popup_width") : 0;                     // 팝업 너비
            int popupHeight = !requestObject.isNull("popup_width")? requestObject.getInt("popup_height") : 0;                   // 팝업 높이
            String popupContent = !requestObject.isNull("popup_content")? requestObject.getString("popup_content") : null;      // 팝업 내용

            String contextPath = UploadConst.contextPath();
            String tempPath = FileUtils.getTempPath();
            
            // 배너 정보
            List<ResultMap> bannerList = UtilJSON.convertJSONArrayToArrayList(bannerArray);
            
            List<String> oldFileList = AdminDAO.selectPortalBannerImages();     // 기존 배너 이미지
            List<String> newFileList = new ArrayList<>();                       // 입력된 배너 이미니
            List<String> overlapList = new ArrayList<>();
            
            for (ResultMap bannerMap : bannerList) {
                String file = bannerMap.getString("file");
                
                newFileList.add(file);
                bannerMap.put("file", file.substring(file.lastIndexOf("/") + 1));
            }
            
            for (String filePath : newFileList) {
                if (oldFileList.contains(filePath)) {
                    overlapList.add(filePath);
                }
            }
            
            oldFileList.removeAll(overlapList);
            newFileList.removeAll(overlapList);
            
            // 파일 삭제
            for (String filePath : oldFileList) {
                FileUtils.delete(contextPath + filePath);
            }
                
            FileDAO.deleteFileList(oldFileList);                                // 파일 삭제

            // 파일 이동 (temp to upload path)
            for (String filePath : newFileList) {
                // String file 은 /file-path/file-name 으로 구성
                int index = filePath.lastIndexOf("/");

                String uploadPath = filePath.substring(0, index);
                String fileName = filePath.substring(index + 1);

                File tempFile = new File(contextPath + tempPath, fileName);
                File uploadFile = new File(contextPath + uploadPath, fileName);

                FileUtils.copy(tempFile, uploadFile, true);      
            }

            FileDAO.updateEnableUpload(newFileList);                               // 파일 업로드 활성화

            bannerArray = UtilJSON.convertArrayListToJSONArray((ArrayList<ResultMap>) bannerList);
           
            result = AdminDAO.updatePortalSetting(bannerArray, albumSort, albumCount, enablePopup, popupWidth, popupHeight, popupContent);      // 설정 정보 저장
        } catch (JSONException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, null, requestObject, responseObject);
            
            return "/ResultJSON.jsp";
        } catch (SQLException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잠시 후 다시 시도해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, null, requestObject, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (result > 0) {
            // 설정 정보 저장 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "설정 정보가 저장되었습니다.");
        } else {
            // 설정 정보 저장 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "설정 정보 저장에 실패하였습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
        
        LogDAO.insertLog(api, user, userAgent, null, requestObject, responseObject);
        
        return "/ResultJSON.jsp";
    }
}
