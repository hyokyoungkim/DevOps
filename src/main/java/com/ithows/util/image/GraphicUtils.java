/*
 *  Copyright 2020. S.O.X. All rights reserved
 */

package com.ithows.util.image;

/**
 * Class GraphicUtils
 * 
 * @author Roi Kim <S.O.X Co. Ltd.>
 */

import java.awt.*;
import java.awt.color.ColorSpace;
import java.awt.geom.AffineTransform;
import java.awt.image.*;
import java.io.File;
import java.io.IOException;

public class GraphicUtils {

    private static BufferedImageOp grayscaleOp, invertOp, invertOp4;
    private static GraphicsEnvironment ge;

    static {
        createFilters();

//        try {
//            registerDefaultFont();
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (FontFormatException e) {
//            e.printStackTrace();
//        }
    }

    public static void registerDefaultFont() throws IOException, FontFormatException {
        Font f = Font.createFont(Font.TRUETYPE_FONT, new File("resource/fonts/malgun.ttf"));
        Font fb = Font.createFont(Font.TRUETYPE_FONT, new File("resource/fonts/NanumBarunGothic.ttf"));

        ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        ge.registerFont(f);
        ge.registerFont(fb);

    }

    /**
     * 이미지 회전
     * @param bi
     * @param angleDegree : 0~360도 회전각
     * @return
     */
    public static BufferedImage rotateBufferedImage(BufferedImage bi, int angleDegree) {
        BufferedImage bi2 = new BufferedImage(bi.getHeight(), bi.getWidth(), bi.getType());
        AffineTransform tx = new AffineTransform();
        tx.translate(bi.getHeight() / 2, bi.getWidth() / 2);
        tx.rotate(Math.toRadians(angleDegree));//(radian,arbit_X,arbit_Y)
        tx.translate(-bi.getWidth() / 2, -bi.getHeight() / 2);
        AffineTransformOp op = new AffineTransformOp(tx, AffineTransformOp.TYPE_BILINEAR);
        op.filter(bi, bi2);//(sourse,destination)
        return bi2;
    }


    public static Color getColorFromRGB(String rgbString){
        return getColorFromRGB(rgbString, 255);
    }

    public static Color getColorFromRGB(String rgbString, int alpha){

        String[] factor = rgbString.split(",");
        if(factor.length != 3){
            return Color.BLACK;
        }

        return new Color(Integer.parseInt(factor[0]),Integer.parseInt(factor[1]), Integer.parseInt(factor[2]), alpha );

    }

    // alpha는 불투명도(0~255 : 값이 클 수록 불투명해 짐)
    public static Color ColorWithAlpha(Color original, int alpha) {
        return new Color(original.getRed(), original.getGreen(), original.getBlue(), alpha);
    }

    // this.graphics2D.drawImage(applyFilter(AwtGraphicFactory.getBitmap(bitmap), filter), left, top, null);
    public static BufferedImage applyFilter(BufferedImage src, Filter filter) {
        if (filter == Filter.NONE) {
            return src;
        }
        BufferedImage dest = null;
        switch (filter) {
            case GRAYSCALE:
                dest = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
                grayscaleOp.filter(src, dest);
                break;
            case GRAYSCALE_INVERT:
                dest = new BufferedImage(src.getWidth(), src.getHeight(), src.getType());
                grayscaleOp.filter(src, dest);
                dest = applyInvertFilter(dest);
                break;
            case INVERT:
                dest = applyInvertFilter(src);
                break;
        }
        return dest;
    }

    private static BufferedImage applyInvertFilter(BufferedImage src) {

        final BufferedImage newSrc;
        if (src.getColorModel() instanceof IndexColorModel) {
            newSrc = new BufferedImage(src.getWidth(), src.getHeight(), src.getColorModel().getNumComponents() == 3 ? BufferedImage.TYPE_INT_RGB : BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2 = newSrc.createGraphics();
            g2.drawImage(src, 0, 0, null);
            g2.dispose();
        } else {
            newSrc = src;
        }
        BufferedImage dest = new BufferedImage(newSrc.getWidth(), newSrc.getHeight(), newSrc.getType());
        switch (newSrc.getColorModel().getNumComponents()) {
            case 3:
                invertOp.filter(newSrc, dest);
                break;
            case 4:
                invertOp4.filter(newSrc, dest);
                break;
        }
        return dest;
    }

    private static void createFilters() {
        grayscaleOp = new ColorConvertOp(ColorSpace.getInstance(ColorSpace.CS_GRAY), null);

        short[] invert = new short[256];
        short[] straight = new short[256];
        for (int i = 0; i < 256; i++) {
            invert[i] = (short) (255 - i);
            straight[i] = (short) i;
        }
        invertOp = new LookupOp(new ShortLookupTable(0, invert), null);
        invertOp4 = new LookupOp(new ShortLookupTable(0, new short[][]{invert, invert, invert, straight}), null);
    }

    public static int filterColor(int color, Filter filter) {
        if (filter == Filter.NONE) {
            return color;
        }
        int a = color >>> 24;
        int r = (color >> 16) & 0xFF;
        int g = (color >> 8) & 0xFF;
        int b = color & 0xFF;
        switch (filter) {
            case GRAYSCALE:
                r = g = b = (int) (0.213f * r + 0.715f * g + 0.072f * b);
                break;
            case GRAYSCALE_INVERT:
                r = g = b = 255 - (int) (0.213f * r + 0.715f * g + 0.072f * b);
                break;
            case INVERT:
                r = 255 - r;
                g = 255 - g;
                b = 255 - b;
                break;
        }
        return (a << 24) | (r << 16) | (g << 8) | b;
    }

    /**
     * @param color color value in layout 0xAARRGGBB.
     * @return the alpha value for the color.
     */
    public static int getAlpha(int color) {
        return (color >> 24) & 0xff;
    }

    /**
     * Given the original image size, as well as width, height, percent parameters,
     * can compute the final image size.
     *
     * @param picWidth    original image width
     * @param picHeight   original image height
     * @param scaleFactor 1
     * @param width       requested width (0: no change)
     * @param height      requested height (0: no change)
     * @param percent     requested scale percent (100: no change)
     */
    public static float[] imageSize(float picWidth, float picHeight, float scaleFactor, int width, int height, int percent) {
        float bitmapWidth = picWidth * scaleFactor;
        float bitmapHeight = picHeight * scaleFactor;

        float aspectRatio = picWidth / picHeight;

        if (width != 0 && height != 0) {
            // both width and height set, override any other setting
            bitmapWidth = width;
            bitmapHeight = height;
        } else if (width == 0 && height != 0) {
            // only width set, calculate from aspect ratio
            bitmapWidth = height * aspectRatio;
            bitmapHeight = height;
        } else if (width != 0 && height == 0) {
            // only height set, calculate from aspect ratio
            bitmapHeight = width / aspectRatio;
            bitmapWidth = width;
        }

        if (percent != 100) {
            bitmapWidth *= percent / 100f;
            bitmapHeight *= percent / 100f;
        }

        return new float[]{bitmapWidth, bitmapHeight};
    }


    // 동영상 녹화를 위한 버퍼이미지 클론
    public static BufferedImage BufferedImageClone(BufferedImage source) {
        return convertToType(source, BufferedImage.TYPE_3BYTE_BGR) ;
    }

    // 버퍼 이미지 타입을 변경하여 복사
    public static BufferedImage convertToType(BufferedImage sourceImage, int targetType) {
        BufferedImage image;


        if (sourceImage.getType() == targetType)
            image = sourceImage;

        else
        {
            image = new BufferedImage(sourceImage.getWidth(),
                    sourceImage.getHeight(), targetType);
            image.getGraphics().drawImage(sourceImage, 0, 0, null);
        }

        return image;
    }

}