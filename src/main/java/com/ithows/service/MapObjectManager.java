/*
 *  Copyright 2020. S.O.X. All rights reserved
 */

package com.ithows.service;

import com.ithows.ResultMap;
import com.ithows.util.geo.MBR ;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Class PoiManager
 * 
 * @author Roi Kim <S.O.X Co. Ltd.>
 */
public class MapObjectManager {

    public static JSONArray makePoiObjectList(ArrayList<ResultMap> list)  {
        JSONArray jArr = new JSONArray();
        
        for(ResultMap objMap : list){
            if(objMap == null || objMap.size() == 0){
                continue;
            }
            jArr.put(makePoiObject(objMap));
        }
        
        return jArr;
    }
    
    
    // @@ Poi에 대한 json 출력부분 관련
    public static JSONObject makePoiObject(ResultMap objMap)  {
        if(objMap == null || objMap.size() == 0){
            return null;
        }
        
        String vMetaStr = "";
        String iMetaStr = "";
        String hMetaStr = "";
        String fMetaStr = "";
        String cMetaStr = "";
        String tMetaStr = "";
        
        JSONObject jPoi = new JSONObject();
        try {
            jPoi.put("no", objMap.getInt("no"));
            
            JSONObject jPosition = new JSONObject();
            jPosition.put("lng", objMap.getDouble("X"));
            jPosition.put("lat", objMap.getDouble("Y"));
            jPoi.put("position", jPosition);
            
            jPoi.put("value", objMap.getString("value"));
            jPoi.put("metric", new JSONObject(objMap.getString("metric")));
            jPoi.put("popup", new JSONObject(objMap.getString("popup")));
            
            JSONObject jMeta = new JSONObject();
            
            try{
                vMetaStr = objMap.getString("videoMeta");
                jMeta.put("video", new JSONObject(vMetaStr));
            }catch(JSONException ex1){
                jMeta.put("video", new JSONObject());
            }
            
            try{
                iMetaStr = objMap.getString("imageMeta");
                jMeta.put("image", new JSONObject(iMetaStr));
            }catch(JSONException ex1){
                jMeta.put("image", new JSONObject());
            }
            
            try{
                hMetaStr = objMap.getString("htmlMeta");
                jMeta.put("html", new JSONObject(hMetaStr));
            }catch(JSONException ex1){
                jMeta.put("html", new JSONObject());
            }
            
            try{
                fMetaStr = objMap.getString("fileMeta");
                jMeta.put("file", new JSONObject(fMetaStr));
            }catch(JSONException ex1){
                jMeta.put("file", new JSONObject());
            }
            
            try{
                cMetaStr = objMap.getString("chartMeta");
                jMeta.put("video", new JSONObject(cMetaStr));
            }catch(JSONException ex1){
                jMeta.put("video", new JSONObject());
            }
            
            try{
                tMetaStr = objMap.getString("tableMeta");
                jMeta.put("table", new JSONObject(tMetaStr));
            }catch(JSONException ex1){
                jMeta.put("table", new JSONObject());
            }
            
            jPoi.put("meta", jMeta);
            
            
        } catch (Exception ex) {
            Logger.getLogger(MapObjectManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return jPoi;
    }
       
    
    
    public static JSONArray makeShapeObjectList(ArrayList<ResultMap> list)  {
        JSONArray jArr = new JSONArray();
        
        for(ResultMap objMap : list){
            if(objMap == null || objMap.size() == 0){
                continue;
            }
            jArr.put(makeShapeObject(objMap));
        }
        
        return jArr;
    }
    
    // @@ Shape에 대한 json 출력부분 관련
    public static JSONObject makeShapeObject(ResultMap objMap)  {
        if(objMap == null || objMap.size() == 0){
            return null;
        }
        
        JSONObject jShape = new JSONObject();
        try {
            jShape.put("no", objMap.getInt("no"));
            jShape.put("type", objMap.getString("shapetype"));
            
            JSONArray coordList = new JSONArray(objMap.getString("coords"));
            jShape.put("coordinates", coordList);
            MBR bound = GeoDataManager.getShapeBoundingBox(coordList);
            jShape.put("bound", new JSONArray("[[" + bound.minY + "," + bound.minX + "],[" + bound.maxY + "," + bound.maxX + "]]") );
            
            JSONObject jData = new JSONObject();
            
            if(objMap.getString("metric") != null && !objMap.getString("metric").equals("")){
                JSONObject jMetric = new JSONObject(objMap.getString("metric"));
                jData.put("metric", jMetric);
                
                try{
                    if(jMetric.getString("val1") == null){
                        jData.put("valuetype", "url");
                    }
                }catch(Exception ex){
                    jData.put("valuetype", "data");
                }                
            }else{
                jData.put("valuetype", "data");
                jData.put("metric", "");
            }
            
            jShape.put("data", jData);
            jShape.put("popup", new JSONObject(objMap.getString("popup")));
            
            
            
        } catch (Exception ex) {
            Logger.getLogger(MapObjectManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return jShape;
    }
    
    
    public static JSONArray makeDataObjectList(ArrayList<ResultMap> list)  {
        JSONArray jArr = new JSONArray();
        
        for(ResultMap objMap : list){
            if(objMap == null || objMap.size() == 0){
                continue;
            }
            jArr.put(makeDataObject(objMap));
        }
        
        return jArr;
    }
    
    // @@ chart, table에 대한 json 출력부분 관련
    public static JSONObject makeDataObject(ResultMap objMap)  {
        if(objMap == null || objMap.size() == 0){
            return null;
        }
        
        JSONObject jDataObj = new JSONObject();
        try {
            jDataObj.put("no", objMap.getInt("no"));
            jDataObj.put("type", objMap.getInt("type")==1 ? "chart" : "table" );
            
            JSONObject jPosition = new JSONObject();
            jPosition.put("lng", objMap.getDouble("X"));
            jPosition.put("lat", objMap.getDouble("Y"));
            jDataObj.put("position", jPosition);
            
            JSONObject jSize = new JSONObject();
            jSize.put("width",  objMap.getInt("width"));
            jSize.put("height", objMap.getInt("height"));
            jDataObj.put("size", jSize);
            
            jDataObj.put("meta", new JSONObject(objMap.getString("meta")));
            
        } catch (Exception ex) {
            Logger.getLogger(MapObjectManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return jDataObj;
    }
}
