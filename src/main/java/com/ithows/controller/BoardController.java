package com.ithows.controller;

import com.codeenator.dao.BoardDAO;
import com.codeenator.dao.FileDAO;
import com.codeenator.dao.LogDAO;
import com.codeenator.dao.UserDAO;
import com.codeenator.model.Response;
import com.codeenator.utils.FileUtils;
import com.codeenator.utils.PageUtils;
import com.ithows.BaseDebug;
import com.ithows.ResultMap;
import com.ithows.SessionInfo;
import com.ithows.base.ControllerClassInfo;
import com.ithows.base.ControllerMethodInfo;
import com.ithows.service.UploadConst;
import com.ithows.util.HttpUtils;
import com.ithows.util.UtilJSON;
import java.io.File;
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
 * 게시판 API Controller
 * 
 * - 공지사항 게시글 목록 조회 --------------------------- {@link #getNoticeList(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 공지사항 게시글 조회 ------------------------------- {@link #getNotice(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 공지사항 게시글 생성 ------------------------------- {@link #setNotice(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 공지사항 게시글 수정 ------------------------------- {@link #modifyNotice(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 공지사항 게시글 삭제 ------------------------------- {@link #deleteNotice(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * 
 * - Q&A 게시글 목록 조회 ------------------------------ {@link #getQuestionList(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - Q&A 게시글 조회 ---------------------------------- {@link #getQuestion(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - Q&A 게시글 생성 ---------------------------------- {@link #setQuestion(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - Q&A 게시글 수정 ---------------------------------- {@link #modifyQuestion(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - Q&A 게시글 삭제 ---------------------------------- {@link #deleteQuestion(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * 
 * - 댓글 목록 조회 ------------------------------------ {@link #getCommentList(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 댓글 작성 ---------------------------------------- {@link #setComment(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 댓글 삭제 ---------------------------------------- {@link #deleteComment(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 */
@ControllerClassInfo(controllerPage="/board/_board.jsp")
public class BoardController {
    
    static {
        BaseDebug.info("***BoardController.class Loading!!");
    }
    
    /**
     * 공지사항 게시글 목록 조회
     */
    @ControllerMethodInfo(id = "/api/board/getNoticeList.do")
    public String getNoticeList(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_GET_NOTICE_LIST;                                  // 인터페이스 번호
        Integer user = null;                                                    // 유저
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        int page = HttpUtils.getParameterInt(request, "page", -1);                          // 페이지 번호
        int searchOption = HttpUtils.getParameterInt(request, "search_option", -1);         // 검색 유형
        String searchText = HttpUtils.getParameterString(request, "search_text", null);     // 검색어
        
        JSONObject queryObject = new JSONObject();
        queryObject.put("page", page != -1? page : null);
        queryObject.put("search_option", searchOption != -1? searchOption : null);
        queryObject.put("search_text", searchText);
        
        page = page != -1? page : 1;
        searchOption = searchOption != -1? searchOption : BoardDAO.SEARCH_OPTION_ALL;
        
        JSONObject responseObject = new JSONObject();
        
        // session
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (sessionMap != null) {
            user = sessionMap.getInt("seq");
        }
        
        List<ResultMap> noticeList = null;                                      // 게시글 목록
        ResultMap pageMap = null;
        int count = 0;
        
        try {
            int contentCount = 10;                                              // 목록 출력 개수
            count = BoardDAO.selectNoticeListCount(searchOption, searchText);   // 게시글 목록 개수 조회
            
            pageMap = PageUtils.getPagination(count, page, contentCount);       // 페이징
            int totalPage = pageMap.getInt("totalPage");
            if (page <= totalPage || page == 1) {
                // 올바른 페이지 번호에 접근한 경우
                int startCount = pageMap.getInt("startCount");
                noticeList = BoardDAO.selectNoticeList(searchOption, searchText, startCount, contentCount);     // 게시글 목록 조회
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
        
        if (noticeList != null) {
            // 게시글 목록 조회 성공
            int i = 0;
            int start = pageMap.getInt("startCount");
            int index = count - start;
            
            for (ResultMap noticeMap : noticeList) {
                noticeMap.put("idx", index - (i++));
            }
            
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "");
            responseObject.put(Response.MAP, UtilJSON.mapToJSon((Map<String, Object>) pageMap));
            responseObject.put(Response.LIST, UtilJSON.convertArrayListToJSONArray((ArrayList) noticeList));
        } else {
            // 게시글 목록 조회 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "공지사항 게시글 목록 조회에 실패하였습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
                
        LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 공지사항 게시글 조회
     */
    @ControllerMethodInfo(id = "/api/board/getNotice.do")
    public String getNotice(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_GET_NOTICE;                                       // 인터페이스 번호
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        int seq = HttpUtils.getParameterInt(request, "seq", -1);                            // 게시글 번호
        
        JSONObject queryObject = new JSONObject();
        queryObject.put("seq", seq != -1? seq : null);
        
        JSONObject responseObject = new JSONObject();
        
        // session
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (sessionMap != null) {
            user = sessionMap.getInt("seq");
        }
        
        if (seq < 1) {
            // 게시글 번호가 없는 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
        
            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        ResultMap noticeMap = null;                                             // 게시글
        
        try {
            noticeMap = BoardDAO.selectDetailNoticeBySeq(seq);                  // 게시글 조회
            
            if (noticeMap != null) {
                // 게시글 조회 성공
                BoardDAO.updateViewCount(seq);                                  // 게시글 조회수 증가
                
                // 첨부파일 확인
                if (noticeMap.containsKey("file") && noticeMap.get("file") != null) {
                    List<ResultMap> fileList = UtilJSON.convertJSONArrayToArrayList(noticeMap.getJSONArray("file"));
                    noticeMap.put("file", fileList);  
                }
                
                responseObject.put(Response.RESULT, Response.Result.SUCCESS);
                responseObject.put(Response.MESSAGE, "");
                responseObject.put(Response.MAP, UtilJSON.mapToJSon((Map<String, Object>) noticeMap));
            } else {
                // 게시글 조회 실패
                responseObject.put(Response.RESULT, Response.Result.FAILED);
                responseObject.put(Response.MESSAGE, "공지사항 게시글 조회에 실패하였습니다.");
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
     * 공지사항 게시글 생성
     */
    @ControllerMethodInfo(id = "/api/board/setNotice.do")
    public String setNotice(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_SET_NOTICE;                                       // 인터페이스 번호
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
        
        int result = 0;                                                         // 게시글 생성 결과
        
        try {
            // 필수 입력 사항
            String title = requestObject.getString("title");                    // 제목
            String content = requestObject.getString("content");                // 내용
            
            // 선택 입력 사항
            JSONArray fileArray = !requestObject.isNull("file")? requestObject.getJSONArray("file") : new JSONArray();     // 첨부파일
            
            // 첨부파일 확인
            // 파일 경로 이동 (temp -> upload path)
            String contextPath = UploadConst.contextPath();
            String tempPath = FileUtils.getTempPath();
            
            List<String> fileList = UtilJSON.convertJSONArrayToArrayList(fileArray);
            for (String file : fileList) {
                // String file 은 /file-path/file-name 으로 구성
                int index = file.lastIndexOf("/");
                
                String uploadPath = file.substring(0, index);
                String fileName = file.substring(index + 1);
                
                File tempFile = new File(contextPath + tempPath, fileName);
                File uploadFile = new File(contextPath + uploadPath, fileName);
                
                FileUtils.copy(tempFile, uploadFile, true);                           
            }
            
            FileDAO.updateEnableUpload(fileList);                               // 파일 업로드 활성화
            
            result = BoardDAO.insertNotice(user, title, content, fileList);     // 게시글 생성
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
            // 게시글 생성 성공
            responseObject.put("result", "success");
            responseObject.put("msg", "공지사항 게시글이 생성되었습니다.");
        } else {
            // 게시글 생성 실패
            responseObject.put("result", "failed");
            responseObject.put("msg", "공지사항 게시글 생성이 실패하였습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);

        LogDAO.insertLog(api, user, userAgent, null, requestObject, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 공지사항 게시글 수정
     */
    @ControllerMethodInfo(id = "/api/board/modifyNotice.do")
    public String modifyNotice(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_MODIFY_NOTICE;                                    // 인터페이스 번호
        int type = UserDAO.USER_TYPE_PUBLIC;                                    // 유저 유형
        Integer user = null;                                                    // 유저 번호

        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        int seq = HttpUtils.getParameterInt(request, "seq", -1);                            // 게시글 번호
        
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
            // 게시글 번호가 없는 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        int result = 0;                                                         // 게시글 수정 결과
        
        try {
            // 필수 입력 사항
            String title = requestObject.getString("title");                    // 제목
            String content = requestObject.getString("content");                // 내용

            // 선택 입력 사항
            JSONArray fileArray = !requestObject.isNull("file")? requestObject.getJSONArray("file") : new JSONArray();     // 첨부파일
            
            ResultMap noticeMap = BoardDAO.selectNoticeBySeq(seq);              // 게시글 조회
            if (noticeMap != null) {
                // 게시글 조회 성공
                
                // 첨부파일 확인
                String contextPath = UploadConst.contextPath();
                String tempPath = FileUtils.getTempPath();
                
                List<String> fileList = UtilJSON.convertJSONArrayToArrayList(fileArray);        // 현재 첨부파일
                
                List<String> oldFileList = (List<String>) noticeMap.get("file");                // 기존 첨부파일
                List<String> newFileList = UtilJSON.convertJSONArrayToArrayList(fileArray);
                List<String> overlapList = new ArrayList<>();

                // 중복 확인
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
                
                FileDAO.deleteFileList(oldFileList);                            // 파일 삭제
                
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
                
                FileDAO.updateEnableUpload(newFileList);                        // 파일 업로드 활성화
                
                result = BoardDAO.updateNotice(seq, title, content, fileList);  // 게시글 수정
            } else {
                // 게시글 조회 실패
                responseObject.put(Response.RESULT, Response.Result.ERROR);
                responseObject.put(Response.MESSAGE, "공지사항 게시글 조회에 실패하였습니다.");
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
            // 게시글 수정 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "공지사항 게시글이 수정되었습니다.");      
        } else {
            // 게시글 수정 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "공지사항 게시글 수정이 실패하였습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
            
        LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 공지사항 게시글 삭제
     */
    @ControllerMethodInfo(id = "/api/board/deleteNotice.do")
    public String deleteNotice(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_DELETE_NOTICE;                                    // 인터페이스 번호
        int type = UserDAO.USER_TYPE_PUBLIC;                                    // 유저 유형
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        int seq = HttpUtils.getParameterInt(request, "seq", -1);                            // 게시글 번호
        
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
            ResultMap noticeMap = BoardDAO.selectNoticeBySeq(seq);              // 게시글 조회
            if (noticeMap != null) {
                // 게시글 조회 성공
                
                // 첨부파일 확인
                String contextPath = UploadConst.contextPath();
                
                List<String> oldFileList = (List<String>) noticeMap.get("file");                // 기존 첨부파일
                
                // 파일 삭제
                for (String filePath : oldFileList) {
                    FileUtils.delete(contextPath + filePath);
                }
                
                FileDAO.deleteFileList(oldFileList);                            // 파일 삭제
                
                result = BoardDAO.updateDisableContent(seq);                    // 게시글 삭제
            } else {
                // 게시글 조회 실패
                responseObject.put(Response.RESULT, Response.Result.FAILED);
                responseObject.put(Response.MESSAGE, "공지사항 게시글 조회에 실패하였습니다.");
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
            responseObject.put(Response.MESSAGE, "공지사항 게시글이 삭제되었습니다.");
        } else {
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "공지사항 게시글 삭제가 실패하였습니다");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
            
        LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * Q&A 게시글 목록 조회
     */
    @ControllerMethodInfo(id = "/api/board/getQuestionList.do")
    public String getQuestionList(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_GET_QUESTION_LIST;                                // 인터페이스 번호
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        int page = HttpUtils.getParameterInt(request, "page", -1);                          // 페이지 번호
        int searchOption = HttpUtils.getParameterInt(request, "search_option", -1);         // 검색 유형
        String searchText = HttpUtils.getParameterString(request, "search_text", null);     // 검색어
        
        JSONObject queryObject = new JSONObject();
        queryObject.put("page", page != -1? page : null);
        queryObject.put("search_option", searchOption != -1? searchOption : null);
        queryObject.put("search_text", searchText);
        
        page = page != -1? page : 1;
        searchOption = searchOption != -1? searchOption : BoardDAO.SEARCH_OPTION_ALL;
        
        JSONObject responseObject = new JSONObject();
        
        // session
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (sessionMap != null) {
            user = sessionMap.getInt("seq");
        }
        
        List<ResultMap> questionList = null;                                    // 게시글 목록
        ResultMap pageMap = null;
        int count = 0;
        
        try {
            int contentCount = 10;                                              // 목록 출력 개수
            count = BoardDAO.selectQuestionListCount(searchOption, searchText); // 게시글 목록 개수 조회
            
            pageMap = PageUtils.getPagination(count, page, contentCount);       // 페이징
            int totalPage = pageMap.getInt("totalPage");
            if (page <= totalPage || page == 1) {
                // 올바른 페이지 번호에 접근한 경우
                int startCount = pageMap.getInt("startCount");
                questionList = BoardDAO.selectQuestionList(searchOption, searchText, startCount, contentCount);     // 게시글 목록 조회
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
        
        if (questionList != null) {
            // 게시글 목록 조회 성공
            int i = 0;
            int start = pageMap.getInt("startCount");
            int index = count - start;
            
            for (ResultMap questionMap : questionList) {
                questionMap.put("idx", index - (i++));
            }
            
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "");
            responseObject.put(Response.MAP, UtilJSON.mapToJSon((Map<String, Object>) pageMap));
            responseObject.put(Response.LIST, UtilJSON.convertArrayListToJSONArray((ArrayList) questionList));
        } else {
            // 게시글 목록 조회 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "Q&A 게시글 목록 조회에 실패하였습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
                
        LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * Q&A 게시글 게시글 조회
     */
    @ControllerMethodInfo(id = "/api/board/getQuestion.do")
    public String getQuestion(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_GET_QUESTION;                                     // 인터페이스 번호
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        int seq = HttpUtils.getParameterInt(request, "seq", -1);                            // 게시글 번호
        
        JSONObject queryObject = new JSONObject();
        queryObject.put("seq", seq != -1? seq : null);
        
        JSONObject responseObject = new JSONObject();
        
        // session
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (sessionMap != null) {
            user = sessionMap.getInt("seq");
        }
        
        if (seq < 1) {
            // 게시글 번호가 없는 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
        
            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        ResultMap questionMap = null;                                           // 게시글
        
        try {
            questionMap = BoardDAO.selectDetailQuestionBySeq(seq);              // 게시글 조회
            if (questionMap != null) {
                // 게시글 조회 성공
                BoardDAO.updateViewCount(seq);                                  // 게시글 조회수 증가
                
                // 첨부파일 확인
                if (questionMap.containsKey("file") && questionMap.get("file") != null) {
                    List<ResultMap> fileList = UtilJSON.convertJSONArrayToArrayList(questionMap.getJSONArray("file"));
                    questionMap.put("file", fileList);
                }
                
                responseObject.put(Response.RESULT, Response.Result.SUCCESS);
                responseObject.put(Response.MESSAGE, "");
                responseObject.put(Response.MAP, UtilJSON.mapToJSon((Map<String, Object>) questionMap));
            } else {
                // 게시글 조회 실패
                responseObject.put(Response.RESULT, Response.Result.FAILED);
                responseObject.put(Response.MESSAGE, "Q&A 게시글 조회에 실패하였습니다.");
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
     * Q&A 게시글 생성
     */
    @ControllerMethodInfo(id = "/api/board/setQuestion.do")
    public String setQuestion(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_SET_QUESTION;                                     // 인터페이스 번호
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);
        
        JSONObject requestObject = HttpUtils.getBodyJson(request);
        JSONObject responseObject = new JSONObject();
        
        // session
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (sessionMap != null) {
            user = sessionMap.getInt("seq");
        } else {
            // 비로그인
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
            request.setAttribute(Response.RESULT, responseObject);
        
            LogDAO.insertLog(api, user, userAgent, null, requestObject, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        int result = 0;                                                         // 게시글 생성 결과
        
        try {
            // 필수 입력 사항
            String title = requestObject.getString("title");                    // 제목
            String content = requestObject.getString("content");                // 내용
            
            // 선택 입력 사항
            JSONArray fileArray = !requestObject.isNull("file")? requestObject.getJSONArray("file") : new JSONArray();     // 첨부파일
            
            // 첨부파일 확인
            // 파일 경로 이동 (temp -> upload path)
            String contextPath = UploadConst.contextPath();
            String tempPath = FileUtils.getTempPath();
            
            List<String> fileList = UtilJSON.convertJSONArrayToArrayList(fileArray);
            for (String file : fileList) {
                // String file 은 /file-path/file-name 으로 구성
                int index = file.lastIndexOf("/");
                
                String uploadPath = file.substring(0, index);
                String fileName = file.substring(index + 1);
                
                File tempFile = new File(contextPath + tempPath, fileName);
                File uploadFile = new File(contextPath + uploadPath, fileName);
                
                FileUtils.copy(tempFile, uploadFile, true);                           
            }
            
            FileDAO.updateEnableUpload(fileList);                               // 파일 업로드 활성화
            
            result = BoardDAO.insertQuestion(user, title, content, fileList);   // Q&A 게시글 생성
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
            // 게시글 생성 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "Q&A 게시글이 생성되었습니다.");
        } else {
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "Q&A 게시글 생성이 실패하였습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
        
        LogDAO.insertLog(api, user, userAgent, null, requestObject, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * Q&A 게시글 수정
     */
    @ControllerMethodInfo(id = "/api/board/modifyQuestion.do")
    public String modifyQuestion(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_MODIFY_QUESTION;                                  // 인터페이스 번호
        int type = UserDAO.USER_TYPE_PUBLIC;                                    // 유저 유형
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        int seq = HttpUtils.getParameterInt(request, "seq", -1);                            // 게시글 번호
        
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
        } else {
            // 비로그인
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
            request.setAttribute(Response.RESULT, responseObject);
        
            LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (seq < 1) {
            // 게시글 번호가 없는 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
        
            LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        int result = 0;                                                         // 게시글 수정 결과
        
        try {
            int writer = 0;                                                     // 게시글 작성자 번호

            // 필수 입력 사항
            String title = requestObject.getString("title");                    // 제목
            String content = requestObject.getString("content");                // 내용
            
            // 선택 입력 사항
            JSONArray fileArray = !requestObject.isNull("file")? requestObject.getJSONArray("file") : new JSONArray();     // 첨부파일
            
            ResultMap questionMap = BoardDAO.selectQuestionBySeq(seq);          // 게시글 조회
            if (questionMap != null) {
                // 게시글 조회 성공
                writer = questionMap.getInt("user_ref");
            } else {
                // 게시글 조회 실패
                responseObject.put(Response.RESULT, Response.Result.FAILED);
                responseObject.put(Response.MESSAGE, "Q&A 게시글 조회에 실패하였습니다.");
                request.setAttribute(Response.RESULT, responseObject);
        
                LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
                
                return "/ResultJSON.jsp";
            }
            
            if (user == writer || type == UserDAO.USER_TYPE_ADMIN) {
                // 게시글 수정 권한이 있는 경우
                
                // 첨부파일 확인
                String contextPath = UploadConst.contextPath();
                String tempPath = FileUtils.getTempPath();
                
                List<String> fileList = UtilJSON.convertJSONArrayToArrayList(fileArray);        // 현재 첨부파일
                
                List<String> oldFileList = (List<String>) questionMap.get("file");              // 기존 첨부파일
                List<String> newFileList = UtilJSON.convertJSONArrayToArrayList(fileArray);
                List<String> overlapList = new ArrayList<>();
                
                // 중복 확인
                for (String newFile : newFileList) {
                    if (oldFileList.contains(newFile)) {
                        overlapList.add(newFile);
                    }
                }

                oldFileList.removeAll(overlapList);
                newFileList.removeAll(overlapList);
                
                // 파일 삭제
                for (String filePath : oldFileList) {
                    FileUtils.delete(contextPath + filePath);
                }
                
                FileDAO.deleteFileList(oldFileList);                            // 파일 삭제
                
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
                
                FileDAO.updateEnableUpload(fileList);                           // 파일 업로드 활성화
                
                result = BoardDAO.updateQuestion(seq, title, content, fileList);    // 게시글 수정
            } else {
                // 게시글 수정 권한이 없는 경우
                responseObject.put(Response.RESULT, Response.Result.ERROR);
                responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
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
            // 게시글 수정 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "Q&A 게시글이 수정되었습니다.");
        } else {
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "Q&A 게시글 수정이 실패하였습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
        
        LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * Q&A 게시글 삭제
     */
    @ControllerMethodInfo(id = "/api/board/deleteQuestion.do")
    public String deleteQuestion(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_DELETE_QUESTION;                                  // 인터페이스 번호
        int type = UserDAO.USER_TYPE_PUBLIC;                                    // 유저 유형
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        int seq = HttpUtils.getParameterInt(request, "seq", -1);                            // 게시글 번호
        
        JSONObject queryObject = new JSONObject();
        queryObject.put("seq", seq != -1 ? seq : null);
        
        JSONObject responseObject = new JSONObject();
        
        // session
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (sessionMap != null) {
            type = sessionMap.getInt("type");
            user = sessionMap.getInt("seq");
        } else {
            // 비로그인
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
            int writer = 0;                                                     // 게시글 작성자 번호
            
            ResultMap questionMap = BoardDAO.selectQuestionBySeq(seq);          // 게시글 조회
            if (questionMap != null) {
                // 게시글 조회 성공
                writer = questionMap.getInt("user_ref");
            } else {
                // 게시글 조회 실패
                responseObject.put(Response.RESULT, Response.Result.FAILED);
                responseObject.put(Response.MESSAGE, "Q&A 게시글 조회에 실패하였습니다.");
                request.setAttribute(Response.RESULT, responseObject);
        
                LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
                
                return "/ResultJSON.jsp";
            }
            
            if (user == writer || type == UserDAO.USER_TYPE_ADMIN) {
                // 게시글 삭제 권한이 있는 경우
                
                // 첨부파일 확인
                String contextPath = UploadConst.contextPath();
                
                List<String> oldFileList = (List<String>) questionMap.get("file");      // 기존 첨부파일
                
                // 파일 삭제
                for (String filePath : oldFileList) {
                    FileUtils.delete(contextPath + filePath);
                }
                
                FileDAO.deleteFileList(oldFileList);                            // 파일 삭제
                
                result = BoardDAO.updateDisableContent(seq);                    // 게시글 삭제
            } else {
                // 게시글 삭제 권한이 없는 경우
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
        
        if (result > 0) {
            // 게시글 삭제 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "Q&A 게시글이 삭제되었습니다.");
        } else {
            // 게시글 삭제 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "Q&A 게시글 삭제가 실패하였습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
        
        LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 댓글 목록 조회
     */
    @ControllerMethodInfo(id = "/api/board/getCommentList.do")
    public String getCommentList(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_GET_BOARD_COMMENT_LIST;                           // 인터페이스 번호
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        int content = HttpUtils.getParameterInt(request, "content", -1);                    // 게시글 번호
        int page = HttpUtils.getParameterInt(request, "page", -1);                          // 페이지 번호
        
        JSONObject queryObject = new JSONObject();
        queryObject.put("content", content != -1? content : null);
        queryObject.put("page", page!= -1? page : null);
        
        page = page != -1? page : 1;
        
        JSONObject responseObject = new JSONObject();
        
        // session
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (sessionMap != null) {
            user = sessionMap.getInt("seq");
        }

        if (content < 1) {
            // 게시글 번호가 없는 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
        
            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        List<ResultMap> commentList = null;                                     // 댓글 목록
        ResultMap pageMap = null;
        
        try {
            ResultMap contentMap = BoardDAO.selectContentBySeq(content);        // 게시글 조회
            if (contentMap != null) {
                // 게시글 조회 성공
                String contentSeq = String.valueOf(content);
                
                int contentCount = 10;                                          // 목록 출력 개수
                int count = BoardDAO.selectCommentListCount(contentSeq);        // 댓글 목록 개수 조회

                pageMap = PageUtils.getPagination(count, page, contentCount);   // 페이징
                int totalPage = pageMap.getInt("totalPage");
                if (page <= totalPage || page == 1) {
                    // 올바른 페이지 번호에 접근한 경우
                    int startCount = pageMap.getInt("startCount");
                    commentList = BoardDAO.selectCommentList(contentSeq, startCount, contentCount);    // 댓글 목록 조회
                } else {
                    // 없는 페이지 번호에 접근한 경우
                    responseObject.put(Response.RESULT, Response.Result.ERROR);
                    responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
                    request.setAttribute(Response.RESULT, responseObject);
        
                    LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
            
                    return "/ResultJSON.jsp";
                }
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
        
        if (commentList != null) {
            // 댓글 목록 조회 성공
            for (ResultMap commentMap : commentList) {
                // 대댓글
                if (commentMap.containsKey("reply") && commentMap.get("reply") != null) {
                    JSONArray replyArray = new JSONArray(commentMap.getJSONArray("reply"));
                    List<ResultMap> replyList = UtilJSON.convertJSONArrayToArrayList(replyArray);
                            
                    commentMap.put("reply", replyList);
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
     * 댓글 작성
     */
    @ControllerMethodInfo(id = "/api/board/setComment.do")
    public String setComment(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_SET_BOARD_COMMENT;                                // 인터페이스 번호
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        int content = HttpUtils.getParameterInt(request, "content", -1);                    // 게시글 번호
        
        JSONObject queryObject = new JSONObject();
        queryObject.put("content", content != -1? content : null);
        
        JSONObject requestObject = HttpUtils.getBodyJson(request);
        JSONObject responseObject = new JSONObject();
        
        // session
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (sessionMap != null) {
            user = sessionMap.getInt("seq");
        } else {
            // 비로그인
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
            request.setAttribute(Response.RESULT, responseObject);
        
            LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (content < 1) {
            // 게시글 번호가 없는 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
        
            LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        int result = 0;                                                         // 댓글 작성 결과
        
        try {
            // 필수 입력 사항
            String comment = requestObject.getString("comment");                // 댓글 내용
            
            // 선택 입력 사항
            int reply = !requestObject.isNull("reply")? requestObject.getInt("reply") : 0;  // 댓글 번호
            
            ResultMap contentMap = BoardDAO.selectContentBySeq(content);        // 게시글 조회
            if (contentMap != null) {
                // 게시글 조회 성공
                String sContent = String.valueOf(content);
                
                result = BoardDAO.insertComment(sContent, user, reply, comment);// 댓글 작성
            } else {
                // 게시글 조회 실패
                responseObject.put(Response.RESULT, Response.Result.FAILED);
                responseObject.put(Response.MESSAGE, "게시글 조회에 실패하였습니다.");
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
            // 댓글 작성 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "댓글이 작성되었습니다.");
        } else {
            // 댓글 작성 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "댓글 작성이 실패하였습니다.");
        }
            
        request.setAttribute(Response.RESULT, responseObject);
        
        LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);
            
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 댓글 삭제
     */
    @ControllerMethodInfo(id = "/api/board/deleteComment.do")
    public String deleteComment(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_DELETE_BOARD_COMMENT;                             // 인터페이스 번호
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
        } else {
            // 비로그인
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
            int writer = 0;                                                     // 댓글 작성자 번호
            
            ResultMap commentMap = BoardDAO.selectCommentBySeq(seq);            // 댓글 조회
            if (commentMap != null) {
                // 댓글 조회 성공
                writer = commentMap.getInt("user_ref");
            } else {
                // 댓글 조회 실패
                responseObject.put(Response.RESULT, Response.Result.FAILED);
                responseObject.put(Response.MESSAGE, "댓글 조회에 실패하였습니다.");
                request.setAttribute(Response.RESULT, responseObject);
        
                LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);
                
                return "/ResultJSON.jsp";
            }
            
            if (user == writer || type == UserDAO.USER_TYPE_ADMIN) {
                // 댓글 삭제 권한이 있는 경우
                result = BoardDAO.updateDisableComment(seq);
            } else {
                // 댓글 삭제 권한이 없는 경우
                responseObject.put(Response.RESULT, Response.Result.FAILED);
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
}
