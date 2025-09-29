package com.ithows.sso;

import org.json.JSONException;
import org.json.JSONObject;

public interface LoginHelper {
    public enum HttpMethod {GET, POST}
    
    public String getLoginURL();
    
    public JSONObject getAccessToken(String code);
    public JSONObject getUserInfo(String accessToken);
    
    public JSONObject request(HttpMethod httpMethod, String host, String apiPath);
    public JSONObject request(HttpMethod httpMethod, String host, String apiPath, String params);
    
    public JSONObject login(String code) throws JSONException;
}
