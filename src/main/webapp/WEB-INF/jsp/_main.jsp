<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="com.ithows.*,com.ithows.base.*"%>
<%@ page import="com.ithows.util.HttpUtils" %>
<%
    /* API 호출시 가장 먼저 실행 - (1) */

    PageBean pageBean = (PageBean) request.getAttribute("pageBean");
    String pageId = pageBean.getId();
    SessionInfo sInfo = HttpUtils.getSessionInfo(session);

    String view = null;

    // System.out.println("1>>>>>>>>>>>>>> pageId = " + pageId);
    // System.out.println("request = " + request.getAttribute("paramId"));
    
    
    // 로그인 세션 체크  
    if (pageId.indexOf("login.do") != -1 || pageId.indexOf("usermap.do") != -1
            || pageId.indexOf("main.do") != -1 || pageId.indexOf("loginForm.do") != -1
            || pageId.indexOf("updateAP.do") != -1 || pageId.indexOf("testmap.do") != -1
            || pageId.indexOf("syncDateTime.do") != -1 || pageId.indexOf("clearSiglog.do") != -1
            || pageId.indexOf("master_main.do") != -1 || pageId.indexOf("testmap2.do") != -1
            || pageId.indexOf("master_main.do") != -1 || pageId.indexOf("studyList.do") != -1
//            이하 테스트용
            || pageId.indexOf("getUserInfo.do") != -1 || pageId.indexOf("loginrc.do") != -1
            || pageId.indexOf("updateAPList.do") != -1 || pageId.indexOf("findPassword.do") != -1
            || pageId.indexOf("updateAPList.do") != -1 || pageId.indexOf("logout.do") != -1
            || pageId.indexOf("join.do") != -1 || pageId.indexOf("loginlg.do") != -1
            || pageId.indexOf("upload.do") != -1 || pageId.indexOf("delete.do") != -1
            || pageId.indexOf("error.do") != -1
            
//            이하 페이지 관련
            || pageId.indexOf("main.do") != -1 || pageId.indexOf("join.do") != -1
            || pageId.indexOf("login.do") != -1 || pageId.indexOf("admin_album.do") != -1
            || pageId.indexOf("admin_board.do") != -1 || pageId.indexOf("admin_potalPage.do") != -1
            || pageId.indexOf("admin_user.do") != -1 || pageId.indexOf("albumList.do") != -1
            || pageId.indexOf("albumViewDetail_scroll.do") != -1 || pageId.indexOf("albumViewDetail_slider.do") != -1
            || pageId.indexOf("modifyAlbum.do") != -1 || pageId.indexOf("board_notice.do") != -1
            || pageId.indexOf("board_notice_create.do") != -1 || pageId.indexOf("board_notice_view.do") != -1
            || pageId.indexOf("board_qna.do") != -1 || pageId.indexOf("board_qna_create.do") != -1
            || pageId.indexOf("board_qna_view.do") != -1 || pageId.indexOf("common_header.do") != -1
            || pageId.indexOf("createAlbum.do") != -1 || pageId.indexOf("findId.do") != -1
            || pageId.indexOf("findPw.do") != -1 || pageId.indexOf("serviceInformation.do") != -1
            || pageId.indexOf("myAlbumComment.do") != -1 || pageId.indexOf("myAlbumList.do") != -1
            || pageId.indexOf("myPage_userInfo.do") != -1 || pageId.indexOf("myPage_userPw.do") != -1
            || pageId.indexOf("myReading.do") != -1
    ) {  //얘만 예외로 풀어준다.

        System.out.println("Session Not Needed");
        
    } else {  // 로그인 세션이 없으면 --> 리다이렉션이 바로 걸림
        view = ServiceInterceptor.checkLogin(session, request);
        System.out.println("Session Need");

    }

    // 여기서는 필터링 조건을 주고 이동 페이지에 대한 처리는 Interceptor에서 한다. 
    if (view == null) {  //로그인이 되었다면
        // 호출 pageBean을 통해 Id(url)을 결정하고 컨트롤러 함수 실행, view (jsp 파일) 결정 - (2)
        view = PageManager.callController(pageContext, session, request, response);

    }else{
//        // @@ 개발시 잠시 풀어 둘 때
//        view = PageManager.callController(pageContext, session, request, response);
    } 


    // view에 지정된 것에 따라 이동 - (3)
    com.ithows.PageManager.moveViewPage(view, pageContext, request, response);

%>