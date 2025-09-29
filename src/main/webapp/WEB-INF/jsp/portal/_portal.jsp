<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.ithows.*,com.ithows.base.*"%>
<%@ page import="com.ithows.util.HttpUtils" %>
<%
    PageBean pageBean = (PageBean) request.getAttribute("pageBean");
    String pageId = pageBean.getId();
    SessionInfo sInfo = HttpUtils.getSessionInfo(session);

    /* 여기서는 필터링 조건을 주고 이동 페이지에 대한 처리는 Interceptor에서 한다. */
    String view = ServiceInterceptor.checkLogin(session, request);
    if (view == null) {  //로그인이 되었다면
        view = PageManager.callController(pageContext, session, request, response);
        
    } else { //로그인이 안된 경우에도 통과 시켜 줄 url
        if (    pageId.indexOf("getPopup.do") != -1
            ||  pageId.indexOf("getBanner.do") != -1
            ||  pageId.indexOf("getAlbumList.do") != -1
            ||  pageId.indexOf("getNewAlbumList.do") != -1
            ||  pageId.indexOf("getAlbumCommentList.do") != -1
            ||  pageId.indexOf("getAlbumCount.do") != -1            ) {  //얘만 예외로 풀어준다.  
            view = PageManager.callController(pageContext, session, request, response);
        }
    }
    
    com.ithows.PageManager.moveViewPage(view, pageContext, request, response);
    //여기에 있는 내용은 모두 com.dreamct.controller.*Controller.java 파일에 있습니다.
    //여기에 있는 내용을 복구하기 위해서는 Rev 97번을 보면 된다.

%>