package com.ithows;

import com.ithows.base.AppProperties;
import java.net.URL;
import java.util.Iterator;
import java.util.Set;
import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

/**
 * Web Application의 제어
 * 
 */
public class AppConfig extends HttpServlet {

    public static AppProperties properties = null; //static으로 myconf를 생성

    static {
        loadAppConfig();
    }

    public static void loadAppConfig() {
        try {
            String OS = System.getProperty("os.name").toLowerCase();
            URL url = AppConfig.class.getResource("/");//class객체의 getResource함수를 이용 경로를 받아냄 디렉토리는 /로 구분
            String configpath;

            if (OS.indexOf("win") >= 0) {
                configpath = url.toString().replaceAll("WEB-INF/classes/", "WEB-INF/classes/configplatform.xml");   //진짜 파일의 경로를 얻어낸다. -- @@ 윈도우즈 적용 
                configpath = configpath.replaceAll("file:/", ""); //경로를 얻어내며 생긴file:/을 제거한다
            } else {
//                configpath = "/usr/share/tomcat/webapps/Coplein/WEB-INF/classes/configplatform.xml";// configplatform.xml 경로를 얻어낸다. -- @@ 리눅스 적용 
                configpath = "/" +  AppConfig.class.getResource("/").toString().replaceAll("file:/", "") + "configplatform.xml" ; //  @@ 리눅스 용 

            }

            properties = new AppProperties(configpath);  //XML파일 로딩시켜준다.       
            
            
        } catch (Exception e) {
            System.out.println(e.getLocalizedMessage() + "\n AppConfig static 영영");
        }
        
    }
    
    //APP_CONFIG 전용 메서드
    public static AppProperties getAppConfig() {
        return properties; //자신을 돌려주는 메서드
    }

    public static String getConf(String key) {//키를 받고 그에 해당하는 값을 얻어냄 myconf에서
        if (properties != null) {
            return properties.getString(key);
        } else {
            return null;
        }
    }

    public static int getConfInt(String key) {//키를 받고 그에 해당하는 값을 얻어냄 myconf에서
        String str = AppConfig.getConf(key);
        int result = -1;
        if (str != null) {
            try {
                result = Integer.parseInt(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        } else {
            return -1;
        }
    }
    public static double getConfDouble(String key) {//키를 받고 그에 해당하는 값을 얻어냄 myconf에서
        String str = AppConfig.getConf(key);
        double result = -1;
        if (str != null) {
            try {
                result = Double.parseDouble(str);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return result;
        } else {
            return -1;
        }
    }

    public static void deleteConf(String key) {//키를 받고 해당 값을 삭제 하고 save
        properties.remove(key);
        if (application != null) {
            application.removeAttribute(key);
        }
        properties.saveProperties();
    }

    public static void setConf(String key, String name) {//키와 벨류값을 저장하고 세이브
        properties.put(key, name);//메모리로만 저장
        if (application != null) {
            application.setAttribute(key, name);
        }
        properties.saveProperties();
    }
    private static ServletContext application = null;

    @Override
    public void init(ServletConfig config) throws ServletException {
        AppConfig.application = config.getServletContext();
        setAppConfigToServletContext();
    }

    private static void setAppConfigToServletContext() {
        if (properties != null && application != null) {
            Set states = properties.keySet(); // get set-view of keys 
            Iterator<String> itr = states.iterator();
            while (itr.hasNext()) {
                String key = itr.next();
                String value = properties.getString(key);
                application.setAttribute(key, value);
            }
        }
    }
    
    public static String getContextPath(){
        String OS = System.getProperty("os.name").toLowerCase();
        if (OS.indexOf("win") >= 0) {
            return  getConf("context_win_dir");
        } else {
            return getConf("context_dir");
        }
    }
}
