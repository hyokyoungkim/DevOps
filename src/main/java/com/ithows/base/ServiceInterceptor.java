/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ithows.base;


import com.ithows.util.HttpUtils;
import com.ithows.SessionInfo;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Interceptor
 * @author dreamct
 */
public class ServiceInterceptor {
    
    /*일단 여기서 로그인을 체킹한다.*/
    public static String checkLogin(HttpSession session, HttpServletRequest request) {
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        
        String view = null;
        if (!sInfo.getLogin()) {
            view = "redirect:" + request.getContextPath() + "/loginForm.do";
        }else{
            // 만약 회원의 상태, 유저의 상태에 따라 강제 이동하고 싶다면 아래 부분을 코딩
            // ** 지우지 말것 
            /*  //////////////////////////////////////////////////////////////////
            Map map = sInfo.getMemberInfo();
            ResultMap map2 = new ResultMap();
            map2.putAll(map);
            int infoBillingStatus = map2.getInt("infoBillingStatus");
            if (infoBillingStatus == 4 || infoBillingStatus == 5 || infoBillingStatus == 7 || infoBillingStatus == 8) {
                view = "redirect:" + request.getContextPath() + "/billing/billingApproval.do";
            }
            /////////////////////////////////////////////////////////////////////   */
        }
        
        // null이면 외부의 처리에 맞긴다는 의미이다.
        return view;
    }

}