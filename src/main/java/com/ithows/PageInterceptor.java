/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ithows;

import com.ithows.util.HttpUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;


public class PageInterceptor {

    public static String checkWorkAuth(HttpSession session, HttpServletRequest request) {
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        String view = null;
        if (!sInfo.getLogin()) {
            view = "redirect:" + request.getContextPath();
        } else {
            view = PageInterceptor.checkWorkAuthCeoStaff(sInfo);
        }
        return view;
    }

    /**
     * 일반적인 곳의 workAuthCeo
     *
     * @param sInfo
     * @return
     */
    private static String checkWorkAuthCeoStaff(SessionInfo sInfo) {
        String view = null;
        
    
        return view;
    }

}