package com.ithows.controller;

import com.codeenator.dao.AlbumDAO;
import com.codeenator.dao.BoardDAO;
import com.codeenator.dao.FileDAO;
import com.codeenator.dao.LogDAO;
import com.codeenator.dao.UserDAO;
import com.codeenator.model.Response;
import com.codeenator.utils.FileUtils;
import com.codeenator.utils.MailUtils;
import com.codeenator.utils.PageUtils;
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
 * 앨범 API Controller
 *
 * - 앨범 목록 조회 -------------------------------------
 * {@link #getAlbumList(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 앨범 조회 -----------------------------------------
 * {@link #getAlbum(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 앨범 생성 -----------------------------------------
 * {@link #setAlbum(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 앨범 수정 -----------------------------------------
 * {@link #modifyAlbum(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 앨범 삭제 -----------------------------------------
 * {@link #deleteAlbum(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 컨텐츠 조회 ---------------------------------------
 * {@link #getContent(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 *
 * - 앨범 수강평 목록 조회 -------------------------------
 * {@link #getCommentList(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 앨범 수강평, 수강평 댓글 작성 ------------------------
 * {@link #setComment(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 앨범 수강평, 수강평 댓글 수정 ------------------------
 * {@link #modifyComment(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 앨범 수강평, 수강평 댓글 삭제 ------------------------
 * {@link #deleteComment(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 *
 * - 링크 공유 -----------------------------------------
 * {@link #shareLink(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 *
 * - 수강 앨범 목록 조회 ---------------------------------
 * {@link #getListenAlbum(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 수강 삭제 -----------------------------------------
 * {@link #deleteListenAlbum(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 *
 * - 내 앨범 목록 조회 -----------------------------------
 * {@link #getMyAlbumList(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 내 앨범 수강평 목록 조회 -----------------------------
 * {@link #getMyAlbumCommentList(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 *
 * - 앨범 추천/추천 취소 ---------------------------------
 * {@link #recommendAlbum(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 */
@ControllerClassInfo(controllerPage = "/album/_album.jsp")
public class AlbumController {

    static {
        BaseDebug.info("***AlbumController.class Loading!!");
    }

    /**
     * 앨범 목록 조회
     */
//    해결해결 여기에 pk 값 있는건 다 안보이게 한다
    @ControllerMethodInfo(id = "/api/album/getAlbumList.do")
    public String getAlbumList(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_GET_ALBUM_LIST;                                   // 인터페이스 번호
        Integer user = null;                                                    // 유저 번호

        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        int page = HttpUtils.getParameterInt(request, "page", -1);                          // 페이지 번호
        String category = HttpUtils.getParameterString(request, "category", null);          // 앨범 분류
        int sortOption = HttpUtils.getParameterInt(request, "sort_option", -1);             // 정렬 유형
        int searchOption = HttpUtils.getParameterInt(request, "search_option", -1);         // 검색 유형
        String searchText = HttpUtils.getParameterString(request, "search_text", null);     // 검색어

        JSONObject queryObject = new JSONObject();
        queryObject.put("page", page != -1 ? page : null);
        queryObject.put("category", category);
        queryObject.put("sort_option", sortOption != -1 ? sortOption : null);
        queryObject.put("search_option", searchOption != -1 ? searchOption : null);
        queryObject.put("search_text", searchText);

        page = page != -1 ? page : 1;
        sortOption = sortOption != -1 ? sortOption : AlbumDAO.ALBUM_SORT_OPTION_RECOMMEND;
        searchOption = searchOption != -1 ? searchOption : AlbumDAO.SEARCH_OPTION_ALL;

        JSONObject responseObject = new JSONObject();

        // session
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (sessionMap != null) {
            user = sessionMap.getInt("seq");
        }

        List<ResultMap> albumList = null;                                       // 앨범 목록
        ResultMap pageMap = null;
        int count = 0;

        try {
            int contentCount = 20;                                              // 목록 출력 개수
            count = AlbumDAO.selectAlbumListCount(category, searchOption, searchText);      // 앨범 목록 개수 조회

            pageMap = PageUtils.getPagination(count, page, contentCount);       // 페이징
            int totalPage = pageMap.getInt("totalPage");
            if (page <= totalPage || page == 1) {
                // 올바른 페이지 번호에 접근한 경우
                int startCount = pageMap.getInt("startCount");
                albumList = AlbumDAO.selectAlbumList(category, sortOption, searchOption, searchText, startCount, contentCount);     // 앨범 목록 조회
            } else {
                // 잘못된 페이지 번호에 접근한 경우
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
            // 앨범 목록 조회 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "");
            responseObject.put(Response.MAP, UtilJSON.mapToJSon((Map<String, Object>) pageMap));
            responseObject.put(Response.LIST, UtilJSON.convertArrayListToJSONArray((ArrayList) albumList));
        } else {
            // 앨범 목록 조회 실패
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
//    해결해결 여기에 pk 파라미터를 하나 더 받고 null일때, 들어왔는데 값일때, 들어왔는데 없는값일때 구분해야한다
    @ControllerMethodInfo(id = "/api/album/getAlbum.do")
    public String getAlbum(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_GET_ALBUM;                                        // 인터페이스 번호
        Integer user = null;                                                    // 유저 번호

        // request
//        여기서 pk 값 하나 더받는다 
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        String id = HttpUtils.getParameterString(request, "id", null);                      // 앨범 아이디
        String pk = HttpUtils.getParameterString(request, "pk", null);                      // 비공개 앨범 키값

        JSONObject queryObject = new JSONObject();
        queryObject.put("id", id);

        JSONObject responseObject = new JSONObject();

        // session
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (sessionMap != null) {
            user = sessionMap.getInt("seq");
        }

        if (id == null) {
            // 앨범 아이디가 없는 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
            request.setAttribute(Response.RESULT, responseObject);

            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);

            return "/ResultJSON.jsp";
        }

//        DB에서 아이디로 pk값 꺼내와서 비교하는 문
//        currectpk 값 없으면 그냥 넘어감
        ResultMap rm = AlbumDAO.getAlbumPk(id);
        System.out.println("rm : " + rm);

        if (rm != null && !rm.isEmpty()) {
            Integer userRef = rm.getInt("user_ref");
            String rmPk = rm.getString("pk");

            // 본인 글이 아닌데
            if (userRef != user) {
                // pk 값이 비어있지 않으면
                if (!rmPk.equals("")) {
                    // 그 값을 비교하고 같지 않으면 접근권한 없음
                    if (pk == null) {
                        responseObject.put(Response.RESULT, Response.Result.FAILED);
                        responseObject.put(Response.MESSAGE, "앨범 접근 권한이 없습니다");
                        request.setAttribute(Response.RESULT, responseObject);
                        return "/ResultJSON.jsp";
                    } else if (!pk.equals(rmPk)) {
                        responseObject.put(Response.RESULT, Response.Result.FAILED);
                        responseObject.put(Response.MESSAGE, "앨범 접근 권한이 없습니다");
                        request.setAttribute(Response.RESULT, responseObject);
                        return "/ResultJSON.jsp";
                    }
                }
            }
        }
        System.out.println("작성자 본인의 글을 조회합니다");

        ResultMap albumMap = null;                                              // 앨범

        try {
            albumMap = AlbumDAO.selectDetailAlbum(id, user);                    // 앨범 조회
            if (albumMap != null) {
                // 앨범 조회 성공
                AlbumDAO.insertAlbumView(id, user);                             // 앨범 열람 이력 생성 (비회원, 작성자 포함)

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
     * 앨범 생성
     */
//    해결해결 여기에 pk 값을 하나 더 받아온다
//    해결해결 여기에 케이스 분할해서 pk 값이 들어왔을때와 아닐때 코드를 만들기는 무슨 그냥 ""들어오면 들어오는대로 넘겨서 나중에 ""인지 검사하면 된다
    @ControllerMethodInfo(id = "/api/album/setAlbum.do")
    public String setAlbum(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_SET_ALBUM;                                        // 인터페이스 번호
        Integer user = null;                                                    // 유저 번호

        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기

        JSONObject requestObject = HttpUtils.getBodyJson(request);
        JSONObject responseObject = new JSONObject();

        // session
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (sessionMap != null) {
            user = sessionMap.getInt("seq");
        } else {
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
            request.setAttribute(Response.RESULT, responseObject);

            LogDAO.insertLog(api, user, userAgent, null, requestObject, responseObject);

            return "/ResultJSON.jsp";
        }

        int result = 0;                                                         // 앨범 생성 결과

        try {
            // 필수 입력 사항
            String name = requestObject.getString("name");                      // 앨범명
            String introduction = requestObject.getString("introduction");      // 소개
            JSONArray chapterArray = requestObject.getJSONArray("chapter");     // 챕터

            // 선택 입력 사항
            String category = !requestObject.isNull("category") ? requestObject.getString("category") : null;        // 카테고리
            String tag = !requestObject.isNull("tag") ? requestObject.getString("tag") : null;                       // 사용자 태그
            String thumbnail = !requestObject.isNull("thumbnail") ? requestObject.getString("thumbnail") : null;     // 썸네일
//            비공개 설정이 된 경우 pk 값을 추가로 가져온다
            String pk = !requestObject.isNull("pk") ? requestObject.getString("pk") : null;
            System.out.println("pk is " + pk);

            List<String> fileList = new ArrayList<>();
            String id = AlbumDAO.createAlbumId();                               // 앨범 아이디 생성

            List<ResultMap> chapterList = UtilJSON.convertJSONArrayToArrayList(chapterArray);
            List<ResultMap> allContentList = new ArrayList<>();
            int chapter = 1;                                                    // 챕터 순서
            for (ResultMap chapterMap : chapterList) {
                String chapterId = AlbumDAO.createChapterId();
                chapterMap.put("id", chapterId);
                chapterMap.put("album", id);
                chapterMap.put("sort", chapter++);

                List<ResultMap> contentList = (List<ResultMap>) chapterMap.get("content");
                int content = 1;                                                // 컨텐츠 순서
                for (ResultMap contentMap : contentList) {
                    String contentId = AlbumDAO.createContentId();
                    contentMap.put("id", contentId);
                    contentMap.put("album", id);
                    contentMap.put("chapter", chapterId);
                    contentMap.put("sort", content++);

                    if (contentMap.containsKey("file") && contentMap.get("file") != null) {
                        // 첨부파일인 경우
                        String file = contentMap.getString("file");

                        // 파일 이름만 저장
                        contentMap.put("file", file.substring(file.lastIndexOf("/") + 1));
                        fileList.add(file);
                    }

                    allContentList.add(contentMap);
                }
            }

            if (thumbnail == null || "".equals(thumbnail)) {
                // 썸네일이 없는 경우
                thumbnail = ThumbnailUtils.createThumbnail(name);               // 썸네일 생성
            }

            fileList.add(thumbnail);
            thumbnail = thumbnail.substring(thumbnail.lastIndexOf("/") + 1);

            int resultChapter = AlbumDAO.insertChapterList(chapterList);        // 챕터 생성
            int resultContent = AlbumDAO.insertContentList(allContentList);     // 컨텐츠 생성

            if (resultChapter > 0 && resultContent > 0) {
                String contextPath = UploadConst.contextPath();
                String tempPath = FileUtils.getTempPath();

                for (String filePath : fileList) {
                    // String file 은 /file-path/file-name 으로 구성
                    int index = filePath.lastIndexOf("/");

                    String uploadPath = filePath.substring(0, index);
                    String fileName = filePath.substring(index + 1);

                    File tempFile = new File(contextPath + tempPath, fileName);
                    File uploadFile = new File(contextPath + uploadPath, fileName);

                    FileUtils.copy(tempFile, uploadFile, true);
                }

                FileDAO.updateEnableUpload(fileList);                           // 첨부파일 업로드 활성화

//                일단 pk값까지 보내고 DAO쪽에서 구분한다
                result = AlbumDAO.insertAlbum(id, pk, user, name, introduction, category, thumbnail, tag);     // 앨범 생성
            }
        } catch (JSONException e) {
            e.printStackTrace();

            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
            request.setAttribute(Response.RESULT, responseObject);

            LogDAO.insertLog(api, user, userAgent, null, requestObject, responseObject);

            return "/ResultJSON.jsp";
        } catch (IOException | FontFormatException | SQLException e) {
            e.printStackTrace();

            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잠시 후 다시 시도해주세요.");

            request.setAttribute(Response.RESULT, responseObject);

            LogDAO.insertLog(api, user, userAgent, null, requestObject, responseObject);

            return "/ResultJSON.jsp";
        }

        if (result > 0) {
            // 앨범 생성 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "앨범이 생성되었습니다.");
        } else {
            // 앨범 생성 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "앨범 생성이 실패하였습니다.");
        }

        request.setAttribute(Response.RESULT, responseObject);

        LogDAO.insertLog(api, user, userAgent, null, requestObject, responseObject);

        return "/ResultJSON.jsp";
    }

    /**
     * 앨범 수정
     */
//    해결해결 여기에다가 pk 값 더 받을 수 있게 하고 ""들어오면 pk 값 걍 그대로 넣어도 되지 않나?? 걍 넣는다
    @ControllerMethodInfo(id = "/api/album/modifyAlbum.do")
    public String modifyAlbum(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_MODIFY_ALBUM;                                     // 인터페이스 번호
        int type = UserDAO.USER_TYPE_PUBLIC;                                    // 유저 유형
        Integer user = null;                                                    // 유저 번호

        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        String id = HttpUtils.getParameterString(request, "id", null);

        JSONObject queryObject = new JSONObject();
        queryObject.put("id", id);

        JSONObject requestObject = HttpUtils.getBodyJson(request);
        JSONObject responseObject = new JSONObject();

        // session
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (sessionMap != null) {
            user = sessionMap.getInt("seq");
        } else {
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
            request.setAttribute(Response.RESULT, responseObject);

            LogDAO.insertLog(api, user, userAgent, null, requestObject, responseObject);

            return "/ResultJSON.jsp";
        }

        int result = 0;                                                         // 앨범 수정 결과

        try {
            int writer = -1;                                                    // 코디네이터

            // 필수 입력 사항
            String name = requestObject.getString("name");                      // 앨범명
            String introduction = requestObject.getString("introduction");      // 소개
            JSONArray chapterArray = requestObject.getJSONArray("chapter");     // 챕터

            // 선택 입력 사항
            String category = !requestObject.isNull("category") ? requestObject.getString("category") : null;        // 카테고리
            String tag = !requestObject.isNull("tag") ? requestObject.getString("tag") : null;                       // 사용자 태그
            String thumbnail = !requestObject.isNull("thumbnail") ? requestObject.getString("thumbnail") : null;     // 썸네일
            String pk = !requestObject.isNull("pk") ? requestObject.getString("pk") : null;                         // pk

            ResultMap albumMap = AlbumDAO.selectDetailAlbum(id);                // 앨범 조회

            if (albumMap == null) {
                // 앨범 조회 실패
                responseObject.put(Response.RESULT, Response.Result.FAILED);
                responseObject.put(Response.MESSAGE, "앨범 조회에 실패하였습니다.");
                request.setAttribute(Response.RESULT, responseObject);

                LogDAO.insertLog(api, user, userAgent, null, requestObject, responseObject);

                return "/ResultJSON.jsp";
            }

            writer = albumMap.getInt("user_ref");

            if (user != writer && type != UserDAO.USER_TYPE_ADMIN) {
                // 코디네이터, 관리자가 아닌 경우
                responseObject.put(Response.RESULT, Response.Result.ERROR);
                responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
                request.setAttribute(Response.RESULT, responseObject);

                LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);

                return "/ResultJSON.jsp";
            }

            List<ResultMap> oldChapterList = (List<ResultMap>) albumMap.get("chapter");             // 기존 챕터 목록
            List<ResultMap> newChapterList = UtilJSON.convertJSONArrayToArrayList(chapterArray);    // 입력 챕터 목록

            // 파일 관리
            List<String> addFileList = new ArrayList<>();
            List<String> removeFileList = new ArrayList<>();

            // 썸네일 관리
            String oldThumbnail = albumMap.getString("thumbnail");

            if (thumbnail != null && !"".equals(thumbnail)) {
                if (!oldThumbnail.equals(thumbnail)) {
                    // 새로운 썸네일
                    removeFileList.add(oldThumbnail);
                    addFileList.add(thumbnail);
                }
            } else {
                removeFileList.add(oldThumbnail);

                thumbnail = ThumbnailUtils.createThumbnail(name);               // 썸네일 생성
                addFileList.add(thumbnail);
            }
            if (oldChapterList.size() > 1) {
            for (ResultMap oldChapterMap : oldChapterList) {
                String oldChapterId = oldChapterMap.getString("id");

                for (ResultMap newChapterMap : newChapterList) {
                    String newChapterId = newChapterMap.getString("id");
                    if (!newChapterId.equals("")) { //챕터아이디가 없으면 == 새챕터면
                        if (oldChapterId.equals(newChapterId)) {
                            List<ResultMap> oldContentList = (List<ResultMap>) oldChapterMap.get("content");
                            List<ResultMap> newContentList = (List<ResultMap>) newChapterMap.get("content");

                            for (ResultMap oldContentMap : oldContentList) {
                                String oldContentId = oldContentMap.getString("id");
                                System.out.println("what is old ID : " + oldContentId);

                                for (ResultMap newContentMap : newContentList) {
                                    System.out.println("what is newContentMap : " + newContentMap);
                                    String newContentId = newContentMap.getString("id");
                                    System.out.println("what is new ID : " + newContentId);
                                    if (newContentId.equals("")) {
                                        String newFile = newContentMap.getString("file");
                                        addFileList.add(newFile);
                                        System.out.println("addFileList : " + addFileList);
                                    } else if (oldContentId.equals(newContentId)) {
                                        String oldFile = oldContentMap.getString("file");
                                        String newFile = newContentMap.getString("file");
                                        System.out.println("new file Name : " + newFile);

                                        if (oldFile != null && newFile != null) {
                                            if (!oldFile.equals(newFile)) {
                                                // 파일 변경
                                                removeFileList.add(oldFile);
                                                addFileList.add(newFile);
                                            }
                                        } else if (oldFile != null && newFile == null) {
                                            removeFileList.add(oldFile);
                                        } else if (oldFile == null && newFile != null) {
                                            addFileList.add(newFile);
                                            System.out.println("addFileList : " + addFileList);
                                        }
//                                }
                                    }
                                }
                            }
                        }
                    } else {
                        List<ResultMap> newContentList = (List<ResultMap>) newChapterMap.get("content");
                        for (ResultMap newContentMap : newContentList) {
                            String newFile = newContentMap.getString("file");
                            addFileList.add(newFile);
                        }

                    }
                }
            }            
            } else {
                for (ResultMap newChapterMap : newChapterList) {
                List<ResultMap> newContentList = (List<ResultMap>) newChapterMap.get("content");
                        for (ResultMap newContentMap : newContentList) {
                            String newFile = newContentMap.getString("file") != null ? newContentMap.getString("file") : "";
                            System.out.println("new File : " + newFile);
                            if(newFile != null || !newFile.equals(""))
                                addFileList.add(newFile);
                        }
                }
            }

            // 첨부파일 
            String contextPath = UploadConst.contextPath();
            String tempPath = FileUtils.getTempPath();

            // 파일 삭제
            for (String filePath : removeFileList) {
                FileUtils.delete(contextPath + filePath);
            }

            FileDAO.deleteFileList(removeFileList);                             // 기존 첨부파일 삭제

            // 파일 이동 (temp to upload path)
            for (String filePath : addFileList) {
                // String file 은 /file-path/file-name 으로 구성
                System.out.println("is it here? : " + filePath);
                filePath = filePath != null ? filePath : "";
                if(!filePath.equals("")) {
                int index = filePath.lastIndexOf("/");
                System.out.println("index : " + index);

                String uploadPath = filePath.substring(0, index);
                System.out.println(uploadPath + " adfkjshkdfghskfdg");
                String fileName = filePath.substring(index + 1);
                System.out.println("fileName : " + fileName);

                File tempFile = new File(contextPath + tempPath, fileName);
                File uploadFile = new File(contextPath + uploadPath, fileName);

                FileUtils.copy(tempFile, uploadFile, true);
                }
            }
            FileDAO.updateEnableUpload(addFileList);                            // 새로운 첨부파일 업로드 활성화

            thumbnail = thumbnail.substring(thumbnail.lastIndexOf("/") + 1);

            // 기존 챕터와 컨텐츠 삭제
            for (ResultMap oldChapterMap : oldChapterList) {
                List<ResultMap> oldContentList = (List<ResultMap>) oldChapterMap.get("content");

                AlbumDAO.deleteAlbumCotentList(oldContentList);                 // 기존 컨텐츠 삭제
            }

            AlbumDAO.deleteAlbumChapterList(oldChapterList);                    // 기존 챕터 삭제

            // 챕터와 컨텐츠 추가
            int chapter = 1;
            for (ResultMap newChapterMap : newChapterList) {
                String newChapterId = newChapterMap.getString("id");
                newChapterMap.put("album", id);
                newChapterMap.put("sort", chapter++);

                if (newChapterId == null || newChapterId.equals("")) {
                    newChapterId = AlbumDAO.createChapterId();
                    newChapterMap.put("id", newChapterId);
                }

                List<ResultMap> newContentList = (List<ResultMap>) newChapterMap.get("content");

                int content = 1;
                for (ResultMap newContentMap : newContentList) {
                    String newContentId = newContentMap.getString("id");
                    newContentMap.put("album", id);
                    newContentMap.put("chapter", newChapterId);
                    newContentMap.put("sort", content++);

                    if (newContentId == null || newContentId.equals("")) {
                        // 새로운 컨텐츠
                        newContentId = AlbumDAO.createContentId();
                        newContentMap.put("id", newContentId);
                    }

                    if (newContentMap.containsKey("file") && newContentMap.get("file") != null) {
                        // 첨부파일인 경우
                        String filePath = newContentMap.getString("file");
                        filePath = filePath.substring(filePath.lastIndexOf("/") + 1);

                        newContentMap.put("file", filePath);
                    }
                }

                AlbumDAO.insertContentList(newContentList);                     // 컨텐츠 추가
            }

            AlbumDAO.insertChapterList(newChapterList);                         // 챕터 추가

            result = AlbumDAO.updateAlbum(id, pk, name, introduction, category, thumbnail, tag);        // 앨범 수정
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
        } catch (Exception e) {
            System.out.println("isdflbsnjfdglkbnsfhnsgmdghmdghns");
            e.printStackTrace();
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

        LogDAO.insertLog(api, user, userAgent, null, requestObject, responseObject);

        return "/ResultJSON.jsp";
    }

    /**
     * 앨범 삭제
     */
    @ControllerMethodInfo(id = "/api/album/deleteAlbum.do")
    public String deleteAlbum(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_DELETE_ALBUM;                                     // 인터페이스 번호
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
        } else {
            // 비로그인
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
            int writer = 0;                                                     // 코디네이터 번호

            ResultMap albumMap = AlbumDAO.selectDetailAlbum(id);                // 앨범 조회
            if (albumMap != null) {
                writer = albumMap.getInt("user_ref");
            } else {
                responseObject.put(Response.RESULT, Response.Result.FAILED);
                responseObject.put(Response.MESSAGE, "앨범 조회에 실패하였습니다.");
                request.setAttribute(Response.RESULT, responseObject);

                LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);

                return "/ResultJSON.jsp";
            }

            if (user == writer || type == UserDAO.USER_TYPE_ADMIN) {
                // 앨범 삭제 권한이 있는 경우
                String contextPath = UploadConst.contextPath();

                List<String> fileList = new ArrayList<>();

                // 썸네일 확인
                if (albumMap.get("thumbnail") != null) {
                    fileList.add(albumMap.getString("thumbnail"));
                }

                // 컨텐츠에 파일 확인
                List<ResultMap> chapterList = (List<ResultMap>) albumMap.get("chapter");

                for (ResultMap chapterMap : chapterList) {
                    List<ResultMap> contentList = (List<ResultMap>) chapterMap.get("content");

                    for (ResultMap contentMap : contentList) {
                        if (contentMap.containsKey("file") && contentMap.getString("file") != null) {
                            String filePath = contentMap.getString("file");

                            fileList.add(filePath);
                        }
                    }
                }

                // 파일 삭제
                for (String filePath : fileList) {
                    FileUtils.delete(contextPath + filePath);
                }

                FileDAO.deleteFileList(fileList);                            // 파일 삭제

                result = AlbumDAO.updateDisableAlbum(id);                       // 앨범 삭제
            } else {
                // 앨범 삭제 권한이 없는 경우
                responseObject.put(Response.RESULT, Response.Result.ERROR);
                responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
                request.setAttribute(Response.RESULT, responseObject);

                LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);

                return "/ResultJSON.jsp";
            }
        } catch (SQLException e) {
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
     * 컨텐츠 조회
     */
    @ControllerMethodInfo(id = "/api/album/getContent.do")
    public String getContent(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_GET_ALBUM_CONTENT;                                // 인터페이스 번호
        Integer user = null;                                                    // 유저 번호

        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        String id = HttpUtils.getParameterString(request, "id", null);                      // 컨텐츠 아이디

        JSONObject queryObject = new JSONObject();
        queryObject.put("id", id);

        JSONObject responseObject = new JSONObject();

        // session
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (sessionMap != null) {
            user = sessionMap.getInt("seq");
        }

        if (id == null) {
            // 컨텐츠 번호가 없는 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
            request.setAttribute(Response.RESULT, responseObject);

            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);

            return "/ResultJSON.jsp";
        }

        ResultMap contentMap = null;                                            // 컨텐츠

        try {
            contentMap = AlbumDAO.selectDetailContent(id);                      // 컨텐츠 조회
            if (contentMap != null) {
                // 파일인 경우, 파일 정보 등록
                if (contentMap.containsKey("file") && contentMap.get("file") != null) {
                    contentMap.put("link", contentMap.remove("file"));
                }

                responseObject.put(Response.RESULT, Response.Result.SUCCESS);
                responseObject.put(Response.MESSAGE, "");
                responseObject.put(Response.MAP, UtilJSON.mapToJSon((Map<String, Object>) contentMap));
            } else {
                responseObject.put(Response.RESULT, Response.Result.FAILED);
                responseObject.put(Response.MESSAGE, "컨텐츠 조회에 실패하였습니다.");
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
     * 앨범 수강평 목록 조회
     */
    @ControllerMethodInfo(id = "/api/album/getCommentList.do")
    public String getCommentList(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_GET_ALBUM_COMMENT_LIST;                           // 인터페이스 번호
        Integer user = null;                                                    // 유저 번호

        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        String album = HttpUtils.getParameterString(request, "album", null);                // 앨범 아이디
        int page = HttpUtils.getParameterInt(request, "page", -1);                          // 페이지 번호

        JSONObject queryObject = new JSONObject();
        queryObject.put("album", album);
        queryObject.put("page", page != -1 ? page : null);

        page = page != -1 ? page : 1;

        JSONObject responseObject = new JSONObject();

        // session
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (sessionMap != null) {
            user = sessionMap.getInt("seq");
        }

        if (album == null) {
            // 앨범 아이디가 없는 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
            request.setAttribute(Response.RESULT, responseObject);

            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);

            return "/ResultJSON.jsp";
        }

        List<ResultMap> commentList = null;                                     // 댓글 목록
        ResultMap pageMap = null;

        try {
            ResultMap albumMap = AlbumDAO.selectAlbum(album);                   // 앨범 조회
            if (albumMap != null) {
                // 앨범 조회 성공
                int contentCount = 10;                                          // 목록 출력 개수
                int count = BoardDAO.selectCommentListCount(album);            // 수강평 목록 개수 조회

                pageMap = PageUtils.getPagination(count, page, contentCount);   // 페이징
                int totalPage = pageMap.getInt("totalPage");
                if (page <= totalPage || page == 1) {
                    // 올바른 페이지 번호에 접근한 경우
                    int startCount = pageMap.getInt("startCount");
                    commentList = BoardDAO.selectCommentList(album, startCount, count);      // 수강평 목록 조회
                } else {
                    // 없는 페이지 번호에 접근한 경우
                    responseObject.put(Response.RESULT, Response.Result.FAILED);
                    responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
                    request.setAttribute(Response.RESULT, responseObject);

                    LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);

                    return "/ResultJSON.jsp";
                }
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
            responseObject.put(Response.MESSAGE, "수강평 목록 조회에 실패하였습니다.");
        }

        request.setAttribute(Response.RESULT, responseObject);

        LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);

        return "/ResultJSON.jsp";
    }

    /**
     * 앨범 수강평, 수강평 댓글 작성
     */
    @ControllerMethodInfo(id = "/api/album/setComment.do")
    public String setComment(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_SET_ALBUM_COMMENT;                                // 인터페이스 번호
        Integer user = null;                                                    // 유저 번호

        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        String album = HttpUtils.getParameterString(request, "album", null);                // 앨범 아이디

        JSONObject queryObject = new JSONObject();
        queryObject.put("album", album);

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

        if (album == null) {
            // 앨범 아이디가 없는 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
            request.setAttribute(Response.RESULT, responseObject);

            LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);

            return "/ResultJSON.jsp";
        }

        int result = 0;                                                         // 수강평, 수강평 댓글 작성 결과

        try {
            int writer = 0;                                                     // 코디네이터 번호

            // 필수 입력 사항
            String comment = requestObject.getString("comment");                // 수강평, 수강평 댓글

            // 선택 입력 사항
            int reply = !requestObject.isNull("reply") ? requestObject.getInt("reply") : 0;  // 댓글 번호

            ResultMap albumMap = AlbumDAO.selectAlbum(album);                   // 앨범 조회
            if (albumMap != null) {
                // 앨범 조회 성공
                writer = albumMap.getInt("user_ref");

                if (reply > 0 && user != writer) {
                    // 수강평 댓글인지 확인, 코디네이터인지 확인
                    responseObject.put(Response.RESULT, Response.Result.ERROR);
                    responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
                    request.setAttribute(Response.RESULT, responseObject);

                    LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);

                    return "/ResultJSON.jsp";
                }

                result = BoardDAO.insertComment(album, user, reply, comment);   // 수강평 작성
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
            // 수강평 작성 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "작성되었습니다.");
        } else {
            // 수강평 작성 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "작성이 실패하였습니다.");
        }

        request.setAttribute(Response.RESULT, responseObject);

        LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);

        return "/ResultJSON.jsp";
    }

    /**
     * 앨범 수강평, 수강평 댓글 수정
     */
    @ControllerMethodInfo(id = "/api/album/modifyComment.do")
    public String modifyComment(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_MODIFY_ALBUM_COMMENT;                             // 인터페이스 번호
        int type = UserDAO.USER_TYPE_PUBLIC;                                    // 유저 유형
        Integer user = null;                                                    // 유저 번호

        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        int seq = HttpUtils.getParameterInt(request, "seq", -1);                            // 댓글 번호

        JSONObject queryObject = new JSONObject();
        queryObject.put("seq", seq != -1 ? seq : null);

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
            // 수강평, 수강평 댓글 번호가 없는 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
            request.setAttribute(Response.RESULT, responseObject);

            LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);

            return "/ResultJSON.jsp";
        }

        int result = 0;                                                         // 수강평, 수강평 댓글 수정 결과

        try {
            int writer = 0;                                                     // 수강평, 수강평 댓글 작성자 번호

            // 필수 입력 사항
            String comment = requestObject.getString("comment");                // 수강평, 수강평 댓글

            ResultMap commentMap = BoardDAO.selectCommentBySeq(seq);            // 수강평, 수강평 댓글 조회
            if (commentMap != null) {
                // 수강평, 수강평 댓글 조회 성공
                writer = commentMap.getInt("user_ref");
            } else {
                // 수강평, 수강평 댓글 조회 실패
                responseObject.put(Response.RESULT, Response.Result.FAILED);
                responseObject.put(Response.MESSAGE, "댓글 조회에 실패하였습니다.");
                request.setAttribute(Response.RESULT, responseObject);

                LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);

                return "/ResultJSON.jsp";
            }

            if (user == writer || type == UserDAO.USER_TYPE_ADMIN) {
                // 수강평, 수강평 댓글 수정 권한이 있는 경우
                result = BoardDAO.updateComment(seq, comment);                  // 수강평, 수강평 댓글 수정
            } else {
                // 수강평, 수강평 댓글 수정 권한이 없는 경우
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
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "수정되었습니다.");
        } else {
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "수정이 실패하였습니다.");
        }

        request.setAttribute(Response.RESULT, responseObject);

        LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);

        return "/ResultJSON.jsp";
    }

    /**
     * 앨범 수강평, 수강평 댓글 삭제
     */
    @ControllerMethodInfo(id = "/api/album/deleteComment.do")
    public String deleteComment(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_DELETE_ALBUM_COMMENT;                             // 인터페이스 번호
        int type = UserDAO.USER_TYPE_PUBLIC;                                    // 유저 유형
        Integer user = null;                                                    // 유저 번호

        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        int seq = HttpUtils.getParameterInt(request, "seq", -1);                            // 댓글 번호

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
            // 수강평, 수강평 댓글 번호가 없는 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
            request.setAttribute(Response.RESULT, responseObject);

            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);

            return "/ResultJSON.jsp";
        }

        int result = 0;                                                         // 수강평, 수강평 댓글 삭제 결과

        try {
            int writer = 0;                                                     // 작성자 번호

            ResultMap commentMap = BoardDAO.selectCommentBySeq(seq);            // 수강평 조회
            if (commentMap != null) {
                // 수강평 조회 성공
                writer = commentMap.getInt("user_ref");
            } else {
                // 수강평 조회 실패
                responseObject.put(Response.RESULT, Response.Result.FAILED);
                responseObject.put(Response.MESSAGE, "댓글 조회에 실패하였습니다.");
                request.setAttribute(Response.RESULT, responseObject);

                LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);

                return "/ResultJSON.jsp";
            }

            if (user == writer || type == UserDAO.USER_TYPE_ADMIN) {
                // 수강평 삭제 권한이 있는 경우
                result = BoardDAO.updateDisableComment(seq);                    // 수강평 삭제
            } else {
                // 수강평 삭제 권한이 없는 경우
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
            // 수강평 삭제 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "삭제되었습니다.");
        } else {
            // 수강평 삭제 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "삭제가 실패하였습니다.");
        }

        request.setAttribute(Response.RESULT, responseObject);

        LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);

        return "/ResultJSON.jsp";
    }

    /**
     * 링크 공유
     */
    @ControllerMethodInfo(id = "/api/album/shareLink.do")
    public String shareLink(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_SHARE_ALBUM_LINK;                                 // 인터페이스 번호
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
            user = sessionMap.getInt("seq");
        } else {
            // 비로그인
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

        boolean isSucceed = false;                                              // 이메일 전송 결과

        try {
            // 필수 입력 사항
            String email = requestObject.getString("email");                    // 이메일
            String url = requestObject.getString("url");                        // 앨범 주소

            ResultMap albumMap = AlbumDAO.selectAlbum(id);                      // 앨범 조회
            if (albumMap != null) {
                // 앨범 조회 성공
                String nickname = sessionMap.getString("nickname");

                // 이메일 제목 및 내용
                StringBuilder sb = new StringBuilder();
                sb.append("[Codeenator] ").append(nickname).append("님이 앨범을 공유하였습니다.");

                String title = sb.toString();

                sb = new StringBuilder();
                sb.append("안녕하세요. Codeenator입니다.").append("<br>");
                sb.append(nickname).append("님이 앨범을 공유하였습니다.").append("<br><br>");
                sb.append("앨범 바로가기 : ").append(url);

                String content = sb.toString();

                isSucceed = MailUtils.sendMail(email, title, content);          // 이메일 전송 (링크 공유)
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

        if (isSucceed) {
            // 이메일 전송 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "링크가 공유되었습니다.");
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
     * 수강 앨범 목록 조회
     */
    @ControllerMethodInfo(id = "/api/album/getListenAlbumList.do")
    public String getListenAlbumList(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_GET_LISTEN_ALBUM_LIST;                            // 인터페이스 번호
        Integer user = null;                                                    // 유저 번호

        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        int page = HttpUtils.getParameterInt(request, "page", -1);                          // 페이지 번호

        JSONObject queryObject = new JSONObject();
        queryObject.put("page", page != -1 ? page : null);

        page = page != -1 ? page : 1;

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

            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);

            return "/ResultJSON.jsp";
        }

        List<ResultMap> albumList = null;                                       // 앨범 목록
        ResultMap pageMap = null;
        int count = 0;

        try {
            int contentCount = 10;                                              // 목록 출력 개수
            count = AlbumDAO.selectListenAlbumListCount(user);                  // 수강 앨범 목록 개수 조회

            pageMap = PageUtils.getPagination(count, page, contentCount);       // 페이징
            int totalPage = pageMap.getInt("totalPage");
            if (page <= totalPage || page == 1) {
                // 올바른 페이지 번호에 접근한 경우
                int startCount = pageMap.getInt("startCount");
                albumList = AlbumDAO.selectListenAlbumList(user, startCount, contentCount);     // 수강 앨벌 목록 조회
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
            // 앨범 목록 조회 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "");
            responseObject.put(Response.MAP, UtilJSON.mapToJSon((Map<String, Object>) pageMap));
            responseObject.put(Response.LIST, UtilJSON.convertArrayListToJSONArray((ArrayList) albumList));
        } else {
            // 앨범 목록 조회 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "수강 목록 조회에 실패하였습니다");
        }

        request.setAttribute(Response.RESULT, responseObject);

        LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);

        return "/ResultJSON.jsp";
    }

    /**
     * 수강 삭제
     */
    @ControllerMethodInfo(id = "/api/album/deleteListenAlbum.do")
    public String deleteListenAlbum(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_DELETE_LISTEN_ALBUM;                              // 인터페이스 번호
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
            user = sessionMap.getInt("seq");
        } else {
            // 비로그인
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

        int result = 0;                                                         // 수강 삭제 결과

        try {
            ResultMap albumMap = AlbumDAO.selectListenAlbum(id, user);          // 앨범 조회
            if (albumMap != null) {
                // 앨범 조회 성공
                result = AlbumDAO.deleteListenAlbum(id, user);                  // 앨범 삭제
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
            // 수강 삭제 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "수강 취소되었습니다.");
        } else {
            // 수강 삭제 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "수강 취소가 실패하였습니다.");
        }

        request.setAttribute(Response.RESULT, responseObject);

        LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);

        return "/ResultJSON.jsp";
    }

    /**
     * 내 앨범 목록 조회
     */
//    해결해결 여기는 딱히 할건 없는데 DAO에서 pk 값까지 같이 넘겨주기는 해야된다.
    @ControllerMethodInfo(id = "/api/album/getMyAlbumList.do")
    public String getMyAlbumList(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_GET_MY_ALBUM_LIST;                                // 인터페이스 번호
        Integer user = null;                                                    // 유저 번호

        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        int page = HttpUtils.getParameterInt(request, "page", -1);                          // 페이지 번호
        int sortOption = HttpUtils.getParameterInt(request, "sort_option", -1);             // 정렬 유형
        String filter = HttpUtils.getParameterString(request, "filter", null);              // 필터

        JSONObject queryObject = new JSONObject();
        queryObject.put("page", page != -1 ? page : null);
        queryObject.put("sort_option", sortOption != -1 ? sortOption : null);
        queryObject.put("filter", filter);

        page = page != -1 ? page : 1;
        sortOption = sortOption != -1 ? sortOption : AlbumDAO.MY_ALBUM_SORT_OPTION_RECENT;

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

            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);

            return "/ResultJSON.jsp";
        }

        List<ResultMap> albumList = null;                                       // 앨범 목록
        ResultMap pageMap = null;
        int count = 0;

        try {
            int contentCount = 10;                                              // 목록 출력 개수
//            내 앨범 보기에서는 짜피 비공개도 다 보이기때문에 변경 안함
            count = AlbumDAO.selectMyAlbumListCount(user, filter);              // 내 앨범 목록 개수 조회

            pageMap = PageUtils.getPagination(count, page, contentCount);       // 페이징
            int totalPage = pageMap.getInt("totalPage");
            if (page <= totalPage || page == 1) {
                // 올바른 페이지 번호에 접근한 경우
                int startCount = pageMap.getInt("startCount");
//                여기서 pk정보도 추가로 가져와야된다
                albumList = AlbumDAO.selectMyAlbumList(user, filter, sortOption, startCount, count);        // 내 앨범 목록 조회
            } else {
                // 없느 페이지 번호에 접근한 경우
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
            // 앨범 목록 조회 성공
            int i = 0;
            int start = pageMap.getInt("startCount");
            int index = count - start;

            for (ResultMap albumMap : albumList) {
                albumMap.put("idx", index - (i++));
            }

            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "");
            responseObject.put(Response.MAP, UtilJSON.mapToJSon((Map<String, Object>) pageMap));
            responseObject.put(Response.LIST, UtilJSON.convertArrayListToJSONArray((ArrayList) albumList));
        } else {
            // 앨범 목록 조회 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "앨범 목록 조회에 실패하였습니다.");
        }

        request.setAttribute(Response.RESULT, responseObject);

        LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);

        return "/ResultJSON.jsp";
    }

    /**
     * 내 앨범 수강평 목록 조회
     */
    @ControllerMethodInfo(id = "/api/album/getMyAlbumCommentList.do")
    public String getMyAlbumCommentList(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_GET_MY_ALBUM_COMMENT_LIST;                        // 인터페이스 번호
        Integer user = null;                                                    // 유저 번호

        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        int page = HttpUtils.getParameterInt(request, "page", -1);                          // 페이지 번호

        JSONObject queryObject = new JSONObject();
        queryObject.put("page", page != -1 ? page : null);

        page = page != -1 ? page : 1;

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

            LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);

            return "/ResultJSON.jsp";
        }

        List<ResultMap> commentList = null;                                     // 수강평 목록
        ResultMap pageMap = null;

        try {
            int contentCount = 10;                                              // 목록 출력 개수
            int count = AlbumDAO.selectMyAlbumCommentListCount(user);           // 내 앨범 수강평 목록 개수 조회

            pageMap = PageUtils.getPagination(count, page, contentCount);       // 페이징
            int totalPage = pageMap.getInt("totalPage");
            if (page <= totalPage || page == 1) {
                // 올바른 페이지 번호에 접근한 경우
                int startCount = pageMap.getInt("startCount");
                commentList = AlbumDAO.selectMyAlbumCommentList(user, startCount, contentCount);    // 내 앨범 수강평 목록 조회
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

        if (commentList != null) {
            // 수강평 목록 조회 성공
            for (ResultMap commentMap : commentList) {
                if (commentMap.containsKey("reply") && commentMap.get("reply") != null) {
                    ResultMap replyMap = UtilJSON.jsonToMap(commentMap.getJSONObject("reply"));
                    commentMap.put("reply", replyMap);
                }
            }

            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "");
            responseObject.put(Response.MAP, UtilJSON.mapToJSon((Map<String, Object>) pageMap));
            responseObject.put(Response.LIST, UtilJSON.convertArrayListToJSONArray((ArrayList) commentList));
        } else {
            // 수강평 목록 조회 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "수강평 목록 조회에 실패하였습니다.");
        }

        request.setAttribute(Response.RESULT, responseObject);

        LogDAO.insertLog(api, user, userAgent, queryObject, null, responseObject);

        return "/ResultJSON.jsp";
    }

    /**
     * 앨범 추천/추천 취소
     */
    @ControllerMethodInfo(id = "/api/album/recommendAlbum.do")
    public String recommendAlbum(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_RECOMMEND_ALBUM;                                  // 인터페이스 번호
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
            user = sessionMap.getInt("seq");
        } else {
            // 비로그인
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

        int result = 0;                                                         // 추천/추천 취소 결과
        boolean status = false;

        try {
            // 필수 입력 사항
            status = requestObject.getBoolean("status");                        // 추천 상태

            ResultMap albumMap = AlbumDAO.selectAlbum(id);                      // 앨범 조회
            if (albumMap != null) {
                // 앨범 조회 성공
                if (status) {
                    if (!AlbumDAO.selectRecommendAlbum(id, user)) {             // 앨범 추천 이력 조회
                        // 추천 이력이 없는 경우
                        result = AlbumDAO.insertRecommendAlbum(id, user);       // 앨범 추천
                    } else {
                        // 추천 이력이 있는 경우
                        responseObject.put(Response.RESULT, Response.Result.FAILED);
                        responseObject.put(Response.MESSAGE, "앨범 추천 이력이 존재합니다.");
                        request.setAttribute(Response.RESULT, responseObject);

                        LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);

                        return "/ResultJSON.jsp";
                    }
                } else {
                    if (AlbumDAO.selectRecommendAlbum(id, user)) {
                        // 추천 이력이 있는 경우
                        result = AlbumDAO.deleteRecommendAlbum(id, user);          // 앨범 추천 취소
                    } else {
                        // 추천 이력이 없는 경우
                        responseObject.put(Response.RESULT, Response.Result.FAILED);
                        responseObject.put(Response.MESSAGE, "앨범 추천 이력이 존재하지 않습니다.");
                        request.setAttribute(Response.RESULT, responseObject);

                        LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);

                        return "/ResultJSON.jsp";
                    }
                }
            } else {
                // 앨범 조회 실패
                responseObject.put(Response.RESULT, Response.Result.ERROR);
                responseObject.put(Response.MESSAGE, "앨범 조회에 실패하였습니다.");
                request.setAttribute(Response.RESULT, responseObject);

                LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);

                return "/ResultJSON.jsp";
            }
        } catch (SQLException e) {
            e.printStackTrace();

            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잠시 후 다시 시도해주세요.");
            request.setAttribute(Response.RESULT, responseObject);

            LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);

            return "/ResultJSON.jsp";
        }

        if (result > 0) {
            // 앨범 추천/추천 취소 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, status ? "앨범을 추천하였습니다." : "앨범 추천을 취소하였습니다.");
        } else {
            // 앨범 추천/추천 취소 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, status ? "앨범 추천에 실패하였습니다." : "앨범 추천 취소에 실패하였습니다.");
        }

        request.setAttribute(Response.RESULT, responseObject);

        LogDAO.insertLog(api, user, userAgent, queryObject, requestObject, responseObject);

        return "/ResultJSON.jsp";
    }
}
