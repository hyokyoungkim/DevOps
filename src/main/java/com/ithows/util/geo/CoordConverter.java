/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ithows.util.geo;

/**
 *
 * @author mailt
 */
public class CoordConverter {
    
	public MBR viewExtent = new MBR();
	public MBR mapExtent = new MBR();
	public double m_resolution;  // 픽셀당 실제 거리  unitPerPixel
	public double m_xresolution;  // X 픽셀당 실제 거리  unitPerPixel
	public double m_yresolution;  // Y 픽셀당 실제 거리  unitPerPixel

	public double m_pexelsPerMeter; // 미터당 픽셀

	// 뷰에서 회전된 앵글임
	// 각도는 라디언. 각도값을 라디언을 바꾸는 함수   Math.toRadians()
	private double m_angle = 0;

	// @@ projection에 따라 모든 레이어를 그릴 수 있도록 하기 위해 준비
	// EPSG:5179   :  +proj=tmerc +lat_0=38 +lon_0=127.5 +k=0.9996 +x_0=1000000 +y_0=2000000 +ellps=GRS80 +units=m +no_defs

	public String projection = "none";
	public String projectionParam = "none";


	GPoint m_screenCenterPoint = new GPoint(0,1);        // 화면 대한 중심점
	GPoint m_mapCenterPoint = new GPoint(0,1);            // 현재지도 중심 점


	public CoordConverter(int viewWidth, int viewHeight) {
		initView(viewWidth, viewHeight);
	}

	public CoordConverter(MBR mbr) {
		resetView(new GPoint(mbr.minX, mbr.minY) , new GPoint(mbr.maxX, mbr.maxY));
	}

	public CoordConverter(GPoint sPt, GPoint ePt) {
		resetView(sPt, ePt);
	}

	// 초기화
	public CoordConverter(GPoint ssPt, GPoint sePt, GPoint msPt, GPoint mePt) {
		resetView(ssPt, sePt);
		resetMap(msPt, mePt);
	}
        
	public CoordConverter(MBR sMbr, MBR rMbr) {
		resetView(sMbr);
		resetMap(rMbr);
	}
        
        

	//<editor-fold defaultstate="collapsed" desc="속성 보기">
	public int getViewWidth() {
		return (int) viewExtent.getWidth();
	}

	public double getRotationAngle() {
		return m_angle;
	}

	public int getViewHeight() {
		return (int) viewExtent.getHeight();
	}

	public GPoint getMapCenterPoint(){
		return m_mapCenterPoint;
	}

	public GPoint V2W(float x, float y) {

		return V2W(new GPoint(x,y));
	}

	public GPoint V2W(GPoint pt) {

		GPoint	tmpMapPoint = new GPoint();

		// 실좌표 x값을 화면의 중심으로부터 구함
		tmpMapPoint.x = m_mapCenterPoint.x + (pt.x - m_screenCenterPoint.x) * m_resolution;
		// 실좌표 y값을 화면의 중심으로부터 구함
		tmpMapPoint.y = m_mapCenterPoint.y - (pt.y - m_screenCenterPoint.y) *  m_resolution;


		// 화면 중심으로 로테이션 : 개별 로테이션은 아님
		if(m_angle != 0.) {
			double x=tmpMapPoint.x-m_mapCenterPoint.x;
			double y=tmpMapPoint.y-m_mapCenterPoint.y;

			// 시계방향 회전
			tmpMapPoint.x = x*Math.cos(m_angle) - y*Math.sin(m_angle) + m_mapCenterPoint.x;
			tmpMapPoint.y = x*Math.sin(m_angle) + y*Math.cos(m_angle) + m_mapCenterPoint.y;

			// 반시계방향 회전
//			tmpMapPoint.x = y*Math.sin(m_angle) + x*Math.cos(m_angle) + m_mapCenterPoint.x;
//			tmpMapPoint.y = y*Math.cos(m_angle) - x*Math.sin(m_angle) + m_mapCenterPoint.y;
		}

		return tmpMapPoint;
	}

	// 상하좌우 resolution이 다른 경우
	public GPoint V2W2(GPoint pt) {

		GPoint	tmpMapPoint = new GPoint();

		// 실좌표 x값을 화면의 중심으로부터 구함
		tmpMapPoint.x = m_mapCenterPoint.x + (pt.x - m_screenCenterPoint.x) * m_xresolution;
		// 실좌표 y값을 화면의 중심으로부터 구함
		tmpMapPoint.y = m_mapCenterPoint.y - (pt.y - m_screenCenterPoint.y) *  m_yresolution;


		// 화면 중심으로 로테이션 : 개별 로테이션은 아님
		if(m_angle != 0.) {
			double x=tmpMapPoint.x-m_mapCenterPoint.x;
			double y=tmpMapPoint.y-m_mapCenterPoint.y;

			// 시계방향 회전 (좌상 기준점에서)
			tmpMapPoint.x = x*Math.cos(m_angle) - y*Math.sin(m_angle) + m_mapCenterPoint.x;
			tmpMapPoint.y = x*Math.sin(m_angle) + y*Math.cos(m_angle) + m_mapCenterPoint.y;

			// 시계방향 회전 (좌하 기준점에서)
//			tmpMapPoint.x = y*Math.sin(m_angle) + x*Math.cos(m_angle) + m_mapCenterPoint.x;
//			tmpMapPoint.y = y*Math.cos(m_angle) - x*Math.sin(m_angle) + m_mapCenterPoint.y;
		}

		return tmpMapPoint;
	}


	public GPoint W2V(GPoint pt) {
		GPoint result = new GPoint();

//	mx=(_MapPoint.x-m_geoCenterPoint.x)/m_lfnowXRatio+m_scrCenterPoint.x;
//	my=(m_geoCenterPoint.y-_MapPoint.y)/m_lfnowYRatio+m_scrCenterPoint.y;

		result.x = (pt.x - m_mapCenterPoint.x) / m_resolution + m_screenCenterPoint.x;
		result.y = (m_mapCenterPoint.y - pt.y) / m_resolution + m_screenCenterPoint.y;

		// 화면 중심으로 로테이션 : 개별 로테이션은 아님
		if (m_angle != 0.) {
			double dx = result.x - m_screenCenterPoint.x;
			double dy = result.y - m_screenCenterPoint.y;

			// 시계방향 회전 (좌상 기준점에서)
			result.x = dx * Math.cos(m_angle) - dy * Math.sin(m_angle) + m_screenCenterPoint.x;
			result.y = dx * Math.sin(m_angle) + dy * Math.cos(m_angle) + m_screenCenterPoint.y;

			// 시계방향 회전 (좌하 기준점에서)
//			result.x = dy * Math.sin(m_angle) + dx * Math.cos(m_angle) + m_screenCenterPoint.x;
//			result.y = dy * Math.cos(m_angle) - dx * Math.sin(m_angle) + m_screenCenterPoint.y;
		}


		result.x = (int) (result.x + .5);
		result.y = (int) (result.y + .5);


		return result;
	}


	// 상하좌우 resolution이 다른 경우
	public GPoint W2V2(GPoint pt) {
		GPoint result = new GPoint();

		result.x = (pt.x - m_mapCenterPoint.x) / m_xresolution + m_screenCenterPoint.x;
		result.y = (m_mapCenterPoint.y - pt.y) / m_yresolution + m_screenCenterPoint.y;

		// 화면 중심으로 로테이션 : 개별 로테이션은 아님
		if (m_angle != 0.) {
			double dx = result.x - m_screenCenterPoint.x;
			double dy = result.y - m_screenCenterPoint.y;

			// 시계방향 회전 (좌상 기준점에서)
			result.x = dx * Math.cos(m_angle) - dy * Math.sin(m_angle) + m_screenCenterPoint.x;
			result.y = dx * Math.sin(m_angle) + dy * Math.cos(m_angle) + m_screenCenterPoint.y;

			// 시계방향 회전 (좌하 기준점에서)
//			result.x = dy * Math.sin(m_angle) + dx * Math.cos(m_angle) + m_screenCenterPoint.x;
//			result.y = dy * Math.cos(m_angle) - dx * Math.sin(m_angle) + m_screenCenterPoint.y;
		}

		result.x = (int) (result.x + .5);
		result.y = (int) (result.y + .5);

		return result;
	}


	public static boolean intersectMBR(MBR mbrA, MBR mbrB) {
		if (mbrA.minX > mbrB.maxX) {
			return false;
		}
		if (mbrA.minY > mbrB.maxY) {
			return false;
		}
		if (mbrA.maxX < mbrB.minX) {
			return false;
		}
		if (mbrA.maxY < mbrB.minY) {
			return false;
		}
		return true;
	}


	public boolean IntersectViewportMBR(MBR mbr) {
		if (mapExtent.minX > mbr.maxX) {
			return false;
		} else if (mapExtent.minY > mbr.maxY) {
			return false;
		} else if (mbr.minX > mapExtent.maxX) {
			return false;
		} else if (mbr.minY > mapExtent.maxY) {
			return false;
		}
		return true;

//		return mbr.minX<=_viewWidth && (mbr.maxX>=0.0f) && (mbr.minY<=_viewHeight) && (mbr.maxY>=0);
	}

	// 현재 화면에 보이는 Extent의 값을 알려 줌
	public MBR getViewportMBR() {

		return mapExtent;
	}

	// 현재 화면에 보이는 화면 Extent의 값을 알려 줌
	public MBR getViewMBR() {

		return viewExtent;
	}

	//</editor-fold>


	//<editor-fold defaultstate="collapsed" desc="Extent 재설정">


	public void initView(int viewWidth, int viewHeight) {
		m_screenCenterPoint = new GPoint(viewWidth / 2, viewHeight / 2);
		viewExtent.minX = 0;
		viewExtent.minY = 0;
		viewExtent.maxX = viewWidth;
		viewExtent.maxY = viewHeight;

		if (viewExtent.getWidth() == 0 && viewExtent.getWidth() == 0 ) {
			m_resolution = 1;
			m_xresolution = 1;
			m_yresolution = 1;

		}else if (viewExtent.getWidth() == 0 ) {
			m_resolution = mapExtent.getHeight() / viewExtent.getHeight();
			m_xresolution = 1;
			m_yresolution = mapExtent.getHeight() / viewExtent.getHeight();

		}else if (viewExtent.getHeight() == 0 ) {
			m_resolution = mapExtent.getWidth() / viewExtent.getWidth();
			m_xresolution = mapExtent.getWidth() / viewExtent.getWidth();
			m_yresolution = 1;

		}else if (mapExtent.getWidth() / viewExtent.getWidth() > mapExtent.getHeight() / viewExtent.getHeight()) {
			m_resolution = mapExtent.getWidth() / viewExtent.getWidth();
			m_xresolution = mapExtent.getWidth() / viewExtent.getWidth();
			m_yresolution = mapExtent.getHeight() / viewExtent.getHeight();

		} else {
			m_resolution = mapExtent.getHeight() / viewExtent.getHeight();
			m_xresolution = mapExtent.getWidth() / viewExtent.getWidth();
			m_yresolution = mapExtent.getHeight() / viewExtent.getHeight();
		}

	}

	// ** 화면의 크기가 새로 정해지거나 바뀔 때
	public void resetView(GPoint sPt, GPoint ePt) {
		MBR mbr = new MBR(sPt, ePt);
		resetView(mbr);
	}

	public void resetView(MBR newExtent) {
		m_screenCenterPoint.x = (newExtent.minX + newExtent.maxX) / 2;
		m_screenCenterPoint.y = (newExtent.minY + newExtent.maxY) / 2;

		resizeView((int)newExtent.getWidth(), (int)newExtent.getHeight());
	}

	public void resizeView(int viewWidth, int viewHeight) {
		viewExtent.minX = m_screenCenterPoint.x - viewWidth / 2;
		viewExtent.minY = m_screenCenterPoint.y - viewHeight / 2;
		viewExtent.maxX = m_screenCenterPoint.x + viewWidth / 2;
		viewExtent.maxY = m_screenCenterPoint.y + viewHeight / 2;

		if (viewExtent.getWidth() == 0 && viewExtent.getWidth() == 0 ) {
			m_resolution = 1;
			m_xresolution = 1;
			m_yresolution = 1;

		}else if (viewExtent.getWidth() == 0 ) {
			m_resolution = mapExtent.getHeight() / viewExtent.getHeight();
			m_xresolution = 1;
			m_yresolution = mapExtent.getHeight() / viewExtent.getHeight();

		}else if (viewExtent.getHeight() == 0 ) {
			m_resolution = mapExtent.getWidth() / viewExtent.getWidth();
			m_xresolution = mapExtent.getWidth() / viewExtent.getWidth();
			m_yresolution = 1;

		}else if (mapExtent.getWidth() / viewExtent.getWidth() > mapExtent.getHeight() / viewExtent.getHeight()) {
			m_resolution = mapExtent.getWidth() / viewExtent.getWidth();
			m_xresolution = mapExtent.getWidth() / viewExtent.getWidth();
			m_yresolution = mapExtent.getHeight() / viewExtent.getHeight();

		} else {
			m_resolution = mapExtent.getHeight() / viewExtent.getHeight();
			m_xresolution = mapExtent.getWidth() / viewExtent.getWidth();
			m_yresolution = mapExtent.getHeight() / viewExtent.getHeight();
		}

	}


	// ** 지도의 크기가 새로 정해지거나 바뀔 때
	public void resetMap(GPoint sPt, GPoint ePt) {
		MBR mbr = new MBR(sPt, ePt);
		resetMap(mbr);
	}

	public void resetMap(MBR newExtent) {
		m_mapCenterPoint.x = (newExtent.minX + newExtent.maxX) / 2;
		m_mapCenterPoint.y = (newExtent.minY + newExtent.maxY) / 2;
		resizeMap(newExtent.getWidth(), newExtent.getHeight());
	}

	public void resizeMap(double mapWidth, double mapHeight) {
		mapExtent.minX = m_mapCenterPoint.x - mapWidth / 2;
		mapExtent.minY = m_mapCenterPoint.y - mapHeight / 2;
		mapExtent.maxX = m_mapCenterPoint.x + mapWidth / 2;
		mapExtent.maxY = m_mapCenterPoint.y + mapHeight / 2;

		if (viewExtent.getWidth() == 0 && viewExtent.getWidth() == 0 ) {
			m_resolution = 1;
			m_xresolution = 1;
			m_yresolution = 1;

		}else if (viewExtent.getWidth() == 0 ) {
			m_resolution = mapExtent.getHeight() / viewExtent.getHeight();
			m_xresolution = 1;
			m_yresolution = mapExtent.getHeight() / viewExtent.getHeight();

		}else if (viewExtent.getHeight() == 0 ) {
			m_resolution = mapExtent.getWidth() / viewExtent.getWidth();
			m_xresolution = mapExtent.getWidth() / viewExtent.getWidth();
			m_yresolution = 1;

		}else if (mapExtent.getWidth() / viewExtent.getWidth() > mapExtent.getHeight() / viewExtent.getHeight()) {
			m_resolution = mapExtent.getWidth() / viewExtent.getWidth();
			m_xresolution = mapExtent.getWidth() / viewExtent.getWidth();
			m_yresolution = mapExtent.getHeight() / viewExtent.getHeight();

		} else {
			m_resolution = mapExtent.getHeight() / viewExtent.getHeight();
			m_xresolution = mapExtent.getWidth() / viewExtent.getWidth();
			m_yresolution = mapExtent.getHeight() / viewExtent.getHeight();
		}
	}

	// TMS에서 레벨이 변경 될때
	// TMS 레이어의 resolution에 맞게 Extent를 조정
	public void resizeMapFitTMS(double resolution) {
		m_resolution = resolution;
		mapExtent.minX = m_mapCenterPoint.x - (viewExtent.getWidth() * m_resolution / 2);
		mapExtent.minY = m_mapCenterPoint.y - (viewExtent.getHeight() * m_resolution / 2);
		mapExtent.maxX = m_mapCenterPoint.x + (viewExtent.getWidth() * m_resolution / 2);
		mapExtent.maxY = m_mapCenterPoint.y + (viewExtent.getHeight() * m_resolution / 2);

	}

	// 픽셀당 거리 : TMS에서 중요
	public double getMetersPerOnePixel() {
		return m_resolution;
	}

	// 픽셀당 거리
	public double getDistPerOnePixel() {

		double pixelsPerMeter;

		if (viewExtent.getWidth() == 0 && viewExtent.getWidth() == 0 ) {
			pixelsPerMeter = 1;

		}else if (viewExtent.getWidth() == 0 ) {
			pixelsPerMeter = mapExtent.getHeight() / viewExtent.getHeight();

		}else if (viewExtent.getHeight() == 0 ) {
			pixelsPerMeter = mapExtent.getWidth() / viewExtent.getWidth();

		}else if (mapExtent.getWidth() / viewExtent.getWidth() > mapExtent.getHeight() / viewExtent.getHeight()) {
			pixelsPerMeter = mapExtent.getWidth() / viewExtent.getWidth();

		} else {
			pixelsPerMeter = mapExtent.getHeight() / viewExtent.getHeight();
		}

		return pixelsPerMeter;
	}

	// %% 임시용
	public void resize(int w, int h){
//		initView(w,h);
		resizeView(w,h);
	}


	//</editor-fold>


	//<editor-fold defaultstate="collapsed" desc="View & Zoom Function">
	//////////////////////////////////////////////////////////////////////
	// Zoom and Move
	public void moveTo(double x, double y)
	{
		m_mapCenterPoint.x = x;
		m_mapCenterPoint.y = y;

		double width = mapExtent.getWidth();
		double height = mapExtent.getHeight();

		mapExtent.minX = m_mapCenterPoint.x - ( width / 2 );
		mapExtent.minY = m_mapCenterPoint.y - ( height / 2 );
		mapExtent.maxX = m_mapCenterPoint.x + ( width / 2 );
		mapExtent.maxY = m_mapCenterPoint.y + ( height / 2 );
	}

	public void rotate(double dRot){

	}

	public void zoomIn(){
		zoomScale(0.8);
	}

	/**
	 *
	 * @param newScale  : 1보다 크면
	 */
	public void zoomScale(double newScale) {
		if(newScale<=0){
			newScale = 1;
		}

		double scale = getDistPerOnePixel();
		scale *= newScale;

		double width = viewExtent.getWidth() * scale ;
		double height = viewExtent.getHeight() * scale;

		mapExtent.minX = m_mapCenterPoint.x - ( width / 2 );
		mapExtent.minY = m_mapCenterPoint.y - ( height / 2 );
		mapExtent.maxX = m_mapCenterPoint.x + ( width / 2 );
		mapExtent.maxY = m_mapCenterPoint.y + ( height / 2 );

		if (viewExtent.getWidth() == 0 && viewExtent.getWidth() == 0 ) {
			m_resolution = 1;
			m_xresolution = 1;
			m_yresolution = 1;

		}else if (viewExtent.getWidth() == 0 ) {
			m_resolution = mapExtent.getHeight() / viewExtent.getHeight();
			m_xresolution = 1;
			m_yresolution = mapExtent.getHeight() / viewExtent.getHeight();

		}else if (viewExtent.getHeight() == 0 ) {
			m_resolution = mapExtent.getWidth() / viewExtent.getWidth();
			m_xresolution = mapExtent.getWidth() / viewExtent.getWidth();
			m_yresolution = 1;

		}else if (mapExtent.getWidth() / viewExtent.getWidth() > mapExtent.getHeight() / viewExtent.getHeight()) {
			m_resolution = mapExtent.getWidth() / viewExtent.getWidth();
			m_xresolution = mapExtent.getWidth() / viewExtent.getWidth();
			m_yresolution = mapExtent.getHeight() / viewExtent.getHeight();

		} else {
			m_resolution = mapExtent.getHeight() / viewExtent.getHeight();
			m_xresolution = mapExtent.getWidth() / viewExtent.getWidth();
			m_yresolution = mapExtent.getHeight() / viewExtent.getHeight();
		}

	}

	public void zoomOut(){
		zoomScale(1.2);
	}

	public void zoomByMBR(MBR mbr) {
		resetMap(mbr);
	}

	

	//</editor-fold>



	//<editor-fold defaultstate="collapsed" desc="Util Function">

	// 화면 길이로 변환
	public double getViewLength(double mapLength) {
		GPoint p1 = new GPoint(mapLength, 0);
		GPoint p2 = new GPoint(0, 0);

		GPoint pt1 = W2V(p1);
		GPoint pt2 = W2V(p2);

		double length = Math.sqrt(Math.pow(pt1.x - pt2.x, 2.0) + Math.pow(pt1.y - pt2.y, 2.0));

		return length;
	}

	// 지도상 거리로 변환
	public double getMapLength(double viewLength) {
		GPoint p1 = new GPoint(viewLength, 0);
		GPoint p2 = new GPoint(0, 0);

		GPoint pt1 = V2W(p1);
		GPoint pt2 = V2W(p2);

		double length = Math.sqrt(Math.pow(pt1.x - pt2.x, 2.0) + Math.pow(pt1.y - pt2.y, 2.0));

		return length;
	}

	//</editor-fold>


	public Object clone() throws CloneNotSupportedException {
		CoordConverter obj = (CoordConverter)super.clone();
		return obj;
	}


	public static void main(String[] args) {
// 		GPoint pt = new GPoint(0,0);
//		int angle = -90;
//		double radian = Math.toRadians(angle);
//		System.out.println(pt + " : " + angle + " --> " + pt.getLotatePoint(new GPoint(100,50), radian));
//		System.out.println(pt + " : " + angle + " --> " + GeoOperator.getPointByAngle(pt, new GPoint(100,50), radian));
//		System.out.println(pt + " : " + angle + " --> " + GeoOperator.rotateCoordinate(pt, new GPoint(100,50), radian));
//
//		pt = new GPoint(200,100);
//		System.out.println(pt + " : " + angle + " --> " + pt.getLotatePoint(new GPoint(100,50), radian));
		GPoint oMin = new GPoint(0,0);
		GPoint oMax = new GPoint(1611 , 961);

		GPoint mMin = new GPoint(0,0);
		GPoint mMax = new GPoint(1611 , 961);

//		CoordConverter con = CoordConverter.createCoordConverter(oMin, oMax, mMin, mMax);
                CoordConverter con = new CoordConverter(oMin, oMax, mMin, mMax);
		// 화면 좌표에 대한 해석은 좌상점을 기준으로 읽어야 한다.
//		System.out.println(oMin + " map --> " + con.V2W2(oMin) );
//		System.out.println(new GPoint(200,100) + " screen --> " + con.W2V2(new GPoint(200,100)) );
                
		oMin = new GPoint(0,0);
		oMax = new GPoint(200 , 100);

//		mMin = new GPoint(0,221);
//		mMax = new GPoint(317 , 96);
                // 45
		mMin = new GPoint(193.93398282201787,-168.19805153394634);
		mMax = new GPoint(406.06601717798213,468.19805153394634);
                // -45
		mMin = new GPoint(-18.198051533946398,256.0660171779821);
		mMax = new GPoint(618.1980515339465,43.933982822017896);
                
                
                GPoint mbMin = new GPoint(0, 0);
                GPoint mbMax = new GPoint(600, 300);
                
                MBR mapSpaceMBR = new MBR(mbMin, mbMax);  // 지도에서 확인해야 함
                
                CoordConverter con2 = CoordConverter.createCoordConverter(oMin, oMax, mMin, mMax, mapSpaceMBR);
                
                System.out.println(new GPoint(200,100) + " screen --> " + con2.V2W2(new GPoint(200,100)) );
	}


        /**
	 * 화면 좌표와 지도좌표를 매핑하여 좌표변환 할 수 있도록 변환기 객체 생성
	 * Extent와 회전을 반영함
         * @param screenSPt
         * @param screenEPt
         * @param mapSPt  : 지도상 확인한 공간
         * @param mapEPt
         * @param realMBR :  지도에서 확인해야 함(지도상 MBR) 
         * @return 
         */
	public static CoordConverter createCoordConverter(GPoint screenSPt, GPoint screenEPt, GPoint mapSPt, GPoint mapEPt, MBR realMBR) {

		int angle;
		double radian;

		// 1. 회전각 구하기 -- 중심점 기준의 실좌표 회전각 - 중심점 기준의 화면 회전각
		GPoint oMin = screenSPt;
		GPoint oMax = screenEPt;

		// y축 변환
		GPoint sMin = new GPoint(oMin.x, oMin.y);
		GPoint sMax = new GPoint(oMax.x, oMax.y);

		GPoint mMin = mapSPt;
		GPoint mMax = mapEPt;

                // 1. 대각좌표로 도형의 각도를 구한다.
		double sRad;
		double mRad;
		sRad = GeoOperator.getRadianBetweenPoints(sMin, new GPoint((sMin.x+sMax.x)/2, (sMin.y+sMax.y)/2 ));
		mRad = GeoOperator.getRadianBetweenPoints(mMin, new GPoint((mMin.x+mMax.x)/2, (mMin.y+mMax.y)/2 ));
//		System.out.println(sMin + " - " + sMax + " : angle --> " + sRad + " (" + Math.toDegrees(sRad) + ") ");
//		System.out.println(mMin + " - " + mMax + " : angle --> " + mRad + " (" + Math.toDegrees(mRad) + ") ");

		// 2. 각도의 차이만큼 화면 사각전체 좌표를 로테이트
		angle = (int)(Math.toDegrees(mRad) - Math.toDegrees(sRad));
		radian = Math.toRadians(angle);
                
                GPoint sPt1 = new GPoint(sMin.x, sMin.y);
                GPoint sPt2 = new GPoint(sMin.x, sMax.y);
                GPoint sPt3 = new GPoint(sMax.x, sMin.y);
                GPoint sPt4 = new GPoint(sMax.x, sMax.y);
                
		GPoint rotatePt1 = GeoOperator.getPointByAngle(new GPoint((sMin.x + sMax.x) / 2, (sMin.y + sMax.y) / 2), sPt1, radian);
		GPoint rotatePt2 = GeoOperator.getPointByAngle(new GPoint((sMin.x + sMax.x) / 2, (sMin.y + sMax.y) / 2), sPt2, radian);
		GPoint rotatePt3 = GeoOperator.getPointByAngle(new GPoint((sMin.x + sMax.x) / 2, (sMin.y + sMax.y) / 2), sPt3, radian);
		GPoint rotatePt4 = GeoOperator.getPointByAngle(new GPoint((sMin.x + sMax.x) / 2, (sMin.y + sMax.y) / 2), sPt4, radian);
		
//		System.out.println(sPt1 + " : " + angle + " --> " + rotatePt1);
//		System.out.println(sPt2 + " : " + angle + " --> " + rotatePt2);
//		System.out.println(sPt3 + " : " + angle + " --> " + rotatePt3);
//		System.out.println(sPt4 + " : " + angle + " --> " + rotatePt4);
//		
//               
//		System.out.println("--------------------------------------------------------------------");

                // test 용
                mMin = new GPoint(0,0);
		mMax = new GPoint(600 , 300);
		angle = -45;
		radian = Math.toRadians(angle);
                GPoint rotateSPt2 = GeoOperator.getPointByAngle(new GPoint((mMin.x + mMax.x) / 2, (mMin.y + mMax.y) / 2), mMin, radian);
		GPoint rotateEPt2 = GeoOperator.getPointByAngle(new GPoint((mMin.x + mMax.x) / 2, (mMin.y + mMax.y) / 2), mMax, radian);
//		System.out.println(mMin + " : " + angle + " --> " + rotateSPt2);
//		System.out.println(mMax + " : " + angle + " --> " + rotateEPt2);


                // 회전된 화면 사각형에 대한 MBR 계산
                MBR screenMBR = new MBR(rotatePt1, rotatePt2, rotatePt3, rotatePt4);

		// 3. 다시 MBR 설정
		CoordConverter con = new CoordConverter(screenMBR, realMBR);

		// 화면 좌표에 대한 해석은 좌상점을 기준으로 읽어야 한다.
//		System.out.println(sMin + " map --> " + con.V2W2(sMin) );
//		System.out.println(mMin + " screen --> " + con.W2V2(mMin) );

		return con;
	}
}
