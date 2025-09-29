/*
 *  Copyright 2020. S.O.X. All rights reserved
 */

package com.ithows.base;

import com.ithows.ResultMap;
import com.ithows.SessionInfo;
import com.ithows.util.HttpUtils;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * Class AuthorityManager
 * 권한 관리를 위한 클래스
 * 
 * @author Roi Kim <S.O.X Co. Ltd.>
 */
public class AuthorityManager {

    public static final int ADMINMENU_ACCESS = 1;
    
    public static boolean checkTest(HttpSession session){
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        
         ResultMap map = sInfo.getMember();
         int userSecurityLevel = map.getInt("userSecurityLevel");
         if(userSecurityLevel > 0){
             return true;
         }
         return false;
    }
    
    public static boolean checkeUserType(HttpSession session){
        SessionInfo sInfo = HttpUtils.getSessionInfo(session);
        
        int userType = sInfo.getUserType() ;
        
        if(userType == 0){
             return true;
         }
         
         return false;
    }
    
    
}
