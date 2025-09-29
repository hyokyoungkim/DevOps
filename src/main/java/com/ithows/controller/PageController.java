/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.ithows.controller;

import com.ithows.*;
import com.ithows.util.HttpUtils;
import com.ithows.base.ControllerClassInfo;
import com.ithows.base.ControllerMethodInfo;
import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 *
 * 
 *
 */
@ControllerClassInfo(controllerPage = "/_main.jsp")
public class PageController {

    static {
        BaseDebug.info("----------------------" + PageController.class.toString() + " Loading!!");

    }
    
    
    @ControllerMethodInfo(id = "/main.do")
    public String main(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        return "/main.jsp";
    }
    
    @ControllerMethodInfo(id = "/join.do")
    public String join(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        return "/join.jsp";
    }
    
    @ControllerMethodInfo(id = "/login.do")
    public String login(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        return "/login.jsp";
    }
    
//    admin 화면 전환
    @ControllerMethodInfo(id = "/page/admin_album.do")
    public String admin_album(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        return "/admin/admin_album.jsp";
    }
    
    @ControllerMethodInfo(id = "/page/admin_board.do")
    public String admin_board(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        return "/admin/admin_board.jsp";
    }
    
    @ControllerMethodInfo(id = "/page/admin_potalPage.do")
    public String admin_potalPage(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        return "/admin/admin_potalPage.jsp";
    }
    
    @ControllerMethodInfo(id = "/page/admin_user.do")
    public String admin_user(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        return "/admin/admin_user.jsp";
    }
    
//    album 화면 전환
    @ControllerMethodInfo(id = "/page/albumList.do")
    public String albumList(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        
        return "/album/albumList.jsp";
    }
    
    @ControllerMethodInfo(id = "/page/albumViewDetail_scroll.do")
    public String albumViewDetail_scroll(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        return "/album/albumViewDetail_scroll.jsp";
    }
    
    @ControllerMethodInfo(id = "/page/albumViewDetail_slider.do")
    public String albumViewDetail_slider(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        return "/album/albumViewDetail_slider.jsp";
    }
    
    @ControllerMethodInfo(id = "/page/modifyAlbum.do")
    public String modifyAlbum(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        return "/album/modifyAlbum.jsp";
    }
    
//    board 화면 전환
    @ControllerMethodInfo(id = "/page/board_notice.do")
    public String board_notice(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        return "/board/board_notice.jsp";
    }
    
    @ControllerMethodInfo(id = "/page/board_notice_create.do")
    public String board_notice_create(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        return "/board/board_notice_create.jsp";
    }
    
    @ControllerMethodInfo(id = "/page/board_notice_view.do")
    public String board_notice_view(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        return "/board/board_notice_view.jsp";
    }
    
    @ControllerMethodInfo(id = "/page/board_qna.do")
    public String board_qna(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        return "/board/board_qna.jsp";
    }
    
    @ControllerMethodInfo(id = "/page/board_qna_create.do")
    public String board_qna_create(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        return "/board/board_qna_create.jsp";
    }
    
    @ControllerMethodInfo(id = "/page/board_qna_view.do")
    public String board_qna_view(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        return "/board/board_qna_view.jsp";
    }
    
//    potal 화면 전환
    @ControllerMethodInfo(id = "/page/common_header.do")
    public String common_header(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        return "/portal/common_header.jsp";
    }
    
    @ControllerMethodInfo(id = "/page/createAlbum.do")
    public String createAlbum(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        return "/portal/createAlbum.jsp";
    }
    
    @ControllerMethodInfo(id = "/page/findId.do")
    public String findId(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        return "/portal/findId.jsp";
    }
    
    @ControllerMethodInfo(id = "/page/findPw.do")
    public String findPw(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        return "/portal/findPw.jsp";
    }
    
    @ControllerMethodInfo(id = "/page/serviceInformation.do")
    public String serviceInformation(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        return "/portal/serviceInformation.jsp";
    }
    
//    user 화면 전환
    @ControllerMethodInfo(id = "/page/myAlbumComment.do")
    public String myAlbumComment(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        return "/user/myAlbumComment.jsp";
    }
    
    @ControllerMethodInfo(id = "/page/myAlbumList.do")
    public String myAlbumList(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        return "/user/myAlbumList.jsp";
    }
    
    @ControllerMethodInfo(id = "/page/myPage_userInfo.do")
    public String myPage_userInfo(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        return "/user/myPage_userInfo.jsp";
    }
    
    @ControllerMethodInfo(id = "/page/myPage_userPw.do")
    public String myPage_userPw(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        return "/user/myPage_userPw.jsp";
    }
    
    @ControllerMethodInfo(id = "/page/myReading.do")
    public String myReading(HttpSession session, HttpServletRequest request, HttpServletResponse response, Object command) {
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        return "/user/myReading.jsp";
    }
    
}
