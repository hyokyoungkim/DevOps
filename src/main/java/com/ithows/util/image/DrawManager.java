/*
 *  Copyright 2020. S.O.X. All rights reserved
 */

package com.ithows.util.image;

import com.ithows.AppConfig;
import com.ithows.util.geo.GPoint;
import com.ithows.util.geo.MBR;
import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.TextLayout;
import java.awt.geom.AffineTransform;
import java.awt.geom.Area;
import java.awt.geom.GeneralPath;
import java.awt.geom.Rectangle2D.Double;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Class DrawManager
 * 
 * @author Roi Kim <S.O.X Co. Ltd.>
 */
public class DrawManager {

    public static final double VERTEX_MARKER_SIZE = 5.0D;
    public static final double ENDPOINT_MARKER_SIZE = 9.0D;
    static int[] arrowX = new int[3];
    static int[] arrowY = new int[3];
    private static boolean showingOrientations = true;
    private static boolean showingVertices = true;
    private static Double rect = new Double(0.0D, 0.0D, 5.0D, 5.0D);
    private static Double rectHighlight = new Double(0.0D, 0.0D, 9.0D, 9.0D);
    private static double vertexSize = 5.0D;

    private static Font basicFont = null;
    private static Font subFont = null;
    
    static{

       setFont();
        
    }

    public static Font getFont(){
        return basicFont;
    }
    
    private static void setFont(){
        String OS = System.getProperty("os.name").toLowerCase();
        String basicFontPath = "" ;        
        String subFontPath = "" ;        
        
        if(OS.indexOf("win") >= 0){ 
           basicFontPath = "D:\\00_project2022\\SSF\\build\\web\\resource\\fonts\\malgun.ttf" ; 
           subFontPath = "D:\\00_project2022\\SSF\\build\\web\\resource\\fonts\\tahoma.ttf" ;

        }else{
           basicFontPath = AppConfig.getContextPath() + AppConfig.getConf("config_file_font_url")  + "malgun.ttf";
           subFontPath = AppConfig.getContextPath() + AppConfig.getConf("config_file_font_url")  + "tahoma.ttf";

        }                
        
        try {
//            InputStream is1 = ClassLoader.getSystemClassLoader().getResourceAsStream(basicFontPath);
//            basicFont = Font.createFont(Font.TRUETYPE_FONT, is1).deriveFont(12f);   // 기본 12포인트 
            basicFont = Font.createFont(Font.TRUETYPE_FONT, new File(basicFontPath)).deriveFont(20f);   // 기본 12포인트 


//            InputStream is2 = ClassLoader.getSystemClassLoader().getResourceAsStream(subFontPath);
//            subFont = Font.createFont(Font.TRUETYPE_FONT, is2).deriveFont(12f);    // 기본 12포인트 
            subFont = Font.createFont(Font.TRUETYPE_FONT, new File(subFontPath)).deriveFont(20f);   // 기본 12포인트 
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    public static void drawTime(Graphics2D g, String timeStr, GPoint pt){
        
//        Font font = new Font("맑은 고딕", Font.PLAIN, 20); 
        Font font = basicFont.deriveFont(20f); 
        g.setFont(font);
        int[] size = getTextSize(g, timeStr, font);
        int width = size[0];
        int height = size[1];

        // 오른쪽 정렬방식으로 처리
        g.drawString(timeStr, (float)(pt.x - width), (float)(pt.y + height / 4));
    }

    public static void drawArrow(Graphics2D g, GPoint p0, GPoint p1) {
        int x1 = (int) p0.x;
        int y1 = (int) p0.y;
        int x2 = (int) p1.x;
        int y2 = (int) p1.y;
        drawArrowline(g, x1, y1, x2, y2, 10, 6);

    }

    private static void drawArrowline(Graphics2D g2, int x1, int y1, int x2, int y2, int d, int h) {

        int dx = x2 - x1, dy = y2 - y1;
        double D = Math.sqrt(dx * dx + dy * dy);
        double xm = D - d, xn = xm, ym = h, yn = -h, x;
        double sin = dy / D, cos = dx / D;

        x = xm * cos - ym * sin + x1;
        ym = xm * sin + ym * cos + y1;
        xm = x;

        x = xn * cos - yn * sin + x1;
        yn = xn * sin + yn * cos + y1;
        xn = x;

        int[] xpoints = {x2, (int) Math.round(xm), (int) Math.round(xn)};
        int[] ypoints = {y2, (int) Math.round(ym), (int) Math.round(yn)};

        g2.drawLine(x1, y1, x2, y2);
        g2.fillPolygon(xpoints, ypoints, 3);

    }

    protected static void drawVertex(Graphics2D g, GPoint p) {
        rect.x = p.x - vertexSize / (double) 2;
        rect.y = p.y - vertexSize / (double) 2;
        g.fill(rect);

    }

    public static void drawDot(Graphics2D g,GPoint p, int dotSize) {
        double x = p.x - dotSize / (double) 2;
        double y = p.y - dotSize / (double) 2;
        g.fillOval((int) x , (int) y, (int) (dotSize), (int) (dotSize));
    }

    public static void drawDotLine(Graphics2D g, GPoint p, int dotSize) {
        double x = p.x - dotSize / (double) 2;
        double y = p.y - dotSize / (double) 2;
        g.drawOval((int) x, (int) y, (int) (dotSize), (int) (dotSize));
    }

    protected static void drawHighlightedVertex(Graphics2D g, GPoint p) {
        rectHighlight.x = p.x - vertexSize;
        rectHighlight.y = p.y - vertexSize;
        g.draw(rectHighlight);
    }

    public static void drawPoint(Graphics2D g, GPoint p) {
        drawVertex(g, p);
        drawHighlightedVertex(g, p);
    }

    public static void drawImageSymbol(Graphics2D g, BufferedImage symbol , GPoint p){
        int imgWidth = symbol.getWidth();
        int imgHeight = symbol.getHeight();
        int x = (int) (p.x - imgWidth / 2);
        int y = (int) (p.y - imgHeight / 2);
        g.drawImage(symbol, x, y, null);
    }

    public static void drawBand(Graphics2D g, ArrayList pts, GPoint p) {
        if (pts.size() > 0) {
            GPoint last = (GPoint) pts.get(pts.size() - 1);
            ((Graphics2D) g).draw(new java.awt.geom.Line2D.Double(p.x, p.y, last.x, last.y));
        }
    }

    public static void drawPoints(Graphics2D g, ArrayList pts) {
        int i;

        //double scale =  1;  // scaleFactor / Math.sqrt((diagram.getHeight() * diagram.getWidth()) / defaultSize);
        BufferedImage symBuf =  SymbolImageMaker.makePlus(Color.RED);
//        BufferedImage symBuf =  SymbolImageMaker.makeIconFromSVG("resource/symbols/sport/canoe.svg", 20);

        for (i = 0; i < pts.size(); ++i) {
            GPoint p = (GPoint) pts.get(i);
//            drawVertex(p, g);
//            drawDot(p, g);
//            drawHighlightedVertex(p,g);

//            drawImageSymbol(GraphicUtils.applyFilter(symBuf, Filter.GRAYSCALE) , p,g);
            drawImageSymbol(g, symBuf, p);

        }
//        drawHighlightedVertex((Point2D) pts.get(0), g);
    }

    public static void drawPolyline(Graphics2D g, ArrayList pts, boolean withVertex) {

        Shape shape = makePolylineShape(pts);

        if (shape != null) {
            g.draw(shape);
        }

        if(withVertex == true) {
//            drawPoints((ArrayList) pts, g);

            int i;
            for (i = 0; i < pts.size(); ++i) {
                GPoint p = (GPoint) pts.get(i);

                drawVertex(g, p);
                if (i == 0) {
                    drawHighlightedVertex(g, p);
                }
            }
        }
    }

    public static void drawPolylineArrow(Graphics2D g, ArrayList pts) {

        Shape shape = makePolylineShape(pts);

        for (int i = 1; i < pts.size(); ++i) {
            drawArrow(g, (GPoint) pts.get(i - 1), (GPoint) pts.get(i));
        }

        drawPoints(g, (ArrayList) pts);
    }

    private static GeneralPath makePolylineShape(ArrayList pts) {
        GeneralPath shape = new GeneralPath();

        for (int i = 0; i < pts.size(); ++i) {
            GPoint p = (GPoint) pts.get(i);
            if (i == 0) {
                shape.moveTo((float) p.x, (float) p.y);
            } else {
                shape.lineTo((float) p.x, (float) p.y);
            }

        }

        return shape;
    }

    public static void drawPolygon(Graphics2D g, ArrayList parts) {

        Shape shape = makePolygonShape(parts);

        if (shape != null) {
            g.draw(shape);
            g.fill(shape);
        }

        for (int i = 0; i < parts.size(); ++i) {
            drawPoints(g, (ArrayList) parts.get(i));
        }
    }

    // shell은 껍데기, hole은 구멍
    // shell도 ring의 하나로 parts 배열의 가장 첫번째 들어 있다.
    // hole도 ring의 형태임
    private static Shape makePolygonShape(ArrayList parts) {
        Area shape = null;
        if (shape == null) {
            Shape shellShape = makeRingShape((ArrayList) parts.get(0));
            if (shellShape != null) {
                shape = new Area(shellShape);

                for (int i = 1; i < parts.size(); ++i) {
                    Area a = (Area) makeRingShape((ArrayList) parts.get(i));
                    if (a != null) {
                        shape.subtract(a);
                    }
                }
            }
        }
        return shape;
    }

    public static void drawRing(Graphics2D g, ArrayList ring) {

        Shape shape = null;
        if (ring.size() >= 3) {
            GeneralPath poly = new GeneralPath();

            for (int i = 0; i < ring.size(); ++i) {
                GPoint p = (GPoint) ring.get(i);
                if (i == 0) {
                    poly.moveTo((float) p.x, (float) p.y);
                } else {
                    poly.lineTo((float) p.x, (float) p.y);
                }
            }

            shape = new Area(poly);
        }

        if (shape != null) {
            g.draw(shape);
            g.fill(shape);
        }

    }

    public static void drawRingLine(Graphics2D g, ArrayList ring, boolean withVertex) {

        Shape shape = null;
        if (ring.size() >= 3) {
            GeneralPath poly = new GeneralPath();

            for (int i = 0; i < ring.size(); ++i) {
                GPoint p = (GPoint) ring.get(i);
                if (i == 0) {
                    poly.moveTo((float) p.x, (float) p.y);
                } else {
                    poly.lineTo((float) p.x, (float) p.y);
                }
            }

            shape = new Area(poly);
        }

        if (shape != null) {
            // g.setStroke(new BasicStroke(10));
            g.draw(shape);
        }

        if (ring.size() >= 3 && withVertex == true) {
            int i;
            for (i = 0; i < ring.size(); ++i) {
                GPoint p = (GPoint) ring.get(i);

                drawVertex(g, p);
                if (i == 0) {
                    drawHighlightedVertex(g, p);
                }
            }

//            for (i = 1; i < ring.size(); ++i) {
//                drawArrow((Point2D) ring.get(i - 1), (Point2D) ring.get(i), g);
//            }

        }
    }

    // ring은 조각의 좌표 리스트
    private static Shape makeRingShape(ArrayList ring) {
        Shape shape = null;
        if (ring.size() >= 3) {
            GeneralPath poly = new GeneralPath();

            for (int i = 0; i < ring.size(); ++i) {
                GPoint p = (GPoint) ring.get(i);

                if (i == 0) {
                    poly.moveTo((float) p.x, (float) p.y);
                } else {
                    poly.lineTo((float) p.x, (float) p.y);
                }
            }

            shape = new Area(poly);
        }
        return shape;
    }

    public static void drawRingBand(Graphics2D g, ArrayList ring, GPoint p) {
        if (ring.size() > 0) {
            GPoint first = (GPoint) ring.get(0);
            GPoint last = (GPoint) ring.get(ring.size() - 1);
            g.draw(new java.awt.geom.Line2D.Double(first.x, first.y, p.x, p.y));
            g.draw(new java.awt.geom.Line2D.Double(p.x, p.y, last.x, last.y));
        }
    }


    // @@ to-do FontSymbol을 받아서 처리하는 것으로 손봐야 함
    public static void drawOutlineText(Graphics2D g, String text, GPoint pt, int txtSize) {

//        Font font = new Font("맑은 고딕", Font.PLAIN, txtSize);
        Font font = basicFont.deriveFont((float)txtSize); 
        
        g.setFont(font);
        int[] size = getTextSize(g, text, font);
        int width = size[0];
        int height = size[1];


        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        AffineTransform transform = g.getTransform();
        transform.translate(pt.x - width / 2, pt.y - height / 2);
        g.transform(transform);

        FontRenderContext frc = g.getFontRenderContext();
        TextLayout textLayout = new TextLayout(text, font, frc);
        g.setPaint(Color.WHITE);
        g.setStroke(new BasicStroke(((float) font.getSize()) * (font.getStyle() == Font.BOLD ? 0.04f : 0.015f), BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        AffineTransform at = AffineTransform.getTranslateInstance(5, font.getSize());
        Shape outline = textLayout.getOutline(at);
        g.fill(outline);
        g.setPaint(Color.BLACK);
        g.draw(outline);

        // 좌표계를 되돌려 두어야 함
        transform.translate((pt.x - width / 2) * (-2), (pt.y - height / 2) * (-2));
        g.transform(transform);

        // @@ 디버그용
//        System.out.println("size = " +  g2.getFontMetrics(font).stringWidth(text) + " , " + g2.getFontMetrics(font).getHeight() + " , " + font.getSize() );
//        g2.drawRect((int)(pt.x - width / 2), (int)(pt.y - height / 2), g2.getFontMetrics(font).stringWidth(text) , g2.getFontMetrics(font).getHeight());

        // 텍스트 위치 표시
        // drawDot(pt, g2);

    }

    // g에 지정된 대로 그냥 Text를 뿌림
    // 좌표가 글자 중심에 오도록 찍는다
    public static void drawSimpleText(Graphics2D g, String text, GPoint pt) {
        Font font = g.getFont();
        int[] size = getTextSize(g, text, font);
        int width = size[0];
        int height = size[1];
        g.drawString(text, (float)(pt.x - width / 2), (float)(pt.y + height / 4));
    }

    
    
    public static void drawString(Graphics2D g, String text, int fontSize, Color color, GPoint pt) {
        g.setColor(color);
        
        Font font = basicFont.deriveFont((float)fontSize);  
        g.setFont(font);
        g.setColor(color);
        
        int[] size = getTextSize(g, text, font);
        int width = size[0];
        int height = size[1];
        
        // 기준점은 좌중간점이다. 밑에서 위로 그리는 셈
        g.drawString(text, (float)(pt.x), (float)(pt.y + height/2 + 5));
        
    }
    
    // pt는 심볼점 중심으로 텍스트를 출력함
    public static void drawText(Graphics2D g, String text, Color color, GPoint pt) {
        
        int size = basicFont.getSize();
        System.out.println("size = "+ size);
        drawText(g, text, size, color, pt, false);
    }
    public static void drawText(Graphics2D g, String text, int fontSize, Color color, GPoint pt, boolean isDotView) {

        // 폰트 적용한 텍스트의 크기를 구하기 위해
        Font font = basicFont.deriveFont((float)fontSize); 
        g.setFont(font);
        g.setColor(color);
        
        int[] size = getTextSize(g, text, font);
        int width = size[0];
        int height = size[1];

        if(isDotView == true){  // 심볼을 찍을 때
            g.drawString(text, (float)(pt.x - width / 2), (float)(pt.y - height / 2));
        }else{  // 심볼은 안 찍을 때
            g.drawString(text, (float)(pt.x - width / 2), (float)(pt.y + height / 4));
        }

//        g2.drawString(text, pt.x, pt.y);

        // @@ 디버그용
//        System.out.println("size = " +  g2.getFontMetrics(font).stringWidth(text) + " , " + g2.getFontMetrics(font).getHeight() + " , " + font.getSize() );
//        g2.drawRect((int)(pt.x - width / 2), (int)(pt.y - height / 2), g2.getFontMetrics(font).stringWidth(text) , g2.getFontMetrics(font).getHeight());

        // 도트 표시
        if (isDotView) {
            drawDot(g, pt, 5);
            drawDotLine(g, pt, 6);
        }

    }

    public static MBR getTextBound(Graphics2D g, String text, GPoint pt){
        MBR rect = new MBR();

        Graphics g2 = g;

        Font font = basicFont;
        g2.setFont(font);

        rect.minX = pt.x;
        rect.minY = pt.y;
        rect.maxX = pt.x + g2.getFontMetrics(font).stringWidth(text);
        rect.maxY = pt.y + g2.getFontMetrics(font).getHeight();

        return rect;

    }

    public static void drawBackground(Graphics2D g, Color color,  int width, int height, boolean transparent) {
//        Color color1 = new Color(192, 192, 192);
//        Color color2 = new Color(224, 224, 224);
//
//        Composite oldComposite = g.getComposite();
//        g.setComposite(AlphaComposite.getInstance(10, 0.75F));
//        g.setPaint(new GradientPaint(0.0F, 0.0F, color1, 0.0F, (float)height, color2));
//        g.fillRoundRect(0, 0, width, height, 4, 4);
//        g.setComposite(oldComposite);

        if(transparent) {
            g.setComposite(AlphaComposite.Clear);
        }
        g.setColor(color);
        g.fillRect(0,0, width, height);
        g.setComposite(AlphaComposite.Src);
    }



    // 앞머리에서 회전
    // double theta = Math.atan2(y2 - y1, x2 - x1);
    //  angle %= 360;  Math.toRadians(angle)
    public static void drawTextRotate1(Graphics2D g, String text, int x1, int y1, double theta , boolean isDotView) {
        if (text == null || text.trim().isEmpty()) {
            return;
        }
        AffineTransform affineTransform = g.getTransform();
        g.rotate(theta, x1, y1);

        Color color = g.getColor();
        drawText(g, text, basicFont.getSize(), color, new GPoint(x1, y1), isDotView);
        g.setTransform(affineTransform);
    }

    // 특정 좌표(포인트점)에서 텍스트 회전 시키기 - 중심에서 회전
    public static void drawTextRotate2(Graphics2D g, String text, GPoint pt, double theta ) {
        if (text == null || text.trim().isEmpty()) {
            return;
        }

        Font font = basicFont;
        g.setFont(font);
        int[] size = getTextSize(g, text, font);
        int width = size[0];
        int height = size[1];

        AffineTransform orgAffineTransform = g.getTransform();

        // 회전 변환 후
        AffineTransform at1 = AffineTransform.getTranslateInstance(pt.x,pt.y);
        AffineTransform at2 = AffineTransform.getRotateInstance(theta);
        AffineTransform at3 = AffineTransform.getTranslateInstance(-pt.x,-pt.y);

        at1.concatenate(at2);
        at1.concatenate(at3);
        g.setTransform(at1);

        g.drawString(text, (float)(pt.x - width / 2), (float)(pt.y + height / 4));

        // 그린 후 되돌려 놓음
        g.setTransform(orgAffineTransform);

    }

    public static int[] getTextSize(Graphics2D g, String text, Font font){
        int[] size = new int[2];

        // 폰트 적용한 텍스트의 크기를 구하기 위해
        size[0] = g.getFontMetrics(font).stringWidth(text);
        size[1] = g.getFontMetrics(font).getHeight();

        // System.out.println("size = " + size[0] + " , " + size[1]);
        return size;
    }
}
