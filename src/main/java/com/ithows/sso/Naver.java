package com.ithows.sso;

import com.ithows.AppConfig;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONException;
import org.json.JSONObject;

public class Naver implements LoginHelper{
    private static final String CLIENT_ID = AppConfig.getConf("naver_client_key");
    private static final String CLIENT_SECRET = AppConfig.getConf("naver_client_secret_key");
    private static final String REDIRECT_URI = AppConfig.getConf("naver_redirect_url");
    
    private static final String AUTH_SERVER_HOST = AppConfig.getConf("naver_auth_server_host");
    private static final String LOGIN_PATH = AppConfig.getConf("naver_login_path");
    private static final String ACCESS_TOKEN_PATH = AppConfig.getConf("naver_access_token_path");
    
    private static final String API_SERVER_HOST = AppConfig.getConf("naver_api_server_host");
    private static final String USER_INFO_PATH = AppConfig.getConf("naver_user_info_path");

    // Authorization 입력 (access-token 사용)
    private static final List<String> authPaths = new ArrayList<String>();
    
    static {
        authPaths.add(USER_INFO_PATH);
    }
    
    private String accessToken;
    
    
    /**
     * 네이버 로그인 URL 가져오기
     * @return  네이버 로그인 URL    
     */
    @Override
    public String getLoginURL() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s?", AUTH_SERVER_HOST + LOGIN_PATH));
        sb.append(String.format("%s=%s", "response_type", "code"));
        sb.append(String.format("&%s=%s", "&client_id", CLIENT_ID));
        sb.append(String.format("&%s=%s", "redirect_uri", REDIRECT_URI));
        sb.append(String.format("&%s=%s", "state", "NAVER"));
        
        return sb.toString();
    }
    
    
    /**
     * 토큰 받기
     * @param code  인가 Code
     * @return      {
     *                  "access_token":"AAAAN1qgQ5bz8XVxMHZpq6ozXw6SBEiJdmNgJ485qo2uby3R8m-g6TdRsArX1bcykbKWGPPIQbKufrtTKmexOLDd-zI",
     *                  "refresh_token":"CVs7NWViiWD4DRewJ5GBR5Mb9h3lXtwhz4w54MNgNVDN1CNvpTcHEqEyOwapistGxCcAct8U7isrisZbYdELW9CiscewrnQkxaMlipg7aH8Rj9AYsie",
     *                  "token_type":"bearer",
     *                  "expires_in":"3600"
     *              }
     */
    @Override
    public JSONObject getAccessToken(String code) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s=%s", "grant_type", "authorization_code"));
        sb.append(String.format("&%s=%s", "client_id", CLIENT_ID));
        sb.append(String.format("&%s=%s", "client_secret", CLIENT_SECRET));
        sb.append(String.format("&%s=%s", "code", code));
        
        return request(HttpMethod.POST, AUTH_SERVER_HOST, ACCESS_TOKEN_PATH, sb.toString());
    }

    
    /**
     * 사용자 정보 가져오기
     * @param accessToken
     * @return              {
     *                          "response":{
     *                              "gender":"M",
     *                              "nickname":"nickname",
     *                              "name":"name",
     *                              "id":"4nKZoCFLlEJlhoCIRJRu3ozwBA0_tvZdCuae6LlOa08",
     *                              "email":"test@naver.com"
     *                          },
     *                          "resultcode":"00",
     *                          "message":"success"
     *                      }
     */
    @Override
    public JSONObject getUserInfo(String accessToken) {
        this.accessToken = accessToken;
        
        return request(HttpMethod.GET, API_SERVER_HOST, USER_INFO_PATH);
    }

    
    @Override
    public JSONObject request(HttpMethod httpMethod, String host, String apiPath) {
        return request(httpMethod, host, apiPath, null);
    }
    

    @Override
    public JSONObject request(HttpMethod httpMethod, String host, String apiPath, String params) {
        String requestURI = host + apiPath;
        
        if (params != null && params.length() > 0 && httpMethod == HttpMethod.GET) {
            requestURI += params;
        }
        
        try {
            URL url = new URL(requestURI);
            
            HttpsURLConnection connection = (HttpsURLConnection) url.openConnection();
            connection.setRequestMethod(httpMethod.toString());
            
            if (authPaths.contains(apiPath)) {
                connection.setRequestProperty("Authorization", "Bearer " + accessToken);
            }
            
            if (params != null && params.length() > 0 && httpMethod == HttpMethod.POST) {
                connection.setDoOutput(true);
                OutputStreamWriter OSW = new OutputStreamWriter(connection.getOutputStream(), "UTF-8");
                OSW.write(params);
                OSW.flush();
                OSW.close();    
            }
            
            BufferedReader br;
            int status = connection.getResponseCode();
            if (status == 200) {
                br = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            } else {
                br = new BufferedReader(new InputStreamReader(connection.getErrorStream(), "UTF-8"));
            }
            
            String str;
            StringBuilder sb = new StringBuilder();
            while ((str = br.readLine()) != null) {
                sb.append(str);
            }
            
            return new JSONObject(sb.toString());
        } catch(Exception e) {
            e.printStackTrace();
        }
        
        return null;
    }

    
    /**
     * 로그인
     * @param code  인가 Code
     * @return      {
     *                  "gender":"M",
     *                  "nickname":"nickname",
     *                  "name":"name",
     *                  "id":"4nKZoCFLlEJlhoCIRJRu3ozwBA0_tvZdCuae6LlOa08",
     *                  "email":"test@naver.com"
     *              }
     * @throws JSONException 
     */
    @Override
    public JSONObject login(String code) throws JSONException {
        JSONObject authObj = this.getAccessToken(code);
        JSONObject userObj = null;
        
        if (authObj != null && authObj.has("access_token")) {
            String token = authObj.getString("access_token");
            userObj = this.getUserInfo(token);
            
            // 필요한 JSONObject만 사용
            JSONObject responseObj = userObj.has("response")? userObj.getJSONObject("response") : null;
            userObj = responseObj;
        }
                
        return userObj;
    }
}
