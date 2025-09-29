package com.ithows.controller;

import com.codeenator.dao.LogDAO;
import com.codeenator.dao.UserDAO;
import com.codeenator.model.Response;
import com.codeenator.utils.MailUtils;
import com.codeenator.utils.RandomUtils;
import com.ithows.BaseDebug;
import com.ithows.ResultMap;
import com.ithows.SessionInfo;
import com.ithows.base.ControllerClassInfo;
import com.ithows.base.ControllerMethodInfo;
import com.ithows.util.HttpUtils;
import com.ithows.util.UtilJSON;
import java.sql.SQLException;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * 유저 API Controller
 * 
 * - 회원가입 ---------------------------------------- {@link #join(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 아이디 중복확인 ---------------------------------- {@link #isUsedId(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 이메일 인증번호 발송 ------------------------------ {@link #getCipher(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 이메일 인증번호 확인 ------------------------------ {@link #checkCipher(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 닉네임 중복확인 ---------------------------------- {@link #isUsedNickname(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 로그인 ----------------------------------------- {@link #login(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 로그아웃 ---------------------------------------- {@link #logout(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 아이디 찾기 ------------------------------------- {@link #findId(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 비밀번호 찾기 ----------------------------------- {@link #findPassword(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 회원정보 조회 ----------------------------------- {@link #getUser(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 회원정보 변경 ----------------------------------- {@link #modifyUser(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 비밀번호 변경 ----------------------------------- {@link #modifyPassword(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 현재 비밀번호 확인 ------------------------------- {@link #isUsedPassword(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 * - 회원탈퇴 --------------------------------------- {@link #withdrawal(javax.servlet.http.HttpSession, javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse, java.lang.Object)}
 */
@ControllerClassInfo(controllerPage="/user/_user.jsp")
public class UserController {
    
    static {
        BaseDebug.info("***UserController.class Loading!!");
    }
   
    /**
     * 회원가입 
     */
    @ControllerMethodInfo(id = "/api/user/join.do")
    public String join(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_JOIN;                                             // 인터페이스 번호
        int type = UserDAO.USER_TYPE_PUBLIC;                                    // 유저 유형
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        
        JSONObject requestObject = HttpUtils.getBodyJson(request);
        JSONObject responseObject = new JSONObject();
        
        int result = 0;                                                         // 회원가입 결과
        
        try {
            // 필수 입력 사항
            String id = requestObject.getString("id");                          // 아이디
            String password = requestObject.getString("password");              // 비밀번호
            String email = requestObject.getString("email");                    // 이메일
            String nickname = requestObject.getString("nickname");              // 닉네임
                
            requestObject.put("password", "");                                  // 비밀번호 제거
            
            result = UserDAO.insertUser(type, id, password, email, nickname);   // 유저 정보 저장
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
            // 회원가입 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "회원가입이 완료되었습니다.");
        } else {
            // 회원가입 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "회원가입에 실패하였습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
        
        LogDAO.insertLog(api, user, userAgent, null, requestObject, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 아이디 중복 확인
     */
    @ControllerMethodInfo(id = "/api/user/isUsedId.do")
    public String isUsedId(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_IS_USED_ID;                                       // 인터페이스 번호
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        
        JSONObject requestObject = HttpUtils.getBodyJson(request);
        JSONObject responseObject = new JSONObject();
        
        int result = 0;                                                         // 아이디 사용중인 유저 번호
        
        try {
            // 필수 입력 사항
            String id = requestObject.getString("id");                          // 아이디
            
            result = UserDAO.selectSeqById(id);                                 // 아이디 중복 확인
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
        
        if (result < 1) {
            // 사용 가능한 아이디
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "사용 가능한 아이디입니다.");
        } else {
            // 중복된 아이디
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "이미 사용중인 아이디입니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
        
        LogDAO.insertLog(api, user, userAgent, null, requestObject, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 이메일 인증번호 발송
     */
    @ControllerMethodInfo(id = "/api/user/email/getCipher.do")
    public String getCipher(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_GET_CIPHER;                                       // 인터페이스 번호
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        
        JSONObject requestObject = HttpUtils.getBodyJson(request);
        JSONObject responseObject = new JSONObject();
        
        int result = 0;                                                         // 이메일 사용 유저 번호
        String email = null;
        
        try {
            // 필수 입력 사항
            email = requestObject.getString("email");                           // 이메일
            
            result = UserDAO.selectSeqByEmail(email);                           // 이메일 중복 확인
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
        
        if (result < 1) {
            // 사용 가능한 이메일
            boolean isSucceed = false;
            String cipher = RandomUtils.createUpper(6);                         // 인증번호 생성
            
            if (email != null && !"".equals(email.trim())) {
                // 제목 및 내용
                String title = "[Codeenator] 이메일 인증번호";                          

                StringBuilder sb = new StringBuilder();
                sb.append("안녕하세요. Codeenator입니다.").append("<br>");
                sb.append("이메일 인증번호를 발급해드렸습니다.").append("<br>");
                sb.append("본인임을 알 수 있도록 아래 발급된 인증번호를 입력해 주세요.").append("<br><br>");
                sb.append("인증번호는 <span style='font-size: 16px; font-weight: bold'>").append(cipher).append("</span> 입니다.").append("<br><br>");
                sb.append("해당 서비스를 이용 중이 아니시라면, 이메일을 삭제해주시길 바랍니다.");
                
                String content = sb.toString();

                isSucceed = MailUtils.sendMail(email, title, content);          // 이메일 전송
            }
            
            if (isSucceed) {
                // 이메일 발송 성공
                ResultMap cipherMap = new ResultMap();                          // 이메일과 인증번호 정보
                cipherMap.put("email", email);
                cipherMap.put("cipher", cipher);
                
                session.setAttribute("cipher", cipherMap);                      // 세션에 인증번호 정보 저장
                
                responseObject.put(Response.RESULT, Response.Result.SUCCESS);
                responseObject.put(Response.MESSAGE, "인증번호가 발송되었습니다.");
            } else {
                // 이메일 발송 실패
                responseObject.put(Response.RESULT, Response.Result.FAILED);
                responseObject.put(Response.MESSAGE, "인증번호 발송이 실패하였습니다.");
            }
        } else {
            // 중복된 이메일
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "이미 사용중인 이메일입니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
        
        LogDAO.insertLog(api, user, userAgent, null, requestObject, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 이메일 인증번호 확인
     */
    @ControllerMethodInfo(id = "/api/user/email/checkCipher.do")
    public String checkCipher(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_CHECK_CIPHER;                                     // 인터페이스 번호
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기

        JSONObject requestObject = HttpUtils.getBodyJson(request);
        JSONObject responseObject = new JSONObject();
        
        ResultMap cipherMap = session.getAttribute("cipher") != null? (ResultMap) session.getAttribute("cipher") : null;    // 세션에서 인증번호 확인
        if (cipherMap == null) {
            // 인증번호 발급 내역이 없는 경우
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, null, requestObject, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        try {
            // 필수 입력 사항
            String inputEmail = requestObject.getString("email");               // 이메일
            String inputCipher = requestObject.getString("cipher");             // 인증번호
            
            String email = cipherMap.containsKey("email")? cipherMap.getString("email") : "";       // 발급받은 이메일
            String cipher = cipherMap.containsKey("cipher")? cipherMap.getString("cipher") : "";    // 발급받은 인증번호
            
            if (email.equals(inputEmail) && cipher.equals(inputCipher)) {
                responseObject.put(Response.RESULT, Response.Result.SUCCESS);
                responseObject.put(Response.MESSAGE, "인증번호가 확인되었습니다.");
            } else {
                responseObject.put(Response.RESULT, Response.Result.FAILED);
                responseObject.put(Response.MESSAGE, "올바른 인증번호가 아닙니다.");
            }
        } catch (JSONException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "입력 값을 확인해주세요.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
        
        LogDAO.insertLog(api, user, userAgent, null, requestObject, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 닉네임 중복 확인
     */
    @ControllerMethodInfo(id = "/api/user/isUsedNickname.do")
    public String isUsedNickname(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_IS_USED_NICKNAME;                                 // 인터페이스 번호
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        
        JSONObject requestObject = HttpUtils.getBodyJson(request);
        JSONObject responseObject = new JSONObject();
        
        int result = 0;                                                         // 닉네임 사용 유저 번호
        
        try {
            // 필수 입력 사항
            String nickname = requestObject.getString("nickname");              // 닉네임
            
            result = UserDAO.selectSeqByNickname(nickname);                     // 닉네임 중복 확인
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
        
        if (result < 1) {
            // 사용 가능한 닉네임
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "사용 가능한 닉네임입니다.");
        } else {
            // 중복된 닉네임
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "이미 사용중인 닉네임입니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
        
        LogDAO.insertLog(api, user, userAgent, null, requestObject, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 로그인
     */
    @ControllerMethodInfo(id = "/api/user/login.do")
    public String login(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_LOGIN;                                            // 인터페이스 번호
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        
        JSONObject requestObject = HttpUtils.getBodyJson(request);
        JSONObject responseObject = new JSONObject();
        
        ResultMap userMap = null;                                               // 유저 정보
        
        try {
            // 필수 입력 사항
            String id = requestObject.getString("id");                          // 아이디
            String password = requestObject.getString("password");              // 비밀번호
            
            requestObject.put("password", "");                                  // 비밀번호 삭제
            
            userMap = SessionInfo.login(session, request, response, id, password);      // 로그인 시도
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
        
        if (userMap != null) {
            // 로그인
            user = userMap.getInt("seq");
            
            int count = LogDAO.selectIsLogin(user);                             // 오늘 접속 여부 확인
            if (count == 0) {
                UserDAO.updateLoginCount(user);                                 // 접속 수 증가
            }
            
            ResultMap resultMap = new ResultMap();
            resultMap.put("type", userMap.get("type"));
            resultMap.put("id", userMap.getString("userId"));
            resultMap.put("nickname", userMap.getString("nickname"));
            
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "");
            responseObject.put(Response.MAP, UtilJSON.mapToJSon((Map<String, Object>) resultMap));
        } else {
            // 로그인 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "아이디 또는 비밀번호가 틀립니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
        
        LogDAO.insertLog(api, user, userAgent, null, requestObject, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 로그아웃 
     */
    @ControllerMethodInfo(id = "/api/user/logout.do")
    public String logout(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_LOGOUT;                                           // 인터페이스 번호
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        
        JSONObject responseObject = new JSONObject();
        
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (sessionMap != null && sInfo.getLogin()) {
            // 로그인 상태
            user = sessionMap.getInt("seq");
            
            sInfo.setLogout();
            session.removeAttribute("sessionInfo");
            
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "로그아웃 되었습니다.");
        } else {
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
        
        LogDAO.insertLog(api, user, userAgent, null, null, responseObject);
    
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 아이디 찾기
     */
    @ControllerMethodInfo(id = "/api/user/findId.do")
    public String findId(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_FIND_ID;                                          // 인터페이스 번호
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        
        JSONObject requestObject = HttpUtils.getBodyJson(request);
        JSONObject responseObject = new JSONObject();
        
        String id = null;
        String email = null;
        
        try {
            // 필수 입력 사항
            email = requestObject.getString("email");                           // 이메일
            
            id = UserDAO.selectIdByEmail(email);                                // 아이디 조회
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
        
        if (id != null && !"".equals(id)) {
            // 아이디 찾기 성공
            boolean isSucceed = false;
            
            if (email != null && !"".equals(email.trim())) {
                // 제목 및 내용
                String title = "[Codeenator] 아이디 찾기";

                StringBuilder sb = new StringBuilder();
                sb.append("안녕하세요. Codeenator입니다.").append("<br>");
                sb.append("아이디 찾기 결과입니다.").append("<br><br>");
                sb.append("회원님의 아이디는 <span style='font-size: 16px; font-weight: bold'>").append(id).append("</span> 입니다.").append("<br><br>");
                sb.append("해당 서비스를 이용 중이 아니시라면, 이메일을 삭제해주시길 바랍니다.");
                
                String content = sb.toString();
            
                isSucceed = MailUtils.sendMail(email, title, content);          // 이메일 전송
            }
            
            if (isSucceed) {
                // 이메일 전송 성공
                responseObject.put(Response.RESULT, Response.Result.SUCCESS);
                responseObject.put(Response.MESSAGE, "가입하신 아이디를 이메일로 전송하였습니다.");
            } else {
                // 이메일 전송 실패
                responseObject.put(Response.RESULT, Response.Result.FAILED);
                responseObject.put(Response.MESSAGE, "이메일 전송에 실패했습니다.");
            }
        } else {
            // 아이디 찾기 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "가입하신 정보와 일치하지 않습니다. 다시 확인해주세요.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
        
        LogDAO.insertLog(api, user, userAgent, null, requestObject, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 비밀번호 찾기
     */
    @ControllerMethodInfo(id = "/api/user/findPassword.do")
    public String findPassword(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_FIND_PSWD;                                        // 인터페이스 번호
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        
        JSONObject requestObject = HttpUtils.getBodyJson(request);
        JSONObject responseObject = new JSONObject();
        
        int result = 0;                                                         // 비밀번호 변경 결과
        
        String email = null;
        String password = null;
        
        try {
            // 필수 입력 사항
            String id = requestObject.getString("id");                          // 아이디
            email = requestObject.getString("email");                           // 비밀번호
            
            int seq = UserDAO.selectSeqByIdAndEmail(id, email);                 // 유저 조회
            if (seq > 0) {
                // 유저 조회 성공
                password = RandomUtils.createUpper(10);                         // 새로운 비밀번호 생성
                result = UserDAO.updatePassword(seq, password);                 // 비밀번호 변경
            } else {
                // 유저 조회 실패
                responseObject.put(Response.RESULT, Response.Result.FAILED);
                responseObject.put(Response.MESSAGE, "가입하신 정보와 일치하지 않습니다. 다시 확인해주세요.");
                request.setAttribute(Response.RESULT, responseObject);
                
                LogDAO.insertLog(api, user, userAgent, null, requestObject, responseObject);
                
                return "/ResultJSON.jsp";
            }
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
            // 비밀번호 변경 성공
            boolean isSucceed = false;
            
            if (email != null && !"".equals(email.trim())) {
                // 제목 및 내용
                String title = "[Codeenator] 비밀번호 찾기";            

                StringBuilder sb = new StringBuilder();
                sb.append("안녕하세요. Codeenator입니다.").append("<br>");
                sb.append("비밀번호 찾기 결과입니다.").append("<br>");
                sb.append("회원님의 비밀번호를 임시 비밀번호로 발급해드렸습니다.").append("<br>");
                sb.append("임시 비밀번호로 로그인하시고, 비밀번호를 변경해주시길 바랍니다.").append("<br><br>");
                sb.append("임시 비밀번호는 <span style='font-size: 16px; font-weught: bold'>").append(password).append("</span> 입니다.").append("<br><br>");
                sb.append("해당 서비스를 이용 중이 아니시라면, 이메일을 삭제해주시길 바랍니다.");
                
                String content = sb.toString();
                
                isSucceed = MailUtils.sendMail(email, title, content);          // 이메일 전송
            }
            
            if (isSucceed) {
                // 이메일 전송 성공
                responseObject.put(Response.RESULT, Response.Result.SUCCESS);
                responseObject.put(Response.MESSAGE, "입력하신 이메일 주소로 임시 비밀번호가 전송되었습니다.");
            } else {
                // 이메일 전송 실패
                responseObject.put(Response.RESULT, Response.Result.FAILED);
                responseObject.put(Response.MESSAGE, "이메일 전송에 실패했습니다.");
            }
        } else {
            // 비밀번호 변경 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "비밀번호 변경이 실패하였습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
            
        LogDAO.insertLog(api, user, userAgent, null, requestObject, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 회원정보 조회
     */
    @ControllerMethodInfo(id = "/api/user/getUser.do")
    public String getUser(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_GET_USER;                                         // 인터페이스 번호
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);       // 접근 기기
        
        JSONObject responseObject = new JSONObject();
        
        // session
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        ResultMap sessionMap = sInfo.getMember();
        if (sessionMap != null) {
            // 로그인
            user = sessionMap.getInt("seq");
            
            ResultMap userMap = new ResultMap();
            userMap.put("type", sessionMap.get("type"));
            userMap.put("email", sessionMap.getString("email"));
            userMap.put("nickname", sessionMap.getString("nickname"));
            
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "");
            responseObject.put(Response.MAP, UtilJSON.mapToJSon((Map<String, Object>) userMap));
        } else {
            // 비로그인
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
            
        LogDAO.insertLog(api, user, userAgent, null, null, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 회원정보 변경
     */
    @ControllerMethodInfo(id = "/api/user/modifyUser.do")
    public String modifyUser(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_MODIFY_USER;                                      // 인터페이스 번호
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
            // 비로그인
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, null, requestObject, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        int result = 0;                                                         // 회원정보 변경 결과
        
        try {
            // 필수 입력 사항
            String email = requestObject.getString("email");                    // 이메일
            String nickname = requestObject.getString("nickname");              // 닉네임
            
            result = UserDAO.updateUser(user, email, nickname);                 // 회원정보 변경
            sInfo.reload(session, request, response);                           // 세션 정보 수정
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
            // 회원정보 변경 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "회원정보가 변경되었습니다.");
        } else {
            // 회원정보 변경 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "회원정보 변경이 실패하였습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
            
        LogDAO.insertLog(api, user, userAgent, null, requestObject, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 비밀번호 변경
     */
    @ControllerMethodInfo(id = "/api/user/modifyPassword.do")
    public String modifyPassword(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_MODIFY_PSWD;                                      // 인터페이스 번호
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
            // 비로그인
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, null, requestObject, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        int result = 0;                                                         // 비밀번호 변경 결과
        
        try {
            // 필수 입력 사항
            String currentPassword = requestObject.getString("current_password");   // 현재 비밀번호
            String newPassword = requestObject.getString("new_password");           // 새로운 비밀번호
            
            requestObject.put("current_password", "");                          // 비밀번호 제거
            requestObject.put("new_password", "");
            
            int seq = UserDAO.selectSeqBySeqAndPassword(user, currentPassword); // 현재 비밀번호 확인
            if (seq > 0) {
                // 현재 사용중인 비밀번호인 경우
                result = UserDAO.updatePassword(user, newPassword);             // 비밀번호 변경
            } else {
                // 현재 비밀번호가 틀린 경우
                responseObject.put(Response.RESULT, Response.Result.FAILED);
                responseObject.put(Response.MESSAGE, "현재 비밀번호와 일치하지 않습니다.");
                request.setAttribute(Response.RESULT, responseObject);
            
                LogDAO.insertLog(api, user, userAgent, null, requestObject, responseObject);
                
                return "/ResultJSON.jsp";
            }
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
            // 비밀번호 변경 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "비밀번호가 변경되었습니다.");
        } else {
            // 비밀번호 변경 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "비밀번호 변경에 실패하였습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
        
        LogDAO.insertLog(api, user, userAgent, null, requestObject, responseObject);
        
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 현재 비밀번호 확인
     */
    @ControllerMethodInfo(id = "/api/user/isUsedPassword.do")
    public String isUsedPassword(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_IS_USED_PSWD;                                     // 인터페이스 번호
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
            // 비로그인
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잘못된 접근입니다.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, null, requestObject, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        int result = 0;                                                         // 비밀번호 일치 결과

        try {
            // 필수 입력 사항
            String password = requestObject.getString("password");              // 비밀번호
            
            requestObject.put("password", "");                                  // 비밀번호 제거
           
            result = UserDAO.selectSeqBySeqAndPassword(user, password);         // 현재 비밀번호 확인
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
            // 현재 사용중인 비밀번호인 경우
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "현재 사용중인 비밀번호입니다.");
        } else {
            // 비밀번호가 틀린 경우
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "현재 비밀번호와 일치하지 않습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);

        LogDAO.insertLog(api, user, userAgent, null, requestObject, responseObject);
            
        return "/ResultJSON.jsp";
    }
    
    
    /**
     * 회원탈퇴 
     */
    @ControllerMethodInfo(id = "/api/user/withdrawal.do")
    public String withdrawal(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        int api = LogDAO.USER_WITHDRAWAL;                                       // 인터페이스 번호
        Integer user = null;                                                    // 유저 번호
        
        // request
        String userAgent = HttpUtils.getParameterString(request, "user_agent", null);
        
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
        
        int result = 0;                                                         // 회원탈퇴 결과
        
        try {
            result = UserDAO.updateDisableUser(user);                           // 회원탈퇴
        } catch (SQLException e) {
            e.printStackTrace();
            
            responseObject.put(Response.RESULT, Response.Result.ERROR);
            responseObject.put(Response.MESSAGE, "잠시 후 다시 시도해주세요.");
            request.setAttribute(Response.RESULT, responseObject);
            
            LogDAO.insertLog(api, user, userAgent, null, null, responseObject);
            
            return "/ResultJSON.jsp";
        }
        
        if (result > 0) {
            // 회원탈퇴 성공
            responseObject.put(Response.RESULT, Response.Result.SUCCESS);
            responseObject.put(Response.MESSAGE, "탈퇴되었습니다.");
        } else {
            // 회원탈퇴 실패
            responseObject.put(Response.RESULT, Response.Result.FAILED);
            responseObject.put(Response.MESSAGE, "회원탈퇴가 실패하였습니다.");
        }
        
        request.setAttribute(Response.RESULT, responseObject);
            
        LogDAO.insertLog(api, user, userAgent, null, null, responseObject);
        
        return "/ResultJSON.jsp";
    }
}
