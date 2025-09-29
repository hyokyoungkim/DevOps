/*
 *  Copyright 2020. S.O.X. All rights reserved
 */

package com.ithows.service;


import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import com.ithows.util.geo.MBR ;
import com.ithows.util.geo.GPoint ;
/**
 * Class GeoDataManager
 * 
 * @author Roi Kim <S.O.X Co. Ltd.>
 */
public class GeoDataManager {
    
    // 좌표 리스트를 json으로 받아서 bounding box를 구함
    // @@ Leaflet JSON에서는 x,y가 반대 = latitude, longtitude 순으로 들어 옴
    public static MBR getShapeBoundingBox(JSONArray ptList){
        MBR bound = new MBR();
        try{
            JSONArray jpt;
            if(ptList.length()>0){
                bound.minX = new GPoint(ptList.getJSONArray(0).getDouble(1), ptList.getJSONArray(0).getDouble(0)).x;
                bound.minY = new GPoint(ptList.getJSONArray(0).getDouble(1), ptList.getJSONArray(0).getDouble(0)).y;
                bound.maxX = new GPoint(ptList.getJSONArray(0).getDouble(1), ptList.getJSONArray(0).getDouble(0)).x;
                bound.maxY = new GPoint(ptList.getJSONArray(0).getDouble(1), ptList.getJSONArray(0).getDouble(0)).y;
            }
            for(int i=0; i < ptList.length() ; i++ ){
                jpt = ptList.getJSONArray(i);
                GPoint pt = new GPoint(jpt.getDouble(1), jpt.getDouble(0));
                bound.append(pt);
            }
        }catch(Exception ex){
            System.out.println(ex.getLocalizedMessage());
        }
        return bound;
    }
    
    
    // @@ Leaflet JSON에서는 x,y가 반대 = latitude, longtitude 순으로 들어 옴
    public static MBR convertMBRFromJArray(JSONArray ptList){
        MBR bound = new MBR();
        try{
            bound.minY = ptList.getJSONArray(0).getDouble(0) ;
            bound.minX = ptList.getJSONArray(0).getDouble(1) ;
            bound.maxY = ptList.getJSONArray(1).getDouble(0) ;
            bound.maxX = ptList.getJSONArray(1).getDouble(1) ;
            
        }catch(Exception ex){
            System.out.println(ex.getLocalizedMessage());
        }
        return bound;
    }
    
    
    
    public static void main(String[] args) {
        String pts = "[[10,20],[30,40],[100,20]]";
        try {
            System.out.println(">>>  " + getShapeBoundingBox(new JSONArray(pts) ));
        } catch (JSONException ex) {
            Logger.getLogger(GeoDataManager.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
}
