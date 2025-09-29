package com.ithows.controller;

import com.codeenator.dao.FileDAO;
import com.codeenator.dao.LogDAO;
import com.codeenator.model.Response;
import com.codeenator.utils.FileUtils;
import com.ithows.BaseDebug;
import com.ithows.FileInfo;
import com.ithows.JakartaUpload;
import com.ithows.ResultMap;
import com.ithows.SessionInfo;
import com.ithows.base.ControllerClassInfo;
import com.ithows.base.ControllerMethodInfo;
import com.ithows.service.UploadConst;
import com.ithows.util.HttpUtils;
import com.ithows.util.UtilJSON;
import java.io.File;
import java.sql.SQLException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONObject;

/**
 * 파일 API Controller
 * 
 * - 파입 업로드 -------------------------------------- { @link #upload(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object) }
 */
@ControllerClassInfo(controllerPage="/api/_file.jsp")
public class FileController {
    
    static {
        BaseDebug.info("***FileController.class Loading!!");
    }
    
    /**
     * 파일 업로드
     */
    @ControllerMethodInfo(id = "/api/file/upload.do")
    public String upload(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.FILE_UPLOAD;                                           // 인터페이스 번호
        Integer user = null;                                                    // 유저 번호
        
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        
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
            
            LogDAO.insertLog(api, user, userAgent, null, null, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        // 업로드 경로
        String contextPath = UploadConst.contextPath();
        String tempPath = FileUtils.getTempPath();
        String uploadPath = FileUtils.getUploadPath();
        
        String realPath = String.format("%s%s", contextPath, tempPath);
        System.out.println("파일 실 저장소 " + realPath);
        
        int result = 0;                                                         // 파일 업로드 여부
        boolean isSucceed = false;                                              // 파일 이름 변경 여부
        
        // request
        JakartaUpload mRequest = new JakartaUpload(request, 10 * 1024 * 1024, realPath, "UTF-8");   // 파일 크기 제한을 10MB로 설정
        FileInfo fInfo = mRequest.getFileInfo("file");
        System.out.println("저장된 파일 " + fInfo);
        if (fInfo == null) {
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, null, null, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        // 파일 정보
        String originalName = fInfo.getFileName();
        long size = fInfo.getFileSize();
        
        String storedName = null;
        
        try {
            // 업로드 할 이름으로 변경
            String uuid = FileUtils.createUUID();
            storedName = FileUtils.rename(originalName, uuid);

            File oldFile = new File(realPath, originalName);
            File newFile = new File(realPath, storedName);
            
            isSucceed = FileUtils.renameTo(oldFile, newFile);                   // 파일 이름 변경
        
            if (isSucceed) {
            // 파일 이름 변경 성공
            result = FileDAO.insertFile(uuid, uploadPath, originalName, storedName, size);     // 파일 정보 저장
            } else {
                // 파일 이름 변경 실패
                responseObject.put(Response.RESULT, Response.Result.FAILED);
                responseObject.put(Response.MESSAGE, "파일 업로드에 실패하였습니다.");
                request.setAttribute(Response.RESULT, responseObject);
            
                LogDAO.insertLog(api, user, userAgent, null, null, responseObject);
                
                return "/ResultJSON.jsp";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잠시 후 다시 시도해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, null, null, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (result > 0) {
            // 파일 업로드 성공
            ResultMap fileMap = new ResultMap();
            fileMap.put("temp_path", tempPath);
            fileMap.put("upload_path", uploadPath);
            fileMap.put("name", originalName);
            fileMap.put("stored_name", storedName);
            
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "");
            responseObject.put(Response.MAP, UtilJSON.mapToJSon((Map<String, Object>) fileMap));
        } else {
            // 파일 업로드 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "파일 업로드에 실패하였습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
            
        LogDAO.insertLog(api, user, userAgent, null, null, responseObject);
        
        return "/ResultJSON.jsp";
    }
}
