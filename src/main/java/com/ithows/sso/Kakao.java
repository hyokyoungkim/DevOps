package com.ithows.sso;

import com.ithows.AppConfig;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import javax.net.ssl.HttpsURLConnection;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Kakao implements LoginHelper {
    private static final String CLIENT_ID = AppConfig.getConf("kakao_client_id");
    private static final String REDIRECT_URI = AppConfig.getConf("kakao_redirect_url");
    
    private static final String AUTH_SERVER_HOST = AppConfig.getConf("kakao_auth_server_host");
    private static final String LOGIN_PATH = AppConfig.getConf("kakao_login_path");
    private static final String ACCESS_TOKEN_PATH = AppConfig.getConf("kakao_access_token_path");
    
    private static final String API_SERVER_HOST = AppConfig.getConf("kakao_api_server_host");
    private static final String USER_INFO_PATH = AppConfig.getConf("kakao_user_info_path");
    
    // Authorization 입력 (access-token 사용)
    private static final List<String> authPaths = new ArrayList<String>();
    
    static {
        authPaths.add(USER_INFO_PATH);
    }
    
    private String accessToken;
 
    /**
     * 카카오 로그인 URL 가져오기
     * @return  카카오 로그인 URL
     */
    @Override
    public String getLoginURL() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s?", AUTH_SERVER_HOST + LOGIN_PATH));
        sb.append(String.format("%s=%s", "client_id", CLIENT_ID));
        sb.append(String.format("&%s=%s", "redirect_uri", REDIRECT_URI));
        sb.append(String.format("&%s=%s", "response_type", "code"));
        sb.append(String.format("&%s=%s", "state", "KAKAO"));
       
        return sb.toString();
    }
    
    
    /**
     * 토큰 받기
     * @param code  인가 Code
     * @return      {   
     *                  "access_token":"wucdXaKkDRa8jMYnvn-AdwXH7pkU5YsyyHGp8AorDNMAAAF--2ClRQ",
     *                  "refresh_token_expires_in":5183999,
     *                  "refresh_token":"Tc9sWACNI1L3hCxCl-eDv1uAafFaaVWV3wba-gorDNMAAAF--2ClRA",
     *                  "scope":"profile_nickname",
     *                  "token_type":"bearer",
     *                  "expires_in":21599
     *              }
     */
    @Override
    public JSONObject getAccessToken(String code) {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s=%s", "grant_type", "authorization_code"));
        sb.append(String.format("&%s=%s", "client_id", CLIENT_ID));
        sb.append(String.format("&%s=%s", "redirect_uri", REDIRECT_URI));
        sb.append(String.format("&%s=%s", "code", code));
        
        return request(HttpMethod.POST, AUTH_SERVER_HOST, ACCESS_TOKEN_PATH, sb.toString());
    }
    

    /**
     * 사용자 정보 가져오기
     * @param accessToken   
     * @return              {
     *                          "id": 0,
     *                          "connected_at":"2022-02-10T08:03:44Z",
     *                          "properties":{
     *                              "nickname":"nickname"
     *                          }
     *                      }
     */
    @Override
    public JSONObject getUserInfo(String accessToken) {
        this.accessToken = accessToken;
        
        JSONArray propertyArr = new JSONArray();
        propertyArr.put("properties.nickname");
        
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%s=%s", "property_keys", propertyArr.toString()));
        
        return request(HttpMethod.POST, API_SERVER_HOST, USER_INFO_PATH, sb.toString());
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
            
            connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            connection.setRequestProperty("charset", "utf-8");
            
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
     *                  "nickname":"nickname",
     *                  "id":0,
     *                  "connected_at":"2022-02-10T08:03:44Z"
     *              }
     * @throws JSONException 
     */
    @Override
    public JSONObject login(String code) throws JSONException{
        JSONObject authObj = this.getAccessToken(code);
        JSONObject userObj = null;
        
        if (authObj != null && authObj.has("access_token")) {
            String token = authObj.getString("access_token");
            userObj = this.getUserInfo(token);
            
            // 사용자 정보 JSONObject depth를 한 단계 낮춤
            JSONObject propertiesObj = userObj.has("properties")? (JSONObject) userObj.remove("properties") : new JSONObject();
            Iterator<String> keyset = propertiesObj.keys();
            while (keyset.hasNext()) {
                String key = keyset.next();
                
                userObj.put(key, propertiesObj.get(key));
            }
        }
        
        return userObj;
    }
}
