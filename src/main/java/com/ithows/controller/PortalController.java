package com.ithows.controller;

import com.codeenator.dao.PortalDAO;
import com.codeenator.model.Response;
import com.ithows.BaseDebug;
import com.ithows.ResultMap;
import com.ithows.base.ControllerClassInfo;
import com.ithows.base.ControllerMethodInfo;
import com.ithows.util.HttpUtils;
import com.ithows.util.UtilJSON;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * 포털 API Controller
 * 
 * - 팝업 조회 ----------------------------------------- {@link #getPopup(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 배너 조회 ----------------------------------------- {@link #getBanner(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 추천 앨범 목록 조회 -------------------------------- {@link #getAlbumList(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 신규 앨범 목록 조회 -------------------------------- {@link #getNewAlbumList(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 수강평 목록 조회 ----------------------------------- {@link #getAlbumCommentList(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 앨범수, 수강수 조회 -------------------------------- {@link #getAlbumCount(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 */
@ControllerClassInfo(controllerPage="/portal/_portal.jsp")
public class PortalController {
    
    static {
        BaseDebug.info("***PortalController.class Loading!!");
    }
    
    /**
     * 팝업 조회
     */
    @ControllerMethodInfo(id = "/api/portal/getPopup.do")
    public String getPopup(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        
        JSONObject responseObject = new JSONObject();
        
        ResultMap popupMap = null;                                              // 팝업 정보
        
        try {
            popupMap = PortalDAO.selectPopup();                                 // 팝업 정보 조회
        } catch (SQLException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잠시 후 다시 시도해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (popupMap != null) {
            // 팝업 조회 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "");
            responseObject.put(Response.MAP, UtilJSON.mapToJSon((Map<String, Object>) popupMap));
        } else {
            // 팝업 조회 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "팝업 정보 조회에 실패하였습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 배너 조회
     */
    @ControllerMethodInfo(id = "/api/portal/getBanner.do")
    public String getBanner(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        
        JSONObject responseObject = new JSONObject();
        
        String banner = null;                                                   // 배너
        
        try {
            banner = PortalDAO.selectBanner();                                  // 배너 조회
        } catch (SQLException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잠시 후 다시 시도해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            return "/ResultJSON.jsp";
        }

        if (banner != null) {
            // 배너 정보 조회 성공
            JSONArray bannerArray = new JSONArray();
            if (!"".equals(banner)) {
                bannerArray = new JSONArray(banner);
            }
            
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "");
            responseObject.put(Response.LIST, bannerArray);
        } else {
            // 배너 정보 조회 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "배너 정보 조회에 실패하였습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 추천 앨범 목록 조회
     */
    @ControllerMethodInfo(id = "/api/portal/getAlbumList.do")
    public String getAlbumList(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        
        JSONObject responseObject = new JSONObject();
        
        List<ResultMap> albumList = null;                                       // 앨범 목록
        
        try {
            ResultMap settingMap = PortalDAO.selectAlbumSetting();              // 앨범 설정 정보 조회
            int sortOption = settingMap.containsKey("album_sort")? settingMap.getInt("album_sort") : 1;     // 정렬 유형
            int count = settingMap.containsKey("album_count")? settingMap.getInt("album_count") : 10;       // 목록 출력 개수
            
            albumList = PortalDAO.selectAlbumList(sortOption, count);           // 추천 앨범 목록 조회
        } catch (SQLException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잠시 후 다시 시도해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (albumList != null) {
            // 앨범 목록 조회 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "");
            responseObject.put(Response.LIST, UtilJSON.convertArrayListToJSONArray((ArrayList) albumList));
        } else {
            // 앨범 목록 조회 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "앨범 목록 조회에 실패하였습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 신규 앨범 목록 조회
     */
//    해결해결 여기서 불러오는 리스트에 비밀글은 포함되면 안된다
    @ControllerMethodInfo(id = "/api/portal/getNewAlbumList.do")
    public String getNewAlbumList(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        
        JSONObject responseObject = new JSONObject();
        
        List<ResultMap> albumList = null;                                       // 앨범 목록
        
        try {
            int contentCount = 8;                                               // 목록 출력 개수
            albumList = PortalDAO.selectNewAlbumList(contentCount);             // 신규 앨범 목록 조회
        } catch (SQLException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잠시 후 다시 시도해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (albumList != null) {
            // 앨범 목록 조회 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "");
            responseObject.put(Response.LIST, UtilJSON.convertArrayListToJSONArray((ArrayList) albumList));
        } else {
            // 앨범 목록 조회 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "앨범 목록 조회에 실패하였습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 수강평 목록 조회
     */
    @ControllerMethodInfo(id = "/api/portal/getAlbumCommentList.do")
    public String getAlbumCommentList(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        
        JSONObject responseObject = new JSONObject();
        
        List<ResultMap> commentList = null;                                     // 수강평 목록
        
        try {
            int contentCount = 5;                                               // 목록 출력 개수
            commentList = PortalDAO.selectAlbumCommentList(contentCount);       // 수강평 목록 조회
        } catch (SQLException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잠시 후 다시 시도해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (commentList != null) {
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "");
            responseObject.put(Response.LIST, UtilJSON.convertArrayListToJSONArray((ArrayList) commentList));
        } else {
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "수강평 목록 조회에 실패하였습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 앨범수, 수강수 조회
     */
    @ControllerMethodInfo(id = "/api/portal/getAlbumCount.do")
    public String getAlbumCount(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        
        JSONObject responseObject = new JSONObject();
        
        ResultMap countMap = null;                                              // 앨범수, 수강수
        
        try {
            countMap = PortalDAO.selectAlbumCount();                            // 앨범수, 수강수 조회
        } catch (SQLException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잠시 후 다시 시도해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (countMap != null) {
            // 앨범수, 수강수 조회 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "");
            responseObject.put(Response.MAP, UtilJSON.mapToJSon((Map<String, Object>) countMap));
        } else {
            // 앨범수, 수강수 조회 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "앨범수, 수강수 조회에 실패하였습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
        
        return "/ResultJSON.jsp";
    }
}
