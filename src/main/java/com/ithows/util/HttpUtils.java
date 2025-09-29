/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ithows.util;


import com.ithows.AppConfig;
import com.ithows.BaseDebug;
import com.ithows.SessionInfo;
import com.ithows.util.string.UtilString;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.URL;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.servlet.ServletContext;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONException;
import org.json.JSONObject;


public class HttpUtils {

    public static SessionInfo getSessionInfo(HttpSession session) {
        SessionInfo sInfo = null;
        Object obj = session.getAttribute("sessionInfo");
        if (obj != null) {  //로그인 정보있을 경우 세션의 회원 정보를 저장한다
            sInfo = (SessionInfo) obj;
        } else {  //세션의 정보가 없을경우 새로운 세션정보를 만든다
            sInfo = new SessionInfo();
            session.setAttribute("sessionInfo", sInfo);
        }
        return (SessionInfo) sInfo;
    }

    public static long getParameterLong(HttpServletRequest request, String param) {
        return getParameterLong(request, param, 0);
    }

    public static long getParameterLong(HttpServletRequest request, String param, int defaultValue) {
        long result = defaultValue;
        String _no = request.getParameter(param);
        if (_no != null && !_no.isEmpty()) {
            try {
                result = Long.parseLong(_no);
            } catch (Exception e) {
                result = defaultValue;
                BaseDebug.log(e);
            }
        }
        return result;
    }

    public static int getParameterInt(HttpServletRequest request, String param) {
        return getParameterInt(request, param, 0);
    }

    public static int getParameterInt(HttpServletRequest request, String param, int defaultValue) {
        int result = defaultValue;
        String _no = request.getParameter(param);
        if (_no != null && !_no.isEmpty()) {
            try {
                result = Integer.parseInt(_no);
            } catch (Exception e) {
                result = defaultValue;
                BaseDebug.log(e);
            }
        }
        return result;
    }

    public static double getParameterDouble(HttpServletRequest request, String param) {
        return getParameterDouble(request, param);
    }
    public static double getParameterDouble(HttpServletRequest request, String param, double defaultValue) {
        double result = defaultValue;
        String _no = request.getParameter(param);
        if (_no != null && !_no.isEmpty()) {
            try {
                result = Double.parseDouble(_no);
            } catch (Exception e) {
                result = defaultValue;
                BaseDebug.log(e);
            }
        }
        return result;
    }

    public static String getParameterString(HttpServletRequest request, String param, String defaultValue) {
        String result = defaultValue;
        String data = request.getParameter(param);
        if (data == null) {
            result = defaultValue;
        } else {
            result = data;
        }
        return result;
    }

    public static boolean isParamEmpty(String param) {
        if (param != null && !param.equals("undefined") && !param.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    public static boolean isParamValid(String param) {
        return !isParamEmpty(param);
    }

    public static boolean getParameterBoolean(HttpServletRequest request, String param) {
        boolean result = false;
        String _pa = request.getParameter(param);

        if (_pa != null && !_pa.isEmpty()) {
            try {
                result = Boolean.parseBoolean(_pa);
            } catch (Exception e) {
                BaseDebug.log(e);
            }
        }
        return result;
    }

    public static Cookie getCookie(HttpServletRequest request, String name) {
        Cookie[] cookies = request.getCookies();
        Cookie returnCookie = null;
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                if (cookies[i].getName().equals(name)) {
                    returnCookie = cookies[i];
                    break;
                }
            }
        }
        return returnCookie;
    }

    
    /**
     * HttpUtils.getBody(request)로 보내려면 클라이언트에서 
     * data: JSON.stringify(form_data),
     * contentType: "application/json",
     * 형식으로 보내야 한다.
     */
    // 'application/json' request에 대해 파라미터를 받기 위해 반드시 필요한 함수
    public static String getBody(HttpServletRequest request) throws IOException {

        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                
                char[] charBuffer = new char[2048];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        body = stringBuilder.toString();
        
        return body;
    }
    
    /**
     * HttpUtils.getBody(request)로 보내려면 클라이언트에서 
     * data: JSON.stringify(form_data),
     * contentType: "application/json",
     */
    // 'application/json' request에 대해 파라미터를 받기 위해 반드시 필요한 함수
    public static String getBodyUTF(HttpServletRequest request) throws IOException {

        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                
                char[] charBuffer = new char[2048];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            }
        } catch (IOException ex) {
            throw ex;
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    throw ex;
                }
            }
        }

        body = stringBuilder.toString();
        body = UtilString.decodeUrl(body, "UTF-8");   // @@ 페이지가 utf-8이 아닌 경우 인코딩을 수정해야 함
        
        return body;
    }
    
    // datatype이 json이 아닌 경우에도 json 객체로 받음 
    public static JSONObject getBodyJsonUTF(HttpServletRequest request) {

        JSONObject jObj = null;
        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                char[] charBuffer = new char[2048];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        
        body = stringBuilder.toString();
        body = UtilString.decodeUrl(body, "UTF-8");   // @@ 페이지가 utf-8이 아닌 경우 인코딩을 수정해야 함

        if(body == null || body.equals("")){
            return jObj;
        }
        
        
        try{
            jObj = new JSONObject(body);
        }catch(Exception ex1){
            try{
                jObj = mapStringToJson(body);
            }catch(Exception ex2){ 
                jObj = null;
            }
        }        
        
        
        return jObj;
    }
    
    // datatype이 json이 아닌 경우에도 json 객체로 받음 
    // UTF 인코딩을 안 함
    public static JSONObject getBodyJson(HttpServletRequest request) {

        JSONObject jObj = null;
        String body = null;
        StringBuilder stringBuilder = new StringBuilder();
        BufferedReader bufferedReader = null;

        try {
            InputStream inputStream = request.getInputStream();
            if (inputStream != null) {
                bufferedReader = new BufferedReader(new InputStreamReader(inputStream, "UTF-8"));
                char[] charBuffer = new char[2048];
                int bytesRead = -1;
                while ((bytesRead = bufferedReader.read(charBuffer)) > 0) {
                    stringBuilder.append(charBuffer, 0, bytesRead);
                }
            }
        } catch (IOException ex) {
            ex.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                try {
                    bufferedReader.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        
        body = stringBuilder.toString();
        
        if(body == null || body.equals("")){
            return jObj;
        }
        
        try{
            jObj = new JSONObject(body);
        }catch(Exception ex1){
            try{
                jObj = mapStringToJson(body);
            }catch(Exception ex2){ 
                jObj = null;
            }
        }        
        
        return jObj;
    }

//    public static JSONObject mapStringToJson(String paramIn) {
//        
//        paramIn = paramIn.replaceAll("=", "\":\"");
//        paramIn = paramIn.replaceAll("&", "\",\"");
//        
//        try {
//            JSONObject jObj = new JSONObject(paramIn);
//        } catch (JSONException ex) {
//            Logger.getLogger(HttpUtils.class.getName()).log(Level.SEVERE, null, ex);
//        }
//        
//        return "{\"" + paramIn + "\"}";
//    }
    
    private static JSONObject mapStringToJson(String payload) throws Exception{
           JSONObject jObj = new JSONObject();
           payload = payload.replace("{", "");
           payload = payload.replace("}", "");

            String[] keyVals = payload.split(", ");
            for(String keyVal:keyVals)
            {
              String[] parts = keyVal.split("=",2);

              if(UtilString.isValidNumeric(parts[1]) == 1){
                  jObj.put(parts[0], Integer.parseInt(parts[1]));
              }else if(UtilString.isValidNumeric(parts[1]) == 2){
                  jObj.put(parts[0], Double.parseDouble(parts[1]));

              }else{

                 jObj.put(parts[0],parts[1]);
              }

            }
            
           return jObj;
           
    }
    
    
//    public static String sendTextFileToClient(HttpServletRequest request, HttpServletResponse response, String sendFilePath) throws Exception {
//        request.setCharacterEncoding("UTF-8");
//
//        ServletContext sc = request.getServletContext();
//
////        String imagePath = imgFilePath;
//        File sendFile = new File(URLDecoder.decode(sendFilePath, "UTF-8"));
//
//        if (!sendFile.exists()) {
//            response.sendError(HttpServletResponse.SC_NOT_FOUND);
//            System.out.println(">>>> " + sendFilePath);
//            return "No file";
//        }
//
//        String contentType = sc.getMimeType(sendFile.getName());
//        response.reset();
//        response.setContentType(contentType);
//        response.setHeader("Content-Length", String.valueOf(sendFile.length()));
//
//        FileInputStream in = new FileInputStream(sendFile);
//        OutputStream out = response.getOutputStream();
//
//        byte[] buf = new byte[4096];
//        int count = 0;
//        while ((count = in.read(buf)) >= 0) {
//            out.write(buf, 0, count);
//        }
//        out.close();
//        in.close();
//
//        return "send file";
//
//    }
    
    // 1. 파일로 보내기
    // HttpUtils.sendBinaryFileToClient(request, response, path);
    public static void sendBinaryFileToClient(HttpServletRequest request, HttpServletResponse response, String sendFileFullName) throws IOException {
		
            String filePath = FilenameUtils.getFullPath(sendFileFullName);
            String realFilNm = FilenameUtils.getName(sendFileFullName);
            String viewFileNm = realFilNm;

            File file = new File( filePath + realFilNm);

            if (file.exists() && file.isFile()) {
                    response.setContentType("application/octet-stream; charset=utf-8");
                    response.setContentLength((int) file.length());
                    String browser = getBrowser(request);
                    String disposition = getDisposition(viewFileNm, browser);
                    response.setHeader("Content-Disposition", disposition);
                    response.setHeader("Content-Transfer-Encoding", "binary");
                    OutputStream out = response.getOutputStream();
                    FileInputStream fis = null;
                    fis = new FileInputStream(file);

                    int byteCount = 0;
                    byte[] buffer = new byte[4096];
                    int bytesRead;
                    while ((bytesRead = fis.read(buffer)) != -1) {
                            out.write(buffer, 0, bytesRead);
                            byteCount += bytesRead;
                    }
                    out.flush();


                    if (fis != null)
                            fis.close();
                    out.flush();
                    out.close();
            }
	}
    
    
    public static String sendImageInImageDir(HttpSession session, HttpServletRequest request, HttpServletResponse response, String imgFilePath) throws IOException {

        ServletContext sc = request.getServletContext();
        String servletPath = session.getServletContext().getRealPath("/");
        String imagePath = servletPath + AppConfig.getConf("user_image_dir");

//        String imagePath = "C:\\project\\APCollector\\web\\images";
//        String [] fragmentFilename = imgFilePath.split("/");
//        //Check if sendFile isn't set
//        if(fragmentFilename.length < 2){
//            return "No file name";
//        }
//        String filename = fragmentFilename[fragmentFilename.length-1];
//        String requestedImage = "/"+filename;
//
//        if(filename == null){
//            response.sendError(HttpServletResponse.SC_NOT_FOUND);
//            return "No file";
//        }
        File image = new File(imagePath, URLDecoder.decode(imgFilePath, "UTF-8"));

        if (!image.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            return "No file";
        }

        String contentType = sc.getMimeType(image.getName());
        response.reset();
        response.setContentType(contentType);
        response.setHeader("Content-Length", String.valueOf(image.length()));

        FileInputStream in = new FileInputStream(image);
        OutputStream out = response.getOutputStream();
        // Files.copy(sendFile.toPath(), out);

        byte[] buf = new byte[1024];
        int count = 0;
        while ((count = in.read(buf)) >= 0) {
            out.write(buf, 0, count);
        }
        out.close();
        in.close();

        return "send image";
    }

    // 이미지 파일 보내기
    public static String sendImage(HttpServletRequest request, HttpServletResponse response, String imgFilePath) throws IOException {

        ServletContext sc = request.getServletContext();

//        String imagePath = imgFilePath;
        File image = new File(URLDecoder.decode(imgFilePath, "UTF-8"));

        if (!image.exists()) {
            response.sendError(HttpServletResponse.SC_NOT_FOUND);
            System.out.println(">>>> " + imgFilePath);
            return "No file";
        }

        String contentType = sc.getMimeType(image.getName());
        response.reset();
        response.setContentType(contentType);
        response.setHeader("Content-Length", String.valueOf(image.length()));

        FileInputStream in = new FileInputStream(image);
        OutputStream out = response.getOutputStream();
        // Files.copy(sendFile.toPath(), out);

        byte[] buf = new byte[1024];
        int count = 0;
        while ((count = in.read(buf)) >= 0) {
            out.write(buf, 0, count);
        }
        out.close();
        in.close();

        return "send image file";
    }

    
    // 2. 버퍼이미지로 보내기
//        BufferedImage bimage = ImageUtils.getBufferedImage(path);
//        if(bimage != null){
//            HttpUtils.sendCanvas(session, request, response, bimage);
//        }
    public static String sendCanvas(HttpSession session, HttpServletRequest request, HttpServletResponse response, BufferedImage image) throws IOException {

        ServletContext sc = request.getServletContext();

        String servletPath = session.getServletContext().getRealPath("/");
//        String imagePath = servletPath + AppConfig.getConf("temp_dir");

        response.setContentType("image/png");

        OutputStream os = response.getOutputStream();
        ImageIO.write(image, "png", os);

        os.close();
        return "send image";
    }
    // request의 헤더 정보를 맵에 담음
    public static Map<String, String> getRequestHeadersInMap(HttpServletRequest request) {

        Map<String, String> result = new HashMap<>();

        Enumeration headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String key = (String) headerNames.nextElement();
            String value = request.getHeader(key);
            result.put(key, value);
        }

        return result;
    }

    // request로 들어온 클라이언트의 IP를 얻음
    public static String getClientIp(HttpServletRequest request) {

        String remoteAddr = "";

        if (request != null) {
            remoteAddr = request.getHeader("X-FORWARDED-FOR");
            if (remoteAddr == null || "".equals(remoteAddr)) {
                remoteAddr = request.getRemoteAddr();
            }
        }

        return remoteAddr;
    }


     // 기기의 외부 IP을 얻음
     // 외부와 연결되어 있어야 함
    public static String getPublicIP(){

        String ip = "";
        URL whatismyip = null;
        
        try {
            whatismyip = new URL("http://checkip.amazonaws.com");
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    whatismyip.openStream()));

            ip = in.readLine(); //you get the IP as a String
        } catch (Exception e) {
            e.printStackTrace();
        }

        return ip;
    }

    // URL에서 IP를 얻어냄
    public static String[] getIPAddressFromURL(String urlAddr) throws Exception {

        //대기업들은 서버의 과부하를 막기 위해서 하나의 도메인에 여러 개의 컴퓨터 IP를 등록할 수 있습니다. 
        InetAddress[] iaArr = InetAddress.getAllByName(urlAddr);

        String[] addrArr = new String[iaArr.length];

        for(int i=0; i<iaArr.length ; i++) {
            addrArr[i] = iaArr[i].getHostAddress();
            System.out.println("" + i + " : " + addrArr[i] );
        }

        return addrArr;
    }
    
    
    /**
     * Comment : 현재 페이지의 서블릿 URL 전체 경로를 추출.
     */
    public static String getCurrentlyURL(HttpServletRequest req) {
        Enumeration<?> param = req.getParameterNames();

        StringBuffer strParam = new StringBuffer();
        StringBuffer strURL = new StringBuffer();

        if (param.hasMoreElements()) {
            strParam.append("?");
        }

        while (param.hasMoreElements()) {
            String name = (String) param.nextElement();
            String value = req.getParameter(name);

            strParam.append(name + "=" + value);

            if (param.hasMoreElements()) {
                strParam.append("&");
            }
        }

        String url;
        if (req.getAttribute("javax.servlet.forward.request_uri") == null) {
            url = req.getRequestURI().toString();
        } else {
            url = req.getAttribute("javax.servlet.forward.request_uri").toString();
        }
        // contextPath 제거, 필요한 값(/index.do)
        url = url.replace(req.getContextPath(), "");

        //# URL 에서 URI 를 제거, 필요 값만 사용(프로토콜, 호스트, 포트)
        String getUrl = req.getRequestURL().toString().replace(req.getRequestURI(), "");
        strURL.append(getUrl);
        strURL.append(url); // servlet 경로 : /index.do 
        strURL.append(strParam); // getQueryString() 값

        // 전체 추출 경로 : http://www.aaa.co.kr/index.do?type=aaa(쿼리스트링)
        return strURL.toString();
    }

    
    // IP 주소에서 호스트 이름을 얻음
    public static String getHostName(String ipStr){
        InetAddress ip;
        String hostname = "";
        String temp = "";
        try {
            ip = InetAddress.getByName(ipStr);
            hostname = ip.getHostName();
            
            StringTokenizer token = new StringTokenizer(hostname);
            temp = token.nextToken(".");
            
            if(Integer.parseInt(temp) >= 0){
                return hostname;
            }
            
//            System.out.println("Your current IP address : " + ip);
//            System.out.println("Your current Hostname : " + hostname);
 
        } catch (UnknownHostException e) {
            e.printStackTrace();
        } catch (NumberFormatException e) {
            hostname = temp;
        }
        return hostname;
    }
    
    public static HashMap getNameIpMap(HashMap<String, String> macIpMap){
        HashMap<String, String> nameIpMap = new HashMap();
        
        for(String key : macIpMap.keySet()){
            nameIpMap.put(getHostName(macIpMap.get(key)), macIpMap.get(key));
        }
        return nameIpMap;
    }
    
    public static JSONObject getClientInfo(HttpServletRequest request){
        JSONObject jObj = null;
        try {
            jObj = new JSONObject();
            String host = request.getRemoteHost();
            String ip = HttpUtils.getClientIp(request);
            String browser = HttpUtils.getBrowser(request);
            String uAgent = request.getHeader("User-Agent").toLowerCase();
            String connectDevice = "";
            if (DeviceChecker.getDeviceType(uAgent) == DeviceChecker.PC) {
                connectDevice = "PC";
            } else {
                connectDevice = "Mobile";
            }
            
            jObj.put("Host", host);
            jObj.put("IP", ip);
            jObj.put("browser", browser);
            jObj.put("User-Agent", uAgent); // python-urllib
            jObj.put("device", connectDevice);
            

        } catch (JSONException ex) {
            System.out.println(ex.getLocalizedMessage());
            jObj = null;
        }
        return jObj;
    }
    
    
    public static String getBrowser(HttpServletRequest request) {
            String header = request.getHeader("User-Agent");
            if (header.indexOf("MSIE") > -1 || header.indexOf("Trident") > -1)
                    return "MSIE";
            else if (header.indexOf("Chrome") > -1)
                    return "Chrome";
            else if (header.indexOf("Opera") > -1)
                    return "Opera";
            else if (header.indexOf("Firefox") > -1)
                return "Firefox";
        return "Others";            
    }

    public static String getDisposition(String filename, String browser) throws UnsupportedEncodingException {
            String dispositionPrefix = "attachment;filename=";
            String encodedFilename = null;
            if (browser.equals("MSIE")) {
                    encodedFilename = URLEncoder.encode(filename, "UTF-8").replaceAll(
                                    "\\+", "%20");
            } else if (browser.equals("Firefox")) {
                    encodedFilename = "\""
                                    + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
            } else if (browser.equals("Opera")) {
                    encodedFilename = "\""
                                    + new String(filename.getBytes("UTF-8"), "8859_1") + "\"";
            } else if (browser.equals("Chrome")) {
                    StringBuffer sb = new StringBuffer();
                    for (int i = 0; i < filename.length(); i++) {
                            char c = filename.charAt(i);
                            if (c > '~') {
                                    sb.append(URLEncoder.encode("" + c, "UTF-8"));
                            } else {
                                    sb.append(c);
                            }
                    }
                    encodedFilename = sb.toString();
            }
            return dispositionPrefix + encodedFilename;
    }
    
    
    
}
