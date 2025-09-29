/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ithows;

import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class ResultMap extends HashMap implements Comparable<Object>{
    
    public static String compareField = "";

    public ResultMap() {

    }

    public ResultMap(Map map) {
        this.putAll(map);
    }

    public ResultMap getResultMap(String key) {
        Object obj = this.get(key);
        if (obj != null) {
            return (ResultMap)obj;
        } else {
            return null;
        }
    }
    
    public String getString(String key) {
        Object obj = this.get(key);
        if (obj != null) {
            return obj.toString();
        } else {
            return null;
        }
    }

    public int getInt(String key) {
        int defaultVal = -1;
        return getInt(key, defaultVal);
    }

    public int getInt(String key, int defaultVal) {
        Object obj = this.get(key);
        int tmp = defaultVal;
        if (obj != null) {
            try {
                tmp = Integer.parseInt(obj.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return tmp;
    }

    public double getDouble(String key) {
        double defaultVal = -1.0;
        return  getDouble(key, defaultVal);
    }

    public double getDouble(String key, double defaultVal) {
        Object obj = this.get(key);
        double tmp = defaultVal;
        if (obj != null) {
            try {
                tmp = Double.parseDouble(obj.toString());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return tmp;
    }

    
    public boolean getBoolean(String key) {
        boolean defaultVal = false;
        return  getBoolean(key, defaultVal);
    }
    
    public boolean getBoolean(String key, boolean defaultVal) {
        Object obj = this.get(key);
        boolean tmp = defaultVal;
        if (obj != null) {
            try {
                tmp = Boolean.parseBoolean(obj.toString());
            } catch (Exception ex) {
                try {
                    int val = Integer.parseInt(obj.toString());
                    if(val == 0){
                        tmp = false;
                    }else{
                        tmp = true;
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return tmp;
    }
    
    public JSONObject getJSONObject(String key) {
        Object obj = this.get(key);
        JSONObject jObj = null;
        
        if (obj != null) {
            try {
                jObj = new JSONObject(obj.toString());
            } catch (JSONException ex) {
                System.out.println(ex.getLocalizedMessage());
                return null;
            }
        } else {
            return null;
        }
        return jObj;
    }
    
    public JSONArray getJSONArray(String key) {
        Object obj = this.get(key);
        JSONArray jObj = null;
        
        if (obj != null) {
            try {
                jObj = new JSONArray(obj.toString());
            } catch (JSONException ex) {
                System.out.println(ex.getLocalizedMessage());
                return null;
            }
        } else {
            return null;
        }
        return jObj;
    }
    
    
    public void printElements(){
        for( Object key : this.keySet() ){
                System.out.println( "[ " + key + " ] : " + this.get(key)  );
        }
    }
    
    
    public void printInsertSQL(String tableName){
       
        System.out.println( "SQL : \n" + convertInsertSQL(tableName) );
    }
    
    public String convertInsertSQL(String tableName){
        String sqlStr = "INSERT INTO " + tableName + " " ;
        String fieldName = "";
        String fieldValue = "";
        int i=1;
        for( Object key : this.keySet() ){
            if(i == this.size()){
                fieldName = fieldName + key + " ";
                
                if( this.get(key) instanceof String ){
                    fieldValue += "'" + this.get(key) + "' ";
                }else{
                    fieldValue = fieldValue + this.get(key) + " ";
                }
            }else{
                fieldName = fieldName + key + ", ";
                
                if( this.get(key) instanceof String ){
                    fieldValue += "'" + this.get(key) + "', ";
                }else{
                    fieldValue = fieldValue + this.get(key) + ", ";
                }

            }
            i++;
        }
        sqlStr = sqlStr + "(" + fieldName + ") " + "VALUES (" + fieldValue + ");" ;

        return sqlStr;
    }
    
    
    public JSONObject convertJSon(){
           JSONObject jObj = new JSONObject();
           
           try {
                for (Object key : this.keySet()) {
                    jObj.put((String) key,  this.get(key));
                }

           } catch (Exception e) {
               e.printStackTrace();
           }

           return jObj;
    }
    
    
    
    @Override
    public int compareTo(Object obj) {
            String orgString = this.get(compareField).toString();
            String objString = ((ResultMap)obj).get(compareField).toString();
            return orgString.compareTo(objString);

    }
    
    public static void main(String[] args) {
        ResultMap map = new ResultMap();
        map.put("one", new ResultMap());
        map.getResultMap("one").put("name", "ddss");
        
        System.out.println(map.getResultMap("one").getString("name"));
    }
}
