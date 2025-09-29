/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ithows;

import com.ithows.base.TemplateBean;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.logging.Logger;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import javax.servlet.jsp.PageContext;
import org.apache.commons.io.FilenameUtils;


/**
 *
 * @author home
 */
public class PageManager {

    static {
        BaseDebug.info("***PageManager Loadiang!!");
    }

    /**
     * 호출할 함수 이름을 추출한다.
     * @param url
     * @return
     */
    protected static String getMethodName(String url) {
        return FilenameUtils.getBaseName(url);   // @@ 확장자 관련
    }

    /**
     * URL에 접속하면 컨트롤러를 검색한 후 컨트롤러의 함수를 호출해서 view 페이지를 리턴한다.
     * 함수의 이름은 접속 URL의 파일명을 이용한다. 이 때 .do를 제외한 파일명이다.
     * 확장자를 제외한 파일명을 가진 메소드를 컨트롤러에서 호출한 후 결과로 view 페이지를 리턴한다.
     * @param pageContext 
     * @param session
     * @param response
     * @param request 
     * @return  
     */
    public static String callController(PageContext pageContext, HttpSession session, HttpServletRequest request, HttpServletResponse response) {
        PageBean pageBean = (PageBean) request.getAttribute("pageBean");
        String pageId = pageBean.getId();
        Object obj = pageBean.getController();

        String view = null;
        if (obj == null) {//컨트롤러가 등록되어 있지 않다면 디폴트 jsp 페이지로 이동한다.

            //@@ 확장자 추가시 이 부분을 수정해야 함
            if(pageId.endsWith(".do")){
                view = pageId.replace(".do", ".jsp");
            }else if(pageId.endsWith(".api")){
                view = pageId.replace(".api", ".jsp");
            }else if(pageId.endsWith(".get")){
                view = pageId.replace(".get", ".jsp");
            }
            
            //@aaa_Logger.getLogger("accessLogger").info(BaseLogger.getLog("정상", "Default 컨트롤러, Default 함수", null, request));
        } else {//컨트롤러가 등록되어 있다면 함수의 이름 규칙을 이용해서 함수를 호출한다.
            String methodName = getMethodName(pageId);

            try {
                Method m = obj.getClass().getMethod(methodName, HttpSession.class, HttpServletRequest.class, HttpServletResponse.class, Object.class);
                Object result = m.invoke(obj, session, request, response, request.getAttribute(pageBean.getCommandName()));
                if (result != null) {
                    view = (String) result;
                }
                //@aaa_Logger.getLogger("accessLogger").info(BaseLogger.getLog("정상", obj.getClass().getSimpleName() + ", " + m.getName(), null, request));
            } catch (NoSuchMethodException e) {//컨트롤러를 못찾았을 경우
                Logger.getLogger("accessLogger").info(BaseLogger.getLog("정상", obj.getClass().getSimpleName() + ", Default 함수", null, request));
                
                 // 컨트롤러 함수가 없는 부분
                //@@ 확장자 추가시 이 부분을 수정해야 함
                if(pageId.endsWith(".do")){
                    view = pageId.replace(".do", ".jsp");
                }else if(pageId.endsWith(".api")){
                    view = pageId.replace(".api", ".jsp");
                }else if(pageId.endsWith(".get")){
                    view = pageId.replace(".get", ".jsp");
                }
                
                //BaseDebug.log(e);
            } catch (Exception e2) {
                Logger.getLogger("accessLogger").info(BaseLogger.getLog("Error ", "심각", null, request));
                BaseDebug.log(e2, "잘못된 접근입니다.[" + methodName  + "] Command 객체를 등록하지 않아서 에러가 발생한 경우가 많다.");
            }
        }
        
        return view;
    }

    /**
     * view Page의 이동을 제어한다.
     * @param view
     * @param pageContext
     * @param request
     * @param response
     */
    public static void moveViewPage(String view, PageContext pageContext, HttpServletRequest request, HttpServletResponse response) {
        try {
            PageBean pageBean = (PageBean) request.getAttribute("pageBean");
            if (view == null) {//0. View가 없다면 아무런 액션도 취하며 안된다. 적어도 error 페이지라도 보내야 한다.
                //BaseDebug.info("nothing view");
            } else if (view.startsWith("redirect")) {//1. Redirect
                response.sendRedirect(view.replace("redirect:", ""));
            } else if (view.equals("RESULT_PAGE")) {// simpleResult.jsp
                pageContext.forward("/WEB-INF/jsp/simpleResult.jsp");
            } else if (view.equals("RESULT_TEXT")) {// simpleResult.jsp
                pageContext.forward("/WEB-INF/jsp/simpleText.jsp");
            } else if (view.equals("RESULT_SIMPLE_JSON")) {// simpleResultJson.jsp
                pageContext.forward("/WEB-INF/jsp/simpleResultJson.jsp");      
            } else if (view.equals("RESULT_COMMON_JSON")) { //  commonResultJson.jsp
                pageContext.forward("/WEB-INF/jsp/commonResultJson.jsp");      
            } else if (view.equals("RESULT_RAW_JSON")) { //  commonResultRawJson.jsp
                pageContext.forward("/WEB-INF/jsp/commonResultRawJson.jsp");      
            } else if (view.equals("RESULT_LIST_JSON")) {//  commonResultJson.jsp
                pageContext.forward("/WEB-INF/jsp/resultListJson.jsp");      
            } else if (view.equals("RESULT_CHART_JSON")) {//  chartResultJson.jsp (차트 매칭)
                pageContext.forward("/WEB-INF/jsp/chartResultJson.jsp");      
            } else if (view.equals("NO_PAGE")) {  //3. 페이지가 없는 경우
                
            } else if (pageBean.getTemplate() != null) {  //4. pageBean.getTemplate()을 얻는다. 
                TemplateBean tempBean = pageBean.getTemplate(); //  main이 디폴트로 
                request.setAttribute("top", "/WEB-INF/jsp" + tempBean.getTop());
                request.setAttribute("bottom", "/WEB-INF/jsp" + tempBean.getBottom());
                request.setAttribute("view", "/WEB-INF/jsp" + view);
                pageContext.forward("/WEB-INF/jsp" + tempBean.getTemplatePage());
            } else {//5. 이도 저도 아닌 경우
                pageContext.forward("/WEB-INF/jsp" + view);
            }
        } catch (IOException e) {
            BaseDebug.log(e, "redirect view 페이지를 확인하십시오. ", view);
        } catch (ServletException e2) {
            BaseDebug.log(e2, "view 페이지를 확인하십시오. ", view);
        }
    }
}
