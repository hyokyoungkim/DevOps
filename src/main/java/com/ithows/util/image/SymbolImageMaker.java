/*
 *  Copyright 2020. S.O.X. All rights reserved
 */

package com.ithows.util.image;

//import com.kitfox.svg.SVGCache;
//import com.kitfox.svg.SVGDiagram;
//import com.kitfox.svg.app.beans.SVGIcon;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;

/**
 * Class SymbolImageMaker
 * 
 * @author Roi Kim <S.O.X Co. Ltd.>
 */
public class SymbolImageMaker {

    public static BufferedImage makeSimpleDot(Color symColor){
        int WIDTH = 10;
        int HEIGHT = 10;
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, 2);

        for(int y = 0; y < 10; ++y) {
            for(int x = 0; x < 10; ++x) {
                image.setRGB(x, y, 0);
            }
        }

        Graphics2D g2d = (Graphics2D)image.getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(symColor);
        g2d.fillOval(0, 0, 9, 9);

        g2d.setColor(Color.gray);
        g2d.drawOval(0, 0, 9, 9);
        return image;
    }

    public static BufferedImage makeIconFromFile(String imgPath)  {
        File img = new File(imgPath);
        BufferedImage in = null;
        if (!img.exists() || !img.isFile()) {
            System.out.println("It is not Image file or not exist.");
            return null;
        }

        try {
            in = ImageIO.read(img);
        }catch(Exception e){}

        return in;
    }
//
//    public static BufferedImage makeIconFromSVG(String svgPath, int symbolSize) {
//
//        BufferedImage in = null;
//        File img = new File(svgPath);
//        if (!img.exists() || !img.isFile()) {
//            System.out.println("It is not SVG file or not exist.");
//            return null;
//        }
//        try {
//            FileInputStream fin = new FileInputStream(svgPath);
//
//            URI uri = SVGCache.getSVGUniverse().loadSVG(fin, svgPath);
//            SVGDiagram diagram = SVGCache.getSVGUniverse().getDiagram(uri);
//
//            int width, height;
//            if(diagram.getWidth()>diagram.getHeight()){
//                width = symbolSize;
//                height = (int) (symbolSize * ( diagram.getHeight() / diagram.getWidth() ));
//            }else{
//                width = (int) (symbolSize * ( diagram.getWidth() / diagram.getHeight() ));
//                height = symbolSize;
//            }
//
//            float[] bmpSize = GraphicUtils.imageSize(diagram.getWidth(), diagram.getHeight(), 1, width, height, 100);
//
//            SVGIcon icon = new SVGIcon();
//            icon.setAntiAlias(true);
//            icon.setAutosize(SVGIcon.AUTOSIZE_STRETCH);
//            icon.setPreferredSize(new Dimension((int) bmpSize[0], (int) bmpSize[1]));
//            icon.setSvgURI(uri);
//            BufferedImage bufferedImage = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
//            icon.paintIcon(null, bufferedImage.createGraphics(), 0, 0);
//            in = (BufferedImage)icon.getImage();
//        }catch(Exception e){
//
//        }
//
//        return in;
//    }


    // 단추형태 아이콘을 그릴 때 기본 바탕이미지로 활용
    private static BufferedImage makeIcon(Color background) {
        int WIDTH = 16;
        int HEIGHT = 16;
        BufferedImage image = new BufferedImage(WIDTH, HEIGHT, 2);

        for(int y = 0; y < 16; ++y) {
            for(int x = 0; x < 16; ++x) {
                image.setRGB(x, y, 0);
            }
        }

        Graphics2D g2d = (Graphics2D)image.getGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setColor(background);
        g2d.fillOval(0, 0, 15, 15);
        double hx = 4.0D;
        double hy = 4.0D;

        for(int y = 0; y < 16; ++y) {
            for(int x = 0; x < 16; ++x) {
                double dx = (double)x - hx;
                double dy = (double)y - hy;
                double dist = Math.sqrt(dx * dx + dy * dy);
                if (dist > 16.0D) {
                    dist = 16.0D;
                }

                int color = image.getRGB(x, y);
                int a = color >>> 24 & 255;
                int r = color >>> 16 & 255;
                int g = color >>> 8 & 255;
                int b = color >>> 0 & 255;
                double coef = 0.7D - 0.7D * dist / 16.0D;
                image.setRGB(x, y, a << 24 | (int)((double)r + coef * (double)(255 - r)) << 16 | (int)((double)g + coef * (double)(255 - g)) << 8 | (int)((double)b + coef * (double)(255 - b)));
            }
        }

        g2d.setColor(Color.gray);
        g2d.drawOval(0, 0, 15, 15);
        return image;
    }

    public static BufferedImage makeXArrow(Color background) {
        BufferedImage image = makeIcon(background);
        Graphics2D g = (Graphics2D)image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.fillPolygon(new int[]{10, 4, 10}, new int[]{5, 8, 11}, 3);
        image.flush();
        return image;
    }

    public static BufferedImage makeYArrow(Color background) {
        BufferedImage image = makeIcon(background);
        Graphics2D g = (Graphics2D)image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.fillPolygon(new int[]{5, 8, 11}, new int[]{10, 4, 10}, 3);
        image.flush();
        return image;
    }

    public static BufferedImage makePlus(Color background) {
        BufferedImage image = makeIcon(background);
        Graphics2D g = (Graphics2D)image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.fillRect(4, 7, 8, 2);
        g.fillRect(7, 4, 2, 8);
        image.flush();
        return image;
    }

    public static BufferedImage makeMinus(Color background) {
        BufferedImage image = makeIcon(background);
        Graphics2D g = (Graphics2D)image.getGraphics();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.fillRect(4, 7, 8, 2);
        image.flush();
        return image;
    }
}
